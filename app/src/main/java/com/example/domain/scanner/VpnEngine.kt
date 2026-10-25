package com.example.domain.scanner

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class VpnServer(
    val id: String,
    val country: String,
    val city: String,
    val flagEmoji: String,
    val pingMs: Int,
    val loadPercent: Int,
    val virtualIp: String,
    val protocol: String = "WireGuard FastTunnel"
)

data class VpnStatus(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val currentServer: VpnServer,
    val realIp: String = "192.168.1.15",
    val virtualIp: String = "",
    val durationSeconds: Long = 0,
    val bytesDownloadedMb: Double = 0.0,
    val bytesUploadedMb: Double = 0.0,
    val downloadSpeedMbps: Double = 0.0,
    val uploadSpeedMbps: Double = 0.0,
    val currentPingMs: Int = 18,
    val killSwitchEnabled: Boolean = true,
    val dnsLeakProtection: Boolean = true
)

class VpnEngine {

    val availableServers = listOf(
        VpnServer("us-ny", "United States", "New York", "🇺🇸", 18, 32, "104.28.19.45"),
        VpnServer("us-ca", "United States", "Silicon Valley", "🇺🇸", 22, 28, "198.41.214.88"),
        VpnServer("de-fra", "Germany", "Frankfurt", "🇩🇪", 28, 41, "185.220.101.5"),
        VpnServer("uk-lon", "United Kingdom", "London", "🇬🇧", 26, 35, "194.36.108.12"),
        VpnServer("jp-tyo", "Japan", "Tokyo", "🇯🇵", 34, 45, "103.251.167.20"),
        VpnServer("sg-sin", "Singapore", "Singapore", "🇸🇬", 31, 38, "139.99.120.14"),
        VpnServer("ca-tor", "Canada", "Toronto", "🇨🇦", 24, 29, "199.195.250.77"),
        VpnServer("au-syd", "Australia", "Sydney", "🇦🇺", 42, 33, "103.1.206.18")
    )

    fun simulateConnectionStream(server: VpnServer): Flow<VpnStatus> = flow {
        // Phase 1: Connecting handshake
        emit(
            VpnStatus(
                isConnected = false,
                isConnecting = true,
                currentServer = server,
                realIp = "192.168.1.15",
                virtualIp = "Securing tunnel..."
            )
        )
        delay(600)

        // Phase 2: Route encryption & IP mask
        emit(
            VpnStatus(
                isConnected = false,
                isConnecting = true,
                currentServer = server,
                realIp = "192.168.1.15",
                virtualIp = "Masking IP with ${server.virtualIp}..."
            )
        )
        delay(500)

        // Phase 3: Connected & Active
        emit(
            VpnStatus(
                isConnected = true,
                isConnecting = false,
                currentServer = server,
                realIp = "192.168.1.15",
                virtualIp = server.virtualIp,
                currentPingMs = server.pingMs,
                downloadSpeedMbps = 84.5,
                uploadSpeedMbps = 42.1,
                bytesDownloadedMb = 1.2,
                bytesUploadedMb = 0.4
            )
        )
    }
}
