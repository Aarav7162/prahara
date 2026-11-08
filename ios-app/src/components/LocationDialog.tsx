import React, { useState } from 'react';
import { Navigation, Check, X, Loader2 } from 'lucide-react';
import { CityLocation } from '../types';
import { predefinedCities } from '../constants/locations';
import { LocationEngine } from '../engine/LocationEngine';

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
  const [statusMsg, setStatusMsg] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleAutoDetect = async () => {
    setIsLocating(true);
    setStatusMsg('Detecting location (GPS / Network)...');

    try {
      const detected = await LocationEngine.detectLocation();
      setStatusMsg(`Detected: ${detected.name} (${detected.latitude.toFixed(2)}°, ${detected.longitude.toFixed(2)}°)`);
      setTimeout(() => {
        onSelectLocation(detected);
        setIsLocating(false);
        onClose();
      }, 700);
    } catch (err: any) {
      setIsLocating(false);
      setStatusMsg(err.message || 'Location unavailable. Please select your city below.');
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-md animate-in fade-in">
      <div className="w-full max-w-sm rounded-[32px] p-5 border border-white/20 bg-black text-white shadow-2xl transition-all">
        <div className="flex items-center justify-between pb-3 border-b border-white/10">
          <h2 className="text-[17px] font-bold text-white tracking-tight">Location & Solar Position</h2>
          <button
            onClick={onClose}
            className="p-1.5 rounded-full hover:bg-white/10 text-slate-400 hover:text-white transition-colors"
          >
            <X size={18} />
          </button>
        </div>

        <div className="py-3">
          {/* Auto-detect button */}
          <button
            onClick={handleAutoDetect}
            disabled={isLocating}
            className="w-full flex items-center justify-center gap-2 py-3 px-4 rounded-[18px] font-bold text-xs bg-white text-black hover:bg-slate-200 transition-all active:scale-[0.98] shadow-md"
          >
            {isLocating ? (
              <Loader2 size={16} className="animate-spin text-black" />
            ) : (
              <Navigation size={16} className="text-black" />
            )}
            <span>{isLocating ? 'Detecting Location...' : 'Auto-Detect Device Location'}</span>
          </button>

          {statusMsg && (
            <p className="text-xs text-slate-300 mt-2 text-center font-medium bg-white/5 py-1 px-2 rounded-lg border border-white/10">
              {statusMsg}
            </p>
          )}

          <div className="text-[11px] font-bold text-slate-400 mt-4 mb-2 uppercase tracking-wider">
            Or select major Jain center:
          </div>

          <div className="max-h-[260px] overflow-y-auto space-y-1.5 pr-1 no-scrollbar">
            {predefinedCities.map((city) => {
              const isSelected = currentLocation.name === city.name && !currentLocation.isAutoDetected;
              return (
                <div
                  key={city.name}
                  onClick={() => {
                    onSelectLocation({ ...city, isGps: false, isAutoDetected: false });
                    onClose();
                  }}
                  className={`flex items-center justify-between px-3.5 py-2.5 rounded-[14px] cursor-pointer transition-colors border ${
                    isSelected
                      ? 'bg-white text-black font-extrabold border-white shadow-sm'
                      : 'bg-white/5 hover:bg-white/10 border-white/5 text-slate-200'
                  }`}
                >
                  <div>
                    <div className="text-sm font-bold">{city.name}</div>
                    <div className={`text-[11px] ${isSelected ? 'text-black/70' : 'text-slate-400'}`}>
                      {city.stateOrCountry}
                    </div>
                  </div>
                  {isSelected && <Check size={17} className="text-black" />}
                </div>
              );
            })}
          </div>
        </div>

        <div className="pt-2 text-right border-t border-white/10">
          <button
            onClick={onClose}
            className="px-4 py-2 text-xs font-bold text-slate-300 hover:text-white"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};
