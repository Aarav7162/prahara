import Foundation

public enum PachakkhanType: String, CaseIterable, Codable {
    case navkarsi = "NAVKARSI"
    case porsi = "PORSI"
    case sadhPorsi = "SADH_PORSI"
    case purimaddh = "PURIMADDH"
    case avaddh = "AVADDH"
    case chauvihar = "CHAUVIHAR"
    case ekashna = "EKASHNA"
    case biyashna = "BIYASHNA"
    case ayambil = "AYAMBIL"
    case upvas = "UPVAS"
}

public struct PachakkhanItem: Identifiable, Codable {
    public let id: String
    public let type: PachakkhanType
    public let titleGujarati: String
    public let titleHindi: String
    public let titleEnglish: String
    public let timingRule: String
    public let durationDescription: String
    public let sutraGujarati: String
    public let sutraHindi: String
    public let sutraEnglishTransliteration: String
    public let englishMeaning: String
    public let benefits: String

    public init(
        id: String,
        type: PachakkhanType,
        titleGujarati: String,
        titleHindi: String,
        titleEnglish: String,
        timingRule: String,
        durationDescription: String,
        sutraGujarati: String,
        sutraHindi: String,
        sutraEnglishTransliteration: String,
        englishMeaning: String,
        benefits: String
    ) {
        self.id = id
        self.type = type
        self.titleGujarati = titleGujarati
        self.titleHindi = titleHindi
        self.titleEnglish = titleEnglish
        self.timingRule = timingRule
        self.durationDescription = durationDescription
        self.sutraGujarati = sutraGujarati
        self.sutraHindi = sutraHindi
        self.sutraEnglishTransliteration = sutraEnglishTransliteration
        self.englishMeaning = englishMeaning
        self.benefits = benefits
    }
}

