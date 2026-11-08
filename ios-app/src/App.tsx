import React, { useState, useEffect } from 'react';
import { Share, X } from 'lucide-react';
import { ScreenTab, CityLocation, LanguageCode, PachakkhanItem } from './types';
import { defaultCity } from './constants/locations';
import { HomeScreen } from './screens/HomeScreen';
import { CalendarScreen } from './screens/CalendarScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { TutorialScreen } from './screens/TutorialScreen';
import { PachakkhanModal } from './components/PachakkhanModal';
import { IOSFloatingBottomBar } from './components/IOSFloatingBottomBar';
import { DynamicIsland, DynamicIslandPayload } from './components/DynamicIsland';

export const App: React.FC = () => {
  const [currentTab, setCurrentTab] = useState<ScreenTab>('home');
  const [currentLocation, setCurrentLocation] = useState<CityLocation>(() => {
    try {
      const saved = localStorage.getItem('prahara_location');
      return saved ? JSON.parse(saved) : defaultCity;
    } catch {
      return defaultCity;
    }
  });

  const [preferredLanguage, setPreferredLanguage] = useState<LanguageCode>(() => {
    try {
      return (localStorage.getItem('prahara_lang') as LanguageCode) || 'hi';
    } catch {
      return 'hi';
    }
  });

  const [isDark, setIsDark] = useState<boolean>(true); // Strictly pure monochrome dark aesthetic

  // Only show tutorial if explicitly requested; never force it repeatedly
  const [showTutorial, setShowTutorial] = useState<boolean>(false);

  const [selectedPachakkhan, setSelectedPachakkhan] = useState<PachakkhanItem | null>(null);
  const [islandPayload, setIslandPayload] = useState<DynamicIslandPayload | null>(null);
  const [showInstallBanner, setShowInstallBanner] = useState(false);

  // Detect iOS standalone mode
  useEffect(() => {
    const isStandalone =
      window.matchMedia('(display-mode: standalone)').matches ||
      // @ts-ignore
      window.navigator.standalone === true;
    const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
    if (isIOS && !isStandalone) {
      setShowInstallBanner(true);
    }
  }, []);

  const handleUpdateLocation = (loc: CityLocation) => {
    setCurrentLocation(loc);
    try {
      localStorage.setItem('prahara_location', JSON.stringify(loc));
    } catch {}
  };

  const handleUpdateLanguage = (lang: LanguageCode) => {
    setPreferredLanguage(lang);
    try {
      localStorage.setItem('prahara_lang', lang);
    } catch {}
  };

  const handleToggleTheme = () => {
    setIsDark((prev) => !prev);
  };

  if (showTutorial) {
    return <TutorialScreen onFinish={() => setShowTutorial(false)} isDark={isDark} />;
  }

  return (
    <div
      className={`min-h-screen w-full transition-colors duration-300 relative flex flex-col justify-between ${
        isDark
          ? 'bg-black text-white'
          : 'bg-[#F7F8FA] text-black'
      }`}
    >
      {/* Dynamic Island / Notch Expansion at Top */}
      <DynamicIsland
        payload={islandPayload}
        onClose={() => setIslandPayload(null)}
        onOpenFullModal={(item) => {
          setSelectedPachakkhan(item);
        }}
        preferredLanguage={preferredLanguage}
      />

      {/* iOS Notched Top Safe Area Spacer */}
      <div className="h-[max(48px,calc(env(safe-area-inset-top)+14px))] shrink-0" />

      {/* Main Container */}
      <main className="flex-1 w-full max-w-md mx-auto px-4 pt-1">
        {/* iOS Add to Home Screen Banner */}
        {showInstallBanner && (
          <div
            className={`mb-3 p-3 rounded-[20px] border text-xs flex items-center justify-between shadow-lg animate-in fade-in ${
              isDark
                ? 'bg-[#121622] border-white/20 text-white'
                : 'bg-white border-black/20 text-black'
            }`}
          >
            <div className="flex items-center gap-2 pr-2">
              <Share size={15} className="text-white shrink-0" />
              <span>
                Install as iPhone App: tap <span className="font-bold underline">Share</span> then{' '}
                <span className="font-bold underline">"Add to Home Screen"</span>
              </span>
            </div>
            <button
              onClick={() => setShowInstallBanner(false)}
              className="p-1 text-slate-400 hover:text-white"
            >
              <X size={15} />
            </button>
          </div>
        )}

        {currentTab === 'home' && (
          <HomeScreen
            currentLocation={currentLocation}
            onUpdateLocation={handleUpdateLocation}
            onSelectPachakkhan={(item, timeFormatted, title) => {
              // Smoothly expand from Notch / Dynamic Island!
              setIslandPayload({
                mode: 'timing',
                timingItem: item,
                timingTitle: title,
                timingTime: timeFormatted
              });
            }}
            isDark={isDark}
          />
        )}

        {currentTab === 'calendar' && (
          <CalendarScreen
            currentLocation={currentLocation}
            isDark={isDark}
            onSelectDateCell={(date, tithi, solar) => {
              // Smoothly expand from Notch / Dynamic Island!
              setIslandPayload({
                mode: 'calendar',
                date: date,
                tithiInfo: tithi,
                solarTimes: solar
              });
            }}
          />
        )}

        {currentTab === 'settings' && (
          <SettingsScreen
            currentLocation={currentLocation}
            onUpdateLocation={handleUpdateLocation}
            preferredLanguage={preferredLanguage}
            onUpdateLanguage={handleUpdateLanguage}
            isDark={isDark}
            onToggleTheme={handleToggleTheme}
            onReplayTutorial={() => setShowTutorial(true)}
          />
        )}
      </main>

      {/* Floating Bottom Navigation Bar */}
      <IOSFloatingBottomBar
        currentTab={currentTab}
        onTabChange={(tab) => {
          setIslandPayload(null);
          setCurrentTab(tab);
        }}
        isDark={isDark}
      />

      {/* Full Pachakkhan Sutra Modal */}
      <PachakkhanModal
        item={selectedPachakkhan}
        onClose={() => setSelectedPachakkhan(null)}
        preferredLanguage={preferredLanguage}
        isDark={isDark}
      />
    </div>
  );
};
