import Foundation

public class PachakkhanRepository {
    public static let allPachakkhans: [PachakkhanItem] = [
        PachakkhanItem(
            id: "navkarsi",
            type: .navkarsi,
            titleGujarati: "નવકારસી (Navkarsi)",
            titleHindi: "नवकारसी (Navkarsi)",
            titleEnglish: "Navkarsi",
            timingRule: "સૂર્યોદય પછી ૪૮ મિનિટ (૨ ઘડી) સુધી",
            durationDescription: "Sunrise + 48 minutes",
            sutraGujarati: "ઉગ્ગએ સૂરે નમુક્કારસહિઅં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे णमुक्कारसहिअं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore namukkara-sahiam pachchakkhai, annatthana-bhogenam, sahasa-garenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "From sunrise until 48 minutes (Navkarsi) after reciting the Namokar Mantra, I renounce food and drink. Except in cases of unintentional oversight, sudden emergency, directive of an elder/guru, or maintaining peace of mind, I renounce all.",
            benefits: "Practiced daily to begin the morning with mindfulness, gratitude to the Pancha Parameshti, and control over morning desires."
        ),
        PachakkhanItem(
            id: "porsi",
            type: .porsi,
            titleGujarati: "પોરિસી (Porsi)",
            titleHindi: "पोरिसी (Porsi)",
            titleEnglish: "Porsi",
            timingRule: "સૂર્યોદયથી દિવસનો ૧/૪ ભાગ (૧ પ્રહર) સુધી",
            durationDescription: "Sunrise + 1/4th daylight (1 Prahar ~ 3 hours)",
            sutraGujarati: "ઉગ્ગએ સૂરે પોરિસિં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, પચ્છન્નકાલેણં, દિસામોહેણં, સાહુભાવેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे पोरिसिं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, पच्छन्नकालेणं, दिसामोहेणं, साहुभावेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore porisim pachchakkhai, annatthana-bhogenam, sahasa-garenam, pachchhanna-kalenam, disa-mohenam, sahu-bhavenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "From sunrise until one Prahar (approx. 3 hours) has elapsed, I renounce all food and drink with proper exceptions.",
            benefits: "Helps maintain focus and spiritual study (Swadhyay) during the first quarter of the day."
        ),
        PachakkhanItem(
            id: "sadh_porsi",
            type: .sadhPorsi,
            titleGujarati: "સાઢ-પોરિસી (Sadh-Porsi)",
            titleHindi: "साढ-पोरिसी (Sadh-Porsi)",
            titleEnglish: "Sadh-Porsi",
            timingRule: "સૂર્યોદયથી દિવસનો ૩/૮ ભાગ (દોઢ પ્રહર) સુધી",
            durationDescription: "Sunrise + 3/8th daylight (1.5 Prahars ~ 4.5 hours)",
            sutraGujarati: "ઉગ્ગએ સૂરે સાડ્ઢપોરિસિં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, પચ્છન્નકાલેણં, દિસામોહેણં, સાહુભાવેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे साड्ढपोरिसिं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, पच्छन्नकालेणं, दिसामोहेणं, साहुभावेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore saddha-porisim pachchakkhai, annatthana-bhogenam, sahasa-garenam, pachchhanna-kalenam, disa-mohenam, sahu-bhavenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "From sunrise until one and a half Prahars have passed, I observe restraint from consuming food or beverages.",
            benefits: "Purifies bodily energies and deepens inner endurance throughout the morning."
        ),
        PachakkhanItem(
            id: "purimaddh",
            type: .purimaddh,
            titleGujarati: "પુરિમડ્ઢ (Purimaddh)",
            titleHindi: "पुरिमड्ढ (Purimaddh)",
            titleEnglish: "Purimaddh",
            timingRule: "સૂર્યોદયથી દિવસનો અડધો ભાગ (૨ પ્રહર / મધ્યાહ્ન) સુધી",
            durationDescription: "Sunrise + 1/2 daylight (2 Prahars / Midday)",
            sutraGujarati: "ઉગ્ગએ સૂરે પુરિમડ્ઢં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, પચ્છન્નકાલેણં, દિસામોહેણં, સાહુભાવેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे पुरिमड्ढं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, पच्छन्नकालेणं, दिसामोहेणं, साहुभावेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore purimaddham pachchakkhai, annatthana-bhogenam, sahasa-garenam, pachchhanna-kalenam, disa-mohenam, sahu-bhavenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I renounce all food and drink until midday (the completion of the first half of daylight, two Prahars).",
            benefits: "Fosters mastery over hunger and aligns digestive rhythm with the natural sun arc."
        ),
        PachakkhanItem(
            id: "avaddh",
            type: .avaddh,
            titleGujarati: "અવડ્ઢ (Avaddh)",
            titleHindi: "अवड્ढ (Avaddh)",
            titleEnglish: "Avaddh",
            timingRule: "સૂર્યોદયથી દિવસનો ૩/૪ ભાગ (૩ પ્રહર) સુધી",
            durationDescription: "Sunrise + 3/4th daylight (3 Prahars ~ late afternoon)",
            sutraGujarati: "ઉગ્ગએ સૂરે અવડ્ઢં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, પચ્છન્નકાલેણં, દિસામોહેણં, સાહુભાવેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे अवड्ढं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, पच्छन्नकालेणं, दिसामोहेणं, साहुभावेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore avaddham pachchakkhai, annatthana-bhogenam, sahasa-garenam, pachchhanna-kalenam, disa-mohenam, sahu-bhavenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I renounce all intake until three Prahars of daylight are complete (approx. late afternoon).",
            benefits: "An advanced daytime vow preparing the soul for Ekashna or Upvas."
        ),
        PachakkhanItem(
            id: "chauvihar",
            type: .chauvihar,
            titleGujarati: "ચૌવિહાર (Chauvihar)",
            titleHindi: "चौविहार (Chauvihar)",
            titleEnglish: "Chauvihar",
            timingRule: "સૂર્યાસ્ત સમયે, રાત્રિ દરમિયાન કોઈ પણ આહાર કે પાણી નહીં",
            durationDescription: "Sunset (Surya Ast) to Next Morning Sunrise/Navkarsi",
            sutraGujarati: "દવ્વ-ઓહારેણં ચઉવ્વિહંપિ આહારં અસણં પાણં ખાઇમં સાઇમં અન્નત્થણાભોગેણં સહસાગારેણં મહત્તરાગારેણં સવ્વસમાહિવત્તિયાગારેણં વોસિરાઇ.",
            sutraHindi: "दव्व-ओहारेणं चउव्विहंपि आहारं असणं पाणं खाइमं साइमं अन्नत्थणाभोगेणं सहसागारेणं महत्तरागारेणं सव्वसमाहिवत्तियागारेणं वोसिराइ।",
            sutraEnglishTransliteration: "Davva-oharenam chauvvihampi aharam asanam panam khaimam saimam annatthana-bhogenam sahasa-garenam mahattara-garenam savva-samahi-vattiya-garenam vosirai.",
            englishMeaning: "I renounce all four kinds of intake from sunset until the next morning.",
            benefits: "Cardinal pillar of Jain Ahimsa, avoiding harm to organisms manifested in dark, and enhancing cellular repair."
        ),
        PachakkhanItem(
            id: "ekashna",
            type: .ekashna,
            titleGujarati: "એકાસણું (Ekashna)",
            titleHindi: "एकासणा (Ekashna)",
            titleEnglish: "Ekashna",
            timingRule: "દિવસમાં એક જ આસને બેસીને એક જ વાર ભોજન કરવું",
            durationDescription: "Single meal seated in one posture, boiled water until Porsi/sunset",
            sutraGujarati: "ઉગ્ગએ સૂરે એગાસણં પચ્ચક્ખાઈ, તિવિહં પિ આહારં... અન્નત્થણાભોગેણં, સહસાગારેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे एगासणं पच्चक्खाइ, तिविहं पि आहारं... अन्नत्थणाभोगेणं, सहसागारेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore egasanam pachchakkhai, tiviham pi aharam... annatthana-bhogenam, sahasa-garenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I vow to consume food and water only once in the day while seated in one unchanging posture.",
            benefits: "Builds physical discipline, curtails culinary attachment, and purifies karma on Parva days."
        ),
        PachakkhanItem(
            id: "biyashna",
            type: .biyashna,
            titleGujarati: "બિયાસણું (Biyashna)",
            titleHindi: "बियासणा (Biyashna)",
            titleEnglish: "Biyashna",
            timingRule: "દિવસમાં બે વાર આસને બેસીને ભોજન કરવું",
            durationDescription: "Two meals seated in single posture, boiled water until sunset",
            sutraGujarati: "ઉગ્ગએ સૂરે બિયાસણં પચ્ચક્ખાઈ, તિવિહંપિ આહારં... અન્નત્થણાભોગેણં, સહસાગારેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे बियासणं पच्चक्खाइ, तिविहंपि आहारं... अन्नत्थणाभोगेणं, सहसागारेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore biyasanam pachchakkhai, tivihampi aharam... annatthana-bhogenam, sahasa-garenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I vow to consume food and water only twice in the day seated in a disciplined posture.",
            benefits: "Develops temperance and controls untimely eating habits."
        ),
        PachakkhanItem(
            id: "ayambil",
            type: .ayambil,
            titleGujarati: "આયંબિલ (Ayambil)",
            titleHindi: "आयंबिल (Ayambil)",
            titleEnglish: "Ayambil",
            timingRule: "૬ વિગય રહિત સાદું ભોજન",
            durationDescription: "Once a day, boiled grains/pulses without spice, oil, or dairy",
            sutraGujarati: "ઉગ્ગએ સૂરે આયંબિલં પચ્ચક્ખાઈ, અન્નત્થણાભોગેણં, સહસાગારેણં, લેવાલેવેણં, ગિહત્થસંસઠ્ઠેણં, ઉક્ખિત્તવિવેગેણં, પડુચ્ચમક્ખિએણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे आयंबिलं पच्चक्खाइ, अन्नत्थणाभोगेणं, सहसागारेणं, लेवालेवेणं, गिहत्थसंसट्ठेणं, उक्खित्तविवेगेणं, पडुच्चमक्खिएणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore ayambilam pachchakkhai, annatthana-bhogenam, sahasa-garenam, levalevenam, gihattha-samsatthenam, ukkhitta-vivegenam, paduchcha-makkhienam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I renounce all six vigais and eat boiled grains once without condiments.",
            benefits: "Subdues the palate (Rasendriya vijaya), promotes mental tranquility, and clears spiritual defilements."
        ),
        PachakkhanItem(
            id: "upvas",
            type: .upvas,
            titleGujarati: "ઉપવાસ (Upvas)",
            titleHindi: "उपवास (Upvas)",
            titleEnglish: "Upvas",
            timingRule: "સંપૂર્ણ અનાહાર (માત્ર ઉકાળેલું પાણી પૌરિસી પછીથી સૂર્યાસ્ત સુધી)",
            durationDescription: "Complete fast (Tivihar / Chauvihar) for 24 to 36 hours",
            sutraGujarati: "ઉગ્ગએ સૂરે ઉપવાસં પચ્ચક્ખાઈ, તિવિહંપિ આહારં અસણં ખાઇમં સાઇમં અન્નત્થણાભોગેણં, સહસાગારેણં, પારિટ્ઠાવણિયાગારેણં, મહત્તરાગારેણં, સવ્વસમાહિવત્તિયાગારેણં, વોસિરાઇ.",
            sutraHindi: "उग्गए सूरे उपवासं पच्चक्खाइ, तिविहंपि आहारं असणं खाइमं साइमं अन्नत्थणाभोगेणं, सहसागारेणं, पारिट्ठावणियागारेणं, महत्तरागारेणं, सव्वसमाहिवत्तियागारेणं, वोसिराइ।",
            sutraEnglishTransliteration: "Uggae soore upavasam pachchakkhai, tivihampi aharam asanam khaimam saimam annatthana-bhogenam, sahasa-garenam, paritthavaniyagarenam, mahattara-garenam, savva-samahi-vattiya-garenam, vosirai.",
            englishMeaning: "I renounce all solid food for the entire day.",
            benefits: "A profound penance that washes away accumulated karmic particles and elevates soul consciousness."
        )
    ]

    public static func getPachakkhan(type: PachakkhanType) -> PachakkhanItem? {
        return allPachakkhans.first(where: { $0.type == type })
    }
}

