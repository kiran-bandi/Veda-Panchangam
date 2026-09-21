package com.example.engine

data class RashiDetail(
    val id: String,
    val sanskritName: String,
    val teluguName: String,
    val westernName: String,
    val symbol: String,
    val ruler: String,
    val teluguRuler: String,
    val element: String,
    val teluguElement: String,
    val quality: String,
    val luckyGemstone: String,
    val teluguGemstone: String,
    val luckyNumber: String,
    val luckyColor: String,
    val teluguColor: String,
    val namingLetters: String,
    val teluguNamingLetters: String,
    val nakshatraList: String,
    val teluguNakshatras: String,
    val description: String,
    val teluguDescription: String,
    val dailyForecast: String,
    val teluguDailyForecast: String,
    val teluguQuality: String = "",
    val traditionalTraits: String = "",
    val challenges: String = "",
    val sourceNote: String = "BPHS (బృహత్పరాశర హోరాశాస్త్రం)"
)

data class NakshatraDetail(
    val number: Int,
    val name: String,
    val teluguName: String,
    val sanskritName: String,
    val symbol: String,
    val deity: String,
    val teluguDeity: String,
    val ruler: String,
    val teluguRuler: String,
    val rashi: String,
    val teluguRashi: String,
    val gana: String,
    val teluguGana: String,
    val yoni: String,
    val teluguYoni: String,
    val namingLetters: String,
    val teluguNamingLetters: String,
    val keyTraits: String,
    val teluguTraits: String,
    val degreeRange: String = "",
    val padasRange: String = "1–4",
    val classicalDescription: String = "",
    val sourceNote: String = "Bṛhat Jātaka / Bṛhat Saṃhitā"
)

data class PadaDetail(
    val nakshatraNumber: Int,
    val nakshatraName: String,
    val nakshatraTeluguName: String,
    val padaNumber: Int,
    val absoluteIndex: Int, // 1 to 108
    val startDegreeStr: String,
    val endDegreeStr: String,
    val rashiName: String,
    val rashiTeluguName: String,
    val navamsaRashiName: String,
    val navamsaRashiTeluguName: String,
    val namingSyllable: String,
    val traitsTe: String,
    val personalityTe: String,
    val purusharthaTe: String
)

object ZodiacRepository {

