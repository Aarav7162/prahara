import Foundation

public struct SolarTimes {
    public let date: Date
    public let latitude: Double
    public let longitude: Double
    public let sunrise: Date
    public let sunset: Date
    public let daylightDurationMinutes: Int
    public let navkarsi: Date
    public let porsi: Date
    public let sadhPorsi: Date
    public let purimaddh: Date
    public let avaddh: Date
    public let chauvihar: Date
}

public class SolarAstronomyEngine {
    private static func toRadians(_ deg: Double) -> Double { deg * .pi / 180.0 }
    private static func toDegrees(_ rad: Double) -> Double { rad * 180.0 / .pi }
    private static func normalizeDeg(_ deg: Double) -> Double {
        var d = deg.truncatingRemainder(dividingBy: 360.0)
        if d < 0 { d += 360.0 }
        return d
    }

    private static func calculateEventUtc(date: Date, latitude: Double, longitude: Double, isSunrise: Bool) -> Date {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(secondsFromGMT: 0)!
        let dayOfYear = calendar.ordinality(of: .day, in: .year, for: date) ?? 1
        let lngHour = longitude / 15.0

        let t: Double = isSunrise
            ? Double(dayOfYear) + ((6.0 - lngHour) / 24.0)
            : Double(dayOfYear) + ((18.0 - lngHour) / 24.0)

        let mDeg = (0.9856 * t) - 3.289
        let mRad = toRadians(mDeg)

        var lDeg = mDeg + (1.916 * sin(mRad)) + (0.020 * sin(2.0 * mRad)) + 282.634
        lDeg = normalizeDeg(lDeg)
        let lRad = toRadians(lDeg)

        let raRad = atan(0.91764 * tan(lRad))
        var raDeg = normalizeDeg(toDegrees(raRad))

        let lQuadrant = floor(lDeg / 90.0) * 90.0
        let raQuadrant = floor(raDeg / 90.0) * 90.0
        raDeg += (lQuadrant - raQuadrant)
        let raHours = raDeg / 15.0

        let sinDec = 0.39782 * sin(lRad)
        let cosDec = cos(asin(sinDec))

        let latRad = toRadians(latitude)
        let zenith = 90.8333
        let cosH = (cos(toRadians(zenith)) - (sinDec * sin(latRad))) / (cosDec * cos(latRad))

        let hHours: Double
        if cosH > 1.0 {
            hHours = 0.0
        } else if cosH < -1.0 {
            hHours = isSunrise ? 0.0 : 24.0
        } else if isSunrise {
            hHours = (360.0 - toDegrees(acos(cosH))) / 15.0
        } else {
            hHours = toDegrees(acos(cosH)) / 15.0
        }

        let localMeanTime = hHours + raHours - (0.06571 * t) - 6.622
        var utHours = localMeanTime - lngHour
        while utHours < 0.0 { utHours += 24.0 }
        while utHours >= 24.0 { utHours -= 24.0 }

        let hours = Int(utHours)
        let minutesTotal = (utHours - Double(hours)) * 60.0
        let minutes = Int(minutesTotal)
        let seconds = min(59, max(0, Int(round((minutesTotal - Double(minutes)) * 60.0))))

        var comps = calendar.dateComponents([.year, .month, .day], from: date)
        comps.hour = hours
        comps.minute = minutes
        comps.second = seconds
        return calendar.date(from: comps) ?? date
    }

    public static func calculateSolarTimes(date: Date, latitude: Double, longitude: Double) -> SolarTimes {
        let sunrise = calculateEventUtc(date: date, latitude: latitude, longitude: longitude, isSunrise: true)
        let sunset = calculateEventUtc(date: date, latitude: latitude, longitude: longitude, isSunrise: false)

        let daylightSec = max(1.0, sunset.timeIntervalSince(sunrise))
        let daylightMins = Int(daylightSec / 60)

        let navkarsi = sunrise.addingTimeInterval(48 * 60)
        let porsi = sunrise.addingTimeInterval(daylightSec / 4.0)
        let sadhPorsi = sunrise.addingTimeInterval(daylightSec * 3.0 / 8.0)
        let purimaddh = sunrise.addingTimeInterval(daylightSec / 2.0)
        let avaddh = sunrise.addingTimeInterval(daylightSec * 3.0 / 4.0)
        let chauvihar = sunset

        return SolarTimes(
            date: date,
            latitude: latitude,
            longitude: longitude,
            sunrise: sunrise,
            sunset: sunset,
            daylightDurationMinutes: daylightMins,
            navkarsi: navkarsi,
            porsi: porsi,
            sadhPorsi: sadhPorsi,
            purimaddh: purimaddh,
            avaddh: avaddh,
            chauvihar: chauvihar
        )
    }
}

