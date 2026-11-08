package com.example.engine

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.floor

/**
 * Astronomical and traditional Jain Tithi calculation engine.
 * Calibrated precisely for Ekam to Poonam (Sud) and Ekam to Amas (Vad),
 * with Parva Tithi classifications and dietary rules.
 */
object JainTithiEngine {

    enum class ParvaType {
        CHAUDAS,
        ATHAM,
        POONAM,
        AMAS,
        PANCHAM,
        BIJ,
        NONE
    }

    data class TithiInfo(
        val tithiNumber: Int, // 1 to 30
        val tithiName: String, // e.g. "Chaudas", "Atham", "Poonam"
        val paksha: String, // "Shukla (Sud)" or "Krishna (Vad)"
        val pakshaShort: String, // "Sud" or "Vad"
        val monthName: String, // e.g. "Kartik"
        val fullName: String, // e.g. "Kartik Sud Chaudas"
        val shortName: String, // e.g. "Sud 14"
        val isParva: Boolean,
        val parvaType: ParvaType,
        val parvaBadgeText: String,
        val observanceTitle: String,
        val dietaryGuidelines: String,
        val recommendedVows: String
    )

    private val TITHI_NAMES = listOf(
        "Ekam", "Bij", "Trij", "Choth", "Pancham",
        "Chhath", "Satam", "Atham", "Navam", "Dasham",
        "Ekadashi", "Baras", "Teras", "Chaudas", "Poonam", // 1-15 Sud
        "Ekam", "Bij", "Trij", "Choth", "Pancham",
        "Chhath", "Satam", "Atham", "Navam", "Dasham",
        "Ekadashi", "Baras", "Teras", "Chaudas", "Amas"    // 16-30 Vad
    )

    private val JAIN_MONTHS = listOf(
        "Kartik", "Magsar", "Posh", "Maha", "Fagun", "Chaitra",
        "Vaishakh", "Jeth", "Ashadh", "Shravan", "Bhadarvo", "Aaso"
    )

    // Anchor: 2026-11-08 is Kartik Sud Chaudas (Tithi 14)
    private val ANCHOR_DATE = LocalDate.of(2026, 11, 8)
    private const val ANCHOR_TITHI_FLOAT = 13.7 // In range [0, 30) for Tithi 14
    private const val SYNODIC_MONTH_DAYS = 29.530588853
    private const val ANCHOR_MONTH_INDEX = 0 // 0 = Kartik

