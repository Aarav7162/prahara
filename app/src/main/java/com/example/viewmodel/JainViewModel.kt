package com.example.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.AudioEngine
import com.example.engine.JainTithiEngine
import com.example.engine.NotificationHelper
import com.example.engine.SolarAstronomyEngine
import com.example.model.CityLocation
import com.example.model.DefaultLocations
import com.example.model.PachakkhanItem
import com.example.repository.PachakkhanRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CountdownState(
    val nextMilestoneName: String,
    val timeRemainingFormatted: String,
    val isPast: Boolean,
    val currentPraharName: String,
    val daylightProgressRatio: Float
)

data class DayTithiSummary(
    val date: LocalDate,
    val tithiInfo: JainTithiEngine.TithiInfo,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean
)

class JainViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationHelper = NotificationHelper(application)
    val audioEngine = AudioEngine(application)

    companion object {
        val IST_ZONE: ZoneId = ZoneId.of("Asia/Kolkata")
    }

    // Current selected location
    private val _currentLocation = MutableStateFlow(DefaultLocations.defaultCity)
    val currentLocation: StateFlow<CityLocation> = _currentLocation.asStateFlow()

    // Location detection status
    private val _locationStatus = MutableStateFlow("Detecting location...")
    val locationStatus: StateFlow<String> = _locationStatus.asStateFlow()

    // Selected date for Daily Timings view (strictly in IST)
    private val _selectedDate = MutableStateFlow(LocalDate.now(IST_ZONE))
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // Solar times for the selected date and location in IST
    val solarTimes: StateFlow<SolarAstronomyEngine.SolarTimes> =
        combine(_selectedDate, _currentLocation) { date, loc ->
            SolarAstronomyEngine.calculateSolarTimes(
                date = date,
                latitude = loc.latitude,
                longitude = loc.longitude,
                timeZone = IST_ZONE
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SolarAstronomyEngine.calculateSolarTimes(
                LocalDate.now(IST_ZONE),
                DefaultLocations.defaultCity.latitude,
                DefaultLocations.defaultCity.longitude,
                timeZone = IST_ZONE
            )
        )

    // Jain Tithi info for the selected date
    val tithiInfo: StateFlow<JainTithiEngine.TithiInfo> = _selectedDate
        .map { date -> JainTithiEngine.calculateTithi(date) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            JainTithiEngine.calculateTithi(LocalDate.now(IST_ZONE))
        )

    // Live countdown state (updated each second)
    private val _countdownState = MutableStateFlow(
        CountdownState(
            nextMilestoneName = "Navkarsi in",
            timeRemainingFormatted = "24m 12s",
            isPast = false,
            currentPraharName = "Morning Prahar",
            daylightProgressRatio = 0.35f
        )
    )
    val countdownState: StateFlow<CountdownState> = _countdownState.asStateFlow()

    // Calendar month navigation (strictly in IST)
    private val _currentMonth = MutableStateFlow(YearMonth.now(IST_ZONE))
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    // Selected Pachakkhan for Modal sheet
    private val _selectedPachakkhan = MutableStateFlow<PachakkhanItem?>(null)
    val selectedPachakkhan: StateFlow<PachakkhanItem?> = _selectedPachakkhan.asStateFlow()

    // Preferred language tab for Pachakkhan ("gu", "hi", "en")
    private val _preferredLanguage = MutableStateFlow("hi")
    val preferredLanguage: StateFlow<String> = _preferredLanguage.asStateFlow()

    // Notification settings
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _eveningAlertEnabled = MutableStateFlow(true)
    val eveningAlertEnabled: StateFlow<Boolean> = _eveningAlertEnabled.asStateFlow()

    private val _morningAlertEnabled = MutableStateFlow(true)
    val morningAlertEnabled: StateFlow<Boolean> = _morningAlertEnabled.asStateFlow()

    private val prefs by lazy {
        application.getSharedPreferences("jain_app_prefs", Context.MODE_PRIVATE)
    }

    // First time tutorial visibility
    private val _showTutorial = MutableStateFlow(
        !application.getSharedPreferences("jain_app_prefs", Context.MODE_PRIVATE)
            .getBoolean("tutorial_completed", false)
    )
    val showTutorial: StateFlow<Boolean> = _showTutorial.asStateFlow()

    fun finishTutorial() {
        prefs.edit().putBoolean("tutorial_completed", true).apply()
        _showTutorial.value = false
    }

    fun replayTutorial() {
        _showTutorial.value = true
    }

    init {
        autoDetectLocation()
        startLiveCountdownLoop()
    }

    fun autoDetectLocation() {
        val context = getApplication<Application>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasFine = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasFine && !hasCoarse) {
                _locationStatus.value = "Default location (permission pending)"
                return
            }
        }

        try {
            val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val loc: Location? = locManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            if (loc != null) {
                val lat = loc.latitude
                val lng = loc.longitude

                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(lat, lng, 1) { addresses ->
                            if (addresses.isNotEmpty()) {
                                val addr = addresses[0]
                                val cityName = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "Current Location"
                                val stateCountry = listOfNotNull(addr.adminArea, addr.countryName).joinToString(", ")
                                applyDetectedLocation(lat, lng, cityName, stateCountry)
                            } else {
                                val nearest = DefaultLocations.findNearestCity(lat, lng)
                                applyDetectedLocation(lat, lng, nearest.name, nearest.stateOrCountry)
                            }
                        }
                        return
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(lat, lng, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val addr = addresses[0]
                            val cityName = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "Current Location"
                            val stateCountry = listOfNotNull(addr.adminArea, addr.countryName).joinToString(", ")
                            applyDetectedLocation(lat, lng, cityName, stateCountry)
                            return
                        }
                    }
                } catch (e: Exception) {
                    // Fall back to nearest known Jain city
                }

                val nearest = DefaultLocations.findNearestCity(lat, lng)
                applyDetectedLocation(lat, lng, nearest.name, nearest.stateOrCountry)
            } else {
                _locationStatus.value = "Default: ${DefaultLocations.defaultCity.name}"
            }
        } catch (e: Exception) {
            _locationStatus.value = "Default: ${DefaultLocations.defaultCity.name}"
        }
    }

    private fun applyDetectedLocation(lat: Double, lng: Double, cityName: String, stateCountry: String) {
        _currentLocation.value = CityLocation(
            name = cityName,
            stateOrCountry = if (stateCountry.isNotEmpty()) stateCountry else String.format("%.2f°, %.2f°", lat, lng),
            latitude = lat,
            longitude = lng,
            isGps = true,
            isAutoDetected = true
        )
        _locationStatus.value = "Auto-detected: $cityName"
    }

    private fun startLiveCountdownLoop() {
        viewModelScope.launch {
            while (isActive) {
                updateCountdown()
                delay(1000)
            }
        }
    }

    private fun updateCountdown() {
        val now = ZonedDateTime.now(IST_ZONE)
        val st = solarTimes.value

        val sunrise = st.sunrise
        val navkarsi = st.navkarsi
        val porsi = st.porsi
        val sadhPorsi = st.sadhPorsi
        val purimaddh = st.purimaddh
        val avaddh = st.avaddh
        val chauvihar = st.chauvihar

        // Daylight progress
        val totalDaylightSeconds = Duration.between(sunrise, chauvihar).seconds.coerceAtLeast(1)
        val elapsedSeconds = Duration.between(sunrise, now).seconds
        val progress = (elapsedSeconds.toFloat() / totalDaylightSeconds.toFloat()).coerceIn(0f, 1f)

        val (nextName, targetTime, praharName) = when {
            now.isBefore(sunrise) -> Triple("Surya Uday in", sunrise, "Brahma Muhurta")
            now.isBefore(navkarsi) -> Triple("Navkarsi in", navkarsi, "1st Prahar")
            now.isBefore(porsi) -> Triple("Porsi in", porsi, "1st Prahar")
            now.isBefore(sadhPorsi) -> Triple("Sadh-Porsi in", sadhPorsi, "2nd Prahar")
            now.isBefore(purimaddh) -> Triple("Purimaddh in", purimaddh, "2nd Prahar")
            now.isBefore(avaddh) -> Triple("Avaddh in", avaddh, "3rd Prahar")
            now.isBefore(chauvihar) -> Triple("Chauvihar in", chauvihar, "4th Prahar")
            else -> Triple(
                "Chauvihar Active",
                sunrise.plusDays(1),
                "Ratri Prahar"
            )
        }

        val remainingSeconds = Duration.between(now, targetTime).seconds
        val formattedTime = if (remainingSeconds > 0) {
            val hours = remainingSeconds / 3600
            val minutes = (remainingSeconds % 3600) / 60
            val secs = remainingSeconds % 60
            if (hours > 0) {
                "${hours}h ${minutes}m ${secs}s"
            } else {
                "${minutes}m ${secs}s"
            }
        } else {
            "00m 00s"
        }

        _countdownState.value = CountdownState(
            nextMilestoneName = nextName,
            timeRemainingFormatted = formattedTime,
            isPast = remainingSeconds <= 0,
            currentPraharName = praharName,
            daylightProgressRatio = progress
        )
    }

    fun setLocation(location: CityLocation) {
        _currentLocation.value = location
        _locationStatus.value = "Selected: ${location.name}"
    }

    fun setGpsLocation(lat: Double, lng: Double, locationLabel: String = "Current Location") {
        applyDetectedLocation(lat, lng, locationLabel, String.format("%.2f°, %.2f°", lat, lng))
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setCalendarMonth(month: YearMonth) {
        _currentMonth.value = month
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun openPachakkhan(item: PachakkhanItem) {
        _selectedPachakkhan.value = item
    }

    fun closePachakkhan() {
        audioEngine.stop()
        _selectedPachakkhan.value = null
    }

    fun setPreferredLanguage(lang: String) {
        _preferredLanguage.value = lang
    }

    fun playSutra(item: PachakkhanItem, languageCode: String = _preferredLanguage.value) {
        val textToSpeak = when (languageCode) {
            "gu" -> item.sutraGujarati
            "en" -> item.sutraEnglishTransliteration
            else -> item.sutraHindi
        }
        audioEngine.playSutra(textToSpeak, item.id, languageCode)
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun toggleEveningAlert(enabled: Boolean) {
        _eveningAlertEnabled.value = enabled
    }

    fun toggleMorningAlert(enabled: Boolean) {
        _morningAlertEnabled.value = enabled
    }

    fun triggerTestNotification() {
        val tithi = tithiInfo.value
        val st = solarTimes.value
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        notificationHelper.sendTithiNotification(
            title = if (tithi.isParva) "Parva Alert: ${tithi.fullName}" else "Today's Tithi: ${tithi.fullName}",
            message = "Navkarsi ends at ${st.navkarsi.format(timeFormatter)}. Chauvihar at ${st.chauvihar.format(timeFormatter)}.\n${tithi.dietaryGuidelines}"
        )
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.shutdown()
    }
}
