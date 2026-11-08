package com.example.model

enum class PachakkhanType {
    NAVKARSI,
    PORSI,
    SADH_PORSI,
    PURIMADDH,
    AVADDH,
    CHAUVIHAR,
    EKASHNA,
    BIYASHNA,
    AYAMBIL,
    UPVAS
}

data class PachakkhanItem(
    val id: String,
    val type: PachakkhanType,
    val titleGujarati: String,
    val titleHindi: String,
    val titleEnglish: String,
    val timingRule: String,
    val durationDescription: String,
    val sutraGujarati: String,
    val sutraHindi: String,
    val sutraEnglishTransliteration: String,
    val englishMeaning: String,
    val benefits: String
)
