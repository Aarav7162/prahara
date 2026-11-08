import React, { useState } from 'react';
import { ChevronLeft, ChevronRight, Calendar as CalendarIcon, Sun, Sunset, Sparkles, AlertCircle } from 'lucide-react';
import { CityLocation, TithiInfo, SolarTimes } from '../types';
import { JainTithiEngine } from '../engine/JainTithiEngine';
import { SolarAstronomyEngine } from '../engine/SolarAstronomyEngine';
import { IOSCard } from '../components/IOSCard';
import { ParvaBadge } from '../components/ParvaBadge';

interface CalendarScreenProps {
  currentLocation: CityLocation;
  isDark?: boolean;
}

export const CalendarScreen: React.FC<CalendarScreenProps> = ({
  currentLocation,
  isDark = true
}) => {
  const today = new Date();
  const [currentYear, setCurrentYear] = useState(today.getFullYear());
  const [currentMonth, setCurrentMonth] = useState(today.getMonth()); // 0-11
  const [selectedDate, setSelectedDate] = useState<Date>(today);

  const prevMonth = () => {
    if (currentMonth === 0) {
      setCurrentMonth(11);
      setCurrentYear((y) => y - 1);
    } else {
      setCurrentMonth((m) => m - 1);
    }
  };

  const nextMonth = () => {
    if (currentMonth === 11) {
      setCurrentMonth(0);
      setCurrentYear((y) => y + 1);
    } else {
      setCurrentMonth((m) => m + 1);
    }
  };

  const jumpToToday = () => {
    setCurrentYear(today.getFullYear());
    setCurrentMonth(today.getMonth());
    setSelectedDate(today);
  };

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
  const firstDayOfWeek = new Date(currentYear, currentMonth, 1).getDay(); // 0 for Sunday

  const selectedTithi: TithiInfo = React.useMemo(() => {
    return JainTithiEngine.calculateTithi(selectedDate);
  }, [selectedDate]);

  const selectedSolar: SolarTimes = React.useMemo(() => {
    return SolarAstronomyEngine.calculateSolarTimes(
      selectedDate,
      currentLocation.latitude,
      currentLocation.longitude
    );
  }, [selectedDate, currentLocation]);

  const format24 = (d: Date): string => {
    const hours = d.getHours().toString().padStart(2, '0');
    const mins = d.getMinutes().toString().padStart(2, '0');
    return `${hours}:${mins}`;
  };

  return (
    <div className="w-full flex flex-col space-y-3.5 pb-24 animate-in fade-in">
      {/* Header */}
      <div className="flex items-center justify-between px-1">
        <div>
          <h2 className="text-xl font-bold tracking-tight">Jain Tithi Calendar</h2>
          <p className="text-xs text-slate-400 font-semibold">
            {monthNames[currentMonth]} {currentYear}
          </p>
        </div>

        <div className="flex items-center gap-1.5">
          <button
            onClick={jumpToToday}
            className={`px-3 py-1 text-xs font-semibold rounded-full border transition-all active:scale-95 ${
              isDark
                ? 'bg-white/10 border-white/10 text-white hover:bg-white/15'
                : 'bg-black/5 border-black/10 text-slate-900 hover:bg-black/10'
            }`}
          >
            Today
          </button>
          <button
            onClick={prevMonth}
            className={`p-1.5 rounded-full border transition-all active:scale-95 ${
              isDark
                ? 'bg-white/10 border-white/10 text-slate-200 hover:bg-white/15'
                : 'bg-black/5 border-black/10 text-slate-800 hover:bg-black/10'
            }`}
          >
            <ChevronLeft size={16} />
          </button>
          <button
            onClick={nextMonth}
            className={`p-1.5 rounded-full border transition-all active:scale-95 ${
              isDark
                ? 'bg-white/10 border-white/10 text-slate-200 hover:bg-white/15'
                : 'bg-black/5 border-black/10 text-slate-800 hover:bg-black/10'
            }`}
          >
            <ChevronRight size={16} />
          </button>
        </div>
      </div>

      {/* Calendar Card */}
      <IOSCard isDark={isDark} className="p-3.5">
        {/* Day of Week labels */}
        <div className="grid grid-cols-7 gap-1 text-center mb-2">
          {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, idx) => (
            <div
              key={idx}
              className={`text-[11px] font-bold ${
                idx === 0 || idx === 6 ? 'text-slate-400' : 'text-slate-500'
              }`}
            >
              {day}
            </div>
          ))}
        </div>

        {/* Days Grid */}
        <div className="grid grid-cols-7 gap-1.5">
          {/* Empty cells before month start */}
          {Array.from({ length: firstDayOfWeek }).map((_, i) => (
            <div key={`empty-${i}`} className="h-13" />
          ))}

          {/* Month Days */}
          {Array.from({ length: daysInMonth }).map((_, i) => {
            const dayNum = i + 1;
            const date = new Date(currentYear, currentMonth, dayNum);
            const isToday =
              date.getDate() === today.getDate() &&
              date.getMonth() === today.getMonth() &&
              date.getFullYear() === today.getFullYear();
            const isSelected =
              date.getDate() === selectedDate.getDate() &&
              date.getMonth() === selectedDate.getMonth() &&
              date.getFullYear() === selectedDate.getFullYear();

            const tithi = JainTithiEngine.calculateTithi(date);

            return (
              <div
                key={dayNum}
                onClick={() => setSelectedDate(date)}
                className={`relative h-13 rounded-[14px] p-1 flex flex-col items-center justify-between cursor-pointer transition-all border ${
                  isSelected
                    ? isDark
                      ? 'bg-white/20 border-white text-white font-bold shadow-md scale-102'
                      : 'bg-slate-900/10 border-slate-900 text-slate-950 font-bold shadow-md scale-102'
                    : isToday
                    ? isDark
                      ? 'bg-white/10 border-white/30 text-white font-semibold'
                      : 'bg-slate-100 border-slate-300 text-slate-900 font-semibold'
                    : isDark
                    ? 'hover:bg-white/5 border-transparent text-slate-200'
                    : 'hover:bg-slate-50 border-transparent text-slate-800'
                }`}
              >
                {/* Gregorian Day Number */}
                <span className="text-[12.5px] leading-tight">{dayNum}</span>

                {/* Tithi short abbreviation */}
                <span className="text-[9.5px] text-slate-400 truncate max-w-full font-medium">
                  {tithi.shortName}
                </span>

                {/* Parva Indicator Dot */}
                <div className="h-1.5 flex items-center justify-center">
                  {tithi.isParva && (
                    <span
                      className={`w-1.5 h-1.5 rounded-full ${
                        tithi.parvaType === 'CHAUDAS' || tithi.parvaType === 'ATHAM'
                          ? 'bg-emerald-400'
                          : tithi.parvaType === 'POONAM' || tithi.parvaType === 'AMAS'
                          ? 'bg-amber-400'
                          : 'bg-slate-400'
                      }`}
                    />
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </IOSCard>

      {/* Selected Day Details Card */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center justify-between pb-2 border-b border-white/10">
          <div>
            <div className="text-base font-bold">{selectedTithi.fullName}</div>
            <div className="text-xs text-slate-400">
              {selectedDate.toLocaleDateString('en-US', {
                weekday: 'long',
                year: 'numeric',
                month: 'short',
                day: 'numeric'
              })}
            </div>
          </div>

          {selectedTithi.isParva && (
            <ParvaBadge
              text={selectedTithi.tithiName.toUpperCase()}
              isMajorParva={true}
              isDark={isDark}
            />
          )}
        </div>

        {/* Timings row for this day */}
        <div className="grid grid-cols-3 gap-2 py-1">
          <div
            className={`p-2 rounded-[14px] text-center border ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="text-[10.5px] text-slate-400 font-medium">Sunrise</div>
            <div className="text-sm font-bold mt-0.5">{format24(selectedSolar.sunrise)}</div>
          </div>

          <div
            className={`p-2 rounded-[14px] text-center border ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="text-[10.5px] text-slate-400 font-medium">Navkarsi</div>
            <div className="text-sm font-bold mt-0.5">{format24(selectedSolar.navkarsi)}</div>
          </div>

          <div
            className={`p-2 rounded-[14px] text-center border ${
              isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
            }`}
          >
            <div className="text-[10.5px] text-slate-400 font-medium">Sunset</div>
            <div className="text-sm font-bold mt-0.5">{format24(selectedSolar.sunset)}</div>
          </div>
        </div>

        {/* Dietary discipline note */}
        <div
          className={`p-3 rounded-[16px] text-xs border ${
            isDark ? 'bg-[#181E2C] border-white/10' : 'bg-slate-100 border-slate-200'
          }`}
        >
          <div className="font-bold mb-1 flex items-center gap-1.5 text-slate-300">
            <AlertCircle size={13} />
            <span>Dietary Disciplines:</span>
          </div>
          <div className="text-slate-400 leading-relaxed">
            {selectedTithi.dietaryGuidelines}
          </div>
          <div className="text-slate-300 font-semibold mt-2">
            {selectedTithi.recommendedVows}
          </div>
        </div>
      </IOSCard>
    </div>
  );
};

