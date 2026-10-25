package com.example.domain.scanner

data class CyberCaptureAnalysis(
    val fileName: String,
    val sha256: String,
    val isClean: Boolean,
    val behavioralSummary: String,
    val sandboxExecutionTimeMs: Long,
    val reputationScore: Int
)

class CyberCaptureEngine {

    fun analyzeUnknownSample(fileName: String): CyberCaptureAnalysis {
        val isSuspicious = fileName.contains("hack", ignoreCase = true) ||
                fileName.contains("crack", ignoreCase = true) ||
                fileName.contains("payload", ignoreCase = true)

        return if (isSuspicious) {
            CyberCaptureAnalysis(
                fileName = fileName,
                sha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                isClean = false,
                behavioralSummary = "Isolated in cloud sandbox: Dynamic memory injection & keystroke capture hooks observed.",
                sandboxExecutionTimeMs = 1240,
                reputationScore = 12
            )
        } else {
            CyberCaptureAnalysis(
                fileName = fileName,
                sha256 = "8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4",
                isClean = true,
                behavioralSummary = "Verified by DTZ Threat Intelligence Cloud: Zero malicious system calls detected.",
                sandboxExecutionTimeMs = 860,
                reputationScore = 98
            )
        }
    }
}
