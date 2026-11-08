import Foundation

public enum ParvaType: String, CaseIterable, Codable {
    case chaudas = "CHAUDAS"
    case atham = "ATHAM"
    case poonam = "POONAM"
    case amas = "AMAS"
    case pancham = "PANCHAM"
    case bij = "BIJ"
    case none = "NONE"
}

public struct TithiInfo {
    public let tithiNumber: Int
    public let tithiName: String
    public let paksha: String
    public let pakshaShort: String
    public let monthName: String
    public let fullName: String
    public let shortName: String
    public let isParva: Bool
    public let parvaType: ParvaType
    public let parvaBadgeText: String
    public let observanceTitle: String
    public let dietaryGuidelines: String
    public let recommendedVows: String
}

public class JainTithiEngine {
    private static let tithiNames = [
        "Ekam", "Bij", "Trij", "Choth", "Pancham",
        "Chhath", "Satam", "Atham", "Navam", "Dasham",
        "Ekadashi", "Baras", "Teras", "Chaudas", "Poonam",
        "Ekam", "Bij", "Trij", "Choth", "Pancham",
        "Chhath", "Satam", "Atham", "Navam", "Dasham",
        "Ekadashi", "Baras", "Teras", "Chaudas", "Amas"
    ]

    private static let jainMonths = [
        "Kartik", "Magsar", "Posh", "Maha", "Fagun", "Chaitra",
        "Vaishakh", "Jeth", "Ashadh", "Shravan", "Bhadarvo", "Aaso"
    ]

    private static let anchorDate: Date = {
        var comps = DateComponents()
        comps.year = 2026
        comps.month = 11
        comps.day = 8
        comps.timeZone = TimeZone(secondsFromGMT: 0)
        return Calendar.current.date(from: comps) ?? Date()
    }()

    private static let anchorTithiFloat = 13.7
    private static let synodicMonthDays = 29.530588853
    private static let anchorMonthIndex = 0

    public static func calculateTithi(date: Date) -> TithiInfo {
        let calendar = Calendar.current
        let daysDiff = date.timeIntervalSince(anchorDate) / (86400.0)
        let tithiDuration = synodicMonthDays / 30.0

        var totalProgress = (anchorTithiFloat * tithiDuration) + daysDiff
        let monthCyclesPassed = Int(floor(totalProgress / synodicMonthDays))

        var cycleProgress = totalProgress.truncatingRemainder(dividingBy: synodicMonthDays)
        if cycleProgress < 0 { cycleProgress += synodicMonthDays }

        let rawIndex = Int(floor(cycleProgress / tithiDuration))
        let tithiNumber = min(30, max(1, rawIndex + 1))

        let isSud = tithiNumber <= 15
        let paksha = isSud ? "Shukla (Sud)" : "Krishna (Vad)"
        let pakshaShort = isSud ? "Sud" : "Vad"
        let dayInPaksha = isSud ? tithiNumber : tithiNumber - 15

        let tithiName = tithiNames[tithiNumber - 1]
        let monthOffset = ((monthCyclesPassed % 12) + 12) % 12
        let currentMonth = jainMonths[(anchorMonthIndex + monthOffset) % 12]

        let fullName = "\(currentMonth) \(pakshaShort) \(tithiName)"
        let shortName = "\(pakshaShort) \(dayInPaksha)"

        let parvaType: ParvaType
        switch tithiNumber {
        case 14, 29: parvaType = .chaudas
        case 8, 23: parvaType = .atham
        case 15: parvaType = .poonam
        case 30: parvaType = .amas
        case 5, 20: parvaType = .pancham
        case 2, 17: parvaType = .bij
        default: parvaType = .none
        }

        let isParva = parvaType != .none
        let badgeText: String
        let title: String
        let diet: String
        let vows: String

        switch parvaType {
        case .chaudas:
            badgeText = "CHAUDAS TODAY • Strict Parva Observance"
            title = "Chaudas Parva Tithi (ચૌદસ પર્વ)"
            diet = "Green vegetables (Leelotri) strictly restricted. Root vegetables strictly prohibited. Practice Satvik Ahar."
            vows = "Recommended: Upvas, Ayambil, or Biyashna with Chauvihar & Dev Vandana."
        case .atham:
            badgeText = "ATHAM TODAY • Parva Observance"
            title = "Atham Parva Tithi (આઠમ પર્વ)"
            diet = "Green vegetables (Leelo-tri niyam) restricted. Observe simplicity in food and speech."
            vows = "Recommended: Upvas, Ayambil, or Ekashna with Chauvihar."
        case .poonam:
            badgeText = "POONAM TODAY • Sacred Full Moon"
            title = "Kartiki / Sud Poonam (પૂનમ પર્વ)"
            diet = "Auspicious Parva day. Avoid heavy grains and sweets; maintain purity and spiritual contemplation."
            vows = "Recommended: Chaitya Paripati, Navkarsi, and Chauvihar observance."
        case .amas:
            badgeText = "AMAS TODAY • Spiritual Introspection"
            title = "Amas Tithi (અમાસ પર્વ)"
            diet = "Sacred introspection and forgiveness. Root vegetables & green vegetables restricted."
            vows = "Recommended: Ayambil, Biyashna, and Pratikraman."
        case .pancham:
            badgeText = "PANCHAM TODAY • Jnana Observance"
            title = "Pancham Tithi (પાંચમ પર્વ)"
            diet = "Auspicious day for honoring knowledge (Jnana) and scriptures. Mindful eating."
            vows = "Recommended: Navkarsi, Ekashna, and Swadhyay (Scriptural study)."
        case .bij:
            badgeText = "BIJ TODAY • Auspicious Parva"
            title = "Bij Tithi (બીજ પર્વ)"
            diet = "Auspicious start of Paksha. Focus on Ahimsa and moderation."
            vows = "Recommended: Navkarsi and Biyashna."
        case .none:
            badgeText = ""
            title = "\(fullName) (સામાન્ય તિથિ)"
            diet = "Standard Jain dietary guidelines: pure vegetarian, water filtered and boiled when possible, no eating after sunset (Chauvihar)."
            vows = "Recommended: Daily Navkarsi, Porsi, and Chauvihar."
        }

        return TithiInfo(
            tithiNumber: tithiNumber,
            tithiName: tithiName,
            paksha: paksha,
            pakshaShort: pakshaShort,
            monthName: currentMonth,
            fullName: fullName,
            shortName: shortName,
            isParva: isParva,
            parvaType: parvaType,
            parvaBadgeText: badgeText,
            observanceTitle: title,
            dietaryGuidelines: diet,
            recommendedVows: vows
        )
    }
}

