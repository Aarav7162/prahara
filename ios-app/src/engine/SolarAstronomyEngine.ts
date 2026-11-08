import { SolarTimes } from '../types';

export class SolarAstronomyEngine {
  private static toRadians(deg: number): number {
    return (deg * Math.PI) / 180.0;
  }

  private static toDegrees(rad: number): number {
    return (rad * 180.0) / Math.PI;
  }

  private static normalizeDeg(deg: number): number {
    let d = deg % 360.0;
    if (d < 0.0) d += 360.0;
    return d;
  }

  private static getDayOfYear(date: Date): number {
    const startOfYear = new Date(Date.UTC(date.getFullYear(), 0, 1));
    const current = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const diff = current.getTime() - startOfYear.getTime();
    return Math.floor(diff / (1000 * 60 * 60 * 24)) + 1;
  }

  private static calculateEventUtc(
    date: Date,
    latitude: number,
    longitude: number,
    isSunrise: boolean
  ): Date {
    const dayOfYear = this.getDayOfYear(date);
    const lngHour = longitude / 15.0;

    const t = isSunrise
      ? dayOfYear + (6.0 - lngHour) / 24.0
      : dayOfYear + (18.0 - lngHour) / 24.0;

    // Sun's mean anomaly
    const mDeg = 0.9856 * t - 3.289;
    const mRad = this.toRadians(mDeg);

    // Sun's true longitude
    let lDeg = mDeg + 1.916 * Math.sin(mRad) + 0.02 * Math.sin(2.0 * mRad) + 282.634;
    lDeg = this.normalizeDeg(lDeg);
    const lRad = this.toRadians(lDeg);

    // Sun's right ascension
    const raRad = Math.atan(0.91764 * Math.tan(lRad));
    let raDeg = this.toDegrees(raRad);
    raDeg = this.normalizeDeg(raDeg);

    // Adjust RA quadrant to match L quadrant
    const lQuadrant = Math.floor(lDeg / 90.0) * 90.0;
    const raQuadrant = Math.floor(raDeg / 90.0) * 90.0;
    raDeg += lQuadrant - raQuadrant;
    const raHours = raDeg / 15.0;

    // Sun's declination
    const sinDec = 0.39782 * Math.sin(lRad);
    const cosDec = Math.cos(Math.asin(sinDec));

    // Sun's local hour angle for zenith 90.8333° (90°50')
    const latRad = this.toRadians(latitude);
    const zenith = 90.8333;
    const cosH =
      (Math.cos(this.toRadians(zenith)) - sinDec * Math.sin(latRad)) /
      (cosDec * Math.cos(latRad));

    let hHours: number;
    if (cosH > 1.0) {
      hHours = isSunrise ? 0.0 : 0.0; // Polar night
    } else if (cosH < -1.0) {
      hHours = isSunrise ? 0.0 : 24.0; // Midnight sun
    } else if (isSunrise) {
      hHours = (360.0 - this.toDegrees(Math.acos(cosH))) / 15.0;
    } else {
      hHours = this.toDegrees(Math.acos(cosH)) / 15.0;
    }

    // Local Mean Time
    const localMeanTime = hHours + raHours - 0.06571 * t - 6.622;

    // Convert to UTC
    let utHours = localMeanTime - lngHour;
    while (utHours < 0.0) utHours += 24.0;
    while (utHours >= 24.0) utHours -= 24.0;

    const hours = Math.floor(utHours);
    const minutesTotal = (utHours - hours) * 60.0;
    const minutes = Math.floor(minutesTotal);
    const seconds = Math.min(59, Math.max(0, Math.round((minutesTotal - minutes) * 60.0)));

    const result = new Date(
      Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), hours, minutes, seconds)
    );
    return result;
  }

  public static calculateSolarTimes(
    date: Date,
    latitude: number,
    longitude: number
  ): SolarTimes {
    const sunriseUtc = this.calculateEventUtc(date, latitude, longitude, true);
    const sunsetUtc = this.calculateEventUtc(date, latitude, longitude, false);

    const sunrise = new Date(sunriseUtc.getTime());
    const sunset = new Date(sunsetUtc.getTime());

    const daylightMs = Math.max(1000, sunset.getTime() - sunrise.getTime());
    const daylightDurationMinutes = Math.floor(daylightMs / (1000 * 60));

    // Navkarsi: Sunrise + 48 minutes (2 Ghadis)
    const navkarsi = new Date(sunrise.getTime() + 48 * 60 * 1000);

    // Porsi: 1 Prahar (1/4th daylight)
    const porsi = new Date(sunrise.getTime() + Math.floor(daylightMs / 4));

    // Sadh-Porsi: 1.5 Prahars (3/8th daylight)
    const sadhPorsi = new Date(sunrise.getTime() + Math.floor((daylightMs * 3) / 8));

    // Purimaddh: 2 Prahars (1/2 daylight / Midday)
    const purimaddh = new Date(sunrise.getTime() + Math.floor(daylightMs / 2));

    // Avaddh: 3 Prahars (3/4th daylight)
    const avaddh = new Date(sunrise.getTime() + Math.floor((daylightMs * 3) / 4));

    // Chauvihar: Sunset (Surya Ast)
    const chauvihar = sunset;

    return {
      date,
      latitude,
      longitude,
      sunrise,
      sunset,
      daylightDurationMinutes,
      navkarsi,
      porsi,
      sadhPorsi,
      purimaddh,
      avaddh,
      chauvihar
    };
  }
}

