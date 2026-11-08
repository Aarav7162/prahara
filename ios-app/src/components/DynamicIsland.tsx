import React, { useState, useEffect } from 'react';
import {
  Sun,
  Sunrise,
  Sunset,
  Sparkles,
  Clock,
  Volume2,
  Play,
  Pause,
  X,
  ChevronDown,
  Calendar,
  AlertCircle
} from 'lucide-react';
import { PachakkhanItem, TithiInfo, SolarTimes, LanguageCode } from '../types';
import { AudioEngine } from '../engine/AudioEngine';

export interface DynamicIslandPayload {
  mode: 'timing' | 'calendar';
  timingItem?: PachakkhanItem;
  timingTitle?: string;
  timingTime?: string;
  tithiInfo?: TithiInfo;
  solarTimes?: SolarTimes;
  date?: Date;
}

interface DynamicIslandProps {
  payload: DynamicIslandPayload | null;
  onClose: () => void;
  onOpenFullModal?: (item: PachakkhanItem) => void;
  preferredLanguage?: LanguageCode;
}

export const DynamicIsland: React.FC<DynamicIslandProps> = ({
  payload,
  onClose,
  onOpenFullModal,
  preferredLanguage = 'hi'
}) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const audioEngine = AudioEngine.getInstance();

  useEffect(() => {
    const unsub = audioEngine.subscribe(() => {
      setIsPlaying(audioEngine.getIsPlaying());
    });
    return () => unsub();
  }, []);

  const isExpanded = payload !== null;

  const handleAudioToggle = (item: PachakkhanItem) => {
    if (isPlaying) {
      audioEngine.stop();
    } else {
      const text =
        preferredLanguage === 'gu'
          ? item.sutraGujarati
          : preferredLanguage === 'en'
          ? item.sutraEnglishTransliteration
          : item.sutraHindi;
      audioEngine.playSutra(text, item.id, preferredLanguage);
    }
  };

  return (
    <div className="fixed top-0 inset-x-0 z-50 flex justify-center pointer-events-none pt-[max(8px,env(safe-area-inset-top))]">
      <div
        className={`pointer-events-auto transition-all duration-400 ease-[cubic-bezier(0.16,1,0.3,1)] overflow-hidden shadow-2xl border border-white/20 bg-black text-white ${
          isExpanded
            ? 'w-[92vw] max-w-sm rounded-[34px] p-5 shadow-[0_20px_60px_rgba(0,0,0,0.9)] ring-1 ring-white/20'
            : 'w-44 h-8 rounded-full px-3 flex items-center justify-between cursor-pointer hover:scale-102 active:scale-95'
        }`}
      >
        {!isExpanded ? (
          /* Resting Compact Dynamic Island */
          <div className="w-full flex items-center justify-between text-[11px] font-semibold text-slate-200">
            <div className="flex items-center gap-1.5">
              <span className="w-2 h-2 rounded-full bg-white animate-pulse" />
              <span className="tracking-tight text-white font-bold">Prahara</span>
            </div>
            <span className="text-[10px] text-slate-400 font-medium">Island Ready</span>
          </div>
        ) : (
          /* Expanded Island from Notch / Dynamic Island */
          <div className="w-full flex flex-col space-y-3.5 animate-in fade-in zoom-in-95 duration-200">
            {/* Top Bar with Island indicator & Close button */}
            <div className="flex items-center justify-between pb-1 border-b border-white/10">
              <div className="flex items-center gap-2">
                <span className="w-2.5 h-2.5 rounded-full bg-white" />
                <span className="text-[11px] uppercase tracking-wider font-extrabold text-slate-400">
                  {payload.mode === 'timing' ? 'Pachakkhan Alert' : 'Tithi Details'}
                </span>
              </div>

              <button
                onClick={onClose}
                className="w-7 h-7 rounded-full bg-white/10 hover:bg-white/20 flex items-center justify-center text-slate-300 hover:text-white transition-colors"
                aria-label="Close Dynamic Island"
              >
                <X size={15} />
              </button>
            </div>

            {/* TIMING EXPANSION */}
            {payload.mode === 'timing' && payload.timingItem && (
              <div className="space-y-3">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex-1 min-w-0">
                    <div className="text-xl font-black text-white tracking-tight leading-tight">
                      {payload.timingItem.titleGujarati}
                    </div>
                    <div className="text-xs text-slate-400 font-medium mt-0.5">
                      {payload.timingItem.durationDescription}
                    </div>
                  </div>

                  {payload.timingTime && (
                    <div className="px-3 py-1.5 rounded-2xl bg-white/10 border border-white/15 text-right shrink-0">
                      <div className="text-[10px] uppercase font-bold text-slate-400">Time</div>
                      <div className="text-lg font-black text-white tabular-nums tracking-tight">
                        {payload.timingTime}
                      </div>
                    </div>
                  )}
                </div>

                {/* Sutra Box */}
                <div className="p-3 rounded-2xl bg-white/5 border border-white/10 text-xs leading-relaxed text-slate-200 font-serif line-clamp-3 select-text">
                  {preferredLanguage === 'gu'
                    ? payload.timingItem.sutraGujarati
                    : preferredLanguage === 'en'
                    ? payload.timingItem.sutraEnglishTransliteration
                    : payload.timingItem.sutraHindi}
                </div>

                {/* Observance Rule */}
                <div className="text-[11.5px] text-slate-400 flex items-center gap-1.5">
                  <Clock size={12} className="text-white shrink-0" />
                  <span className="line-clamp-1">{payload.timingItem.timingRule}</span>
                </div>

                {/* Action Buttons: Audio Recite & Full Modal */}
                <div className="flex items-center gap-2 pt-1">
                  <button
                    onClick={() => handleAudioToggle(payload.timingItem!)}
                    className="flex-1 py-2.5 px-3 rounded-2xl font-bold text-xs bg-white text-black hover:bg-slate-200 flex items-center justify-center gap-1.5 transition-transform active:scale-95"
                  >
                    {isPlaying ? <Pause size={14} /> : <Play size={14} />}
                    <span>{isPlaying ? 'Pause Recitation' : 'Listen Audio'}</span>
                  </button>

                  {onOpenFullModal && (
                    <button
                      onClick={() => {
                        onClose();
                        onOpenFullModal(payload.timingItem!);
                      }}
                      className="py-2.5 px-4 rounded-2xl font-bold text-xs bg-white/10 border border-white/15 text-white hover:bg-white/20 transition-all active:scale-95"
                    >
                      View Full Vow
                    </button>
                  )}
                </div>
              </div>
            )}

            {/* CALENDAR DATE EXPANSION */}
            {payload.mode === 'calendar' && payload.tithiInfo && (
              <div className="space-y-3">
                <div className="flex items-start justify-between gap-2">
                  <div>
                    <div className="text-lg font-black text-white leading-tight">
                      {payload.tithiInfo.fullName}
                    </div>
                    <div className="text-xs text-slate-400 mt-0.5">
                      {payload.date
                        ? payload.date.toLocaleDateString('en-US', {
                            weekday: 'short',
                            month: 'short',
                            day: 'numeric',
                            year: 'numeric'
                          })
                        : ''}
                    </div>
                  </div>

                  {payload.tithiInfo.isParva && (
                    <span className="px-2.5 py-1 rounded-full text-[10px] font-bold bg-white text-black uppercase tracking-wider shrink-0">
                      {payload.tithiInfo.parvaType} PARVA
                    </span>
                  )}
                </div>

                {/* Solar Timings Grid */}
                {payload.solarTimes && (
                  <div className="grid grid-cols-3 gap-2 text-center text-xs">
                    <div className="p-2 rounded-xl bg-white/5 border border-white/10">
                      <div className="text-[10px] text-slate-400">Sunrise</div>
                      <div className="font-bold text-white mt-0.5">
                        {payload.solarTimes.sunrise.getHours().toString().padStart(2, '0')}:
                        {payload.solarTimes.sunrise.getMinutes().toString().padStart(2, '0')}
                      </div>
                    </div>
                    <div className="p-2 rounded-xl bg-white/5 border border-white/10">
                      <div className="text-[10px] text-slate-400">Navkarsi</div>
                      <div className="font-bold text-white mt-0.5">
                        {payload.solarTimes.navkarsi.getHours().toString().padStart(2, '0')}:
                        {payload.solarTimes.navkarsi.getMinutes().toString().padStart(2, '0')}
                      </div>
                    </div>
                    <div className="p-2 rounded-xl bg-white/5 border border-white/10">
                      <div className="text-[10px] text-slate-400">Sunset</div>
                      <div className="font-bold text-white mt-0.5">
                        {payload.solarTimes.sunset.getHours().toString().padStart(2, '0')}:
                        {payload.solarTimes.sunset.getMinutes().toString().padStart(2, '0')}
                      </div>
                    </div>
                  </div>
                )}

                {/* Dietary guideline */}
                <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 text-xs text-slate-300 leading-relaxed">
                  <div className="font-bold text-white mb-0.5 flex items-center gap-1 text-[11px]">
                    <AlertCircle size={12} /> Dietary Observance
                  </div>
                  <div>{payload.tithiInfo.dietaryGuidelines}</div>
                </div>

                <div className="pt-1">
                  <button
                    onClick={onClose}
                    className="w-full py-2.5 rounded-2xl font-bold text-xs bg-white text-black hover:bg-slate-200 transition-transform active:scale-95"
                  >
                    Done
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

