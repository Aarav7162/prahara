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
      className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold border border-white/20 bg-white text-black shadow-sm ${className}`}
    >
      <span className="w-1.5 h-1.5 rounded-full bg-black" />
      <span>{text}</span>
    </div>
  );
};

