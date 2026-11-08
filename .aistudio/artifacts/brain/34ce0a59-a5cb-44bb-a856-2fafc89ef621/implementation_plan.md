# Jain Daily Pachakkhan & Tithi Tracker

A native Android application built with Jetpack Compose featuring precise astronomical solar calculations (Sunrise, Sunset, Navkarsi, Porsi, Sadh-Porsi, Purimaddh, Avaddh, Chauvihar), accurate lunar elongation Tithi tracking, Parva Tithi alerts and dietary rules, and trilingual Pachakkhan vows with audio recitation in Gujarati, Hindi, and English.

### User Review & Critical Decisions

> [!IMPORTANT]
> The user confirmed the following architectural and domain choices:
> - **Platform**: Native Android app built in Jetpack Compose with Material 3 and Liquid Glass aesthetic.
> - **Tithi System**: Full lunar cycle covering Ekam to Poonam and Amas with Parva Tithi observances (Atham, Chaudas, Pancham, Bij, Poonam, Amas) calibrated to the confirmed reference point (today November 8, 2026 is Sud Chaudas).
> - **Pachakkhan Recitation**: Audio recitation via Android Text-to-Speech & audio playback, complete with Gujarati script, Devanagari (Prakrit/Hindi), English transliteration, and spiritual meanings.

---

## 1. Overview & Core Concept

The **Jain Daily Pachakkhan & Tithi Tracker** is designed for Jain shravaks and shravikas to observe daily vows (Pachakkhans), track Jain Tithis according to sunrise (Surya Uday), and maintain Parva Tithi disciplines (e.g. green vegetable restrictions on Atham/Chaudas, Chauvihar rules, Ayambil, and Upvas).

### Key Features
1. **Astronomy Engine**: Real-time solar math computing exact Sunrise, Sunset, daylight division (Prahars), Navkarsi (Sunrise + 48 min / 2 Ghadis), Porsi (1/4 daylight), Sadh-Porsi (3/8 daylight), Purimaddh (1/2 daylight), Avaddh (3/4 daylight), and Chauvihar.
2. **Jain Tithi & Parva System**: Astronomical lunar elongation algorithm calculating the active Tithi at local sunrise. Prominent badges and alerts for Parva days (Chaudas, Atham, Pancham, Bij, Poonam, Amas) with dietary guidance (e.g. "Green vegetable restriction / Leelo Sakhar niyam").
3. **Trilingual Pachakkhan Vows & Audio Recitation**: Full authentic Prakrit texts for Navkarsi, Porsi, Sadh-Porsi, Purimaddh, Avaddh, Biyashna, Ekashna, Ayambil, Upvas, and Chauvihar, presented in Gujarati, Hindi/Devanagari, and English transliteration with high-clarity native audio recital.
4. **Interactive Monthly Calendar**: Full month grid displaying Gregorian dates alongside Jain Tithis (Sud 1-15, Vad 1-15), color-coded Parva dots, and an expandable bottom sheet with sunrise/sunset, Navkarsi, and fasting recommendations for any selected date.
5. **Location & Offline Autonomy**: Auto GPS location detection with manual fallback to major global Jain centers (Ahmedabad, Mumbai, Palitana, Surat, Delhi, Bengaluru, London, New York, etc.) or custom coordinates. Runs 100% offline with zero external network dependencies.
6. **Reminders & Alerts**: Schedule local notifications for Parva eve (8:00 PM) and sunrise Pachakkhan timings (6:00 AM).

---

## 2. User Experience & Visual Design

### Aesthetic Direction: Liquid Glass & Warm Saffron Minimalist
- **Background Atmosphere**: Warm neutral parchment (`#FDFBF7` / `#FAF6EE`), subtle amber glass borders (`#F3E8D6`), and deep slate typography (`#1E293B`).
- **Accent Tones**: Sacred saffron / warm amber (`#D97706`), deep ochre (`#B45309`), and subtle jade accents for Parva tags.
- **Glassmorphism**: Translucent floating cards with subtle blur effects, layered surface elevations, and smooth progress arcs.
- **Typography Hierarchy**: Distinct, readable serif displays for Jain Tithis and sacred vow titles, paired with crisp sans-serif for numerical timings and countdown timers.