    val RASHIS = listOf(
        RashiDetail(
            id = "mesha",
            sanskritName = "मेष (Mesha)",
            teluguName = "మేష రాశి",
            westernName = "Aries",
            symbol = "♈",
            ruler = "Mars (Kujudu)",
            teluguRuler = "కుజుడు (అంగారకుడు)",
            element = "Fire (Agni)",
            teluguElement = "అగ్ని తత్త్వం",
            quality = "Chara (Movable)",
            luckyGemstone = "Red Coral (Pagam)",
            teluguGemstone = "కెంపు / పగడం",
            luckyNumber = "9, 1, 8",
            luckyColor = "Red / Pink",
            teluguColor = "ఎరుపు, గులాబీ రంగు",
            namingLetters = "A, La, I (అ, ల, ఇ)",
            teluguNamingLetters = "అ, ల, ఇ, చూ, చే, చో, లా, లీ, లూ, లే, లో",
            nakshatraList = "Ashwini (4), Bharani (4), Krittika (1)",
            teluguNakshatras = "అశ్విని (4 పాదాలు), భరణి (4 పాదాలు), కృత్తిక (1వ పాదం)",
            description = "Pioneering, dynamic, courageous, independent, and natural leaders.",
            teluguDescription = "ధైర్యసాహసాలు, నాయకత్వ లక్షణాలు, స్వతంత్ర ఆలోచన, ఉత్సాహం మరియు కార్యదీక్ష కలిగినవారు. ఏ రంగంలోనైనా ముందుంటారు.",
            dailyForecast = "Mars infuses vigor into your work today. Focus energy constructively on pending tasks for success.",
            teluguDailyForecast = "ఈ రోజు పనులలో విజయం లభిస్తుంది. ధైర్యంతో అడుగు ముందుకు వేయండి. ఆర్థిక వ్యవహారాలు అనుకూలిస్తాయి."
        ),
        RashiDetail(
            id = "vrishabha",
            sanskritName = "वृषभ (Vrishabha)",
            teluguName = "వృషభ రాశి",
            westernName = "Taurus",
            symbol = "♉",
            ruler = "Venus (Shukra)",
            teluguRuler = "శుక్రుడు",
            element = "Earth (Prithvi)",
            teluguElement = "భూ తత్త్వం",
            quality = "Sthira (Fixed)",
            luckyGemstone = "Diamond / White Sapphire",
            teluguGemstone = "వజ్రం / శ్వేత పుష్యరాగం",
            luckyNumber = "6, 5, 8",
            luckyColor = "White / Cream",
            teluguColor = "తెలుపు, క్రీమ్ వర్ణం",
            namingLetters = "I, U, E, O, Wa, Vi",
            teluguNamingLetters = "ఇ, ఉ, ఏ, ఓ, వ, వి, వు, వే, వో",
            nakshatraList = "Krittika (2,3,4), Rohini (4), Mrigashira (1,2)",
            teluguNakshatras = "కృత్తిక (2,3,4 పాదాలు), రోహిణి (4 పాదాలు), మృగశిర (1,2 పాదాలు)",
            description = "Steadfast, patient, dependable, lover of comfort, art and financial security.",
            teluguDescription = "స్థిరమైన స్వభావం, ఓర్పు, కళాత్మక దృష్టి, కుటుంబ సౌఖ్యం మరియు ఆర్థిక క్రమశిక్షణ కలిగినవారు.",
            dailyForecast = "Venus brings harmony in family and artistic endeavors. Financial progress is indicated.",
            teluguDailyForecast = "కుటుంబంలో ఆనందోత్సాహాలు నిండుతాయి. కళారంగం వారికి మంచి పురోగతి. ధన లాభ సూచనలు ఉన్నాయి."
        ),
        RashiDetail(
            id = "mithuna",
            sanskritName = "मिथुन (Mithuna)",
            teluguName = "మిథున రాశి",
            westernName = "Gemini",
            symbol = "♊",
            ruler = "Mercury (Budha)",
            teluguRuler = "బుధుడు",
            element = "Air (Vayu)",
            teluguElement = "వాయు తత్త్వం",
            quality = "Dvisvabhava (Dual)",
            luckyGemstone = "Emerald (Panna)",
            teluguGemstone = "మరకతం (పచ్చ)",
            luckyNumber = "5, 6, 3",
            luckyColor = "Green / Yellow",
            teluguColor = "ఆకుపచ్చ, పసుపు",
            namingLetters = "Ka, Ki, Ku, Gha, Chha, Ke, Ko",
            teluguNamingLetters = "కా, కీ, కూ, ఘ, ఙ, ఛ, కే, కో, హా",
            nakshatraList = "Mrigashira (3,4), Ardra (4), Punarvasu (1,2,3)",
            teluguNakshatras = "మృగశిర (3,4 పాదాలు), ఆరుద్ర (4 పాదాలు), పునర్వసు (1,2,3 పాదాలు)",
            description = "Intellectual, witty, adaptable, communicative, versatile and analytical.",
            teluguDescription = "చతురత, బుద్ధికుశలత, సమయస్ఫూర్తి, మంచి సంభాషణా చాతుర్యం మరియు విషయగ్రహణ శక్తి కలిగినవారు.",
            dailyForecast = "Mercury enhances mental sharpness. Communication and trading activities yield profitable results.",
            teluguDailyForecast = "మానసిక ప్రశాంతత, తెలివితో కూడిన నిర్ణయాలు ప్రయోజనాన్ని ఇస్తాయి. వ్యాపార చర్చలు సఫలమవుతాయి."
        ),
        RashiDetail(
            id = "karkataka",
            sanskritName = "कर्क (Karkataka)",
            teluguName = "కర్కాటక రాశి",
            westernName = "Cancer",
            symbol = "♋",
            ruler = "Moon (Chandra)",
            teluguRuler = "చంద్రుడు",
            element = "Water (Jala)",
            teluguElement = "జల తత్త్వం",
            quality = "Chara (Movable)",
            luckyGemstone = "Pearl (Moti) / Moonstone",
            teluguGemstone = "ముత్యం",
            luckyNumber = "2, 7, 9",
            luckyColor = "Silver / Milky White",
            teluguColor = "వెండి రంగు, తెలుపు",
            namingLetters = "Hi, Hu, He, Ho, Da, Dee",
            teluguNamingLetters = "హి, హు, హే, హో, డా, డీ, డూ, డే, డో",
            nakshatraList = "Punarvasu (4), Pushya (4), Ashlesha (4)",
            teluguNakshatras = "పునర్వసు (4వ పాదం), పుష్యమి (4 పాదాలు), ఆశ్లేష (4 పాదాలు)",
            description = "Intuitive, nurturing, empathetic, deeply devoted to family, tradition and home.",
            teluguDescription = "భావోద్వేగ సంపన్నత, దయాగుణం, మాతృభక్తి, కుటుంబ అనురాగం మరియు గృహప్రేమ కలిగినవారు.",
            dailyForecast = "Lunar influence heightens sensitivity and peaceful family atmosphere. Spiritual activities bring joy.",
            teluguDailyForecast = "ఆధ్యాత్మిక ఆలోచనలు పెరుగుతాయి. బంధుమిత్రుల కలయిక ఆనందాన్ని ఇస్తుంది. ప్రయాణాలు అనుకూలిస్తాయి."
        ),
        RashiDetail(
            id = "simha",
            sanskritName = "सिंह (Simha)",
            teluguName = "సింహ రాశి",
            westernName = "Leo",
            symbol = "♌",
            ruler = "Sun (Surya)",
            teluguRuler = "సూర్యుడు",
            element = "Fire (Agni)",
            teluguElement = "అగ్ని తత్త్వం",
            quality = "Sthira (Fixed)",
            luckyGemstone = "Ruby (Manikya)",
            teluguGemstone = "మాణిక్యం (కెంపు)",
            luckyNumber = "1, 9, 5",
            luckyColor = "Gold / Orange / Red",
            teluguColor = "బంగారు రంగు, కాషాయం",
            namingLetters = "Ma, Mi, Mu, Me, Mo, Ta, Tee",
            teluguNamingLetters = "మా, మీ, మూ, మే, మో, టా, టీ, టూ, టే",
            nakshatraList = "Magha (4), Purva Phalguni (4), Uttara Phalguni (1)",
            teluguNakshatras = "మఖ (4 పాదాలు), పుబ్బ (4 పాదాలు), ఉత్తర (1వ పాదం)",
            description = "Regal, generous, commanding, creative, charismatic and noble leader.",
            teluguDescription = "రాజసము, ఉదారస్వభావం, ఆత్మగౌరవం, నాయకత్వ పటిమ మరియు ఆకర్షణీయమైన వ్యక్తిత్వం కలిగినవారు.",
            dailyForecast = "Solar energy brings recognition at work and authority in decisions. Display humble confidence.",
            teluguDailyForecast = "ఉద్యోగ వ్యాపారాలలో అధికార పరిధి పెరుగుతుంది. ప్రముఖుల సహాయ సహకారాలు లభిస్తాయి."
        ),
        RashiDetail(
            id = "kanya",
            sanskritName = "कन्या (Kanya)",
            teluguName = "కన్యా రాశి",
            westernName = "Virgo",
            symbol = "♍",
            ruler = "Mercury (Budha)",
            teluguRuler = "బుధుడు",
            element = "Earth (Prithvi)",
            teluguElement = "భూ తత్త్వం",
            quality = "Dvisvabhava (Dual)",
            luckyGemstone = "Emerald (Panna)",
            teluguGemstone = "మరకతం (పచ్చ)",
            luckyNumber = "5, 2, 7",
            luckyColor = "Dark Green / Grey",
            teluguColor = "గాఢ ఆకుపచ్చ",
            namingLetters = "To, Pa, Pee, Poo, Sha, Na, Tha",
            teluguNamingLetters = "తో, పా, పీ, పూ, ష, ణ, ఠ, పే, పో",
            nakshatraList = "Uttara Phalguni (2,3,4), Hasta (4), Chitra (1,2)",
            teluguNakshatras = "ఉత్తర (2,3,4 పాదాలు), హస్త (4 పాదాలు), చిత్త (1,2 పాదాలు)",
            description = "Analytical, meticulous, service-oriented, organized and discerning.",
            teluguDescription = "సమర్థవంతమైన నిర్వహణ, విశ్లేషణాత్మక బుద్ధి, సేవాభావం మరియు పరిశుభ్రతపై ప్రత్యేక శ్రద్ధ కలిగినవారు.",
            dailyForecast = "Auspicious day for organizing complex tasks and financial planning. Precision brings reward.",
            teluguDailyForecast = "పనులలో ఖచ్చితత్వం విజయాన్ని ఇస్తుంది. ఆర్థిక లావాదేవీలు క్రమబద్ధంగా సాగుతాయి."
        ),
        RashiDetail(
            id = "tula",
            sanskritName = "तुला (Tula)",
            teluguName = "తులా రాశి",
            westernName = "Libra",
            symbol = "♎",
            ruler = "Venus (Shukra)",
            teluguRuler = "శుక్రుడు",
            element = "Air (Vayu)",
            teluguElement = "వాయు తత్త్వం",
            quality = "Chara (Movable)",
            luckyGemstone = "Diamond / Opal",
            teluguGemstone = "వజ్రం / ఓపల్",
            luckyNumber = "6, 8, 5",
            luckyColor = "Blue / White",
            teluguColor = "నీలం, తెలుపు",
            namingLetters = "Ra, Ree, Roo, Re, Ro, Ta, Tee",
            teluguNamingLetters = "రా, రీ, రూ, రే, రో, తా, తీ, తూ, తే",
            nakshatraList = "Chitra (3,4), Swati (4), Vishakha (1,2,3)",
            teluguNakshatras = "చిత్త (3,4 పాదాలు), స్వాతి (4 పాదాలు), విశాఖ (1,2,3 పాదాలు)",
            description = "Diplomatic, fair, balanced, lover of harmony, partnership and fine arts.",
            teluguDescription = "న్యాయబద్ధత, సమతుల్యత, మృదుస్వభావం, భాగస్వామ్య వ్యాపారాలలో రాణింపు మరియు కళాభిరుచి కలిగినవారు.",
            dailyForecast = "Venus promotes compromise and successful partnerships. Creative and artistic projects flourish.",
            teluguDailyForecast = "మిత్రుల కలయిక సంతోషాన్ని ఇస్తుంది. భాగస్వామ్య వ్యాపారాలు అనుకూలిస్తాయి. సుఖసంతోషాలు కలుగుతాయి."
        ),
        RashiDetail(
            id = "vrischika",
            sanskritName = "वृश्चिक (Vrischika)",
            teluguName = "వృశ్చిక రాశి",
            westernName = "Scorpio",
            symbol = "♏",
            ruler = "Mars (Kujudu)",
            teluguRuler = "కుజుడు",
            element = "Water (Jala)",
            teluguElement = "జల తత్త్వం",
            quality = "Sthira (Fixed)",
            luckyGemstone = "Red Coral (Pagam)",
            teluguGemstone = "పగడం",
            luckyNumber = "9, 1, 4",
            luckyColor = "Deep Red / Maroon",
            teluguColor = "రక్త వర్ణం, ముదురు ఎరుపు",
            namingLetters = "To, Na, Nee, Noo, Ne, No, Ya",
            teluguNamingLetters = "తో, నా, నీ, నూ, నే, నో, యా, యీ, యూ",
            nakshatraList = "Vishakha (4), Anuradha (4), Jyeshtha (4)",
            teluguNakshatras = "విశాఖ (4వ పాదం), అనూరాధ (4 పాదాలు), జ్యేష్ఠ (4 పాదాలు)",
            description = "Intense, resolute, mystical, transformative, deeply loyal and determined.",
            teluguDescription = "పట్టుదల, లోతైన ఆలోచన, నిగూఢ శక్తి, ఆధ్యాత్మిక ఆసక్తి మరియు అనుకున్నది సాధించే గుణం కలిగినవారు.",
            dailyForecast = "Strong inner focus solves complex issues today. Research and spiritual contemplation bring breakthroughs.",
            teluguDailyForecast = "కష్టసాధ్యమైన పనులు కూడా పట్టుదలతో పూర్తవుతాయి. ఆధ్యాత్మిక అనుభూతి లభిస్తుంది."
        ),
        RashiDetail(
            id = "dhanu",
            sanskritName = "धनु (Dhanu)",
            teluguName = "ధనూ రాశి",
            westernName = "Sagittarius",
            symbol = "♐",
            ruler = "Jupiter (Guru)",
            teluguRuler = "గురుడు (బృహస్పతి)",
            element = "Fire (Agni)",
            teluguElement = "అగ్ని తత్త్వం",
            quality = "Dvisvabhava (Dual)",
            luckyGemstone = "Yellow Sapphire (Pukhraj)",
            teluguGemstone = "కనక పుష్యరాగం",
            luckyNumber = "3, 9, 7",
            luckyColor = "Yellow / Saffron",
            teluguColor = "పసుపు, పీతాంబరం",
            namingLetters = "Ye, Yo, Bha, Bhee, Bhoo, Dha, Pha",
            teluguNamingLetters = "యే, యో, భా, భీ, భూ, ధా, ఫా, డా, భే",
            nakshatraList = "Mula (4), Purva Ashadha (4), Uttara Ashadha (1)",
            teluguNakshatras = "మూల (4 పాదాలు), పూర్వాషాఢ (4 పాదాలు), ఉత్తరాషాఢ (1వ పాదం)",
            description = "Philosophical, optimistic, truth-seeking, wise, generous and expansive.",
            teluguDescription = "ధర్మచింతన, గురుభక్తి, సత్యపరిపాలన, విద్యార్జనపై ఆసక్తి మరియు ఆశావాద దృక్పథం కలిగినవారు.",
            dailyForecast = "Jupiter expands wisdom and divine blessings. Seek advice from elders and gurus for guidance.",
            teluguDailyForecast = "పెద్దల దీవెనలు లభిస్తాయి. దైవ దర్శనం, శుభ కార్యాలు అనుకూలిస్తాయి. విద్యా రంగంలో ప్రగతి."
        ),
        RashiDetail(
            id = "makara",
            sanskritName = "मकर (Makara)",
            teluguName = "మకర రాశి",
            westernName = "Capricorn",
            symbol = "♑",
            ruler = "Saturn (Shani)",
            teluguRuler = "శనిభగవానుడు",
            element = "Earth (Prithvi)",
            teluguElement = "భూ తత్త్వం",
            quality = "Chara (Movable)",
            luckyGemstone = "Blue Sapphire (Neelam)",
            teluguGemstone = "ఇంద్రనీలం",
            luckyNumber = "8, 4, 6",
            luckyColor = "Black / Dark Blue",
            teluguColor = "నలుపు, గాఢ నీలం",
            namingLetters = "Bho, Ja, Jee, Khee, Khoo, Khe",
            teluguNamingLetters = "భో, జా, జీ, ఖీ, ఖూ, ఖే, ఖో, గా, గీ",
            nakshatraList = "Uttara Ashadha (2,3,4), Shravana (4), Dhanishta (1,2)",
            teluguNakshatras = "ఉత్తరాషాఢ (2,3,4 పాదాలు), శ్రవణం (4 పాదాలు), ధనిష్ఠ (1,2 పాదాలు)",
            description = "Disciplined, structured, pragmatic, resilient, persevering and ambitious.",
            teluguDescription = "నియమబద్ధత, కార్యదీక్ష, ఓర్పు, క్రమశిక్షణ మరియు బాధ్యతాయుతమైన ప్రవర్తన కలిగినవారు.",
            dailyForecast = "Saturn rewards methodical patience and hard work. Long-term foundation projects move steadily.",
            teluguDailyForecast = "శ్రమకు తగిన ఫలితం లభిస్తుంది. ఉద్యోగంలో స్థిరత్వం పెరుగుతుంది. పెద్దల సలహాలు మేలు చేస్తాయి."
        ),
        RashiDetail(
            id = "kumbha",
            sanskritName = "कुम्भ (Kumbha)",
            teluguName = "కుంభ రాశి",
            westernName = "Aquarius",
            symbol = "♒",
            ruler = "Saturn (Shani)",
            teluguRuler = "శనిభగవానుడు",
            element = "Air (Vayu)",
            teluguElement = "వాయు తత్త్వం",
            quality = "Sthira (Fixed)",
            luckyGemstone = "Blue Sapphire / Amethyst",
            teluguGemstone = "నీలం / అమితెస్ట్",
            luckyNumber = "8, 4, 9",
            luckyColor = "Light Blue / Violet",
            teluguColor = "ఆకాశ నీలం, ఊదా వర్ణం",
            namingLetters = "Goo, Ge, Go, Sa, See, Soo, Se, So, Da",
            teluguNamingLetters = "గూ, గే, గో, సా, సీ, సూ, సే, సో, దా",
            nakshatraList = "Dhanishta (3,4), Shatabhisha (4), Purva Bhadrapada (1,2,3)",
            teluguNakshatras = "ధనిష్ఠ (3,4 పాదాలు), శతభిషం (4 పాదాలు), పూర్వాభాద్ర (1,2,3 పాదాలు)",
            description = "Humanitarian, visionary, inventive, progressive and social-minded.",
            teluguDescription = "సమాజసేవా దృక్పథం, నూతన ఆలోచనలు, నిష్పాక్షికత మరియు స్నేహపూర్వక స్వభావం కలిగినవారు.",
            dailyForecast = "Innovative ideas gain momentum today. Community work and networking achieve remarkable progress.",
            teluguDailyForecast = "కొత్త పథకాలు, ఆలోచనలు రూపుదిద్దుకుంటాయి. సమాజంలో గుర్తింపు లభిస్తుంది. మిత్రుల సహకారం."
        ),
        RashiDetail(
            id = "meena",
            sanskritName = "मीन (Meena)",
            teluguName = "మీన రాశి",
            westernName = "Pisces",
            symbol = "♓",
            ruler = "Jupiter (Guru)",
            teluguRuler = "గురుడు (బృహస్పతి)",
            element = "Water (Jala)",
            teluguElement = "జల తత్త్వం",
            quality = "Dvisvabhava (Dual)",
            luckyGemstone = "Yellow Sapphire (Pukhraj)",
            teluguGemstone = "కనక పుష్యరాగం",
            luckyNumber = "3, 7, 1",
            luckyColor = "Yellow / Sea Green",
            teluguColor = "పసుపు, సముద్రపు ఆకుపచ్చ",
            namingLetters = "Dee, Doo, Tha, Jha, Tra, De, Do, Cha",
            teluguNamingLetters = "దీ, దూ, థ, ఝ, ఞ, దే, దో, చా, చీ",
            nakshatraList = "Purva Bhadrapada (4), Uttara Bhadrapada (4), Revati (4)",
            teluguNakshatras = "పూర్వాభాద్ర (4వ పాదం), ఉత్తరాభాద్ర (4 పాదాలు), రేవతి (4 పాదాలు)",
            description = "Compassionate, spiritual, imaginative, artistic and spiritually evolved.",
            teluguDescription = "కరుణ, దయ, లోతైన ఆధ్యాత్మికత, ఊహాశక్తి మరియు పరోపకార గుణం కలిగినవారు.",
            dailyForecast = "Spiritual intuition is heightened today. Quiet prayer, meditation and creative activities bring peace.",
            teluguDailyForecast = "ఆధ్యాత్మిక చింతన సంతోషాన్ని ఇస్తుంది. పుణ్యక్షేత్ర దర్శన యోగం. మనశ్శాంతి లభిస్తుంది."
        )
    )

