import React, { useState } from 'react';
import { Navigation, Check, X } from 'lucide-react';
import { CityLocation } from '../types';
import { predefinedCities, findNearestCity } from '../constants/locations';

interface LocationDialogProps {
  currentLocation: CityLocation;
  isOpen: boolean;
  onClose: () => void;
  onSelectLocation: (city: CityLocation) => void;
  isDark?: boolean;
}

export const LocationDialog: React.FC<LocationDialogProps> = ({
  currentLocation,
  isOpen,
  onClose,
  onSelectLocation,
  isDark = true
}) => {
  const [isLocating, setIsLocating] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleGpsDetect = () => {
    if (!navigator.geolocation) {
      setErrorMsg('Geolocation is not supported by your browser.');
      return;
    }

    setIsLocating(true);
    setErrorMsg(null);

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setIsLocating(false);
        const { latitude, longitude } = pos.coords;
        const nearest = findNearestCity(latitude, longitude);
        onSelectLocation({
          ...nearest,
          latitude,
          longitude,
          isGps: true,
          isAutoDetected: true
        });
        onClose();
      },
      (err) => {
        setIsLocating(false);
        setErrorMsg('Unable to retrieve your location. Please select a city manually.');
      },
      { timeout: 10000, enableHighAccuracy: true }
    );
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in">
      <div
        className={`w-full max-w-sm rounded-[28px] p-5 border shadow-2xl transition-all ${
          isDark
            ? 'bg-[#161B26] border-white/15 text-slate-100'
            : 'bg-white border-slate-200 text-slate-900'
        }`}
      >
        <div className="flex items-center justify-between pb-3 border-b border-white/10">
          <h2 className="text-[17px] font-bold">Location & Solar Position</h2>
          <button
            onClick={onClose}
            className={`p-1.5 rounded-full ${
              isDark ? 'hover:bg-white/10 text-slate-400' : 'hover:bg-slate-100 text-slate-600'
            }`}
          >
            <X size={18} />
          </button>
        </div>

        <div className="py-3">
          {/* Auto-detect button */}
          <button
            onClick={handleGpsDetect}
            disabled={isLocating}
            className={`w-full flex items-center justify-center gap-2 py-3 px-4 rounded-[16px] font-semibold text-sm transition-all active:scale-[0.98] ${
              isDark
                ? 'bg-[#222938] hover:bg-[#2A3448] text-white border border-white/10'
                : 'bg-[#E8ECF2] hover:bg-[#DEE3EB] text-slate-900 border border-slate-300'
            }`}
          >
            <Navigation size={16} className={isLocating ? 'animate-spin' : ''} />
            <span>{isLocating ? 'Detecting GPS...' : 'Auto-Detect Device GPS Location'}</span>
          </button>

          {errorMsg && (
            <p className="text-xs text-rose-400 mt-2 text-center">{errorMsg}</p>
          )}

          <div className="text-[11.5px] font-semibold text-slate-400 mt-4 mb-2 uppercase tracking-wider">
            Or choose major Jain center:
          </div>

          <div className="max-h-[260px] overflow-y-auto space-y-1 pr-1 no-scrollbar">
            {predefinedCities.map((city) => {
              const isSelected = currentLocation.name === city.name && !currentLocation.isAutoDetected;
              return (
                <div
                  key={city.name}
                  onClick={() => {
                    onSelectLocation({ ...city, isGps: false, isAutoDetected: false });
                    onClose();
                  }}
                  className={`flex items-center justify-between px-3.5 py-2.5 rounded-[12px] cursor-pointer transition-colors ${
                    isSelected
                      ? isDark
                        ? 'bg-[#2A3448] text-white font-bold'
                        : 'bg-[#E8ECF2] text-slate-900 font-bold'
                      : isDark
                      ? 'hover:bg-white/5 text-slate-200'
                      : 'hover:bg-slate-50 text-slate-800'
                  }`}
                >
                  <div>
                    <div className="text-sm font-semibold">{city.name}</div>
                    <div className="text-[11px] text-slate-400">{city.stateOrCountry}</div>
                  </div>
                  {isSelected && <Check size={17} className={isDark ? 'text-white' : 'text-slate-900'} />}
                </div>
              );
            })}
          </div>
        </div>

        <div className="pt-2 text-right">
          <button
            onClick={onClose}
            className={`px-4 py-2 text-sm font-bold ${
              isDark ? 'text-slate-300 hover:text-white' : 'text-slate-700 hover:text-slate-950'
            }`}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

