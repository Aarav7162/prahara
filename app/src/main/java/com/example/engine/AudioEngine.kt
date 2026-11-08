package com.example.engine

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioEngine(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentlyPlayingId = MutableStateFlow<String?>(null)
    val currentlyPlayingId: StateFlow<String?> = _currentlyPlayingId.asStateFlow()

    private val _speechRate = MutableStateFlow(0.95f) // Slightly serene, devotional pace
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isPlaying.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentlyPlayingId.value = null
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentlyPlayingId.value = null
                    }
                })
            } else {
                Log.e("AudioEngine", "TTS initialization failed: $status")
            }
        }
    }

    fun playSutra(
        text: String,
        id: String,
        languageCode: String = "hi"
    ) {
        if (!isInitialized || tts == null) return

        if (_isPlaying.value && _currentlyPlayingId.value == id) {
            stop()
            return
        }

        stop()

        val locale = when (languageCode) {
            "gu" -> Locale("gu", "IN")
            "en" -> Locale.US
            else -> Locale("hi", "IN") // Hindi provides ideal phonetic rendering for Prakrit sutras
        }

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to Hindi or default
            tts?.setLanguage(Locale("hi", "IN"))
        }

        tts?.setSpeechRate(_speechRate.value)
        tts?.setPitch(1.0f)

        _currentlyPlayingId.value = id
        _isPlaying.value = true

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, id)
    }

    fun setSpeed(rate: Float) {
        _speechRate.value = rate.coerceIn(0.7f, 1.5f)
        tts?.setSpeechRate(_speechRate.value)
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
        _currentlyPlayingId.value = null
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
