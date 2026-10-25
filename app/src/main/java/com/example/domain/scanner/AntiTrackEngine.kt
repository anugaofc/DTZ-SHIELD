package com.example.domain.scanner

data class TrackingAttempt(
    val id: String,
    val trackerName: String,
    val trackerCategory: String, // Advertising, Analytics, Social Widget, Fingerprinting
    val targetDomain: String,
    val blockedAt: Long = System.currentTimeMillis()
)

data class AntiTrackStatus(
    val isEnabled: Boolean = true,
    val fingerprintDisguised: Boolean = true,
    val totalTrackersBlocked: Int = 142,
    val activeFingerprintId: String = "Chrome/130.0 (Linux; x86_64; Encrypted Canvas)",
    val recentBlockedTrackers: List<TrackingAttempt> = listOf(
        TrackingAttempt("1", "DoubleClick Ad Exchange", "Advertising", "doubleclick.net"),
        TrackingAttempt("2", "Facebook Pixel Radar", "Social Widget", "connect.facebook.net"),
        TrackingAttempt("3", "Google Tag Services", "Analytics", "googletagmanager.com"),
        TrackingAttempt("4", "AudioContext Fingerprinter", "Fingerprinting", "track-device-id.xyz"),
        TrackingAttempt("5", "Criteo Behavioral Targeter", "Advertising", "criteo.com")
    )
)

class AntiTrackEngine {
    fun getStatus(): AntiTrackStatus = AntiTrackStatus()
}
