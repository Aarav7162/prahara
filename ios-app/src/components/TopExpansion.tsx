import React, { useState, useEffect } from 'react';
import { X, Play, Pause, Clock, AlertCircle } from 'lucide-react';
import { PachakkhanItem, TithiInfo, SolarTimes, LanguageCode } from '../types';
import { AudioEngine } from '../engine/AudioEngine';

export interface TopExpansionPayload {
  mode: 'timing' | 'calendar';
  timingItem?: PachakkhanItem;
  timingTitle?: string;
  timingTime?: string;
  tithiInfo?: TithiInfo;
  solarTimes?: SolarTimes;
  date?: Date;
}

interface TopExpansionProps {
  payload: TopExpansionPayload | null;
  onClose: () => void;
  onOpenFullModal?: (item: PachakkhanItem) => void;
  preferredLanguage?: LanguageCode;
}

export const TopExpansion: React.FC<TopExpansionProps> = ({
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

  if (!payload) return null;

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
    <>
      {/* Backdrop overlay to dismiss on tap */}
      <div
        onClick={onClose}
        className="fixed inset-0 z-40 bg-black/50 backdrop-blur-xs animate-in fade-in duration-200"
      />

      {/* Expansion cascading directly out from the physical notch / island at top edge */}
      <div className="fixed top-0 inset-x-0 z-50 flex justify-center pointer-events-none">
        <div
          className="pointer-events-auto w-full max-w-md bg-black text-white rounded-b-[36px] border-b border-x border-white/20 shadow-[0_25px_60px_rgba(0,0,0,0.95)] pt-[max(12px,calc(env(safe-area-inset-top)+6px))] pb-5 px-5 animate-in slide-in-from-top-4 duration-300 ease-[cubic-bezier(0.16,1,0.3,1)]"
        >
          {/* Top Notch Gripper bar (swipe up to dismiss) */}
          <div className="flex justify-center pb-2 cursor-pointer" onClick={onClose}>
            <div className="w-12 h-1 rounded-full bg-white/30" />
          </div>

          {/* TIMING EXPANSION */}
          {payload.mode === 'timing' && payload.timingItem && (
            <div className="space-y-3 pt-1">
              <div className="flex items-start justify-between gap-3">
                <div className="flex-1 min-w-0">
                  <div className="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                    Pachakkhan Timing
                  </div>
                  <div className="text-xl font-black text-white tracking-tight leading-tight mt-0.5">
                    {payload.timingItem.titleGujarati}
                  </div>
                  <div className="text-xs text-slate-400 font-medium">
                    {payload.timingItem.durationDescription}
                  </div>
                </div>

                {payload.timingTime && (
                  <div className="px-3.5 py-1.5 rounded-2xl bg-white/10 border border-white/20 text-right shrink-0">
                    <div className="text-[9.5px] uppercase font-bold text-slate-400">Time</div>
                    <div className="text-lg font-black text-white tabular-nums tracking-tight">
                      {payload.timingTime}
                    </div>
                  </div>
                )}
              </div>

              {/* Sutra Box */}
              <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 text-xs leading-relaxed text-slate-200 font-serif select-text line-clamp-3">
                {preferredLanguage === 'gu'
                  ? payload.timingItem.sutraGujarati
                  : preferredLanguage === 'en'
                  ? payload.timingItem.sutraEnglishTransliteration
                  : payload.timingItem.sutraHindi}
              </div>

              {/* Timing Rule */}
              <div className="text-[11.5px] text-slate-400 flex items-center gap-1.5">
                <Clock size={13} className="text-white shrink-0" />
                <span className="line-clamp-1">{payload.timingItem.timingRule}</span>
              </div>

              {/* Actions */}
              <div className="flex items-center gap-2 pt-1">
                <button
                  onClick={() => handleAudioToggle(payload.timingItem!)}
                  className="flex-1 py-2.5 px-3 rounded-2xl font-bold text-xs bg-white text-black hover:bg-slate-200 flex items-center justify-center gap-1.5 transition-transform active:scale-95 shadow-md"
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
                    className="py-2.5 px-4 rounded-2xl font-bold text-xs bg-white/10 border border-white/20 text-white hover:bg-white/20 transition-all active:scale-95"
                  >
                    View Full Vow
                  </button>
                )}
              </div>
            </div>
          )}

          {/* CALENDAR DATE EXPANSION */}
          {payload.mode === 'calendar' && payload.tithiInfo && (
            <div className="space-y-3 pt-1">
              <div className="flex items-start justify-between gap-2">
                <div>
                  <div className="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                    Selected Tithi
                  </div>
                  <div className="text-lg font-black text-white leading-tight mt-0.5">
                    {payload.tithiInfo.fullName}
                  </div>
                  <div className="text-xs text-slate-400 mt-0.5">
                    {payload.date
                      ? payload.date.toLocaleDateString('en-US', {
                          weekday: 'long',
                          month: 'short',
                          day: 'numeric',
                          year: 'numeric'
                        })
                      : ''}
                  </div>
                </div>

                {payload.tithiInfo.isParva && (
                  <span className="px-3 py-1 rounded-full text-[10.5px] font-extrabold bg-white text-black uppercase tracking-wider shrink-0 shadow-sm">
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
              <div className="p-3 rounded-2xl bg-white/5 border border-white/10 text-xs text-slate-300 leading-relaxed">
                <div className="font-bold text-white mb-0.5 flex items-center gap-1.5 text-[11.5px]">
                  <AlertCircle size={13} /> Dietary Observance
                </div>
                <div>{payload.tithiInfo.dietaryGuidelines}</div>
                <div className="text-white font-semibold mt-1.5">
                  {payload.tithiInfo.recommendedVows}
                </div>
              </div>

              <div className="pt-1">
                <button
                  onClick={onClose}
                  className="w-full py-2.5 rounded-2xl font-bold text-xs bg-white text-black hover:bg-slate-200 transition-transform active:scale-95 shadow-md"
                >
                  Done
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </>
  );
};
