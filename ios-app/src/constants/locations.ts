import { CityLocation } from '../types';

export const predefinedCities: CityLocation[] = [
  { name: 'Ahmedabad', stateOrCountry: 'Gujarat, India', latitude: 23.0225, longitude: 72.5714 },
  { name: 'Palitana', stateOrCountry: 'Gujarat, India', latitude: 21.5222, longitude: 71.8290 },
  { name: 'Mumbai', stateOrCountry: 'Maharashtra, India', latitude: 19.0760, longitude: 72.8777 },
  { name: 'Surat', stateOrCountry: 'Gujarat, India', latitude: 21.1702, longitude: 72.8311 },
  { name: 'Rajkot', stateOrCountry: 'Gujarat, India', latitude: 22.3039, longitude: 70.8022 },
  { name: 'Jaipur', stateOrCountry: 'Rajasthan, India', latitude: 26.9124, longitude: 75.7873 },
  { name: 'New Delhi', stateOrCountry: 'Delhi, India', latitude: 28.6139, longitude: 77.2090 },
  { name: 'Bengaluru', stateOrCountry: 'Karnataka, India', latitude: 12.9716, longitude: 77.5946 },
  { name: 'Pune', stateOrCountry: 'Maharashtra, India', latitude: 18.5204, longitude: 73.8567 },
  { name: 'Kolkata', stateOrCountry: 'West Bengal, India', latitude: 22.5726, longitude: 88.3639 },
  { name: 'London', stateOrCountry: 'UK', latitude: 51.5074, longitude: -0.1278 },
  { name: 'New York', stateOrCountry: 'USA', latitude: 40.7128, longitude: -74.0060 },
  { name: 'Chicago', stateOrCountry: 'USA', latitude: 41.8781, longitude: -87.6298 },
  { name: 'San Francisco', stateOrCountry: 'USA', latitude: 37.7749, longitude: -122.4194 },
  { name: 'Sydney', stateOrCountry: 'Australia', latitude: -33.8688, longitude: 151.2093 }
];

export const defaultCity: CityLocation = predefinedCities[0];

export function findNearestCity(lat: number, lng: number): CityLocation {
  let nearest = defaultCity;
  let minDistance = Number.MAX_VALUE;

  for (const city of predefinedCities) {
    const dLat = ((city.latitude - lat) * Math.PI) / 180.0;
    const dLng = ((city.longitude - lng) * Math.PI) / 180.0;
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos((lat * Math.PI) / 180.0) *
        Math.cos((city.latitude * Math.PI) / 180.0) *
        Math.sin(dLng / 2) *
        Math.sin(dLng / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const dist = 6371.0 * c;

    if (dist < minDistance) {
      minDistance = dist;
      nearest = city;
    }
  }

  return nearest;
}

