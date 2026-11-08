import { ParvaType, TithiInfo } from '../types';

export class JainTithiEngine {
  private static readonly TITHI_NAMES = [
    'Ekam', 'Bij', 'Trij', 'Choth', 'Pancham',
    'Chhath', 'Satam', 'Atham', 'Navam', 'Dasham',
    'Ekadashi', 'Baras', 'Teras', 'Chaudas', 'Poonam', // 1-15 Sud
    'Ekam', 'Bij', 'Trij', 'Choth', 'Pancham',
    'Chhath', 'Satam', 'Atham', 'Navam', 'Dasham',
    'Ekadashi', 'Baras', 'Teras', 'Chaudas', 'Amas' // 16-30 Vad
  ];

  private static readonly JAIN_MONTHS = [
    'Kartik', 'Magsar', 'Posh', 'Maha', 'Fagun', 'Chaitra',
    'Vaishakh', 'Jeth', 'Ashadh', 'Shravan', 'Bhadarvo', 'Aaso'
  ];

  // Anchor: 2026-11-08 is Kartik Sud Chaudas (Tithi 14)
  private static readonly ANCHOR_DATE = new Date(Date.UTC(2026, 10, 8)); // Month is 0-indexed (10 = November)
  private static readonly ANCHOR_TITHI_FLOAT = 13.7;
  private static readonly SYNODIC_MONTH_DAYS = 29.530588853;
  private static readonly ANCHOR_MONTH_INDEX = 0; // 0 = Kartik

  public static calculateTithi(date: Date): TithiInfo {
    const targetUtc = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const daysDiff = (targetUtc.getTime() - this.ANCHOR_DATE.getTime()) / (1000 * 60 * 60 * 24);
    const tithiDuration = this.SYNODIC_MONTH_DAYS / 30.0;

    let totalProgress = this.ANCHOR_TITHI_FLOAT * tithiDuration + daysDiff;
    const monthCyclesPassed = Math.floor(totalProgress / this.SYNODIC_MONTH_DAYS);

    let cycleProgress = totalProgress % this.SYNODIC_MONTH_DAYS;
    if (cycleProgress < 0) {
      cycleProgress += this.SYNODIC_MONTH_DAYS;
    }

    const rawIndex = Math.floor(cycleProgress / tithiDuration);
    const tithiNumber = Math.min(30, Math.max(1, rawIndex + 1));

    const isSud = tithiNumber <= 15;
    const paksha = isSud ? 'Shukla (Sud)' : 'Krishna (Vad)';
    const pakshaShort = isSud ? 'Sud' : 'Vad';
    const dayInPaksha = isSud ? tithiNumber : tithiNumber - 15;

    const tithiName = this.TITHI_NAMES[tithiNumber - 1];

    const monthOffset = ((monthCyclesPassed % 12) + 12) % 12;
    const currentMonth = this.JAIN_MONTHS[(this.ANCHOR_MONTH_INDEX + monthOffset) % 12];

    const fullName = `${currentMonth} ${pakshaShort} ${tithiName}`;
    const shortName = `${pakshaShort} ${dayInPaksha}`;

    let parvaType: ParvaType = 'NONE';
    if (tithiNumber === 14 || tithiNumber === 29) {
      parvaType = 'CHAUDAS';
    } else if (tithiNumber === 8 || tithiNumber === 23) {
      parvaType = 'ATHAM';
    } else if (tithiNumber === 15) {
      parvaType = 'POONAM';
    } else if (tithiNumber === 30) {
      parvaType = 'AMAS';
    } else if (tithiNumber === 5 || tithiNumber === 20) {
      parvaType = 'PANCHAM';
    } else if (tithiNumber === 2 || tithiNumber === 17) {
      parvaType = 'BIJ';
    }

    const isParva = parvaType !== 'NONE';

    let badgeText = '';
    let title = '';
    let diet = '';
    let vows = '';

    switch (parvaType) {
      case 'CHAUDAS':
        badgeText = 'CHAUDAS TODAY • Strict Parva Observance';
        title = 'Chaudas Parva Tithi (ચૌદસ પર્વ)';
        diet = 'Green vegetables (Leelotri) strictly restricted. Root vegetables strictly prohibited. Practice Satvik Ahar.';
        vows = 'Recommended: Upvas, Ayambil, or Biyashna with Chauvihar & Dev Vandana.';
        break;
      case 'ATHAM':
        badgeText = 'ATHAM TODAY • Parva Observance';
        title = 'Atham Parva Tithi (આઠમ પર્વ)';
        diet = 'Green vegetables (Leelo-tri niyam) restricted. Observe simplicity in food and speech.';
        vows = 'Recommended: Upvas, Ayambil, or Ekashna with Chauvihar.';
        break;
      case 'POONAM':
        badgeText = 'POONAM TODAY • Sacred Full Moon';
        title = 'Kartiki / Sud Poonam (પૂનમ પર્વ)';
        diet = 'Auspicious Parva day. Avoid heavy grains and sweets; maintain purity and spiritual contemplation.';
        vows = 'Recommended: Chaitya Paripati, Navkarsi, and Chauvihar observance.';
        break;
      case 'AMAS':
        badgeText = 'AMAS TODAY • Spiritual Introspection';
        title = 'Amas Tithi (અમાસ પર્વ)';
        diet = 'Sacred introspection and forgiveness. Root vegetables & green vegetables restricted.';
        vows = 'Recommended: Ayambil, Biyashna, and Pratikraman.';
        break;
      case 'PANCHAM':
        badgeText = 'PANCHAM TODAY • Jnana Observance';
        title = 'Pancham Tithi (પાંચમ પર્વ)';
        diet = 'Auspicious day for honoring knowledge (Jnana) and scriptures. Mindful eating.';
        vows = 'Recommended: Navkarsi, Ekashna, and Swadhyay (Scriptural study).';
        break;
      case 'BIJ':
        badgeText = 'BIJ TODAY • Auspicious Parva';
        title = 'Bij Tithi (બીજ પર્વ)';
        diet = 'Auspicious start of Paksha. Focus on Ahimsa and moderation.';
        vows = 'Recommended: Navkarsi and Biyashna.';
        break;
      default:
        badgeText = '';
        title = `${fullName} (સામાન્ય તિથિ)`;
        diet = 'Standard Jain dietary guidelines: pure vegetarian, water filtered and boiled when possible, no eating after sunset (Chauvihar).';
        vows = 'Recommended: Daily Navkarsi, Porsi, and Chauvihar.';
        break;
    }

    return {
      tithiNumber,
      tithiName,
      paksha,
      pakshaShort,
      monthName: currentMonth,
      fullName,
      shortName,
      isParva,
      parvaType,
      parvaBadgeText: badgeText,
      observanceTitle: title,
      dietaryGuidelines: diet,
      recommendedVows: vows
    };
  }
}

