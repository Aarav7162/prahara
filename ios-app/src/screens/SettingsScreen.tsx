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
  Heart,
  QrCode,
  Info,
  ExternalLink
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
      <h2 className="text-xl font-bold tracking-tight px-1">Settings & Preferences</h2>

      {/* App Brand Header Card */}
      <IOSCard isDark={isDark} className="p-4">
        <div className="flex items-center gap-3.5">
          <div
            className={`w-14 h-14 rounded-[18px] flex items-center justify-center shrink-0 border ${
              isDark ? 'bg-[#222938] border-white/10' : 'bg-[#E8ECF2] border-slate-200'
            }`}
          >
            <img
              src="./logo.png"
              alt="Prahara Logo"
              className="w-11 h-11 rounded-[13px] object-cover"
            />
          </div>
          <div>
            <h3 className="text-base font-bold">Prahara • પ્રહર</h3>
            <p className="text-xs text-slate-400 font-medium">
              Jain Daily Pachakkhan & Tithi Tracker
            </p>
            <div className="flex items-center gap-2 mt-1">
              <span className="text-[10.5px] px-2 py-0.5 rounded-full font-semibold bg-white/10 text-slate-300">
                v1.0.0 iOS Standalone
              </span>
              <span className="text-[10.5px] text-emerald-400 font-semibold">
                ● 100% Offline
              </span>
            </div>
          </div>
        </div>
      </IOSCard>

      {/* Location Settings */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <MapPin size={17} className="text-slate-400" />
            <span className="text-sm font-bold">Solar Location</span>
          </div>
          <button
            onClick={() => setShowLocationDialog(true)}
            className={`text-xs font-bold px-3 py-1 rounded-full border transition-all ${
              isDark
                ? 'bg-white/10 border-white/10 text-white hover:bg-white/15'
                : 'bg-black/5 border-black/10 text-slate-900 hover:bg-black/10'
            }`}
          >
            Change
          </button>
        </div>

        <div
          className={`p-3 rounded-[16px] border flex items-center justify-between text-xs ${
            isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
          }`}
        >
          <div>
            <div className="font-bold text-sm">{currentLocation.name}</div>
            <div className="text-slate-400">{currentLocation.stateOrCountry}</div>
          </div>
          <div className="text-right text-[11px] font-semibold text-slate-400">
            {currentLocation.latitude.toFixed(4)}° N, {currentLocation.longitude.toFixed(4)}° E
          </div>
        </div>
      </IOSCard>

      {/* Recitation Voice & Language */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2">
          <Volume2 size={17} className="text-slate-400" />
          <span className="text-sm font-bold">Recitation & Audio</span>
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-400 mb-2 block">
            Default Recitation Language
          </label>
          <div
            className={`flex p-1 rounded-[14px] border ${
              isDark ? 'bg-[#181D28] border-white/10' : 'bg-[#E8ECF2] border-slate-200'
            }`}
          >
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
                    ? isDark
                      ? 'bg-[#2A3448] text-white shadow'
                      : 'bg-white text-slate-900 shadow'
                    : 'text-slate-400 hover:text-slate-200'
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
                    ? isDark
                      ? 'bg-white text-slate-900 border-white'
                      : 'bg-slate-900 text-white border-slate-900'
                    : isDark
                    ? 'bg-white/5 border-white/10 text-slate-400'
                    : 'bg-slate-100 border-slate-200 text-slate-600'
                }`}
              >
                {s}x
              </button>
            ))}
          </div>
        </div>
      </IOSCard>

      {/* Notifications & Reminders */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2">
          <Bell size={17} className="text-slate-400" />
          <span className="text-sm font-bold">Parva Reminders</span>
        </div>

        <div className="space-y-2">
          <div className="flex items-center justify-between text-xs py-1">
            <div>
              <div className="font-semibold">Parva Eve Reminder (8:00 PM)</div>
              <div className="text-slate-400">Notifies of upcoming Atham / Chaudas Parva</div>
            </div>
            <button
              onClick={() => setEveningAlert(!eveningAlert)}
              className={`w-11 h-6 flex items-center rounded-full p-1 transition-colors ${
                eveningAlert ? 'bg-emerald-500' : isDark ? 'bg-white/15' : 'bg-slate-300'
              }`}
            >
              <div
                className={`bg-white w-4 h-4 rounded-full shadow-md transform transition-transform ${
                  eveningAlert ? 'translate-x-5' : ''
                }`}
              />
            </button>
          </div>

          <div className="flex items-center justify-between text-xs py-1">
            <div>
              <div className="font-semibold">Morning Sunrise Alert (6:00 AM)</div>
              <div className="text-slate-400">Daily reminder for Navkarsi vow</div>
            </div>
            <button
              onClick={() => setMorningAlert(!morningAlert)}
              className={`w-11 h-6 flex items-center rounded-full p-1 transition-colors ${
                morningAlert ? 'bg-emerald-500' : isDark ? 'bg-white/15' : 'bg-slate-300'
              }`}
            >
              <div
                className={`bg-white w-4 h-4 rounded-full shadow-md transform transition-transform ${
                  morningAlert ? 'translate-x-5' : ''
                }`}
              />
            </button>
          </div>
        </div>
      </IOSCard>

      {/* Appearance & Walkthrough */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm font-bold">
            {isDark ? <Moon size={17} className="text-slate-400" /> : <Sun size={17} className="text-slate-400" />}
            <span>Liquid Glass Appearance</span>
          </div>
          <button
            onClick={onToggleTheme}
            className={`text-xs font-semibold px-3 py-1 rounded-full border transition-all ${
              isDark
                ? 'bg-white/10 border-white/10 text-white'
                : 'bg-black/5 border-black/10 text-slate-900'
            }`}
          >
            {isDark ? 'Dark Mode' : 'Light Mode'}
          </button>
        </div>

        <div className="pt-2 border-t border-white/10 flex items-center justify-between">
          <span className="text-xs text-slate-400">Introductory Walkthrough</span>
          <button
            onClick={onReplayTutorial}
            className={`flex items-center gap-1.5 text-xs font-semibold px-3 py-1 rounded-full border transition-all ${
              isDark
                ? 'bg-white/10 border-white/10 text-slate-200'
                : 'bg-black/5 border-black/10 text-slate-800'
            }`}
          >
            <RotateCcw size={13} />
            <span>Replay Guide</span>
          </button>
        </div>
      </IOSCard>

      {/* Support & Sevabhāv (Donation) */}
      <IOSCard isDark={isDark} className="p-4 space-y-3">
        <div className="flex items-center gap-2">
          <Heart size={17} className="text-rose-400" />
          <span className="text-sm font-bold">Support Prahara (Seva & Dana)</span>
        </div>
        <p className="text-xs text-slate-400 leading-relaxed">
          Prahara is a community seva initiative dedicated to Jinashasana, built with zero ads
          and pure open-source spirit. If this app aids your daily Pachakkhan & Tithi observance,
          your contribution supports continuous maintenance and servers.
        </p>

        <div
          className={`p-3 rounded-[16px] border flex items-center justify-between ${
            isDark ? 'bg-white/5 border-white/10' : 'bg-slate-50 border-slate-200'
          }`}
        >
          <div>
            <div className="text-[10px] text-slate-400 font-semibold uppercase">UPI ID</div>
            <div className="text-sm font-mono font-bold">{upiId}</div>
          </div>
          <button
            onClick={handleCopyUpi}
            className={`flex items-center gap-1 text-xs font-bold px-3 py-1.5 rounded-full border transition-all ${
              copiedUpi
                ? 'bg-emerald-500 text-white border-emerald-500'
                : isDark
                ? 'bg-white/10 border-white/10 text-white hover:bg-white/15'
                : 'bg-black/5 border-black/10 text-slate-900 hover:bg-black/10'
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

