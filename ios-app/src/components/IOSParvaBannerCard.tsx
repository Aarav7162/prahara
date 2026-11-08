import React from 'react';
import { IOSCard } from './IOSCard';

interface IOSParvaBannerCardProps {
  title: string;
  line1: string;
  line2: string;
  isDark?: boolean;
  onClick?: () => void;
}

export const IOSParvaBannerCard: React.FC<IOSParvaBannerCardProps> = ({
  title,
  line1,
  line2,
  isDark = true,
  onClick
}) => {
  return (
    <IOSCard isDark={isDark} onClick={onClick} className="w-full">
      <div className="flex items-center gap-3.5 px-4 py-3.5">
        {/* Frosted Squircle Container with Logo */}
        <div
          className={`w-[46px] h-[46px] rounded-[15px] flex items-center justify-center shrink-0 border ${
            isDark
              ? 'bg-[#222938] border-[#333D50]'
              : 'bg-[#E8ECF2] border-[#DCE1E9]'
          }`}
        >
          <img
            src="./logo.png"
            alt="Prahara Logo"
            className="w-[36px] h-[36px] rounded-[11px] object-cover"
            onError={(e) => {
              // fallback if logo image fails
              (e.currentTarget as HTMLElement).style.display = 'none';
            }}
          />
        </div>

        {/* Text Details */}
        <div className="flex flex-col flex-1 min-w-0">
          <div
            className={`text-sm font-bold tracking-wide truncate ${
              isDark ? 'text-slate-100' : 'text-slate-900'
            }`}
          >
            {title}
          </div>
          <div
            className={`text-[12.5px] leading-tight truncate ${
              isDark ? 'text-slate-400' : 'text-slate-600'
            }`}
          >
            {line1}
          </div>
          <div
            className={`text-[12.5px] leading-tight truncate ${
              isDark ? 'text-slate-400' : 'text-slate-600'
            }`}
          >
            {line2}
          </div>
        </div>
      </div>
    </IOSCard>
  );
};

