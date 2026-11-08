import { CityLocation } from '../types';
import { findNearestCity } from '../constants/locations';

export class LocationEngine {
  public static async detectLocation(): Promise<CityLocation> {
    // 1. Try Device GPS first
    if (typeof window !== 'undefined' && 'geolocation' in navigator) {
      try {
        const gpsLocation = await this.getGpsCoordinates();
        const cityName = await this.reverseGeocode(
          gpsLocation.latitude,
          gpsLocation.longitude
        );

        return {
          name: cityName || gpsLocation.nearestCity.name,
          stateOrCountry: gpsLocation.nearestCity.stateOrCountry,
          latitude: gpsLocation.latitude,
          longitude: gpsLocation.longitude,
          isGps: true,
          isAutoDetected: true
        };
      } catch (gpsError) {
        console.warn('Device GPS unavailable, falling back to IP Geolocation:', gpsError);
      }
    }

    // 2. Fallback to IP-based Geolocation (works over HTTP, indoors, or if GPS is blocked)
    try {
      const ipLocation = await this.getIpLocation();
      return ipLocation;
    } catch (ipError) {
      console.warn('IP Geolocation failed:', ipError);
      throw new Error('Could not auto-detect location. Please choose a city from the list.');
    }
  }

  private static getGpsCoordinates(): Promise<{
    latitude: number;
    longitude: number;
    nearestCity: CityLocation;
  }> {
    return new Promise((resolve, reject) => {
      // First try with enableHighAccuracy: true, timeout: 6000ms
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          const lat = pos.coords.latitude;
          const lng = pos.coords.longitude;
          const nearest = findNearestCity(lat, lng);
          resolve({ latitude: lat, longitude: lng, nearestCity: nearest });
        },
        () => {
          // If high accuracy fails/times out, retry with cellular/wifi coarse accuracy
          navigator.geolocation.getCurrentPosition(
            (pos) => {
              const lat = pos.coords.latitude;
              const lng = pos.coords.longitude;
              const nearest = findNearestCity(lat, lng);
              resolve({ latitude: lat, longitude: lng, nearestCity: nearest });
            },
            (err) => reject(err),
            { enableHighAccuracy: false, timeout: 8000, maximumAge: 60000 }
          );
        },
        { enableHighAccuracy: true, timeout: 5000, maximumAge: 30000 }
      );
    });
  }

  private static async reverseGeocode(lat: number, lng: number): Promise<string | null> {
    try {
      const controller = new AbortController();
      const id = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(
        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=10`,
        { signal: controller.signal }
      );
      clearTimeout(id);
      if (res.ok) {
        const data = await res.json();
        const city =
          data.address?.city ||
          data.address?.town ||
          data.address?.state_district ||
          data.address?.state;
        return city || null;
      }
    } catch {}
    return null;
  }

  private static async getIpLocation(): Promise<CityLocation> {
    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), 5000);

    // Try ipwho.is (fast, HTTPS, CORS-friendly, zero API key)
    const res = await fetch('https://ipwho.is/', { signal: controller.signal });
    clearTimeout(id);

    if (!res.ok) {
      throw new Error('IP service responded with error');
    }

    const data = await res.json();
    if (data.success === false) {
      throw new Error(data.message || 'IP lookup failed');
    }

    const lat = data.latitude;
    const lng = data.longitude;
    const nearest = findNearestCity(lat, lng);

    return {
      name: data.city || nearest.name,
      stateOrCountry: `${data.region || nearest.stateOrCountry}, ${data.country_code || 'IN'}`,
      latitude: lat,
      longitude: lng,
      isGps: false,
      isAutoDetected: true
    };
  }
}

