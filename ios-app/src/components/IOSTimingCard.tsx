import React from 'react';

interface IOSTimingCardProps {
  title: string;
  timeFormatted: string;
  icon: React.ReactNode;
  progressFraction: number;
  isCurrentActive?: boolean;
  isDark?: boolean;
  onClick: () => void;
}

export const IOSTimingCard: React.FC<IOSTimingCardProps> = ({
  title,
  timeFormatted,
  icon,
  progressFraction,
  isCurrentActive = false,
  isDark = true,
  onClick
}) => {
  const clampedProgress = Math.min(1, Math.max(0, progressFraction));

  return (
    <div
      onClick={onClick}
      className={`relative flex flex-col items-center justify-between p-2.5 rounded-[22px] transition-all duration-200 cursor-pointer active:scale-95 ${
        isDark
          ? `bg-gradient-to-b from-[#181D28]/90 to-[#121622]/90 ${
              isCurrentActive
                ? 'border-[1.5px] border-slate-300 shadow-[0_0_16px_rgba(255,255,255,0.12)]'
                : 'border border-white/12 shadow-[0_4px_16px_rgba(0,0,0,0.3)]'
            }`
          : `bg-gradient-to-b from-white/95 to-slate-50/90 ${
              isCurrentActive
                ? 'border-[1.5px] border-slate-800 shadow-[0_4px_16px_rgba(0,0,0,0.1)]'
                : 'border border-[#DEE3EB] shadow-[0_2px_10px_rgba(10,16,32,0.04)]'
            }`
      }`}
      style={{ aspectRatio: '0.86 / 1' }}
    >
      {/* Top Specular Rim */}
      <div className="absolute inset-x-0 top-0 h-[1px] bg-gradient-to-r from-transparent via-white/20 to-transparent rounded-t-[22px]" />

      {/* Frosted Circular Glyph Pill */}
      <div
        className={`w-[34px] h-[34px] rounded-full flex items-center justify-center shrink-0 border ${
          isCurrentActive
            ? 'bg-white text-black border-white shadow-sm'
            : 'bg-white/10 border-white/15 text-white'
        }`}
      >
        {icon}
      </div>

      {/* Timing Title */}
      <div className="text-[12px] font-semibold tracking-tight text-center line-clamp-1 text-slate-300">
        {title}
      </div>

      {/* Large Time Digits */}
      <div className="text-[17px] font-extrabold tracking-tight text-center tabular-nums text-white">
        {timeFormatted}
      </div>

      {/* Mini Capsule Progress Indicator */}
      <div className="w-[78%] h-[4px] rounded-full overflow-hidden bg-white/10">
        <div
          className="h-full rounded-full transition-all duration-300 bg-white"
          style={{ width: `${clampedProgress * 100}%` }}
        />
      </div>
    </div>
  );
};

