import React, { useState, useEffect } from 'react';
import { Share, PlusSquare, X } from 'lucide-react';
import { ScreenTab, CityLocation, LanguageCode, PachakkhanItem } from './types';
import { defaultCity } from './constants/locations';
import { HomeScreen } from './screens/HomeScreen';
import { CalendarScreen } from './screens/CalendarScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { TutorialScreen } from './screens/TutorialScreen';
import { PachakkhanModal } from './components/PachakkhanModal';
import { IOSFloatingBottomBar } from './components/IOSFloatingBottomBar';

export const App: React.FC = () => {
  const [currentTab, setCurrentTab] = useState<ScreenTab>('home');
  const [currentLocation, setCurrentLocation] = useState<CityLocation>(() => {
    const saved = localStorage.getItem('prahara_location');
    return saved ? JSON.parse(saved) : defaultCity;
  });
  const [preferredLanguage, setPreferredLanguage] = useState<LanguageCode>(() => {
    return (localStorage.getItem('prahara_lang') as LanguageCode) || 'hi';
  });
  const [isDark, setIsDark] = useState<boolean>(() => {
    const saved = localStorage.getItem('prahara_theme');
    return saved !== null ? saved === 'dark' : true;
  });
  const [showTutorial, setShowTutorial] = useState<boolean>(() => {
    return localStorage.getItem('prahara_tutorial_seen') !== 'true';
  });
  const [selectedPachakkhan, setSelectedPachakkhan] = useState<PachakkhanItem | null>(null);
  const [showInstallBanner, setShowInstallBanner] = useState(false);

  // Detect if running as standalone PWA on iOS
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
    localStorage.setItem('prahara_location', JSON.stringify(loc));
  };

  const handleUpdateLanguage = (lang: LanguageCode) => {
    setPreferredLanguage(lang);
    localStorage.setItem('prahara_lang', lang);
  };

  const handleToggleTheme = () => {
    const next = !isDark;
    setIsDark(next);
    localStorage.setItem('prahara_theme', next ? 'dark' : 'light');
  };

  const handleFinishTutorial = () => {
    setShowTutorial(false);
    localStorage.setItem('prahara_tutorial_seen', 'true');
  };

  const handleReplayTutorial = () => {
    setShowTutorial(true);
  };

  if (showTutorial) {
    return <TutorialScreen onFinish={handleFinishTutorial} isDark={isDark} />;
  }

  return (
    <div
      className={`min-h-screen w-full transition-colors duration-300 relative flex flex-col justify-between ${
        isDark
          ? 'bg-gradient-to-b from-[#11141B] via-[#0B0D12] to-[#06070A] text-slate-100'
          : 'bg-gradient-to-b from-[#F7F8FA] via-[#EDF0F5] to-[#E4E8EF] text-slate-900'
      }`}
    >
      {/* iOS Notched Top Safe Area Spacer */}
      <div className="h-[max(8px,env(safe-area-inset-top))] shrink-0" />

      {/* Main Container */}
      <main className="flex-1 w-full max-w-md mx-auto px-4 pt-1">
        {/* iOS Add to Home Screen banner (shows when opened on iPhone Safari outside standalone) */}
        {showInstallBanner && (
          <div
            className={`mb-3 p-3 rounded-[18px] border text-xs flex items-center justify-between shadow-lg animate-in fade-in ${
              isDark
                ? 'bg-[#181E2C] border-white/20 text-slate-200'
                : 'bg-white border-slate-300 text-slate-900'
            }`}
          >
            <div className="flex items-center gap-2 pr-2">
              <Share size={16} className="text-sky-400 shrink-0" />
              <span>
                Install on your iPhone: tap <span className="font-bold">Share</span> then{' '}
                <span className="font-bold">"Add to Home Screen"</span>
              </span>
            </div>
            <button
              onClick={() => setShowInstallBanner(false)}
              className="p-1 text-slate-400 hover:text-white"
            >
              <X size={16} />
            </button>
          </div>
        )}

        {currentTab === 'home' && (
          <HomeScreen
            currentLocation={currentLocation}
            onUpdateLocation={handleUpdateLocation}
            onSelectPachakkhan={(item) => setSelectedPachakkhan(item)}
            isDark={isDark}
          />
        )}

        {currentTab === 'calendar' && (
          <CalendarScreen currentLocation={currentLocation} isDark={isDark} />
        )}

        {currentTab === 'settings' && (
          <SettingsScreen
            currentLocation={currentLocation}
            onUpdateLocation={handleUpdateLocation}
            preferredLanguage={preferredLanguage}
            onUpdateLanguage={handleUpdateLanguage}
            isDark={isDark}
            onToggleTheme={handleToggleTheme}
            onReplayTutorial={handleReplayTutorial}
          />
        )}
      </main>

      {/* Floating Bottom Navigation Bar */}
      <IOSFloatingBottomBar
        currentTab={currentTab}
        onTabChange={(tab) => setCurrentTab(tab)}
        isDark={isDark}
      />

      {/* Pachakkhan Sutra Modal */}
      <PachakkhanModal
        item={selectedPachakkhan}
        onClose={() => setSelectedPachakkhan(null)}
        preferredLanguage={preferredLanguage}
        isDark={isDark}
      />
    </div>
  );
};

