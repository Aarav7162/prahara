package com.example.model

import kotlin.math.*

data class CityLocation(
    val name: String,
    val stateOrCountry: String,
    val latitude: Double,
    val longitude: Double,
    val isGps: Boolean = false,
    val isAutoDetected: Boolean = false
)

object DefaultLocations {
    val predefinedCities = listOf(
        CityLocation("Ahmedabad", "Gujarat, India", 23.0225, 72.5714),
        CityLocation("Palitana", "Gujarat, India", 21.5222, 71.8290),
        CityLocation("Mumbai", "Maharashtra, India", 19.0760, 72.8777),
        CityLocation("Surat", "Gujarat, India", 21.1702, 72.8311),
        CityLocation("Rajkot", "Gujarat, India", 22.3039, 70.8022),
        CityLocation("Jaipur", "Rajasthan, India", 26.9124, 75.7873),
        CityLocation("New Delhi", "Delhi, India", 28.6139, 77.2090),
        CityLocation("Bengaluru", "Karnataka, India", 12.9716, 77.5946),
        CityLocation("Pune", "Maharashtra, India", 18.5204, 73.8567),
        CityLocation("Kolkata", "West Bengal, India", 22.5726, 88.3639),
        CityLocation("London", "UK", 51.5074, -0.1278),
        CityLocation("New York", "USA", 40.7128, -74.0060),
        CityLocation("Chicago", "USA", 41.8781, -87.6298),
        CityLocation("San Francisco", "USA", 37.7749, -122.4194),
        CityLocation("Sydney", "Australia", -33.8688, 151.2093)
    )

    val defaultCity = predefinedCities[0] // Ahmedabad

    fun findNearestCity(lat: Double, lng: Double): CityLocation {
        return predefinedCities.minByOrNull { city ->
            val dLat = Math.toRadians(city.latitude - lat)
            val dLng = Math.toRadians(city.longitude - lng)
            val a = sin(dLat / 2).pow(2) +
                    cos(Math.toRadians(lat)) * cos(Math.toRadians(city.latitude)) * sin(dLng / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            6371.0 * c
        } ?: defaultCity
    }
}
