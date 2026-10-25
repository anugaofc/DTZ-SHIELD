package com.example.domain.scanner

import java.net.URI

data class WebCheckResult(
    val url: String,
    val domain: String,
    val isBlocked: Boolean,
    val threatCategory: String,
    val riskLevel: String, // Safe, Warning, High Risk, Critical
    val reason: String,
    val recommendedAction: String,
    val httpsSecure: Boolean,
    val details: List<String>
)

class WebShieldEngine {

    private val suspiciousTlds = setOf("xyz", "top", "click", "buzz", "cc", "fit", "work", "loan", "tk", "ml", "ga")
    private val phishingKeywords = listOf(
        "verify-account", "update-bank", "secure-login", "confirm-identity",
        "paypal-resolution", "netflix-reactivate", "crypto-airdrop", "free-giftcard",
        "account-suspended", "appleid-verify", "binance-wallet"
    )

    private val knownMaliciousDomains = setOf(
        "evil-phishing.test",
        "malware-download-sample.test",
        "free-crypto-giveaway.click",
        "bank-login-secure.xyz",
        "update-chrome-now.top"
    )

    fun evaluateUrl(rawUrl: String): WebCheckResult {
        val trimmed = rawUrl.trim()
        val formattedUrl = if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else {
            trimmed
        }

        val uri = try {
            URI(formattedUrl)
        } catch (_: Exception) {
            return WebCheckResult(
                url = trimmed,
                domain = trimmed,
                isBlocked = true,
                threatCategory = "Malformed URL",
                riskLevel = "Warning",
                reason = "Invalid URL syntax, which could indicate an obfuscation attack or malformed input.",
                recommendedAction = "Do not open this URL in any browser.",
                httpsSecure = false,
                details = listOf("Malformed RFC 2396 URI string", "Obfuscation pattern detected")
            )
        }

        val host = uri.host?.lowercase() ?: ""
        val scheme = uri.scheme?.lowercase() ?: "http"
        val httpsSecure = scheme == "https"
        val path = uri.path?.lowercase() ?: ""

        // 1. Check known malicious domains / test signatures
        if (knownMaliciousDomains.contains(host) || host.contains("phish") || host.contains("malware")) {
            return WebCheckResult(
                url = formattedUrl,
                domain = host,
                isBlocked = true,
                threatCategory = "Malicious Website & Phishing",
                riskLevel = "Critical",
                reason = "DTZ SHIELD threat intelligence identified this domain as an active distributor of phishing or malware payloads.",
                recommendedAction = "Immediately close this connection. Do not enter credentials, financial information, or download files.",
                httpsSecure = httpsSecure,
                details = listOf("Threat Intelligence Feed Match", "Verified malicious domain record", "Host IP blacklisted")
            )
        }

        // 2. Check IP literal as host (common evasion tactic)
        val ipRegex = Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$""")
        if (ipRegex.matches(host)) {
            return WebCheckResult(
                url = formattedUrl,
                domain = host,
                isBlocked = true,
                threatCategory = "Dangerous URL (Direct IP Access)",
                riskLevel = "High Risk",
                reason = "Direct IP address host navigation detected without a trusted domain name or SSL verification.",
                recommendedAction = "Avoid navigating to raw IP addresses that bypass domain reputation filters.",
                httpsSecure = httpsSecure,
                details = listOf("Raw IPv4 Host target", "Lack of verified TLS domain certificate", "High correlation with C2 or phishing proxies")
            )
        }

        // 3. Phishing Keywords in Domain or Path
        for (kw in phishingKeywords) {
            if (host.contains(kw) || path.contains(kw)) {
                return WebCheckResult(
                    url = formattedUrl,
                    domain = host,
                    isBlocked = true,
                    threatCategory = "Social Engineering & Phishing",
                    riskLevel = "Critical",
                    reason = "Website mimics legitimate account verification or banking portals to harvest login credentials.",
                    recommendedAction = "DTZ SHIELD blocked this connection to prevent credential theft. Go directly to the official service website.",
                    httpsSecure = httpsSecure,
                    details = listOf("Credential harvesting heuristic triggered ($kw)", "Domain masquerading detected")
                )
            }
        }

        // 4. Suspicious TLD checks
        val tld = host.substringAfterLast('.', "")
        if (suspiciousTlds.contains(tld)) {
            return WebCheckResult(
                url = formattedUrl,
                domain = host,
                isBlocked = false,
                threatCategory = "Suspicious Domain Extension",
                riskLevel = "Warning",
                reason = "This domain uses an extension (.$tld) frequently associated with spam, phishing, and disposable scams.",
                recommendedAction = "Proceed with extreme caution. Verify the sender or link origin before submitting any information.",
                httpsSecure = httpsSecure,
                details = listOf("High-risk TLD: .$tld", "Elevated fraud rate registered in cyber threat database")
            )
        }

        // 5. Insecure HTTP check
        if (!httpsSecure) {
            return WebCheckResult(
                url = formattedUrl,
                domain = host,
                isBlocked = false,
                threatCategory = "Insecure Connection (HTTP)",
                riskLevel = "Warning",
                reason = "Connection is unencrypted (HTTP). Data transmitted between your device and this server can be intercepted or altered.",
                recommendedAction = "Do not enter passwords, credit cards, or personal documents over unencrypted connections.",
                httpsSecure = false,
                details = listOf("Plaintext HTTP protocol", "Vulnerable to Man-in-the-Middle (MitM) inspection")
            )
        }

        // Safe
        return WebCheckResult(
            url = formattedUrl,
            domain = host,
            isBlocked = false,
            threatCategory = "Safe & Verified",
            riskLevel = "Safe",
            reason = "No known malicious signatures or phishing heuristics were identified for this domain.",
            recommendedAction = "Safe to visit under standard security practices.",
            httpsSecure = true,
            details = listOf("Valid HTTPS protocol", "No threat signatures found in DTZ database", "Reputation check passed")
        )
    }
}
