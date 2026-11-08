import React, { useState } from 'react';
import {
  MapPin,
  Volume2,
  Bell,
  Sun,
  Moon,
  Copy,
  Check,
  RotateCcw,
  Heart
} from 'lucide-react';
import { CityLocation, LanguageCode } from '../types';
import { AudioEngine } from '../engine/AudioEngine';
import { IOSCard } from '../components/IOSCard';
import { LocationDialog } from '../components/LocationDialog';

interface SettingsScreenProps {
  currentLocation: CityLocation;
  onUpdateLocation: (loc: CityLocation) => void;
  preferredLanguage: LanguageCode;
  onUpdateLanguage: (lang: LanguageCode) => void;
  isDark: boolean;
  onToggleTheme: () => void;
  onReplayTutorial: () => void;
}

export const SettingsScreen: React.FC<SettingsScreenProps> = ({
  currentLocation,
  onUpdateLocation,
  preferredLanguage,
  onUpdateLanguage,
  isDark,
  onToggleTheme,
  onReplayTutorial
}) => {
  const [showLocationDialog, setShowLocationDialog] = useState(false);
  const [copiedUpi, setCopiedUpi] = useState(false);
  const [eveningAlert, setEveningAlert] = useState(true);
  const [morningAlert, setMorningAlert] = useState(true);

  const audioEngine = AudioEngine.getInstance();
  const [speed, setSpeed] = useState(audioEngine.getSpeechRate());

  const upiId = '8850783566@fam';

  const handleCopyUpi = () => {
    navigator.clipboard?.writeText(upiId).then(() => {
      setCopiedUpi(true);
      setTimeout(() => setCopiedUpi(false), 2000);
    });
  };

  const handleSpeedChange = (rate: number) => {
    audioEngine.setSpeechRate(rate);
    setSpeed(rate);
  };

  return (
    <div className="w-full flex flex-col space-y-4 pb-24 animate-in fade-in">
      <h2 className="text-xl font-bold tracking-tight text-white px-1">Settings & Preferences</h2>

      {/* App Brand Header Card */}
      <IOSCard isDark={isDark} className="p-4">
        <div className="flex items-center gap-3.5">
          <div className="w-14 h-14 rounded-[18px] flex items-center justify-center shrink-0 border bg-[#222938] border-white/10">
            <img
              src="./logo.png"
              alt="Prahara Logo"
              className="w-11 h-11 rounded-[13px] object-cover"
            />
          </div>
          <div>
            <h3 className="text-base font-bold text-white">Prahara • પ્રહર</h3>
            <p className="text-xs text-slate-400 font-medium">
              Jain Daily Pachakkhan & Tithi Tracker
            </p>
            <div className="flex items-center gap-2 mt-1">
              <span className="text-[10.5px] px-2 py-0.5 rounded-full font-semibold bg-white/10 text-white">
                v1.0.0 iOS Standalone
              </span>
              <span className="text-[10.5px] text-white font-semibold">
                ● 100% Offline
              </span>
            </div>
          </div>
        </div>
      </IOSCard>

      {/* Location Settings */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-white">
            <MapPin size={17} className="text-white" />
            <span className="text-sm font-bold">Solar Location</span>
          </div>
          <button
            onClick={() => setShowLocationDialog(true)}
            className="text-xs font-bold px-3 py-1 rounded-full border border-white/15 bg-white/10 text-white hover:bg-white/20 transition-all"
          >
            Change
          </button>
        </div>

        <div className="p-3 rounded-[16px] border border-white/10 bg-white/5 flex items-center justify-between text-xs">
          <div>
            <div className="font-bold text-sm text-white">{currentLocation.name}</div>
            <div className="text-slate-400">{currentLocation.stateOrCountry}</div>
          </div>
          <div className="text-right text-[11px] font-semibold text-slate-400">
            {currentLocation.latitude.toFixed(4)}° N, {currentLocation.longitude.toFixed(4)}° E
          </div>
        </div>
      </IOSCard>

      {/* Recitation Voice & Language */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2 text-white">
          <Volume2 size={17} className="text-white" />
          <span className="text-sm font-bold">Recitation & Audio</span>
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-400 mb-2 block">
            Default Recitation Language
          </label>
          <div className="flex p-1 rounded-[14px] border border-white/10 bg-[#181D28]">
            {[
              { code: 'gu', label: 'ગુજરાતી' },
              { code: 'hi', label: 'हिन्दी / प्राकृत' },
              { code: 'en', label: 'English' }
            ].map((lang) => (
              <button
                key={lang.code}
                onClick={() => onUpdateLanguage(lang.code as LanguageCode)}
                className={`flex-1 py-1.5 text-xs font-semibold rounded-[10px] transition-all ${
                  preferredLanguage === lang.code
                    ? 'bg-white text-black shadow font-bold'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                {lang.label}
              </button>
            ))}
          </div>
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-400 mb-2 block">
            Pacing (Devotional Speed: {speed.toFixed(1)}x)
          </label>
          <div className="flex items-center gap-2">
            {[0.8, 0.9, 1.0, 1.2].map((s) => (
              <button
                key={s}
                onClick={() => handleSpeedChange(s)}
                className={`flex-1 py-1.5 rounded-[12px] text-xs font-bold border transition-all ${
                  Math.abs(speed - s) < 0.05
                    ? 'bg-white text-black border-white shadow'
                    : 'bg-white/5 border-white/10 text-slate-400 hover:text-white'
                }`}
              >
                {s}x
              </button>
            ))}
          </div>
        </div>
      </IOSCard>

      {/* Notifications & Reminders (Pure Monochrome) */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2 text-white">
          <Bell size={17} className="text-white" />
          <span className="text-sm font-bold">Parva Reminders</span>
        </div>

        <div className="space-y-2">
          <div className="flex items-center justify-between text-xs py-1">
            <div>
              <div className="font-semibold text-white">Parva Eve Reminder (8:00 PM)</div>
              <div className="text-slate-400">Notifies of upcoming Atham / Chaudas Parva</div>
            </div>
            <button
              onClick={() => setEveningAlert(!eveningAlert)}
              className={`w-11 h-6 flex items-center rounded-full p-1 transition-colors border ${
                eveningAlert ? 'bg-white border-white' : 'bg-white/15 border-white/20'
              }`}
            >
              <div
                className={`w-4 h-4 rounded-full shadow-md transform transition-transform ${
                  eveningAlert ? 'translate-x-5 bg-black' : 'bg-white'
                }`}
              />
            </button>
          </div>

          <div className="flex items-center justify-between text-xs py-1">
            <div>
              <div className="font-semibold text-white">Morning Sunrise Alert (6:00 AM)</div>
              <div className="text-slate-400">Daily reminder for Navkarsi vow</div>
            </div>
            <button
              onClick={() => setMorningAlert(!morningAlert)}
              className={`w-11 h-6 flex items-center rounded-full p-1 transition-colors border ${
                morningAlert ? 'bg-white border-white' : 'bg-white/15 border-white/20'
              }`}
            >
              <div
                className={`w-4 h-4 rounded-full shadow-md transform transition-transform ${
                  morningAlert ? 'translate-x-5 bg-black' : 'bg-white'
                }`}
              />
            </button>
          </div>
        </div>
      </IOSCard>

      {/* Walkthrough Guide trigger */}
      <IOSCard isDark={isDark} className="p-4">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-sm font-bold text-white">Introductory Walkthrough</div>
            <div className="text-xs text-slate-400">Replay the visual features guide</div>
          </div>
          <button
            onClick={onReplayTutorial}
            className="flex items-center gap-1.5 text-xs font-semibold px-3 py-1.5 rounded-full border border-white/15 bg-white/10 text-white hover:bg-white/20 transition-all"
          >
            <RotateCcw size={13} />
            <span>Open Guide</span>
          </button>
        </div>
      </IOSCard>

      {/* Support & Sevabhāv (Donation) */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2 text-white">
          <Heart size={17} className="text-white" />
          <span className="text-sm font-bold">Support Prahara (Seva & Dana)</span>
        </div>
        <p className="text-xs text-slate-400 leading-relaxed">
          Prahara is a community seva initiative dedicated to Jinashasana, built with zero ads
          and pure open-source spirit. If this app aids your daily Pachakkhan & Tithi observance,
          your contribution supports continuous maintenance and servers.
        </p>

        <div className="p-3 rounded-[16px] border border-white/10 bg-white/5 flex items-center justify-between">
          <div>
            <div className="text-[10px] text-slate-400 font-semibold uppercase">UPI ID</div>
            <div className="text-sm font-mono font-bold text-white">{upiId}</div>
          </div>
          <button
            onClick={handleCopyUpi}
            className={`flex items-center gap-1 text-xs font-bold px-3 py-1.5 rounded-full border transition-all ${
              copiedUpi
                ? 'bg-white text-black border-white'
                : 'bg-white/10 border-white/15 text-white hover:bg-white/20'
            }`}
          >
            {copiedUpi ? <Check size={13} /> : <Copy size={13} />}
            <span>{copiedUpi ? 'Copied' : 'Copy'}</span>
          </button>
        </div>

        {/* QR Code */}
        <div className="flex items-center justify-center pt-2">
          <img
            src="./qr_upi_donation.png"
            alt="UPI QR Code"
            className="w-36 h-36 rounded-xl border border-white/20 p-1 bg-white"
          />
        </div>
      </IOSCard>

      {/* Location Dialog */}
      <LocationDialog
        currentLocation={currentLocation}
        isOpen={showLocationDialog}
        onClose={() => setShowLocationDialog(false)}
        onSelectLocation={onUpdateLocation}
        isDark={isDark}
      />
    </div>
  );
};
