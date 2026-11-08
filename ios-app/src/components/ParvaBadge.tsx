import React from 'react';

interface ParvaBadgeProps {
  text: string;
  isMajorParva?: boolean;
  isDark?: boolean;
  className?: string;
}

export const ParvaBadge: React.FC<ParvaBadgeProps> = ({
  text,
  isMajorParva = true,
  isDark = true,
  className = ''
}) => {
  return (
    <div
      className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold ${
        isDark ? 'bg-[#222836] text-slate-100' : 'bg-[#E9ECF2] text-slate-900'
      } ${className}`}
    >
      <span
        className={`w-1.5 h-1.5 rounded-full ${
          isMajorParva ? (isDark ? 'bg-white' : 'bg-slate-900') : 'bg-slate-400'
        }`}
      />
      <span>{text}</span>
    </div>
  );
};

