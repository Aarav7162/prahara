import Foundation
import AVFoundation

public class NativeAudioEngine: NSObject, ObservableObject, AVSpeechSynthesizerDelegate {
    public static let shared = NativeAudioEngine()

    private let synthesizer = AVSpeechSynthesizer()
    @Published public var isPlaying: Bool = false
    @Published public var currentlyPlayingId: String? = nil
    @Published public var speechRate: Float = 0.48 // Calm serenity

    override private init() {
        super.init()
        synthesizer.delegate = self
    }

    public func playSutra(text: String, id: String, languageCode: String = "hi") {
        if isPlaying && currentlyPlayingId == id {
            stop()
            return
        }
        stop()

        let utterance = AVSpeechUtterance(string: text)
        utterance.rate = speechRate
        utterance.pitchMultiplier = 1.0

        let bcp47: String
        switch languageCode {
        case "gu": bcp47 = "gu-IN"
        case "en": bcp47 = "en-US"
        default: bcp47 = "hi-IN"
        }
        utterance.voice = AVSpeechSynthesisVoice(language: bcp47)

        currentlyPlayingId = id
        isPlaying = true
        synthesizer.speak(utterance)
    }

    public func stop() {
        if synthesizer.isSpeaking {
            synthesizer.stopSpeaking(at: .immediate)
        }
        isPlaying = false
        currentlyPlayingId = nil
    }

    public func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didFinish utterance: AVSpeechUtterance) {
        DispatchQueue.main.async {
            self.isPlaying = false
            self.currentlyPlayingId = nil
        }
    }

    public func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didCancel utterance: AVSpeechUtterance) {
        DispatchQueue.main.async {
            self.isPlaying = false
            self.currentlyPlayingId = nil
        }
    }
}

