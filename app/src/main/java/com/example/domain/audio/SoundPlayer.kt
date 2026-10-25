package com.example.domain.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundPlayer(private val scope: CoroutineScope) {
    var isSoundEnabled: Boolean = true

    fun playClick() {
        if (!isSoundEnabled) return
        scope.launch(Dispatchers.Default) {
            generateTone(frequency = 1200.0, durationMs = 25, volume = 0.2f)
        }
    }

    fun playShieldToggled(active: Boolean) {
        if (!isSoundEnabled) return
        scope.launch(Dispatchers.Default) {
            if (active) {
                generateTone(523.25, 60, 0.35f)
                generateTone(659.25, 80, 0.4f)
                generateTone(783.99, 120, 0.45f)
            } else {
                generateTone(783.99, 60, 0.3f)
                generateTone(523.25, 90, 0.25f)
            }
        }
    }

    fun playScanStart() {
        if (!isSoundEnabled) return
        scope.launch(Dispatchers.Default) {
            generateChirp(startFreq = 400.0, endFreq = 950.0, durationMs = 180, volume = 0.35f)
        }
    }

    fun playScanComplete() {
        if (!isSoundEnabled) return
        scope.launch(Dispatchers.Default) {
            generateTone(659.25, 80, 0.3f)
            generateTone(783.99, 90, 0.35f)
            generateTone(1046.50, 220, 0.4f)
        }
    }

    fun playThreatAlert() {
        if (!isSoundEnabled) return
        scope.launch(Dispatchers.Default) {
            for (i in 0..2) {
                generateTone(980.0, 70, 0.5f)
                generateTone(540.0, 70, 0.5f)
            }
        }
    }

    private fun generateTone(frequency: Double, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
            val generatedSnd = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val envelope = when {
                    i < numSamples * 0.1 -> i / (numSamples * 0.1)
                    i > numSamples * 0.8 -> (numSamples - i) / (numSamples * 0.2)
                    else -> 1.0
                }
                val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
                val sample = (sin(angle) * Short.MAX_VALUE * volume * envelope).toInt()
                generatedSnd[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(numSamples * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, numSamples)
            audioTrack.play()
            Thread.sleep(durationMs.toLong() + 20)
            audioTrack.release()
        } catch (_: Exception) {
            // AudioTrack failure fallback safely ignored
        }
    }

    private fun generateChirp(startFreq: Double, endFreq: Double, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
            val generatedSnd = ShortArray(numSamples)

            var phase = 0.0
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val freq = startFreq + (endFreq - startFreq) * (t / (durationMs / 1000.0))
                phase += 2.0 * Math.PI * freq / sampleRate
                val envelope = sin(Math.PI * (i.toDouble() / numSamples))
                val sample = (sin(phase) * Short.MAX_VALUE * volume * envelope).toInt()
                generatedSnd[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(numSamples * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, numSamples)
            audioTrack.play()
            Thread.sleep(durationMs.toLong() + 20)
            audioTrack.release()
        } catch (_: Exception) {
        }
    }
}
