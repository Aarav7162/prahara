import React from 'react';
import { IOSCard } from './IOSCard';

interface IOSHeroSplitCardProps {
  milestoneName: string;
  countdownFormatted: string;
  tithiFullName: string;
  sunriseSunsetRange: string;
  isDark?: boolean;
}

export const IOSHeroSplitCard: React.FC<IOSHeroSplitCardProps> = ({
  milestoneName,
  countdownFormatted,
  tithiFullName,
  sunriseSunsetRange,
  isDark = true
}) => {
  return (
    <IOSCard isDark={isDark} className="w-full">
      <div className="flex items-center px-4.5 py-4.5">
        {/* Left Column: Countdown */}
        <div className="flex-1 flex flex-col justify-center min-w-0 pr-2">
          <div
            className={`text-[14px] font-medium tracking-tight ${
              isDark ? 'text-slate-200' : 'text-slate-800'
            }`}
          >
            {milestoneName}
          </div>
          <div
            className={`text-[27px] font-extrabold tracking-tight mt-0.5 tabular-nums ${
              isDark ? 'text-white' : 'text-slate-950'
            }`}
          >
            {countdownFormatted}
          </div>
        </div>

        {/* Vertical subtle divider */}
        <div
          className={`w-[1px] h-[52px] shrink-0 mx-2 ${
            isDark ? 'bg-[#262E3E]' : 'bg-[#E2E6ED]'
          }`}
        />

        {/* Right Column: Local Tithi & Sunrise-Sunset */}
        <div className="flex-1 flex flex-col justify-center min-w-0 pl-2">
          <div className="text-[11px] font-medium text-slate-400">Local Tithi</div>
          <div
            className={`text-[13px] font-bold truncate leading-snug ${
              isDark ? 'text-slate-100' : 'text-slate-900'
            }`}
          >
            {tithiFullName}
          </div>

          <div className="text-[11px] font-medium text-slate-400 mt-1.5">Sunrise–Sunset</div>
          <div
            className={`text-[12px] font-semibold truncate leading-snug ${
              isDark ? 'text-slate-200' : 'text-slate-800'
            }`}
          >
            {sunriseSunsetRange}
          </div>
        </div>
      </div>
    </IOSCard>
  );
};

