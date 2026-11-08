import React from 'react';
import { Home, Calendar, Settings } from 'lucide-react';
import { ScreenTab } from '../types';

interface IOSFloatingBottomBarProps {
  currentTab: ScreenTab;
  onTabChange: (tab: ScreenTab) => void;
  isDark?: boolean;
}

export const IOSFloatingBottomBar: React.FC<IOSFloatingBottomBarProps> = ({
  currentTab,
  onTabChange,
  isDark = true
}) => {
  const tabs: Array<{ id: ScreenTab; label: string; icon: React.ReactNode }> = [
    { id: 'home', label: 'Home', icon: <Home size={19} /> },
    { id: 'calendar', label: 'Calendar', icon: <Calendar size={19} /> },
    { id: 'settings', label: 'Settings', icon: <Settings size={19} /> }
  ];

  return (
    <div className="fixed bottom-0 inset-x-0 z-40 px-6 pb-[max(12px,env(safe-area-inset-bottom))] pointer-events-none">
      <div
        className={`pointer-events-auto mx-auto max-w-sm rounded-[36px] px-3 py-1.5 flex items-center justify-around border backdrop-blur-2xl transition-all shadow-2xl ${
          isDark
            ? 'bg-[#161B26]/90 border-white/14 shadow-[0_12px_40px_rgba(0,0,0,0.6)]'
            : 'bg-white/90 border-slate-200 shadow-[0_10px_30px_rgba(0,0,0,0.1)]'
        }`}
      >
        {tabs.map((tab) => {
          const isSelected = currentTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => onTabChange(tab.id)}
              className={`flex flex-col items-center justify-center py-1.5 px-5 rounded-[22px] transition-all duration-200 active:scale-90 ${
                isSelected
                  ? isDark
                    ? 'bg-white/10 text-white'
                    : 'bg-slate-900/10 text-slate-900'
                  : isDark
                  ? 'text-slate-400 hover:text-slate-200'
                  : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              <div className={`${isSelected ? 'scale-105' : 'scale-100'} transition-transform`}>
                {tab.icon}
              </div>
              <span
                className={`text-[11px] font-medium mt-0.5 ${
                  isSelected ? 'font-semibold' : ''
                }`}
              >
                {tab.label}
              </span>
            </button>
          );
        })}
      </div>
    </div>
  );
};

