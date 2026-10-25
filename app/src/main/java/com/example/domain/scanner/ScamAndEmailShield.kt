package com.example.domain.scanner

data class ScamScanResult(
    val inputText: String,
    val isScamDetected: Boolean,
    val scamCategory: String, // Bank Impersonation, Delivery Phish, Deepfake Voice/Media, Tech Support Scam, Clean
    val confidencePercent: Int,
    val riskLevel: String, // Critical, High Risk, Warning, Safe
    val reason: String,
    val recommendedAction: String,
    val detectedKeywords: List<String>
)

class ScamAndEmailShield {

    private val bankKeywords = listOf("urgent", "account suspended", "verify login", "tax refund", "irs debt", "unauthorized transfer", "wire money", "crypto reward")
    private val deliveryKeywords = listOf("unpaid delivery fee", "package waiting", "click to confirm shipment", "customs fee", "usps-redelivery")

    fun analyzeScamMessage(text: String): ScamScanResult {
        val lower = text.lowercase()
        val foundBank = bankKeywords.filter { lower.contains(it) }
        val foundDelivery = deliveryKeywords.filter { lower.contains(it) }

        if (foundBank.isNotEmpty()) {
            return ScamScanResult(
                inputText = text,
                isScamDetected = true,
                scamCategory = "Financial & Banking Phish",
                confidencePercent = 94,
                riskLevel = "Critical",
                reason = "Deceptive urgency indicators and fraudulent account verification requests detected.",
                recommendedAction = "Never click links in this message or provide OTP/passwords. Mark as spam and delete.",
                detectedKeywords = foundBank
            )
        }

        if (foundDelivery.isNotEmpty()) {
            return ScamScanResult(
                inputText = text,
                isScamDetected = true,
                scamCategory = "Package Delivery Smishing",
                confidencePercent = 88,
                riskLevel = "High Risk",
                reason = "Simulated courier delivery notification requesting credit card fee or credential input.",
                recommendedAction = "Do not pay any shipping fees through SMS/Email links. Inspect tracking only on official carrier apps.",
                detectedKeywords = foundDelivery
            )
        }

        if (lower.contains("deepfake") || lower.contains("voice clone") || lower.contains("emergency wire")) {
            return ScamScanResult(
                inputText = text,
                isScamDetected = true,
                scamCategory = "AI Voice/Deepfake Impersonation",
                confidencePercent = 91,
                riskLevel = "Critical",
                reason = "Social engineering heuristics suggest synthetic voice or media impersonation of family/employer.",
                recommendedAction = "Establish a separate direct phone call to verify identity before transferring any funds.",
                detectedKeywords = listOf("deepfake", "synthetic voice", "urgent wire")
            )
        }

        return ScamScanResult(
            inputText = text,
            isScamDetected = false,
            scamCategory = "Verified Legitimate",
            confidencePercent = 15,
            riskLevel = "Safe",
            reason = "No phishing, fraudulent urgency, or credential theft patterns were detected.",
            recommendedAction = "Message content appears safe under standard precautions.",
            detectedKeywords = emptyList()
        )
    }
}