    val NAKSHATRAS = listOf(
        NakshatraDetail(
            number = 1, name = "Ashwini", teluguName = "1. అశ్విని", sanskritName = "अश्विनी", symbol = "Horse's Head (గుర్రపు స్వభావం)",
            deity = "Ashvini Kumaras", teluguDeity = "అశ్విని దేవతలు (దివ్య వైద్యులు)",
            ruler = "Ketu", teluguRuler = "కేతువు",
            rashi = "Mesha (Aries)", teluguRashi = "మేష రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Horse (అశ్వం)", teluguYoni = "గుర్రం (అశ్వం)",
            namingLetters = "Chu, Che, Cho, La (చూ, చే, చో, లా)", teluguNamingLetters = "చూ, చే, చో, లా",
            keyTraits = "Swift action, healing ability, enthusiasm, pioneering energy.",
            teluguTraits = "కార్యక్రమాలలో వేగం, ఆరోగ్య ప్రదాతలు, ఉత్సాహం, నూతన విషయాలు నేర్చుకోవడంలో శ్రద్ధ."
        ),
        NakshatraDetail(
            number = 2, name = "Bharani", teluguName = "2. భరణి", sanskritName = "भरणी", symbol = "Yoni / Vessel (పాకం / యోని)",
            deity = "Yama Dharmaraja", teluguDeity = "యమధర్మరాజు",
            ruler = "Venus", teluguRuler = "శుక్రుడు",
            rashi = "Mesha (Aries)", teluguRashi = "మేష రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Elephant (గజం)", teluguYoni = "ఏనుగు (గజం)",
            namingLetters = "Lee, Lu, Le, Lo (లీ, లూ, లే, లో)", teluguNamingLetters = "లీ, లూ, లే, లో",
            keyTraits = "Moral discipline, determination, truthfulness, endurance.",
            teluguTraits = "సత్యపాలన, క్రమశిక్షణ, సహనశీలత, ఆత్మనిగ్రహం మరియు ధర్మబుద్ధి."
        ),
        NakshatraDetail(
            number = 3, name = "Krittika", teluguName = "3. కృత్తిక", sanskritName = "कृत्तिका", symbol = "Flame / Knife (అగ్నిజ్వాల / కత్తి)",
            deity = "Agni Deva", teluguDeity = "అగ్నిదేవుడు",
            ruler = "Sun", teluguRuler = "సూర్యుడు",
            rashi = "Mesha & Vrishabha", teluguRashi = "మేషం (1) & వృషభం (2,3,4)",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Sheep (మేషం)", teluguYoni = "గొర్రె (మేషం)",
            namingLetters = "A, Ee, U, Ae (అ, ఈ, ఉ, ఏ)", teluguNamingLetters = "అ, ఈ, ఉ, ఏ",
            keyTraits = "Radiance, sharp intelligence, purification, leadership.",
            teluguTraits = "తేజస్సు, సూక్ష్మ బుద్ధి, పరిశుద్ధత, నాయకత్వ లక్షణాలు మరియు అగ్ని వంటి తీక్షణత."
        ),
        NakshatraDetail(
            number = 4, name = "Rohini", teluguName = "4. రోహిణి", sanskritName = "रोहिणी", symbol = "Chariot / Cart (రథం)",
            deity = "Brahma / Prajapati", teluguDeity = "బ్రహ్మదేవుడు / ప్రజాపతి",
            ruler = "Moon", teluguRuler = "చంద్రుడు",
            rashi = "Vrishabha (Taurus)", teluguRashi = "వృషభ రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Serpent (సర్పం)", teluguYoni = "పాము (సర్పం)",
            namingLetters = "O, Va, Vi, Vu (ఓ, వ, వి, వు)", teluguNamingLetters = "ఓ, వ, వి, వు",
            keyTraits = "Immense beauty, prosperity, artistic passion, growth.",
            teluguTraits = "సౌందర్యారాధన, సృజనాత్మకత, కళాపోషణ, లక్ష్మీ కటాక్షం మరియు ప్రశాంతత."
        ),
        NakshatraDetail(
            number = 5, name = "Mrigashirsha", teluguName = "5. మృగశిర", sanskritName = "मृगशीर्षा", symbol = "Deer's Head (జింక తల)",
            deity = "Soma (Moon Deva)", teluguDeity = "సోముడు (చంద్రదేవుడు)",
            ruler = "Mars", teluguRuler = "కుజుడు",
            rashi = "Vrishabha & Mithuna", teluguRashi = "వృషభం (1,2) & మిథునం (3,4)",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Serpent (సర్పం)", teluguYoni = "పాము (సర్పం)",
            namingLetters = "Ve, Vo, Ka, Kee (వే, వో, కా, కీ)", teluguNamingLetters = "వే, వో, కా, కీ",
            keyTraits = "Curiosity, search for truth, gentle nature, research focus.",
            teluguTraits = "జిజ్ఞాస, నిత్య అన్వేషణ, మృదుస్వభావం, పరిశోధనాత్మక బుద్ధి."
        ),
        NakshatraDetail(
            number = 6, name = "Ardra", teluguName = "6. ఆర్ద్ర (ఆరుద్ర)", sanskritName = "आर्द्रा", symbol = "Teardrop / Diamond (కంటిపాప / రత్నం)",
            deity = "Rudra (Shiva)", teluguDeity = "రుద్రుడు (పరమశివుడు)",
            ruler = "Rahu", teluguRuler = "రాహువు",
            rashi = "Mithuna (Gemini)", teluguRashi = "మిథున రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Dog (శునకం)", teluguYoni = "కుక్క (శునకం)",
            namingLetters = "Koo, Gha, Nga, Chha (కూ, ఘ, ఙ, ఛ)", teluguNamingLetters = "కూ, ఘ, ఙ, ఛ",
            keyTraits = "Intensity, breaking obstacles, deep transformation, clarity.",
            teluguTraits = "కష్టాలను అధిగమించే శక్తి, పట్టుదల, లోతైన ఆలోచన, భావోద్వేగాలు."
        ),
        NakshatraDetail(
            number = 7, name = "Punarvasu", teluguName = "7. పునర్వసు", sanskritName = "पुनर्वसु", symbol = "Bow & Quiver (విల్లు & బాణాలు)",
            deity = "Aditi (Mother of Gods)", teluguDeity = "అదితి (దేవమాత)",
            ruler = "Jupiter", teluguRuler = "గురుడు",
            rashi = "Mithuna & Karkataka", teluguRashi = "మిథునం (1,2,3) & కర్కాటకం (4)",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Cat (మార్జాలం)", teluguYoni = "పిల్లి (మార్జాలం)",
            namingLetters = "Ke, Ko, Ha, Hee (కే, కో, హా, హీ)", teluguNamingLetters = "కే, కో, హా, హీ",
            keyTraits = "Renewal, return of prosperity, spiritual resilience, virtue.",
            teluguTraits = "పునర్వైభవం, సానుకూల దృక్పథం, ఆధ్యాత్మిక బలం, ధర్మ ప్రవర్తన."
        ),
        NakshatraDetail(
            number = 8, name = "Pushya", teluguName = "8. పుష్యమి", sanskritName = "पुष्य", symbol = "Cow's Udder / Lotus (ఆవు పొదుగు / పద్మం)",
            deity = "Brihaspati (Guru Deva)", teluguDeity = "బృహస్పతి (దేవగురువు)",
            ruler = "Saturn", teluguRuler = "శని",
            rashi = "Karkataka (Cancer)", teluguRashi = "కర్కాటక రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Goat (అజం)", teluguYoni = "మేక (అజం)",
            namingLetters = "Hoo, He, Ho, Da (హూ, హే, హో, డా)", teluguNamingLetters = "హూ, హే, హో, డా",
            keyTraits = "Supreme nourishment, most auspicious for rites, devotion.",
            teluguTraits = "సకల శుభకార్యాలకు మహోన్నతమైనది, పోషణ, గురుభక్తి, సంపదవృద్ధి."
        ),
        NakshatraDetail(
            number = 9, name = "Ashlesha", teluguName = "9. ఆశ్లేష", sanskritName = "आश्लेषा", symbol = "Coiled Serpent (చుట్టుకున్న సర్పం)",
            deity = "Nagaraja (Serpent Gods)", teluguDeity = "నాగదేవతలు (సర్పరాజులు)",
            ruler = "Mercury", teluguRuler = "బుధుడు",
            rashi = "Karkataka (Cancer)", teluguRashi = "కర్కాటక రాశి",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Cat (మార్జాలం)", teluguYoni = "పిల్లి (మార్జాలం)",
            namingLetters = "Dee, Doo, De, Do (డీ, డూ, డే, డో)", teluguNamingLetters = "డీ, డూ, డే, డో",
            keyTraits = "Mystical power, intense concentration, Kundalini energy.",
            teluguTraits = "గూఢ విద్యలు, రహస్య పరిశోధన, ఏకాగ్రత, నాడీ శాస్త్ర పరిజ్ఞానం."
        ),
        NakshatraDetail(
            number = 10, name = "Magha", teluguName = "10. మఖ", sanskritName = "मघा", symbol = "Palanquin / Throne (పల్లకి / సింహాసనం)",
            deity = "Pitru Devatas (Ancestors)", teluguDeity = "పితృదేవతలు",
            ruler = "Ketu", teluguRuler = "కేతువు",
            rashi = "Simha (Leo)", teluguRashi = "సింహ రాశి",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Rat (మూషికం)", teluguYoni = "ఎలుక (మూషికం)",
            namingLetters = "Ma, Mee, Moo, Me (మా, మీ, మూ, మే)", teluguNamingLetters = "మా, మీ, మూ, మే",
            keyTraits = "Ancestral dignity, royal heritage, nobility, leadership.",
            teluguTraits = "వంశ గౌరవం, పితృభక్తి, రాజపూజ్యం, ఆధిపత్యం మరియు ధార్మికత."
        ),
        NakshatraDetail(
            number = 11, name = "Purva Phalguni (Pubba)", teluguName = "11. పుబ్బ (పూర్వఫాల్గుణి)", sanskritName = "पूर्वाफाल्गुनी", symbol = "Front legs of Bed (మంచం ముందు భాగాలు)",
            deity = "Bhaga Deva", teluguDeity = "భగదేవుడు (సంపద ప్రదాత)",
            ruler = "Venus", teluguRuler = "శుక్రుడు",
            rashi = "Simha (Leo)", teluguRashi = "సింహ రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Rat (మూషికం)", teluguYoni = "ఎలుక (మూషికం)",
            namingLetters = "Mo, Ta, Tee, Too (మో, టా, టీ, టూ)", teluguNamingLetters = "మో, టా, టీ, టూ",
            keyTraits = "Marital happiness, love, relaxation, music and arts.",
            teluguTraits = "దాంపత్య సుఖం, సంగీత సాహిత్యాభిరుచి, వినోదం, ఆనందమయ జీవితం."
        ),
        NakshatraDetail(
            number = 12, name = "Uttara Phalguni (Uttara)", teluguName = "12. ఉత్తర (ఉత్తరఫాల్గుణి)", sanskritName = "उत्तराफाल्गुनी", symbol = "Back legs of Bed (మంచం వెనుక భాగాలు)",
            deity = "Aryaman Deva", teluguDeity = "అర్యముడు (స్నేహదేవుడు)",
            ruler = "Sun", teluguRuler = "సూర్యుడు",
            rashi = "Simha & Kanya", teluguRashi = "సింహం (1) & కన్య (2,3,4)",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Cow (గోవు)", teluguYoni = "ఆవు (గోవు)",
            namingLetters = "Te, To, Pa, Pee (టే, తో, పా, పీ)", teluguNamingLetters = "టే, తో, పా, పీ",
            keyTraits = "Charity, duty, steadfast friendship, honor.",
            teluguTraits = "దానగుణం, మైత్రి, ధర్మనిష్ఠ, విశ్వసనీయత మరియు సమాజ గౌరవం."
        ),
        NakshatraDetail(
            number = 13, name = "Hasta", teluguName = "13. హస్త", sanskritName = "हस्त", symbol = "Open Palm (చేతి అరచేయి)",
            deity = "Savitar (Sun God)", teluguDeity = "సవితా (సూర్య భగవానుడు)",
            ruler = "Moon", teluguRuler = "చంద్రుడు",
            rashi = "Kanya (Virgo)", teluguRashi = "కన్యా రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Buffalo (మహిషం)", teluguYoni = "గేదె (మహిషం)",
            namingLetters = "Poo, Sha, Na, Tha (పూ, ష, ణ, ఠ)", teluguNamingLetters = "పూ, ష, ణ, ఠ",
            keyTraits = "Dexterity, craftsmanship, healing touch, wisdom.",
            teluguTraits = "హస్తకళలు, చేతివృత్తులలో నైపుణ్యం, ఆరోగ్య ప్రదానం, సూక్ష్మ దృష్టి."
        ),
        NakshatraDetail(
            number = 14, name = "Chitra", teluguName = "14. చిత్త", sanskritName = "चित्रा", symbol = "Bright Gem / Pearl (మణి / ముత్యం)",
            deity = "Vishwakarma (Celestial Architect)", teluguDeity = "విశ్వకర్మ (దివ్య శిల్పి)",
            ruler = "Mars", teluguRuler = "కుజుడు",
            rashi = "Kanya & Tula", teluguRashi = "కన్య (1,2) & తులా (3,4)",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Tiger (వ్యాఘ్రం)", teluguYoni = "పులి (వ్యాఘ్రం)",
            namingLetters = "Pe, Po, Ra, Ree (పే, పో, రా, రీ)", teluguNamingLetters = "పే, పో, రా, రీ",
            keyTraits = "Architectural brilliance, vivid design, creative genius.",
            teluguTraits = "భవన నిర్మాణం, రూపకల్పన, డిజైనింగ్, అలంకరణ మరియు శిల్పకళ."
        ),
        NakshatraDetail(
            number = 15, name = "Swati", teluguName = "15. స్వాతి", sanskritName = "स्वाति", symbol = "Young Shoot / Coral (లేతమొక్క / పగడం)",
            deity = "Vayu Deva (Wind God)", teluguDeity = "వాయుదేవుడు",
            ruler = "Rahu", teluguRuler = "రాహువు",
            rashi = "Tula (Libra)", teluguRashi = "తులా రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Buffalo (మహిషం)", teluguYoni = "గేదె (మహిషం)",
            namingLetters = "Roo, Re, Ro, Ta (రూ, రే, రో, తా)", teluguNamingLetters = "రూ, రే, రో, తా",
            keyTraits = "Flexibility, trade acumen, polite speech, independence.",
            teluguTraits = "స్వతంత్ర భావాలు, వాణిజ్య నైపుణ్యం, మృదువైన మాటలు, అనుకూలత."
        ),
        NakshatraDetail(
            number = 16, name = "Vishakha", teluguName = "16. విశాఖ", sanskritName = "विशाखा", symbol = "Triumphal Archway (తోరణం)",
            deity = "Indra & Agni", teluguDeity = "ఇంద్రుడు & అగ్నిదేవుడు",
            ruler = "Jupiter", teluguRuler = "గురుడు",
            rashi = "Tula & Vrischika", teluguRashi = "తులా (1,2,3) & వృశ్చికం (4)",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Tiger (వ్యాఘ్రం)", teluguYoni = "పులి (వ్యాఘ్రం)",
            namingLetters = "Tee, Too, Te, To (తీ, తూ, తే, తో)", teluguNamingLetters = "తీ, తూ, తే, తో",
            keyTraits = "Target-oriented ambition, triumph, single-minded focus.",
            teluguTraits = "విజయ సాధన, లక్ష్య నిష్ఠ, పట్టుదల, అనుకున్నది సాధించడంలో దిట్ట."
        ),
        NakshatraDetail(
            number = 17, name = "Anuradha", teluguName = "17. అనూరాధ", sanskritName = "अनुराधा", symbol = "Lotus / Row of Stars (పద్మం)",
            deity = "Mitra Deva (God of Friendship)", teluguDeity = "మిత్రదేవుడు (స్నేహ దైవం)",
            ruler = "Saturn", teluguRuler = "శని",
            rashi = "Vrischika (Scorpio)", teluguRashi = "వృశ్చిక రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Deer (హరిణం)", teluguYoni = "జింక (హరిణం)",
            namingLetters = "Na, Nee, Noo, Ne (నా, నీ, నూ, నే)", teluguNamingLetters = "నా, నీ, నూ, నే",
            keyTraits = "Devotion, unconditional friendship, organizational skill.",
            teluguTraits = "నిర్మలమైన స్నేహం, భక్తి, సంఘటిత శక్తి, సుముహూర్తాలకు అత్యంత అనుకూలం."
        ),
        NakshatraDetail(
            number = 18, name = "Jyeshtha", teluguName = "18. జ్యేష్ఠ", sanskritName = "ज्येष्ठा", symbol = "Amulet / Ring (రక్షారేకు / ఉంగరం)",
            deity = "Indra Deva (King of Gods)", teluguDeity = "ఇంద్రదేవుడు (దేవేంద్రుడు)",
            ruler = "Mercury", teluguRuler = "బుధుడు",
            rashi = "Vrischika (Scorpio)", teluguRashi = "వృశ్చిక రాశి",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Deer (హరిణం)", teluguYoni = "జింక (హరిణం)",
            namingLetters = "No, Ya, Yee, Yoo (నో, యా, యీ, యూ)", teluguNamingLetters = "నో, యా, యీ, యూ",
            keyTraits = "Seniority, leadership, overcoming adversity, heroism.",
            teluguTraits = "జ్యేష్ఠత్వ గౌరవం, రక్షణ శక్తి, ధైర్యసాహసాలు, సవాళ్లను ఎదుర్కొనే శక్తి."
        ),
        NakshatraDetail(
            number = 19, name = "Mula (Moola)", teluguName = "19. మూల", sanskritName = "मूल", symbol = "Tied Roots (చెట్టు వేర్లు)",
            deity = "Nirriti Deva", teluguDeity = "నిర్ఋతి (మూలదేవత)",
            ruler = "Ketu", teluguRuler = "కేతువు",
            rashi = "Dhanu (Sagittarius)", teluguRashi = "ధనుస్సు రాశి",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Dog (శునకం)", teluguYoni = "కుక్క (శునకం)",
            namingLetters = "Ye, Yo, Bha, Bhee (యే, యో, భా, భీ)", teluguNamingLetters = "యే, యో, భా, భీ",
            keyTraits = "Root research, deep investigation, spiritual detachment.",
            teluguTraits = "విషయాల మూలాలను శోధించడం, ఆధ్యాత్మిక విరక్తి, జ్ఞానార్జన."
        ),
        NakshatraDetail(
            number = 20, name = "Purva Ashadha", teluguName = "20. పూర్వాషాఢ", sanskritName = "पूर्वाषाढ़ा", symbol = "Fan / Elephant Tusk (విసనకర్ర / దంతం)",
            deity = "Apah Deva (Water Deities)", teluguDeity = "జలదేవతలు (ఆపస్)",
            ruler = "Venus", teluguRuler = "శుక్రుడు",
            rashi = "Dhanu (Sagittarius)", teluguRashi = "ధనుస్సు రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Monkey (వానరం)", teluguYoni = "కోతి (వానరం)",
            namingLetters = "Bhoo, Dha, Pha, Dha (భూ, ధా, ఫా, డా)", teluguNamingLetters = "భూ, ధా, ఫా, డా",
            keyTraits = "Invincibility, purity, inspiring speech, pride.",
            teluguTraits = "ఓటమి లేని పట్టుదల, స్వచ్ఛత, ప్రభావవంతమైన ఉపన్యాసం."
        ),
        NakshatraDetail(
            number = 21, name = "Uttara Ashadha", teluguName = "21. ఉత్తరాషాఢ", sanskritName = "उत्तराषाढ़ा", symbol = "Plank / Bed (చిన్న మంచం)",
            deity = "Vishvedevas (Universal Gods)", teluguDeity = "విశ్వేదేవతలు",
            ruler = "Sun", teluguRuler = "సూర్యుడు",
            rashi = "Dhanu & Makara", teluguRashi = "ధనుస్సు (1) & మకరం (2,3,4)",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Mongoose (ముంగిస)", teluguYoni = "ముంగిస",
            namingLetters = "Bhe, Bho, Ja, Jee (భే, భో, జా, జీ)", teluguNamingLetters = "భే, భో, జా, జీ",
            keyTraits = "Permanent victory, ethical leadership, enduring legacy.",
            teluguTraits = "శాశ్వత విజయం, ధార్మిక నాయకత్వం, సద్గుణాలు, స్థిరమైన ప్రగతి."
        ),
        NakshatraDetail(
            number = 22, name = "Shravana", teluguName = "22. శ్రవణం", sanskritName = "श्रवण", symbol = "Three Footprints / Ear (మూడు అడుగులు / చెవి)",
            deity = "Lord Maha Vishnu", teluguDeity = "శ్రీ మహావిష్ణువు",
            ruler = "Moon", teluguRuler = "చంద్రుడు",
            rashi = "Makara (Capricorn)", teluguRashi = "మకర రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Monkey (వానరం)", teluguYoni = "కోతి (వానరం)",
            namingLetters = "Khee, Khoo, Khe, Kho (ఖీ, ఖూ, ఖే, ఖో)", teluguNamingLetters = "ఖీ, ఖూ, ఖే, ఖో",
            keyTraits = "Sacred listening, oral learning, devotion, wisdom.",
            teluguTraits = "శ్రవణం (వినడం), విద్యాభ్యాసం, విష్ణుభక్తి, వేదశాస్త్ర విజ్ఞానం."
        ),
        NakshatraDetail(
            number = 23, name = "Dhanishta", teluguName = "23. ధనిష్ఠ", sanskritName = "धनिष्ठा", symbol = "Drum / Flute (మృదంగం / మురళి)",
            deity = "Eight Vasus", teluguDeity = "అష్టవసువులు",
            ruler = "Mars", teluguRuler = "కుజుడు",
            rashi = "Makara & Kumbha", teluguRashi = "మకరం (1,2) & కుంభం (3,4)",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Lion (సింహం)", teluguYoni = "సింహం",
            namingLetters = "Ga, Gee, Goo, Ge (గా, గీ, గూ, గే)", teluguNamingLetters = "గా, గీ, గూ, గే",
            keyTraits = "Wealth, musical rhythm, celebration, influence.",
            teluguTraits = "ధనసమృద్ధి, సంగీతాభిరుచి, నృత్యం, మంగళకరమైన వేడుకలు."
        ),
        NakshatraDetail(
            number = 24, name = "Shatabhisha", teluguName = "24. శతభిషం", sanskritName = "शतभिषा", symbol = "Circle of 100 Doctors (నూటొక్క వైద్యులు)",
            deity = "Varuna Deva (Ocean God)", teluguDeity = "వరుణదేవుడు",
            ruler = "Rahu", teluguRuler = "రాహువు",
            rashi = "Kumbha (Aquarius)", teluguRashi = "కుంభ రాశి",
            gana = "Rakshasa Gana", teluguGana = "రాక్షస గణం",
            yoni = "Horse (అశ్వం)", teluguYoni = "గుర్రం (అశ్వం)",
            namingLetters = "Go, Sa, See, Soo (గో, సా, సీ, సూ)", teluguNamingLetters = "గో, సా, సీ, సూ",
            keyTraits = "Healing 100 maladies, secrecy, cosmic vision, solitude.",
            teluguTraits = "రోగ నివారణ శక్తి, వైద్య శాస్త్రం, నిగూఢ జ్ఞానం, ఏకాంతం."
        ),
        NakshatraDetail(
            number = 25, name = "Purva Bhadrapada", teluguName = "25. పూర్వాభాద్ర", sanskritName = "पूर्वभाद्रपदा", symbol = "Front of Funeral Cot (శవపీఠం ముందుభాగం)",
            deity = "Aja Ekapada Deva", teluguDeity = "అజైకపాదుడు (రుద్రాంశ)",
            ruler = "Jupiter", teluguRuler = "గురుడు",
            rashi = "Kumbha & Meena", teluguRashi = "కుంభం (1,2,3) & మీనం (4)",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Lion (సింహం)", teluguYoni = "సింహం",
            namingLetters = "Se, So, Da, Dee (సే, సో, దా, డీ)", teluguNamingLetters = "సే, సో, దా, డీ",
            keyTraits = "Spiritual penance, intense purification, idealism.",
            teluguTraits = "తపోశక్తి, ఆధ్యాత్మిక సాధన, తీవ్రమైన త్యాగనిరతి, ఆదర్శాలు."
        ),
        NakshatraDetail(
            number = 26, name = "Uttara Bhadrapada", teluguName = "26. ఉత్తరాభాద్ర", sanskritName = "उत्तरभाद्रपदा", symbol = "Back of Cot (మంచం వెనుక భాగం)",
            deity = "Ahirbudhnya (Water Serpent)", teluguDeity = "అహిర్బుధ్న్యుడు (జలనాగుడు)",
            ruler = "Saturn", teluguRuler = "శని",
            rashi = "Meena (Pisces)", teluguRashi = "మీన రాశి",
            gana = "Manushya Gana", teluguGana = "మనుష్య గణం",
            yoni = "Cow (గోవు)", teluguYoni = "ఆవు (గోవు)",
            namingLetters = "Du, Tha, Jha, Jna (దూ, థ, ఝ, ఞ)", teluguNamingLetters = "దూ, థ, ఝ, ఞ",
            keyTraits = "Oceanic depth, tranquil wisdom, protective nature.",
            teluguTraits = "ప్రశాంతమైన జ్ఞానం, గంభీరత, రక్షణ శక్తి, లక్ష్మీ కటాక్షం."
        ),
        NakshatraDetail(
            number = 27, name = "Revati", teluguName = "27. రేవతి", sanskritName = "रेवती", symbol = "Pair of Fish / Drum (చేపల జంట / మృదంగం)",
            deity = "Pushan Deva (Protector)", teluguDeity = "పూషా (మార్గదర్శక దేవుడు)",
            ruler = "Mercury", teluguRuler = "బుధుడు",
            rashi = "Meena (Pisces)", teluguRashi = "మీన రాశి",
            gana = "Deva Gana", teluguGana = "దేవ గణం",
            yoni = "Elephant (గజం)", teluguYoni = "ఏనుగు (గజం)",
            namingLetters = "De, Do, Cha, Chee (దే, దో, చా, చీ)", teluguNamingLetters = "దే, దో, చా, చీ",
            keyTraits = "Safe journeys, tender guidance, complete spiritual culmination.",
            teluguTraits = "రక్షిత ప్రయాణాలు, ప్రేమ, మృదువైన హృదయం, పూర్ణత్వం, సకల శుభం."
        )
    )