### Key Screens & Navigation
1. **Home Screen (Daily Timings & Active Pachakkhan)**:
   - Header with Gregorian date, current city, and Jain Tithi banner (e.g., *"Kartik Sud Chaudas - Parva Tithi"*).
   - Prominent Parva badge when applicable (e.g. *"CHAUDAS TODAY • Green vegetables restricted / Fasting observance"*).
   - Live daylight countdown card displaying current Prahar, time elapsed, and countdown to next upcoming Pachakkhan deadline (e.g. Navkarsi in 24m).
   - Timings Grid: Cards for Surya Uday, Navkarsi, Porsi, Sadh-Porsi, Purimaddh, Avaddh, and Surya Ast / Chauvihar.
   - Quick "Take Pachakkhan" tap on any card to launch the trilingual vow card with audio playback.
2. **Pachakkhan Vow Reader & Audio Player**:
   - Modal bottom sheet displaying the authentic Prakrit sutra.
   - Language tabs: Gujarati, Hindi (Devanagari), English Transliteration & Meaning.
   - Audio recital controls: Play / Pause, repetition loop, speed control (0.8x, 1.0x, 1.2x).
3. **Calendar Screen**:
   - Month view with smooth month navigation.
   - Day cells with Gregorian day number, Tithi abbreviation (e.g., *Sud 14*), and Parva indicator dots.
   - Interactive selection showing day detail bottom sheet with sunrise, sunset, Paksha, and dietary guidelines.
4. **Settings Screen**:
   - Location manager: Auto GPS or preset cities (Ahmedabad, Mumbai, Surat, Palitana, Rajkot, Jaipur, Delhi, Bengaluru, Chicago, London, Sydney, etc.).
   - Parva notification toggles (Evening alert at 8:00 PM, Morning alert at 6:00 AM).
   - Audio voice settings and language preferences.

---

## 3. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                   MainActivity                         │
│           (Material 3 Liquid Glass Theme)              │
└───────────────────────────┬────────────────────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       ▼                    ▼                    ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  HomeScreen  │     │CalendarScreen│     │SettingsScreen│
└──────┬───────┘     └──────┬───────┘     └──────┬───────┘
       │                    │                    │
       └────────────────────┼────────────────────┘
                            ▼
               ┌─────────────────────────┐
               │    JainViewModel        │
               └────────────┬────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       ▼                    ▼                    ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│SolarAstronomy│     │ JainTithi    │     │Pachakkhan    │
│Engine        │     │ Engine       │     │Repository &  │
│(Sunrise/     │     │(Elongation,  │     │Audio Engine  │
│ Sunset/Prahar│     │ Paksha, Parva│     │(Prakrit/Guj/ │
│ calculations)│     │ observances) │     │ Hin/Eng/TTS) │
└──────────────┘     └──────────────┘     └──────────────┘
```

### Core Modules:
- **SolarAstronomyEngine.kt**: Implements NOAA / Jean Meeus solar position algorithm: Julian Day calculation, Solar Mean Anomaly, Center Equation, Solar Ecliptic Longitude, Declination, Equation of Time, and local zenith hour angles for 90°50' to compute accurate Sunrise and Sunset for any latitude, longitude, and date.
- **JainTithiEngine.kt**: Implements lunar elongation formula:
  $$\Delta \lambda = (\lambda_{\text{Moon}} - \lambda_{\text{Sun}}) \pmod{360^\circ}$$
  $$\text{Tithi Index} = \lfloor \Delta \lambda / 12^\circ \rfloor + 1$$
  Tithi evaluated at local sunrise determines the prevailing Tithi of that day, with Parva classification (Bij, Pancham, Atham, Chaudas, Poonam, Amas) and specific dietary restriction rules.
- **PachakkhanRepository.kt**: Houses complete, authentic Prakrit texts, Gujarati script, Devanagari script, English transliteration, and English explanations for:
  - Navkarsi
  - Porsi & Sadh-Porsi
  - Purimaddh & Avaddh
  - Tivihar & Chauvihar
  - Ekashna & Biyashna
  - Ayambil
  - Upvas
- **AudioEngine.kt**: Manages Android `TextToSpeech` with fine-tuned phonetic pauses and pronunciation parameters for Gujarati and Hindi spiritual chants.

---

## 4. Verification Plan

- **Compilation**: Run `compile_applet` to ensure error-free Kotlin and Jetpack Compose build.
- **Astronomy Verification**: Validate sunrise/sunset and Prahar math against standard astronomical tables for Mumbai and Ahmedabad.
- **Tithi Calibration**: Verify that November 8, 2026 accurately resolves to Sud Chaudas (14th Tithi of Shukla Paksha / Kartik Sud Chaudas) as specified.
- **Audio & TTS Verification**: Validate seamless playback of Pachakkhan vows without audio thread locking.
- **Responsive & Inset Verification**: Verify safe drawing insets, notch avoidance, and smooth bottom navigation padding.
