import React from 'react';

interface IOSCardProps {
  children: React.ReactNode;
  className?: string;
  onClick?: () => void;
  isDark?: boolean;
}

export const IOSCard: React.FC<IOSCardProps> = ({
  children,
  className = '',
  onClick,
  isDark = true
}) => {
  return (
    <div
      onClick={onClick}
      className={`relative rounded-[24px] transition-all duration-200 ${
        onClick ? 'cursor-pointer active:scale-[0.985]' : ''
      } ${
        isDark
          ? 'bg-gradient-to-b from-[#181D28]/90 to-[#121622]/90 border border-white/12 shadow-[0_8px_30px_rgb(0,0,0,0.35)]'
          : 'bg-gradient-to-b from-white/95 to-slate-50/90 border border-[#DEE3EB] shadow-[0_4px_20px_rgba(10,16,32,0.06)]'
      } ${className}`}
    >
      {/* Specular top highlight */}
      <div className="absolute inset-x-0 top-0 h-[1px] bg-gradient-to-r from-transparent via-white/20 to-transparent rounded-t-[24px]" />
      {children}
    </div>
  );
};