    fun get108Padas(): List<PadaDetail> {
        val padas = mutableListOf<PadaDetail>()
        val rashiNamesTe = listOf(
            "మేషం", "వృషభం", "మిథునం", "కర్కాటకం", "సింహం", "కన్య", "తుల", "వృశ్చికం", "ధనుస్సు", "మకరం", "కుంభం", "మీనం"
        )
        val rashiNamesEn = listOf(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
        )
        
        val syllables = listOf(
            listOf("చూ", "చే", "చో", "లా"), // Ashwini
            listOf("లీ", "లూ", "లే", "లో"), // Bharani
            listOf("ఆ", "ఈ", "ఊ", "ఏ"),    // Krittika
            listOf("ఓ", "వా", "వీ", "వూ"),  // Rohini
            listOf("వే", "వో", "కా", "కీ"), // Mrigashirsha
            listOf("కూ", "ఘ", "ఙ", "ఛ"),   // Ardra
            listOf("కే", "కో", "హా", "హీ"), // Punarvasu
            listOf("హూ", "హే", "హో", "డా"), // Pushya
            listOf("డీ", "డూ", "డే", "డో"), // Ashlesha
            listOf("మా", "మీ", "మూ", "మే"), // Magha
            listOf("మో", "టా", "టీ", "టూ"), // Purva Phalguni
            listOf("టే", "తో", "పా", "పీ"), // Uttara Phalguni
            listOf("పూ", "ష", "ణ", "ఠ"),   // Hasta
            listOf("పే", "పో", "రా", "రీ"), // Chitra
            listOf("రూ", "రే", "రో", "తా"), // Swati
            listOf("తీ", "తూ", "తే", "తో"), // Vishakha
            listOf("నా", "నీ", "నూ", "నే"), // Anuradha
            listOf("నో", "యా", "యీ", "యూ"), // Jyeshtha
            listOf("యే", "యో", "భా", "భీ"), // Mula
            listOf("భూ", "ధా", "ఫా", "ఢా"), // Purva Ashadha
            listOf("భే", "భో", "జా", "జీ"), // Uttara Ashadha
            listOf("ఖీ", "ఖూ", "ఖే", "ఖో"), // Shravana
            listOf("గా", "గీ", "గూ", "గే"), // Dhanishta
            listOf("గో", "సా", "సీ", "సూ"), // Shatabhisha
            listOf("సే", "సో", "దా", "దీ"), // Purva Bhadrapada
            listOf("దూ", "థా", "ఝా", "ఞా"), // Uttara Bhadrapada
            listOf("దే", "దో", "చా", "చీ")  // Revati
        )

        for (k in 0 until 108) {
            val nakIdx = k / 4
            val padaNum = (k % 4) + 1
            val nak = NAKSHATRAS[nakIdx]
            
            val startMinutes = k * 200
            val endMinutes = (k + 1) * 200
            
            val startRashiMinutes = startMinutes % 1800
            val endRashiMinutes = endMinutes % 1800
            
            val startDeg = startRashiMinutes / 60
            val startMin = startRashiMinutes % 60
            val endDeg = if (endRashiMinutes == 0 && endMinutes > 0) 30 else endRashiMinutes / 60
            val endMin = if (endRashiMinutes == 0 && endMinutes > 0) 0 else endRashiMinutes % 60
            
            val startStr = String.format("%02d°%02d'", startDeg, startMin)
            val endStr = String.format("%02d°%02d'", endDeg, endMin)
            
            val rashiIdx = (startMinutes / 1800).coerceIn(0, 11)
            val navamsaIdx = k % 12
            
            val syllable = syllables.getOrNull(nakIdx)?.getOrNull(padaNum - 1) ?: "ఓ"
            
            val purushartha = when (padaNum) {
                1 -> "ధర్మ పురుషార్థం (Dharma)"
                2 -> "అర్థ పురుషార్థం (Artha)"
                3 -> "కామ పురుషార్థం (Kama)"
                else -> "మోక్ష పురుషార్థం (Moksha)"
            }
            
            val traits = when (padaNum) {
                1 -> "ప్రథమ పాదం అగ్ని తత్త్వానికి చెందుతుంది. ఇది ధైర్యం, ఆత్మవిశ్వాసం, నాయకత్వ లక్షణాలు మరియు నూతన ఆవిష్కరణలను ప్రోత్సహిస్తుంది."
                2 -> "ద్వితీయ పాదం భూ తత్త్వానికి చెందుతుంది. ఇది స్థిరత్వం, ఆర్థిక పురోగతి, ఓర్పు మరియు ఆచరణాత్మక నైపుణ్యాలను సూచిస్తుంది."
                3 -> "తృతీయ పాదం వాయు తత్త్వానికి చెందుతుంది. ఇది మేధస్సు, బుద్ధి కుశలత, సృజనాత్మకత, కళల పట్ల ఆసక్తి మరియు చక్కని వాక్చాతుర్యం ఇస్తుంది."
                else -> "చతుర్థ పాదం జల తత్త్వానికి చెందుతుంది. ఇది आध्यात्मिक చింతన, దయాగుణం, అంతర్జ్ఞానం మరియు మానసిక శాంతిని చేకూరుస్తుంది."
            }
            
            val personality = "ఈ పాదంలో జన్మించిన వారు ${rashiNamesTe[navamsaIdx]} నవాంశ ప్రభావంతో వివేకం, దైవభక్తి మరియు కార్యదీక్ష కలిగి ఉంటారు. వీరి అక్షరధ్వని '${syllable}' తో ప్రారంభమవుతుంది. వీరు ఏ రంగంలోనైనా పట్టుదలతో శ్రమించి ఉన్నత శిఖరాలను అధిరోహిస్తారు."
            
            padas.add(
                PadaDetail(
                    nakshatraNumber = nak.number,
                    nakshatraName = nak.name,
                    nakshatraTeluguName = nak.teluguName.replace(Regex("^\\d+\\.\\s*"), ""),
                    padaNumber = padaNum,
                    absoluteIndex = k + 1,
                    startDegreeStr = startStr,
                    endDegreeStr = endStr,
                    rashiName = rashiNamesEn[rashiIdx],
                    rashiTeluguName = rashiNamesTe[rashiIdx],
                    navamsaRashiName = rashiNamesEn[navamsaIdx],
                    navamsaRashiTeluguName = rashiNamesTe[navamsaIdx],
                    namingSyllable = syllable,
                    traitsTe = traits,
                    personalityTe = personality,
                    purusharthaTe = purushartha
                )
            )
        }
        return padas
    }
}
