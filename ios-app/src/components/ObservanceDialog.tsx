import React from 'react';
import { TithiInfo } from '../types';
import { X, AlertCircle } from 'lucide-react';

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
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-md animate-in fade-in">
      <div className="w-full max-w-sm rounded-[32px] p-5 border border-white/20 bg-black text-white shadow-2xl transition-all">
        <div className="flex items-center justify-between pb-2 border-b border-white/10">
          <h2 className="text-[16.5px] font-bold text-white tracking-tight flex items-center gap-1.5">
            <AlertCircle size={16} />
            <span>{tithiInfo.observanceTitle}</span>
          </h2>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-slate-400 hover:text-white"
          >
            <X size={17} />
          </button>
        </div>

        <div className="py-4 space-y-3.5 text-sm">
          <div>
            <div className="font-bold text-[13px] mb-1 text-white">
              Dietary Disciplines:
            </div>
            <div className="text-[13px] leading-relaxed text-slate-300">
              {tithiInfo.dietaryGuidelines}
            </div>
          </div>

          <div>
            <div className="font-bold text-[13px] mb-1 text-white">
              Recommended Observances:
            </div>
            <div className="text-[13px] leading-relaxed text-slate-300">
              {tithiInfo.recommendedVows}
            </div>
          </div>
        </div>

        <div className="pt-2 text-right border-t border-white/10">
          <button
            onClick={onClose}
            className="w-full py-2.5 text-xs font-bold rounded-2xl bg-white text-black hover:bg-slate-200 transition-transform active:scale-95"
          >
            Got It
          </button>
        </div>
      </div>
    </div>
  );
};
