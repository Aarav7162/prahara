import React from 'react';
import { TithiInfo } from '../types';

interface ObservanceDialogProps {
  tithiInfo: TithiInfo;
  isOpen: boolean;
  onClose: () => void;
  isDark?: boolean;
}

export const ObservanceDialog: React.FC<ObservanceDialogProps> = ({
  tithiInfo,
  isOpen,
  onClose,
  isDark = true
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in">
      <div
        className={`w-full max-w-sm rounded-[28px] p-5 border shadow-2xl transition-all ${
          isDark
            ? 'bg-[#161B26] border-white/15 text-slate-100'
            : 'bg-white border-slate-200 text-slate-900'
        }`}
      >
        <h2 className="text-[17px] font-bold pb-2 border-b border-white/10">
          {tithiInfo.observanceTitle}
        </h2>

        <div className="py-4 space-y-3.5 text-sm">
          <div>
            <div
              className={`font-bold text-[13px] mb-1 ${
                isDark ? 'text-white' : 'text-slate-900'
              }`}
            >
              Dietary Disciplines:
            </div>
            <div
              className={`text-[13px] leading-relaxed ${
                isDark ? 'text-slate-300' : 'text-slate-700'
              }`}
            >
              {tithiInfo.dietaryGuidelines}
            </div>
          </div>

          <div>
            <div
              className={`font-bold text-[13px] mb-1 ${
                isDark ? 'text-white' : 'text-slate-900'
              }`}
            >
              Recommended Observances:
            </div>
            <div
              className={`text-[13px] leading-relaxed ${
                isDark ? 'text-slate-300' : 'text-slate-700'
              }`}
            >
              {tithiInfo.recommendedVows}
            </div>
          </div>
        </div>

        <div className="pt-2 text-right border-t border-white/10">
          <button
            onClick={onClose}
            className={`px-5 py-2 text-sm font-bold rounded-full ${
              isDark
                ? 'bg-white/10 hover:bg-white/15 text-white'
                : 'bg-slate-900/10 hover:bg-slate-900/15 text-slate-900'
            }`}
          >
            Got It
          </button>
        </div>
      </div>
    </div>
  );
};

