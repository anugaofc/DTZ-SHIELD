package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.scanner.WebShieldEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DTZ SHIELD", appName)
  }

  @Test
  fun `web shield correctly detects phishing and malicious domains`() {
    val engine = WebShieldEngine()
    val phishingResult = engine.evaluateUrl("https://bank-login-secure.xyz")
    assertTrue(phishingResult.isBlocked)

    val cleanResult = engine.evaluateUrl("https://google.com")
    assertTrue(!cleanResult.isBlocked)
  }
}
