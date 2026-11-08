export type PachakkhanType =
  | 'NAVKARSI'
  | 'PORSI'
  | 'SADH_PORSI'
  | 'PURIMADDH'
  | 'AVADDH'
  | 'CHAUVIHAR'
  | 'EKASHNA'
  | 'BIYASHNA'
  | 'AYAMBIL'
  | 'UPVAS';

export interface PachakkhanItem {
  id: string;
  type: PachakkhanType;
  titleGujarati: string;
  titleHindi: string;
  titleEnglish: string;
  timingRule: string;
  durationDescription: string;
  sutraGujarati: string;
  sutraHindi: string;
  sutraEnglishTransliteration: string;
  englishMeaning: string;
  benefits: string;
}

export interface CityLocation {
  name: string;
  stateOrCountry: string;
  latitude: Double;
  longitude: Double;
  isGps?: boolean;
  isAutoDetected?: boolean;
}

type Double = number;

export type ParvaType =
  | 'CHAUDAS'
  | 'ATHAM'
  | 'POONAM'
  | 'AMAS'
  | 'PANCHAM'
  | 'BIJ'
  | 'NONE';

export interface TithiInfo {
  tithiNumber: number; // 1 to 30
  tithiName: string; // e.g. "Chaudas", "Atham", "Poonam"
  paksha: string; // "Shukla (Sud)" or "Krishna (Vad)"
  pakshaShort: string; // "Sud" or "Vad"
  monthName: string; // e.g. "Kartik"
  fullName: string; // e.g. "Kartik Sud Chaudas"
  shortName: string; // e.g. "Sud 14"
  isParva: boolean;
  parvaType: ParvaType;
  parvaBadgeText: string;
  observanceTitle: string;
  dietaryGuidelines: string;
  recommendedVows: string;
}

export interface SolarTimes {
  date: Date;
  latitude: number;
  longitude: number;
  sunrise: Date;
  sunset: Date;
  daylightDurationMinutes: number;
  navkarsi: Date;
  porsi: Date;
  sadhPorsi: Date;
  purimaddh: Date;
  avaddh: Date;
  chauvihar: Date;
}

export interface CountdownState {
  nextMilestoneName: string;
  timeRemainingFormatted: string;
  isPast: boolean;
  currentPraharName: string;
  daylightProgressRatio: number;
}

export type ScreenTab = 'home' | 'calendar' | 'settings';

export type LanguageCode = 'gu' | 'hi' | 'en';

