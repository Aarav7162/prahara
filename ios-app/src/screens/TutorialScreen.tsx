import React, { useState } from 'react';
import { Sun, Calendar, Sparkles, Smartphone, ArrowRight, Check } from 'lucide-react';
import { IOSCard } from '../components/IOSCard';

interface TutorialScreenProps {
  onFinish: () => void;
  isDark?: boolean;
}

export const TutorialScreen: React.FC<TutorialScreenProps> = ({
  onFinish,
  isDark = true
}) => {
  const [currentStep, setCurrentStep] = useState(0);

  const steps = [
    {
      title: 'Welcome to Prahara',
      subtitle: 'પ્રહર • Jain Panchang Tracker',
      description:
        'A mobile-first Jain Daily Pachakkhan & Tithi Tracker featuring precise NOAA astronomical solar calculations, sacred Parva tithi observances, and trilingual sutras with serene audio recitations.',
      icon: <Sparkles size={36} className="text-amber-400" />
    },
    {
      title: 'Astronomical Solar Timings',
      subtitle: 'Navkarsi, Porsi, Purimaddh & Chauvihar',
      description:
        'Computes exact Sunrise, Sunset, and day divisions: Navkarsi (Sunrise + 48 min / 2 Ghadis), Porsi (1/4th daylight), Sadh-Porsi (3/8th), Purimaddh (midday), Avaddh, and Chauvihar for any city or GPS coordinates.',
      icon: <Sun size={36} className="text-amber-400" />
    },
    {
      title: 'Jain Tithi & Parva Observances',
      subtitle: 'Sud & Vad Lunar Cycles',
      description:
        'Full calendar calibrated to traditional Jain Panchang. Automatic badges and dietary guidelines for sacred Parva days (Chaudas, Atham, Poonam, Amas, Pancham, Bij) to help maintain Leelo-tri and fasting disciplines.',
      icon: <Calendar size={36} className="text-emerald-400" />
    },
    {
      title: 'Install on iPhone Without App Store',
      subtitle: 'Native Home Screen Standalone App',
      description:
        'Simply tap the Share button in Safari, then tap "Add to Home Screen". Prahara installs directly onto your iPhone with full offline support, custom app icon, and zero App Store friction!',
      icon: <Smartphone size={36} className="text-sky-400" />
    }
  ];

  const step = steps[currentStep];

  const handleNext = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1);
    } else {
      onFinish();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex flex-col justify-between p-6 bg-gradient-to-b from-[#11141B] via-[#0B0D12] to-[#06070A] text-slate-100 animate-in fade-in select-none">
      {/* Top Header with Progress Bars & Skip Button */}
      <div className="pt-[max(8px,env(safe-area-inset-top))] flex items-center justify-between">
        {/* Step Indicator Pills */}
        <div className="flex items-center gap-1.5">
          {steps.map((_, idx) => (
            <div
              key={idx}
              className={`h-1.5 rounded-full transition-all duration-300 ${
                idx === currentStep ? 'w-6 bg-white' : 'w-2 bg-white/20'
              }`}
            />
          ))}
        </div>

        <button
          onClick={onFinish}
          className="text-xs font-semibold text-slate-400 hover:text-white px-2 py-1 transition-colors"
        >
          Skip
        </button>
      </div>

      {/* Main Content Card */}
      <div className="my-auto py-6">
        <IOSCard isDark={true} className="p-6 text-center max-w-sm mx-auto">
          {/* Circular Icon Container */}
          <div className="w-20 h-20 rounded-full mx-auto flex items-center justify-center bg-white/5 border border-white/10 mb-6 shadow-inner">
            {step.icon}
          </div>

          <h2 className="text-2xl font-bold tracking-tight text-white mb-1">{step.title}</h2>
          <div className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-4">
            {step.subtitle}
          </div>

          <p className="text-sm leading-relaxed text-slate-300">{step.description}</p>
        </IOSCard>
      </div>

      {/* Bottom Action Button */}
      <div className="pb-[max(16px,env(safe-area-inset-bottom))] max-w-sm mx-auto w-full">
        <button
          onClick={handleNext}
          className="w-full py-3.5 px-6 rounded-[20px] font-bold text-sm bg-white text-slate-950 hover:bg-slate-100 flex items-center justify-center gap-2 shadow-xl transition-all active:scale-[0.98]"
        >
          <span>{currentStep === steps.length - 1 ? 'Start Practicing' : 'Continue'}</span>
          {currentStep === steps.length - 1 ? <Check size={18} /> : <ArrowRight size={18} />}
        </button>
      </div>
    </div>
  );
};

