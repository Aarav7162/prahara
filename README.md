# Prahara (પ્રહર) • A Jain Panchang & Pachakkhan Tracker

A mobile-first Jain Daily Pachakkhan & Tithi Tracker featuring precise NOAA astronomical solar calculations, sacred Parva Tithi observances, and trilingual sutras with serene audio recitations.

Available for both **Android** and **iOS** (standalone installation without App Store).

---

## 📱 Features

- **Astronomical Solar Calculations**: Real-time NOAA / US Naval Observatory solar math computing exact Sunrise, Sunset, Navkarsi (Sunrise + 48 min / 2 Ghadis), Porsi (1/4th daylight), Sadh-Porsi (3/8th), Purimaddh (midday), Avaddh, and Chauvihar.
- **Jain Tithi & Parva System**: Astronomical lunar elongation algorithm tracking Ekam to Poonam (Sud) and Ekam to Amas (Vad) with alerts and dietary guidelines for Parva days (Chaudas, Atham, Poonam, Amas, Pancham, Bij).
- **Trilingual Pachakkhan Vows & Audio**: Full authentic texts for Navkarsi, Porsi, Sadh-Porsi, Purimaddh, Avaddh, Chauvihar, Ekashna, Biyashna, Ayambil, and Upvas in Gujarati, Hindi/Devanagari, and English transliteration with serene voice recitation.
- **Interactive Monthly Calendar**: Full month grid with Tithis, color-coded Parva indicators, and detailed day breakdown.
- **Location & Autonomy**: Device GPS auto-detection with fallback to 15 major global Jain centers (Ahmedabad, Palitana, Mumbai, Surat, Rajkot, Jaipur, Delhi, Bengaluru, etc.). Works **100% offline**.
- **Liquid Glass Design System**: Pure monochrome iOS 18/26 minimalist aesthetic (translucent frosted glass, specular rim-light borders, soft shadows, floating dock).

---

## 🍏 Installing on iPhone Without App Store

See the complete guide in [README_IOS.md](README_IOS.md).

### Quick Install (30 Seconds via Safari)
1. Run local development server or open the deployed link:
   ```bash
   npm run dev
   ```
2. On your iPhone connected to the same Wi-Fi, open **Safari** and visit:
   `http://192.168.0.101:5173` *(or your deployed GitHub Pages URL)*
3. Tap the **Share** button in Safari (square with up arrow).
4. Tap **"Add to Home Screen"** and tap **"Add"**.
5. The Prahara app icon is now on your iPhone Home Screen and runs full-screen with offline support!

---

## 📁 Project Structure

```text
├── app/                  # Android Jetpack Compose application (Kotlin)
├── ios-app/              # iOS Application (Vite + React + TypeScript + Tailwind)
│   ├── src/              # iOS UI components, engines, screens, types
│   ├── public/           # PWA manifest, service worker, icons, logos
│   └── ios/App/          # Capacitor Xcode iOS native project
├── ios-native/           # Pure SwiftUI native iOS codebase
├── .github/workflows/    # CI/CD workflows (GitHub Pages & iOS IPA build)
├── README_IOS.md         # Detailed iPhone installation guide
└── package.json          # Root convenience scripts
```

---

## 🛠️ Development Scripts

```bash
# Start iOS development server (accessible over local Wi-Fi)
npm run dev

# Build production iOS app and sync to Xcode project
npm run build

# Preview production build locally
npm run preview

# Open iOS project in Xcode (macOS only)
npm run ios:open
```

---

## 🙏 Sevabhāv & Acknowledgments

Prahara is developed as a community seva for Jinashasana with zero ads and open-source spirit.
- UPI Support: `8850783566@fam`

