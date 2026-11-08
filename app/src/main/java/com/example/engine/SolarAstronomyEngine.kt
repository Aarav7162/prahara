package com.example.engine

import java.time.*
import kotlin.math.*

/**
 * Astronomical Solar Calculations based on the official NOAA / US Naval Observatory algorithms.
 * Computes exact Sunrise, Sunset, and Jain Pachakkhan daylight divisions.
 */
object SolarAstronomyEngine {

    data class SolarTimes(
        val date: LocalDate,
        val latitude: Double,
        val longitude: Double,
        val timeZone: ZoneId,
        val sunrise: ZonedDateTime,
        val sunset: ZonedDateTime,
        val daylightDurationMinutes: Long,
        val navkarsi: ZonedDateTime,
        val porsi: ZonedDateTime,
        val sadhPorsi: ZonedDateTime,
        val purimaddh: ZonedDateTime,
        val avaddh: ZonedDateTime,
        val chauvihar: ZonedDateTime
    )

    val IST_ZONE: ZoneId = ZoneId.of("Asia/Kolkata")

    fun calculateSolarTimes(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        timeZone: ZoneId = IST_ZONE
    ): SolarTimes {
        val sunriseUtc = calculateEventUtc(date, latitude, longitude, isSunrise = true)
        val sunsetUtc = calculateEventUtc(date, latitude, longitude, isSunrise = false)

        val sunrise = sunriseUtc.withZoneSameInstant(timeZone)
        val sunset = sunsetUtc.withZoneSameInstant(timeZone)

        val daylightSeconds = Duration.between(sunrise, sunset).seconds.coerceAtLeast(1)
        val daylightDurationMinutes = daylightSeconds / 60

        // Navkarsi: Sunrise + 48 minutes (2 Ghadis)
        val navkarsi = sunrise.plusMinutes(48)

        // Porsi: 1 Prahar (1/4th daylight)
        val porsi = sunrise.plusSeconds(daylightSeconds / 4)

        // Sadh-Porsi: 1.5 Prahars (3/8th daylight)
        val sadhPorsi = sunrise.plusSeconds((daylightSeconds * 3) / 8)

        // Purimaddh: 2 Prahars (1/2 daylight / Midday)
        val purimaddh = sunrise.plusSeconds(daylightSeconds / 2)

        // Avaddh: 3 Prahars (3/4th daylight)
        val avaddh = sunrise.plusSeconds((daylightSeconds * 3) / 4)

        // Chauvihar: Sunset (Surya Ast)
        val chauvihar = sunset

        return SolarTimes(
            date = date,
            latitude = latitude,
            longitude = longitude,
            timeZone = timeZone,
            sunrise = sunrise,
            sunset = sunset,
            daylightDurationMinutes = daylightDurationMinutes,
            navkarsi = navkarsi,
            porsi = porsi,
            sadhPorsi = sadhPorsi,
            purimaddh = purimaddh,
            avaddh = avaddh,
            chauvihar = chauvihar
        )
    }

    private fun calculateEventUtc(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        isSunrise: Boolean
    ): ZonedDateTime {
        val dayOfYear = date.dayOfYear
        val lngHour = longitude / 15.0

        val t = if (isSunrise) {
            dayOfYear + ((6.0 - lngHour) / 24.0)
        } else {
            dayOfYear + ((18.0 - lngHour) / 24.0)
        }

        // Sun's mean anomaly
        val mDeg = (0.9856 * t) - 3.289
        val mRad = Math.toRadians(mDeg)

        // Sun's true longitude
        var lDeg = mDeg + (1.916 * sin(mRad)) + (0.020 * sin(2.0 * mRad)) + 282.634
        lDeg = normalizeDeg(lDeg)
        val lRad = Math.toRadians(lDeg)

        // Sun's right ascension
        val raRad = atan(0.91764 * tan(lRad))
        var raDeg = Math.toDegrees(raRad)
        raDeg = normalizeDeg(raDeg)

        // Adjust RA quadrant to match L quadrant
        val lQuadrant = floor(lDeg / 90.0) * 90.0
        val raQuadrant = floor(raDeg / 90.0) * 90.0
        raDeg += (lQuadrant - raQuadrant)
        val raHours = raDeg / 15.0

        // Sun's declination
        val sinDec = 0.39782 * sin(lRad)
        val cosDec = cos(asin(sinDec))

        // Sun's local hour angle for zenith 90.8333° (90°50')
        val latRad = Math.toRadians(latitude)
        val zenith = 90.8333
        val cosH = (cos(Math.toRadians(zenith)) - (sinDec * sin(latRad))) / (cosDec * cos(latRad))

        val hHours = when {
            cosH > 1.0 -> if (isSunrise) 0.0 else 0.0 // Polar night
            cosH < -1.0 -> if (isSunrise) 0.0 else 24.0 // Midnight sun
            isSunrise -> (360.0 - Math.toDegrees(acos(cosH))) / 15.0
            else -> Math.toDegrees(acos(cosH)) / 15.0
        }

        // Local Mean Time
        val localMeanTime = hHours + raHours - (0.06571 * t) - 6.622

        // Convert to UTC
        var utHours = localMeanTime - lngHour
        while (utHours < 0.0) utHours += 24.0
        while (utHours >= 24.0) utHours -= 24.0

        val hours = utHours.toInt()
        val minutesTotal = (utHours - hours) * 60.0
        val minutes = minutesTotal.toInt()
        val seconds = ((minutesTotal - minutes) * 60.0).roundToInt().coerceIn(0, 59)

        return ZonedDateTime.of(
            date,
            LocalTime.of(hours, minutes, seconds),
            ZoneOffset.UTC
        )
    }

    private fun normalizeDeg(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0.0) d += 360.0
        return d
    }
}
