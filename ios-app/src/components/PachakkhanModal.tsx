import React, { useState, useEffect } from 'react';
import { X, Play, Pause, Volume2, Sparkles, Clock, BookOpen, Heart } from 'lucide-react';
import { PachakkhanItem, LanguageCode } from '../types';
import { AudioEngine } from '../engine/AudioEngine';

interface PachakkhanModalProps {
  item: PachakkhanItem | null;
  onClose: () => void;
  preferredLanguage?: LanguageCode;
  isDark?: boolean;
}

export const PachakkhanModal: React.FC<PachakkhanModalProps> = ({
  item,
  onClose,
  preferredLanguage = 'hi',
  isDark = true
}) => {
  const [selectedLang, setSelectedLang] = useState<LanguageCode>(preferredLanguage);
  const audioEngine = AudioEngine.getInstance();
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentId, setCurrentId] = useState<string | null>(null);
  const [speed, setSpeed] = useState(audioEngine.getSpeechRate());

  useEffect(() => {
    setSelectedLang(preferredLanguage);
  }, [preferredLanguage]);

  useEffect(() => {
    const unsubscribe = audioEngine.subscribe(() => {
      setIsPlaying(audioEngine.getIsPlaying());
      setCurrentId(audioEngine.getCurrentlyPlayingId());
      setSpeed(audioEngine.getSpeechRate());
    });
    return () => {
      unsubscribe();
      audioEngine.stop();
    };
  }, []);

  if (!item) return null;

  const isCurrentPlaying = isPlaying && currentId === item.id;

  const getSutraText = () => {
    switch (selectedLang) {
      case 'gu':
        return item.sutraGujarati;
      case 'en':
        return item.sutraEnglishTransliteration;
      case 'hi':
      default:
        return item.sutraHindi;
    }
  };

  const handleTogglePlay = () => {
    if (isCurrentPlaying) {
      audioEngine.stop();
    } else {
      audioEngine.playSutra(getSutraText(), item.id, selectedLang);
    }
  };

  const handleSpeedChange = (newSpeed: number) => {
    audioEngine.setSpeechRate(newSpeed);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/60 backdrop-blur-sm animate-in fade-in">
      <div
        className={`w-full max-w-lg rounded-t-[32px] sm:rounded-[32px] max-h-[90vh] flex flex-col border shadow-2xl transition-all ${
          isDark
            ? 'bg-[#121622] border-white/15 text-slate-100'
            : 'bg-white border-slate-200 text-slate-900'
        }`}
      >
        {/* iOS Pull Handle */}
        <div className="pt-3 pb-1 flex justify-center sm:hidden">
          <div className="w-10 h-1.5 rounded-full bg-slate-500/40" />
        </div>

        {/* Modal Header */}
        <div className="flex items-center justify-between px-6 pt-3 pb-3 border-b border-white/10">
          <div>
            <h2 className="text-xl font-bold tracking-tight">{item.titleGujarati}</h2>
            <p className="text-xs text-slate-400 font-medium">{item.durationDescription}</p>
          </div>
          <button
            onClick={onClose}
            className={`p-2 rounded-full ${
              isDark ? 'hover:bg-white/10 text-slate-400' : 'hover:bg-slate-100 text-slate-600'
            }`}
          >
            <X size={20} />
          </button>
        </div>

        {/* Scrollable Content */}
        <div className="flex-1 overflow-y-auto px-6 py-4 space-y-4 no-scrollbar">
          {/* Language Tabs */}
          <div
            className={`flex p-1 rounded-[16px] border ${
              isDark ? 'bg-[#181D28] border-white/10' : 'bg-[#E8ECF2] border-slate-200'
            }`}
          >
            <button
              onClick={() => setSelectedLang('gu')}
              className={`flex-1 py-1.5 text-xs font-semibold rounded-[12px] transition-all ${
                selectedLang === 'gu'
                  ? isDark
                    ? 'bg-[#2A3448] text-white shadow'
                    : 'bg-white text-slate-900 shadow'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              ગુજરાતી (Guj)
            </button>
            <button
              onClick={() => setSelectedLang('hi')}
              className={`flex-1 py-1.5 text-xs font-semibold rounded-[12px] transition-all ${
                selectedLang === 'hi'
                  ? isDark
                    ? 'bg-[#2A3448] text-white shadow'
                    : 'bg-white text-slate-900 shadow'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              हिन्दी / प्राकृत (Hin)
            </button>
            <button
              onClick={() => setSelectedLang('en')}
              className={`flex-1 py-1.5 text-xs font-semibold rounded-[12px] transition-all ${
                selectedLang === 'en'
                  ? isDark
                    ? 'bg-[#2A3448] text-white shadow'
                    : 'bg-white text-slate-900 shadow'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              English
            </button>
          </div>

          {/* Sutra Box */}
          <div
            className={`p-4 rounded-[20px] border relative ${
              isDark
                ? 'bg-gradient-to-b from-[#181E2C] to-[#141824] border-white/15'
                : 'bg-gradient-to-b from-slate-50 to-white border-slate-200'
            }`}
          >
            <div className="flex items-center justify-between mb-2">
              <span className="text-[11px] uppercase tracking-wider font-bold text-slate-400 flex items-center gap-1.5">
                <BookOpen size={13} /> Sacred Pachakkhan Sutra
              </span>
              <span className="text-[11px] font-semibold text-slate-400">
                {selectedLang === 'gu' ? 'ગુજરાતી' : selectedLang === 'hi' ? 'प्राकृत / देवनागरी' : 'Transliteration'}
              </span>
            </div>
            <p
              className={`text-[16px] leading-relaxed font-medium select-text ${
                selectedLang === 'gu' ? 'font-serif' : ''
              }`}
            >
              {getSutraText()}
            </p>
          </div>

          {/* Audio Recitation Player Card */}
          <div
            className={`p-3.5 rounded-[20px] border flex items-center justify-between gap-3 ${
              isDark
                ? 'bg-[#181D28] border-white/10'
                : 'bg-slate-100 border-slate-200'
            }`}
          >
            <div className="flex items-center gap-3">
              <button
                onClick={handleTogglePlay}
                className={`w-11 h-11 rounded-full flex items-center justify-center shrink-0 transition-transform active:scale-90 ${
                  isCurrentPlaying
                    ? 'bg-rose-500 text-white'
                    : isDark
                    ? 'bg-white text-slate-950 hover:bg-slate-200'
                    : 'bg-slate-950 text-white hover:bg-slate-800'
                }`}
              >
                {isCurrentPlaying ? <Pause size={20} /> : <Play size={20} className="ml-0.5" />}
              </button>
              <div>
                <div className="text-xs font-bold flex items-center gap-1.5">
                  <Volume2 size={13} />
                  <span>{isCurrentPlaying ? 'Reciting Sutra...' : 'Listen Audio Recitation'}</span>
                </div>
                <div className="text-[11px] text-slate-400">
                  {selectedLang === 'gu' ? 'Gujarati' : selectedLang === 'hi' ? 'Hindi / Prakrit' : 'English'} voice
                </div>
              </div>
            </div>

            {/* Speed toggle */}
            <div className="flex items-center gap-1">
              {[0.8, 1.0, 1.2].map((s) => (
                <button
                  key={s}
                  onClick={() => handleSpeedChange(s)}
                  className={`px-2 py-1 rounded-[8px] text-[10.5px] font-bold transition-colors ${
                    Math.abs(speed - s) < 0.05
                      ? isDark
                        ? 'bg-[#2A3448] text-white'
                        : 'bg-white text-slate-900 shadow-sm'
                      : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  {s}x
                </button>
              ))}
            </div>
          </div>

          {/* Timing Rule */}
          <div
            className={`p-3.5 rounded-[18px] border text-xs ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="font-bold flex items-center gap-1.5 mb-1 text-slate-400 uppercase tracking-wide text-[10.5px]">
              <Clock size={12} /> Observance Rule
            </div>
            <div className="font-medium text-slate-200">{item.timingRule}</div>
          </div>

          {/* English Meaning */}
          <div
            className={`p-3.5 rounded-[18px] border text-xs ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="font-bold flex items-center gap-1.5 mb-1 text-slate-400 uppercase tracking-wide text-[10.5px]">
              <Sparkles size={12} /> English Meaning & Exceptions
            </div>
            <div className="leading-relaxed text-slate-300">{item.englishMeaning}</div>
          </div>

          {/* Spiritual Benefits */}
          <div
            className={`p-3.5 rounded-[18px] border text-xs ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="font-bold flex items-center gap-1.5 mb-1 text-slate-400 uppercase tracking-wide text-[10.5px]">
              <Heart size={12} /> Spiritual Benefits
            </div>
            <div className="leading-relaxed text-slate-300">{item.benefits}</div>
          </div>
        </div>

        {/* Modal Bottom Confirm Button */}
        <div className="p-4 border-t border-white/10 pb-[max(16px,env(safe-area-inset-bottom))]">
          <button
            onClick={onClose}
            className={`w-full py-3 rounded-[16px] font-bold text-sm transition-transform active:scale-[0.98] ${
              isDark
                ? 'bg-white text-slate-950 hover:bg-slate-100'
                : 'bg-slate-950 text-white hover:bg-slate-800'
            }`}
          >
            Completed / Vow Taken
          </button>
        </div>
      </div>
    </div>
  );
};

