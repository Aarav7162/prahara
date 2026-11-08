import Foundation
import CoreLocation

public struct CityLocation: Identifiable, Codable, Equatable {
    public var id: String { name }
    public let name: String
    public let stateOrCountry: String
    public let latitude: Double
    public let longitude: Double
    public var isGps: Bool = false
    public var isAutoDetected: Bool = false

    public init(name: String, stateOrCountry: String, latitude: Double, longitude: Double, isGps: Bool = false, isAutoDetected: Bool = false) {
        self.name = name
        self.stateOrCountry = stateOrCountry
        self.latitude = latitude
        self.longitude = longitude
        self.isGps = isGps
        self.isAutoDetected = isAutoDetected
    }
}

public struct DefaultLocations {
    public static let predefinedCities: [CityLocation] = [
        CityLocation(name: "Ahmedabad", stateOrCountry: "Gujarat, India", latitude: 23.0225, longitude: 72.5714),
        CityLocation(name: "Palitana", stateOrCountry: "Gujarat, India", latitude: 21.5222, longitude: 71.8290),
        CityLocation(name: "Mumbai", stateOrCountry: "Maharashtra, India", latitude: 19.0760, longitude: 72.8777),
        CityLocation(name: "Surat", stateOrCountry: "Gujarat, India", latitude: 21.1702, longitude: 72.8311),
        CityLocation(name: "Rajkot", stateOrCountry: "Gujarat, India", latitude: 22.3039, longitude: 70.8022),
        CityLocation(name: "Jaipur", stateOrCountry: "Rajasthan, India", latitude: 26.9124, longitude: 75.7873),
        CityLocation(name: "New Delhi", stateOrCountry: "Delhi, India", latitude: 28.6139, longitude: 77.2090),
        CityLocation(name: "Bengaluru", stateOrCountry: "Karnataka, India", latitude: 12.9716, longitude: 77.5946),
        CityLocation(name: "Pune", stateOrCountry: "Maharashtra, India", latitude: 18.5204, longitude: 73.8567),
        CityLocation(name: "Kolkata", stateOrCountry: "West Bengal, India", latitude: 22.5726, longitude: 88.3639),
        CityLocation(name: "London", stateOrCountry: "UK", latitude: 51.5074, longitude: -0.1278),
        CityLocation(name: "New York", stateOrCountry: "USA", latitude: 40.7128, longitude: -74.0060),
        CityLocation(name: "Chicago", stateOrCountry: "USA", latitude: 41.8781, longitude: -87.6298),
        CityLocation(name: "San Francisco", stateOrCountry: "USA", latitude: 37.7749, longitude: -122.4194),
        CityLocation(name: "Sydney", stateOrCountry: "Australia", latitude: -33.8688, longitude: 151.2093)
    ]

    public static let defaultCity = predefinedCities[0] // Ahmedabad

    public static func findNearestCity(lat: Double, lng: Double) -> CityLocation {
        let city = predefinedCities.min(by: { a, b in
            let distA = distanceKm(lat1: lat, lon1: lng, lat2: a.latitude, lon2: a.longitude)
            let distB = distanceKm(lat1: lat, lon1: lng, lat2: b.latitude, lon2: b.longitude)
            return distA < distB
        })
        return city ?? defaultCity
    }

    private static func distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double) -> Double {
        let r = 6371.0
        let dLat = (lat2 - lat1) * .pi / 180.0
        let dLon = (lon2 - lon1) * .pi / 180.0
        let a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1 * .pi / 180.0) * cos(lat2 * .pi / 180.0) *
                sin(dLon / 2) * sin(dLon / 2)
        let c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

