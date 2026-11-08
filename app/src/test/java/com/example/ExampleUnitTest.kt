package com.example

import com.example.engine.JainTithiEngine
import com.example.engine.SolarAstronomyEngine
import org.junit.Assert.*
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class ExampleUnitTest {

  @Test
  fun testTithiAnchorOnNov8_2026IsChaudas() {
    val date = LocalDate.of(2026, 11, 8)
    val tithi = JainTithiEngine.calculateTithi(date)

    assertEquals(14, tithi.tithiNumber)
    assertEquals("Chaudas", tithi.tithiName)
    assertTrue(tithi.isParva)
    assertEquals(JainTithiEngine.ParvaType.CHAUDAS, tithi.parvaType)
  }

  @Test
  fun testTithiOnNov9_2026IsPoonam() {
    val date = LocalDate.of(2026, 11, 9)
    val tithi = JainTithiEngine.calculateTithi(date)

    assertEquals(15, tithi.tithiNumber)
    assertEquals("Poonam", tithi.tithiName)
    assertTrue(tithi.isParva)
    assertEquals(JainTithiEngine.ParvaType.POONAM, tithi.parvaType)
  }

  @Test
  fun testSolarAstronomyCalculations() {
    val date = LocalDate.of(2026, 11, 8)
    val solar = SolarAstronomyEngine.calculateSolarTimes(
      date = date,
      latitude = 23.0225, // Ahmedabad
      longitude = 72.5714
    )

    // Solar times must be in Indian Standard Time (IST - Asia/Kolkata)
    assertEquals(java.time.ZoneId.of("Asia/Kolkata"), solar.timeZone)
    assertEquals(java.time.ZoneId.of("Asia/Kolkata"), solar.sunrise.zone)

    // Sunrise should be before sunset
    assertTrue(solar.sunrise.isBefore(solar.sunset))

    // Navkarsi should be exactly 48 minutes after sunrise
    val navkarsiMinutes = Duration.between(solar.sunrise, solar.navkarsi).toMinutes()
    assertEquals(48L, navkarsiMinutes)

    // Porsi should be after sunrise and before sunset
    assertTrue(solar.porsi.isAfter(solar.sunrise))
    assertTrue(solar.porsi.isBefore(solar.sunset))

    // Purimaddh should be after Porsi
    assertTrue(solar.purimaddh.isAfter(solar.porsi))

    // Avaddh should be after Purimaddh
    assertTrue(solar.avaddh.isAfter(solar.purimaddh))
  }
}
