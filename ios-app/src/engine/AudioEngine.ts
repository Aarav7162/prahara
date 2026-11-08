import { LanguageCode } from '../types';

export class AudioEngine {
  private static instance: AudioEngine;
  private isPlaying = false;
  private currentlyPlayingId: string | null = null;
  private speechRate = 0.92; // Slightly serene, devotional pace
  private listeners: Array<() => void> = [];

  private constructor() {
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      window.speechSynthesis.onvoiceschanged = () => {};
    }
  }

  public static getInstance(): AudioEngine {
    if (!AudioEngine.instance) {
      AudioEngine.instance = new AudioEngine();
    }
    return AudioEngine.instance;
  }

  public subscribe(listener: () => void): () => void {
    this.listeners.push(listener);
    return () => {
      this.listeners = this.listeners.filter((l) => l !== listener);
    };
  }

  private notify() {
    this.listeners.forEach((l) => l());
  }

  public getIsPlaying(): boolean {
    return this.isPlaying;
  }

  public getCurrentlyPlayingId(): string | null {
    return this.currentlyPlayingId;
  }

  public getSpeechRate(): number {
    return this.speechRate;
  }

  public setSpeechRate(rate: number) {
    this.speechRate = Math.min(1.5, Math.max(0.7, rate));
    this.notify();
  }

  public playSutra(text: string, id: string, language: LanguageCode = 'hi') {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) {
      console.warn('SpeechSynthesis is not supported on this device');
      return;
    }

    if (this.isPlaying && this.currentlyPlayingId === id) {
      this.stop();
      return;
    }

    this.stop();

    const utterance = new SpeechSynthesisUtterance(text);
    utterance.rate = this.speechRate;
    utterance.pitch = 1.0;

    let langCode = 'hi-IN';
    if (language === 'gu') {
      langCode = 'gu-IN';
    } else if (language === 'en') {
      langCode = 'en-US';
    }
    utterance.lang = langCode;

    // Try finding matching voice
    const voices = window.speechSynthesis.getVoices();
    const matchedVoice = voices.find(
      (v) => v.lang.startsWith(langCode.slice(0, 2)) || v.lang.replace('_', '-') === langCode
    );
    if (matchedVoice) {
      utterance.voice = matchedVoice;
    }

    utterance.onstart = () => {
      this.isPlaying = true;
      this.currentlyPlayingId = id;
      this.notify();
    };

    utterance.onend = () => {
      this.isPlaying = false;
      this.currentlyPlayingId = null;
      this.notify();
    };

    utterance.onerror = () => {
      this.isPlaying = false;
      this.currentlyPlayingId = null;
      this.notify();
    };

    window.speechSynthesis.speak(utterance);
  }

  public stop() {
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      window.speechSynthesis.cancel();
    }
    this.isPlaying = false;
    this.currentlyPlayingId = null;
    this.notify();
  }
}

