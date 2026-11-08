import React, { useState, useEffect, useRef } from 'react';
import {
  MapPin,
  RefreshCw,
  Sun,
  Sunrise,
  Sunset,
  Clock,
  Sparkles,
  Utensils,
  Moon,
  Droplet,
  ChevronDown
} from 'lucide-react';
import { CityLocation, SolarTimes, TithiInfo, CountdownState, PachakkhanItem } from '../types';
import { SolarAstronomyEngine } from '../engine/SolarAstronomyEngine';
import { JainTithiEngine } from '../engine/JainTithiEngine';
import { getPachakkhan } from '../repository/PachakkhanRepository';
import { IOSParvaBannerCard } from '../components/IOSParvaBannerCard';
import { IOSHeroSplitCard } from '../components/IOSHeroSplitCard';
import { IOSTimingCard } from '../components/IOSTimingCard';
import { LocationDialog } from '../components/LocationDialog';
import { ObservanceDialog } from '../components/ObservanceDialog';

interface HomeScreenProps {
  currentLocation: CityLocation;
  onUpdateLocation: (loc: CityLocation) => void;
  onSelectPachakkhan: (item: PachakkhanItem, timeFormatted?: string, title?: string) => void;
  isDark?: boolean;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  currentLocation,
  onUpdateLocation,
  onSelectPachakkhan,
  isDark = true
}) => {
  const [showLocationDialog, setShowLocationDialog] = useState(false);
  const [showObservanceDialog, setShowObservanceDialog] = useState(false);
  const [currentPage, setCurrentPage] = useState<0 | 1>(0);
  const [now, setNow] = useState(new Date());

  const carouselRef = useRef<HTMLDivElement>(null);
  const touchStartX = useRef<number | null>(null);

  // Update clock every second
  useEffect(() => {
    const timer = setInterval(() => {
      setNow(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Compute solar times
  const solarTimes: SolarTimes = React.useMemo(() => {
    return SolarAstronomyEngine.calculateSolarTimes(
      now,
      currentLocation.latitude,
      currentLocation.longitude
    );
  }, [now.toDateString(), currentLocation.latitude, currentLocation.longitude]);

  // Compute Tithi Info
  const tithiInfo: TithiInfo = React.useMemo(() => {
    return JainTithiEngine.calculateTithi(now);
  }, [now.toDateString()]);

  // Format 24h & 12h range
  const format24 = (d: Date): string => {
    const hours = d.getHours().toString().padStart(2, '0');
    const mins = d.getMinutes().toString().padStart(2, '0');
    return `${hours}:${mins}`;
  };

  const format12 = (d: Date): string => {
    let hours = d.getHours();
    const mins = d.getMinutes().toString().padStart(2, '0');
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    return `${hours.toString().padStart(2, '0')}:${mins} ${ampm}`;
  };

  const sunriseSunsetStr = `${format12(solarTimes.sunrise)} - ${format12(solarTimes.sunset)} IST`;

  // Compute countdown state
  const countdownState: CountdownState = React.useMemo(() => {
    const milestones = [
      { name: 'Sunrise in', time: solarTimes.sunrise },
      { name: 'Navkarsi in', time: solarTimes.navkarsi },
      { name: 'Porsi in', time: solarTimes.porsi },
      { name: 'Sadh-Porsi in', time: solarTimes.sadhPorsi },
      { name: 'Purimaddh in', time: solarTimes.purimaddh },
      { name: 'Avaddh in', time: solarTimes.avaddh },
      { name: 'Chauvihar in', time: solarTimes.sunset }
    ];

    const upcoming = milestones.find((m) => m.time.getTime() > now.getTime());

    if (upcoming) {
      const diffMs = upcoming.time.getTime() - now.getTime();
      const totalSec = Math.floor(diffMs / 1000);
      const hours = Math.floor(totalSec / 3600);
      const mins = Math.floor((totalSec % 3600) / 60);
      const secs = totalSec % 60;

      const formatted =
        hours > 0
          ? `${hours}h ${mins}m ${secs}s`
          : `${mins}m ${secs.toString().padStart(2, '0')}s`;

      return {
        nextMilestoneName: upcoming.name,
        timeRemainingFormatted: formatted,
        isPast: false,
        currentPraharName: 'Daylight Phase',
        daylightProgressRatio: 0.5
      };
    } else {
      const tomorrow = new Date(now.getTime() + 24 * 60 * 60 * 1000);
      const tomorrowSolar = SolarAstronomyEngine.calculateSolarTimes(
        tomorrow,
        currentLocation.latitude,
        currentLocation.longitude
      );
      const diffMs = tomorrowSolar.sunrise.getTime() - now.getTime();
      const totalSec = Math.max(0, Math.floor(diffMs / 1000));
      const hours = Math.floor(totalSec / 3600);
      const mins = Math.floor((totalSec % 3600) / 60);
      const secs = totalSec % 60;

      return {
        nextMilestoneName: 'Sunrise in',
        timeRemainingFormatted: `${hours}h ${mins}m ${secs}s`,
        isPast: false,
        currentPraharName: 'Night Chauvihar',
        daylightProgressRatio: 1.0
      };
    }
  }, [now, solarTimes, currentLocation]);

  const calcProgress = (target: Date): number => {
    const elapsed = now.getTime() - solarTimes.sunrise.getTime();
    const total = target.getTime() - solarTimes.sunrise.getTime();
    if (total <= 0) return 0;
    return Math.min(1, Math.max(0, elapsed / total));
  };

  const handleScroll = () => {
    if (!carouselRef.current) return;
    const { scrollLeft, clientWidth } = carouselRef.current;
    if (clientWidth === 0) return;
    const pageIndex = Math.round(scrollLeft / clientWidth);
    if (pageIndex === 0 || pageIndex === 1) {
      setCurrentPage(pageIndex);
    }
  };

  const scrollToPage = (pageIndex: 0 | 1) => {
    if (!carouselRef.current) return;
    carouselRef.current.scrollTo({
      left: pageIndex * carouselRef.current.clientWidth,
      behavior: 'smooth'
    });
    setCurrentPage(pageIndex);
  };

  const handleTouchStart = (e: React.TouchEvent) => {
    touchStartX.current = e.touches[0].clientX;
  };

  const handleTouchEnd = (e: React.TouchEvent) => {
    if (touchStartX.current === null) return;
    const diffX = touchStartX.current - e.changedTouches[0].clientX;
    touchStartX.current = null;
    if (diffX > 45 && currentPage === 0) {
      scrollToPage(1);
    } else if (diffX < -45 && currentPage === 1) {
      scrollToPage(0);
    }
  };

  return (
    <div className="w-full flex flex-col space-y-3 pb-24 animate-in fade-in select-none">
      {/* Sleek Minimalist Location Pill */}
      <div className="flex items-center justify-between px-1 pt-1">
        <button
          onClick={() => setShowLocationDialog(true)}
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold border border-white/15 bg-white/5 text-white hover:bg-white/10 transition-all active:scale-95"
        >
          <MapPin size={13} className="text-white" />
          <span>
            {currentLocation.name}
            {currentLocation.isAutoDetected ? ' • GPS Auto' : ''}
          </span>
          <ChevronDown size={13} className="text-slate-400" />
        </button>

        <button
          onClick={async () => {
            try {
              const { LocationEngine } = await import('../engine/LocationEngine');
              const detected = await LocationEngine.detectLocation();
              onUpdateLocation(detected);
            } catch {
              setShowLocationDialog(true);
            }
          }}
          className="p-1.5 rounded-full border border-white/15 bg-white/5 text-white hover:bg-white/10 transition-all active:scale-95"
          title="Refresh Location"
        >
          <RefreshCw size={13} />
        </button>
      </div>

      {/* Parva Banner Card */}
      <IOSParvaBannerCard
        title={`${tithiInfo.tithiName.toUpperCase()} TODAY •`}
        line1={
          tithiInfo.isParva
            ? 'Green vegetables restricted /'
            : 'Standard Satvik observances /'
        }
        line2={tithiInfo.isParva ? 'Fasting observance' : 'Chauvihar recommended'}
        isDark={isDark}
        onClick={() => setShowObservanceDialog(true)}
      />

      {/* Split Hero Card */}
      <IOSHeroSplitCard
        milestoneName={countdownState.nextMilestoneName}
        countdownFormatted={countdownState.timeRemainingFormatted}
        tithiFullName={tithiInfo.fullName}
        sunriseSunsetRange={sunriseSunsetStr}
        isDark={isDark}
      />

      {/* Section Title with Swipe Hint */}
      <div className="flex items-center justify-between px-1 pt-1">
        <div className="flex items-center gap-2">
          <h3 className="text-[14.5px] font-bold text-slate-300">Pachakkhan Timings</h3>
          <span className="text-[10px] uppercase tracking-wider text-slate-500 font-bold bg-white/5 px-2 py-0.5 rounded-md">
            Swipe ‹ ›
          </span>
        </div>
        <span className="text-[11px] font-semibold text-slate-400">IST (UTC+5:30)</span>
      </div>

      {/* Touch-Swipeable 3-Column Timing Carousel */}
      <div
        ref={carouselRef}
        onScroll={handleScroll}
        onTouchStart={handleTouchStart}
        onTouchEnd={handleTouchEnd}
        className="w-full flex overflow-x-auto snap-x snap-mandatory scroll-smooth no-scrollbar touch-pan-x"
        style={{ WebkitOverflowScrolling: 'touch' }}
      >
        {/* Page 1: 6 Primary Timings */}
        <div className="w-full min-w-full shrink-0 snap-center pr-1 space-y-2.5">
          {/* Row 1: Surya Uday | Navkarsi | Porsi */}
          <div className="grid grid-cols-3 gap-2.5">
            <IOSTimingCard
              title="Surya Uday"
              timeFormatted={format24(solarTimes.sunrise)}
              icon={<Sunrise size={18} className="text-white" />}
              progressFraction={now >= solarTimes.sunrise ? 1 : 0.55}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('NAVKARSI');
                if (item) onSelectPachakkhan(item, format24(solarTimes.sunrise), 'Surya Uday');
              }}
            />
            <IOSTimingCard
              title="Navkarsi"
              timeFormatted={format24(solarTimes.navkarsi)}
              icon={<Sun size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.navkarsi) || 0.45}
              isCurrentActive={true}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('NAVKARSI');
                if (item) onSelectPachakkhan(item, format24(solarTimes.navkarsi), 'Navkarsi');
              }}
            />
            <IOSTimingCard
              title="Porsi"
              timeFormatted={format24(solarTimes.porsi)}
              icon={<Sparkles size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.porsi) || 0.3}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('PORSI');
                if (item) onSelectPachakkhan(item, format24(solarTimes.porsi), 'Porsi');
              }}
            />
          </div>

          {/* Row 2: Purimaddh | Sadh-Porsi | Chauvihar */}
          <div className="grid grid-cols-3 gap-2.5">
            <IOSTimingCard
              title="Purimaddh"
              timeFormatted={format24(solarTimes.purimaddh)}
              icon={<Sun size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.purimaddh) || 0.35}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('PURIMADDH');
                if (item) onSelectPachakkhan(item, format24(solarTimes.purimaddh), 'Purimaddh');
              }}
            />
            <IOSTimingCard
              title="Sadh-Porsi"
              timeFormatted={format24(solarTimes.sadhPorsi)}
              icon={<Clock size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.sadhPorsi) || 0.25}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('SADH_PORSI');
                if (item) onSelectPachakkhan(item, format24(solarTimes.sadhPorsi), 'Sadh-Porsi');
              }}
            />
            <IOSTimingCard
              title="Chauvihar"
              timeFormatted={format24(solarTimes.chauvihar)}
              icon={<Sunset size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.chauvihar) || 0.6}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('CHAUVIHAR');
                if (item) onSelectPachakkhan(item, format24(solarTimes.chauvihar), 'Chauvihar');
              }}
            />
          </div>
        </div>

        {/* Page 2: Extended Penance Vows (Avaddh, Ekashna, Biyashna, Ayambil, Upvas, Tivihar) */}
        <div className="w-full min-w-full shrink-0 snap-center pl-1 space-y-2.5">
          <div className="grid grid-cols-3 gap-2.5">
            <IOSTimingCard
              title="Avaddh"
              timeFormatted={format24(solarTimes.avaddh)}
              icon={<Clock size={18} className="text-white" />}
              progressFraction={calcProgress(solarTimes.avaddh) || 0.5}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('AVADDH');
                if (item) onSelectPachakkhan(item, format24(solarTimes.avaddh), 'Avaddh');
              }}
            />
            <IOSTimingCard
              title="Ekashna"
              timeFormatted="Once"
              icon={<Utensils size={18} className="text-white" />}
              progressFraction={0.5}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('EKASHNA');
                if (item) onSelectPachakkhan(item, 'Once Seated', 'Ekashna');
              }}
            />
            <IOSTimingCard
              title="Biyashna"
              timeFormatted="Twice"
              icon={<Utensils size={18} className="text-white" />}
              progressFraction={0.5}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('BIYASHNA');
                if (item) onSelectPachakkhan(item, 'Twice Seated', 'Biyashna');
              }}
            />
          </div>

          <div className="grid grid-cols-3 gap-2.5">
            <IOSTimingCard
              title="Ayambil"
              timeFormatted="No Vigai"
              icon={<Sparkles size={18} className="text-white" />}
              progressFraction={0.5}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('AYAMBIL');
                if (item) onSelectPachakkhan(item, 'No Vigai', 'Ayambil');
              }}
            />
            <IOSTimingCard
              title="Upvas"
              timeFormatted="Fast"
              icon={<Moon size={18} className="text-white" />}
              progressFraction={0.8}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('UPVAS');
                if (item) onSelectPachakkhan(item, 'Complete Fast', 'Upvas');
              }}
            />
            <IOSTimingCard
              title="Tivihar"
              timeFormatted="Water Only"
              icon={<Droplet size={18} className="text-white" />}
              progressFraction={0.5}
              isDark={isDark}
              onClick={() => {
                const item = getPachakkhan('CHAUVIHAR');
                if (item) onSelectPachakkhan(item, 'Water Only', 'Tivihar');
              }}
            />
          </div>
        </div>
      </div>

      {/* Pagination Dots Indicator: ● ○ */}
      <div className="flex items-center justify-center gap-2 py-2">
        <button
          onClick={() => scrollToPage(0)}
          className={`h-1.5 rounded-full transition-all cursor-pointer ${
            currentPage === 0 ? 'w-6 bg-white' : 'w-1.5 bg-white/30'
          }`}
          aria-label="Primary Timings"
        />
        <button
          onClick={() => scrollToPage(1)}
          className={`h-1.5 rounded-full transition-all cursor-pointer ${
            currentPage === 1 ? 'w-6 bg-white' : 'w-1.5 bg-white/30'
          }`}
          aria-label="Extended Vows"
        />
      </div>

      {/* Location Dialog */}
      <LocationDialog
        currentLocation={currentLocation}
        isOpen={showLocationDialog}
        onClose={() => setShowLocationDialog(false)}
        onSelectLocation={onUpdateLocation}
        isDark={isDark}
      />

      {/* Observance Dialog */}
      <ObservanceDialog
        tithiInfo={tithiInfo}
        isOpen={showObservanceDialog}
        onClose={() => setShowObservanceDialog(false)}
        isDark={isDark}
      />
    </div>
  );
};