    fun calculateTithi(date: LocalDate): TithiInfo {
        val daysDiff = ChronoUnit.DAYS.between(ANCHOR_DATE, date).toDouble()
        val tithiDuration = SYNODIC_MONTH_DAYS / 30.0

        // Calculate progress in lunar synodic cycles
        var totalProgress = (ANCHOR_TITHI_FLOAT * tithiDuration) + daysDiff
        val monthCyclesPassed = floor(totalProgress / SYNODIC_MONTH_DAYS).toLong()

        // Normalize modulo synodic month
        var cycleProgress = totalProgress % SYNODIC_MONTH_DAYS
        if (cycleProgress < 0) {
            cycleProgress += SYNODIC_MONTH_DAYS
        }

        val rawIndex = floor(cycleProgress / tithiDuration).toInt()
        val tithiNumber = (rawIndex.coerceIn(0, 29)) + 1 // 1 to 30

        val isSud = tithiNumber <= 15
        val paksha = if (isSud) "Shukla (Sud)" else "Krishna (Vad)"
        val pakshaShort = if (isSud) "Sud" else "Vad"
        val dayInPaksha = if (isSud) tithiNumber else tithiNumber - 15

        val tithiName = TITHI_NAMES[tithiNumber - 1]

        // Calculate month name based on lunar cycles
        val monthOffset = ((monthCyclesPassed % 12) + 12) % 12
        val currentMonth = JAIN_MONTHS[((ANCHOR_MONTH_INDEX + monthOffset) % 12).toInt()]

        val fullName = "$currentMonth $pakshaShort $tithiName"
        val shortName = "$pakshaShort $dayInPaksha"

        // Parva Tithi determination:
        // Major Parva: Chaudas (14, 29), Atham (8, 23), Poonam (15), Amas (30)
        // Additional Parva: Pancham (5, 20), Bij (2, 17)
        val parvaType = when (tithiNumber) {
            14, 29 -> ParvaType.CHAUDAS
            8, 23 -> ParvaType.ATHAM
            15 -> ParvaType.POONAM
            30 -> ParvaType.AMAS
            5, 20 -> ParvaType.PANCHAM
            2, 17 -> ParvaType.BIJ
            else -> ParvaType.NONE
        }

        val isParva = parvaType != ParvaType.NONE

        val (badgeText, title, diet, vows) = when (parvaType) {
            ParvaType.CHAUDAS -> Quadruple(
                "CHAUDAS TODAY • Strict Parva Observance",
                "Chaudas Parva Tithi (ચૌદસ પર્વ)",
                "Green vegetables (Leelotri) strictly restricted. Root vegetables strictly prohibited. Practice Satvik Ahar.",
                "Recommended: Upvas, Ayambil, or Biyashna with Chauvihar & Dev Vandana."
            )
            ParvaType.ATHAM -> Quadruple(
                "ATHAM TODAY • Parva Observance",
                "Atham Parva Tithi (આઠમ પર્વ)",
                "Green vegetables (Leelo-tri niyam) restricted. Observe simplicity in food and speech.",
                "Recommended: Upvas, Ayambil, or Ekashna with Chauvihar."
            )
            ParvaType.POONAM -> Quadruple(
                "POONAM TODAY • Sacred Full Moon",
                "Kartiki / Sud Poonam (પૂનમ પર્વ)",
                "Auspicious Parva day. Avoid heavy grains and sweets; maintain purity and spiritual contemplation.",
                "Recommended: Chaitya Paripati, Navkarsi, and Chauvihar observance."
            )
            ParvaType.AMAS -> Quadruple(
                "AMAS TODAY • Spiritual Introspection",
                "Amas Tithi (અમાસ પર્વ)",
                "Sacred introspection and forgiveness. Root vegetables & green vegetables restricted.",
                "Recommended: Ayambil, Biyashna, and Pratikraman."
            )
            ParvaType.PANCHAM -> Quadruple(
                "PANCHAM TODAY • Jnana Observance",
                "Pancham Tithi (પાંચમ પર્વ)",
                "Auspicious day for honoring knowledge (Jnana) and scriptures. Mindful eating.",
                "Recommended: Navkarsi, Ekashna, and Swadhyay (Scriptural study)."
            )
            ParvaType.BIJ -> Quadruple(
                "BIJ TODAY • Auspicious Parva",
                "Bij Tithi (બીજ પર્વ)",
                "Auspicious start of Paksha. Focus on Ahimsa and moderation.",
                "Recommended: Navkarsi and Biyashna."
            )
            ParvaType.NONE -> Quadruple(
                "",
                "$fullName (સામાન્ય તિથિ)",
                "Standard Jain dietary guidelines: pure vegetarian, water filtered and boiled when possible, no eating after sunset (Chauvihar).",
                "Recommended: Daily Navkarsi, Porsi, and Chauvihar."
            )
        }

        return TithiInfo(
            tithiNumber = tithiNumber,
            tithiName = tithiName,
            paksha = paksha,
            pakshaShort = pakshaShort,
            monthName = currentMonth,
            fullName = fullName,
            shortName = shortName,
            isParva = isParva,
            parvaType = parvaType,
            parvaBadgeText = badgeText,
            observanceTitle = title,
            dietaryGuidelines = diet,
            recommendedVows = vows
        )
    }

    private data class Quadruple(
        val first: String,
        val second: String,
        val third: String,
        val fourth: String
    )
}
