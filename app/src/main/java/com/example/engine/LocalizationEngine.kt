package com.example.engine

import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.model.DayPanchanga
import com.example.model.FestivalCategory
import com.example.model.FestivalItem
import com.example.model.IndianDistrictsRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

object LocalizationEngine {

    private val translations = mapOf(
        // App header & Navigation
        "app_title" to mapOf(
            AppLanguage.EN to "Veda Panchangam",
            AppLanguage.HI to "वैदिक पंचांग",
            AppLanguage.TE to "వేద పంచాంగం",
            AppLanguage.TA to "வேத பஞ்சாங்கம்",
            AppLanguage.KN to "ವೇದ ಪಂಚಾಂಗ",
            AppLanguage.MR to "वैदिक पंचांग",
            AppLanguage.GU to "વૈદિક પંચાંગ",
            AppLanguage.BN to "বৈদিক পঞ্জিকা",
            AppLanguage.ML to "വേദ പഞ്ചാംഗം",
            AppLanguage.OR_LANG to "ବୈଦିକ ପଞ୍ଜିକା"
        ),
        "nav_today" to mapOf(
            AppLanguage.EN to "Today",
            AppLanguage.HI to "आज",
            AppLanguage.TE to "ఈ రోజు",
            AppLanguage.TA to "இன்று",
            AppLanguage.KN to "ಇಂದು",
            AppLanguage.MR to "आज",
            AppLanguage.GU to "આજ",
            AppLanguage.BN to "আজ",
            AppLanguage.ML to "ഇന്ന്",
            AppLanguage.OR_LANG to "ଆଜି"
        ),
        "nav_calendar" to mapOf(
            AppLanguage.EN to "Calendar",
            AppLanguage.HI to "कैलेंडर",
            AppLanguage.TE to "క్యాలెండర్",
            AppLanguage.TA to "நாட்காட்டி",
            AppLanguage.KN to "ಕ್ಯಾಲೆಂಡರ್",
            AppLanguage.MR to "दिनदर्शिका",
            AppLanguage.GU to "કેલેન્ડર",
            AppLanguage.BN to "ক্যালেন্ডার",
            AppLanguage.ML to "കലണ്ടർ",
            AppLanguage.OR_LANG to "କ୍ୟାଲେଣ୍ଡର"
        ),
        "nav_festivals" to mapOf(
            AppLanguage.EN to "Festivals & Vrat",
            AppLanguage.HI to "त्यौहार एवं व्रत",
            AppLanguage.TE to "పండుగలు",
            AppLanguage.TA to "பண்டிகைகள் & விரதங்கள்",
            AppLanguage.KN to "ಹಬ್ಬಗಳು & ವ್ರತಗಳು",
            AppLanguage.MR to "सण आणि व्रते",
            AppLanguage.GU to "તહેવારો અને વ્રત",
            AppLanguage.BN to "উৎসব ও ব্রত",
            AppLanguage.ML to "ഉത്സവങ്ങളും വ്രതങ്ങളും",
            AppLanguage.OR_LANG to "ପର୍ବପର୍ବାଣୀ ଓ ବ୍ରତ"
        ),
        "nav_muhurtha" to mapOf(
            AppLanguage.EN to "Muhurtha",
            AppLanguage.HI to "शुभ मुहूर्त",
            AppLanguage.TE to "ముహూర్తం",
            AppLanguage.TA to "சுப முகூர்த்தம்",
            AppLanguage.KN to "ಮುಹೂರ್ತ",
            AppLanguage.MR to "शुभ मुहूर्त",
            AppLanguage.GU to "શુભ મુહૂર્ત",
            AppLanguage.BN to "শুভ মুহূর্ত",
            AppLanguage.ML to "മുഹൂർത്തം",
            AppLanguage.OR_LANG to "ଶୁଭ ମୁହୂର୍ତ୍ତ"
        ),
        "nav_saved" to mapOf(
            AppLanguage.EN to "Saved",
            AppLanguage.HI to "सुरक्षित",
            AppLanguage.TE to "సేకరణ",
            AppLanguage.TA to "சேமிக்கப்பட்டவை",
            AppLanguage.KN to "ಉಳಿಸಲಾಗಿದೆ",
            AppLanguage.MR to "जतन केलेले",
            AppLanguage.GU to "સાચવેલ",
            AppLanguage.BN to "সংরক্ষিত",
            AppLanguage.ML to "സംരക്ഷിച്ചവ",
            AppLanguage.OR_LANG to "ସଂରକ୍ଷିତ"
        ),
        "nav_explore" to mapOf(
            AppLanguage.EN to "Explore",
            AppLanguage.HI to "अन्वेषण",
            AppLanguage.TE to "అన్వేషణ",
            AppLanguage.TA to "ஆராய்வு",
            AppLanguage.KN to "ಅನ್ವೇಷಣೆ",
            AppLanguage.MR to "शोध",
            AppLanguage.GU to "શોધ",
            AppLanguage.BN to "অনুসন্ধান",
            AppLanguage.ML to "കൂടുതൽ",
            AppLanguage.OR_LANG to "ଅନୁସନ୍ଧାନ"
        ),

        // Date & Time Banner
        "live_current_date_time" to mapOf(
            AppLanguage.EN to "Current Live Date & Time",
            AppLanguage.HI to "वर्तमान दिनांक एवं समय",
            AppLanguage.TE to "ప్రస్తుత ప్రత్యక్ష తేదీ & సమయం",
            AppLanguage.TA to "தற்போதைய தேதி மற்றும் நேரம்",
            AppLanguage.KN to "ಪ್ರಸ್ತುತ ದಿನಾಂಕ ಮತ್ತು ಸಮಯ",
            AppLanguage.MR to "सध्याची तारीख आणि वेळ",
            AppLanguage.GU to "વર્તમાન તારીખ અને સમય",
            AppLanguage.BN to "বর্তমান তারিখ ও সময়",
            AppLanguage.ML to "നിലവിലെ തീയതിയും സമയവും",
            AppLanguage.OR_LANG to "ବର୍ତ୍ତମାନ ତାରିଖ ଓ ସମୟ"
        ),
        "current_time_label" to mapOf(
            AppLanguage.EN to "Live Time",
            AppLanguage.HI to "समय",
            AppLanguage.TE to "సమయం",
            AppLanguage.TA to "நேரம்",
            AppLanguage.KN to "ಸಮಯ",
            AppLanguage.MR to "वेळ",
            AppLanguage.GU to "સમય",
            AppLanguage.BN to "সময়",
            AppLanguage.ML to "സമയം",
            AppLanguage.OR_LANG to "ସମୟ"
        ),
        "today" to mapOf(
            AppLanguage.EN to "Today",
            AppLanguage.HI to "आज",
            AppLanguage.TE to "నేడు",
            AppLanguage.TA to "இன்று",
            AppLanguage.KN to "ಇಂದು",
            AppLanguage.MR to "आज",
            AppLanguage.GU to "આજ",
            AppLanguage.BN to "আজ",
            AppLanguage.ML to "ഇന്ന്",
            AppLanguage.OR_LANG to "ଆଜି"
        ),
        "tap_jump_today" to mapOf(
            AppLanguage.EN to "Tap to jump to Today",
            AppLanguage.HI to "आज पर जाने के लिए टैप करें",
            AppLanguage.TE to "ఈ రోజుకు వెళ్ళడానికి నొక్కండి",
            AppLanguage.TA to "இன்று செல்ல தட்டவும்",
            AppLanguage.KN to "ಇಂದಿನ ದಿನಾಂಕಕ್ಕೆ ಹೋಗಿ",
            AppLanguage.MR to "आजवर जाण्यासाठी टॅप करा",
            AppLanguage.GU to "આજ પર જવા માટે ટેપ કરો",
            AppLanguage.BN to "আজকে যেতে ট্যাপ করুন",
            AppLanguage.ML to "ഇന്നത്തേക്ക് പോകാൻ തൊടുക",
            AppLanguage.OR_LANG to "ଆଜିକୁ ଯିବାକୁ ଟ୍ୟାପ୍ କରନ୍ତୁ"
        ),
        "viewing_different_date" to mapOf(
            AppLanguage.EN to "Viewing Past/Future Date",
            AppLanguage.HI to "अन्य तिथि देखी जा रही है",
            AppLanguage.TE to "మరొక తేదీని చూస్తున్నారు",
            AppLanguage.TA to "வேறு தேதி பார்க்கப்படுகிறது",
            AppLanguage.KN to "ಬೇರೆ ದಿನಾಂಕ ವೀಕ್ಷಿಸಲಾಗುತ್ತಿದೆ",
            AppLanguage.MR to "दुसरी तारीख पाहिली जात आहे",
            AppLanguage.GU to "અન્ય તારીખ જોઈ રહ્યા છો",
            AppLanguage.BN to "অন্য তারিখ দেখা হচ্ছে",
            AppLanguage.ML to "മറ്റൊരു തീയതി കാണുന്നു",
            AppLanguage.OR_LANG to "ଅନ୍ୟ ତାରିଖ ଦେଖାଯାଉଛି"
        ),
        "jump_back_to_today" to mapOf(
            AppLanguage.EN to "Jump to Today",
            AppLanguage.HI to "आज पर जाएँ",
            AppLanguage.TE to "ఈ రోజుకు రండి",
            AppLanguage.TA to "இன்றுக்கு செல்",
            AppLanguage.KN to "ಇಂದಿಗೆ ಹೋಗಿ",
            AppLanguage.MR to "आजवर जा",
            AppLanguage.GU to "આજ પર જાઓ",
            AppLanguage.BN to "আজকে যান",
            AppLanguage.ML to "ഇന്നത്തേക്ക് പോകുക",
            AppLanguage.OR_LANG to "ଆଜିକୁ ଯାଆନ୍ତୁ"
        ),

        // Live status & transitions
        "whats_happening_now" to mapOf(
            AppLanguage.EN to "What's Happening Now?",
            AppLanguage.HI to "वर्तमान कालखंड (अभी क्या चल रहा है?)",
            AppLanguage.TE to "ప్రస్తుతం ఏమి జరుగుతోంది?",
            AppLanguage.TA to "இப்போது என்ன நடக்கிறது?",
            AppLanguage.KN to "ಈಗ ಏನು ನಡೆಯುತ್ತಿದೆ?",
            AppLanguage.MR to "आत्ता काय चालू आहे?",
            AppLanguage.GU to "હમણાં શું ચાલે છે?",
            AppLanguage.BN to "এখন কি চলছে?",
            AppLanguage.ML to "ഇപ്പോൾ എന്താണ് സംഭവിക്കുന്നത്?",
            AppLanguage.OR_LANG to "ବର୍ତ୍ତମାନ କ’ଣ ଚାଲିଛି?"
        ),
        "next_event_transition" to mapOf(
            AppLanguage.EN to "Next Event / Transition",
            AppLanguage.HI to "अगली घटना / परिवर्तन",
            AppLanguage.TE to "తదుపరి ఘట్టం / మార్పు",
            AppLanguage.TA to "அடுத்த நிகழ்வு / மாற்றம்",
            AppLanguage.KN to "ಮುಂದಿನ ಘಟನೆ / ಬದಲಾವಣೆ",
            AppLanguage.MR to "पुढील घटना / संक्रमण",
            AppLanguage.GU to "આગામી ઘટના / પરિવર્તન",
            AppLanguage.BN to "পরবর্তী ঘটনা / পরিবর্তন",
            AppLanguage.ML to "അടുത്ത മാറ്റം",
            AppLanguage.OR_LANG to "ପରବର୍ତ୍ତୀ ଘଟଣା"
        ),
        "active_now" to mapOf(
            AppLanguage.EN to "⚠️ Active Now",
            AppLanguage.HI to "⚠️ अभी सक्रिय",
            AppLanguage.TE to "⚠️ ప్రస్తుతం అమలులో ఉంది",
            AppLanguage.TA to "⚠️ இப்போது செயலில்",
            AppLanguage.KN to "⚠️ ಈಗ ಸಕ್ರಿಯ",
            AppLanguage.MR to "⚠️ आत्ता चालू आहे",
            AppLanguage.GU to "⚠️ અત્યારે સક્રિય",
            AppLanguage.BN to "⚠️ এখন সক্রিয়",
            AppLanguage.ML to "⚠️ ഇപ്പോൾ സജീവം",
            AppLanguage.OR_LANG to "⚠️ ବର୍ତ୍ତମାନ ସକ୍ରିୟ"
        ),
        "inactive" to mapOf(
            AppLanguage.EN to "Inactive",
            AppLanguage.HI to "निष्क्रिय",
            AppLanguage.TE to "అమలులో లేదు",
            AppLanguage.TA to "செயலில் இல்லை",
            AppLanguage.KN to "ನಿಷ್ಕ್ರಿಯ",
            AppLanguage.MR to "निष्क्रिय",
            AppLanguage.GU to "નિષ્ક્રિય",
            AppLanguage.BN to "নিষ্ক্রিয়",
            AppLanguage.ML to "നിഷ്ക്രിയം",
            AppLanguage.OR_LANG to "ନିଷ୍କ୍ରିୟ"
        ),

        // Limbs & Core Headings
        "core_panchanga_title" to mapOf(
            AppLanguage.EN to "THE FIVE LIMBS (PANCHANGA)",
            AppLanguage.HI to "पंचांग के पांच अंग",
            AppLanguage.TE to "పంచాంగ ఐదు అంగాలు",
            AppLanguage.TA to "பஞ்சாங்கத்தின் ஐந்து அங்கங்கள்",
            AppLanguage.KN to "ಪಂಚಾಂಗದ ಐದು ಅಂಗಗಳು",
            AppLanguage.MR to "पंचांगाचे पाच अंग",
            AppLanguage.GU to "પંચાંગના પાંચ અંગ",
            AppLanguage.BN to "পঞ্জিকার পঞ্চ অঙ্গ",
            AppLanguage.ML to "പഞ്ചാംഗത്തിലെ അഞ്ച് അംഗങ്ങൾ",
            AppLanguage.OR_LANG to "ପଞ୍ଜିକାର ପାଞ୍ଚ ଅଙ୍ଗ"
        ),
        "tithi" to mapOf(
            AppLanguage.EN to "Tithi",
            AppLanguage.HI to "तिथि",
            AppLanguage.TE to "తిథి",
            AppLanguage.TA to "திதி",
            AppLanguage.KN to "ತಿಥಿ",
            AppLanguage.MR to "तिथी",
            AppLanguage.GU to "તિથિ",
            AppLanguage.BN to "তিথি",
            AppLanguage.ML to "തിഥി",
            AppLanguage.OR_LANG to "ତିଥି"
        ),
        "nakshatra" to mapOf(
            AppLanguage.EN to "Nakshatra",
            AppLanguage.HI to "नक्षत्र",
            AppLanguage.TE to "నక్షత్రం",
            AppLanguage.TA to "நட்சத்திரம்",
            AppLanguage.KN to "ನಕ್ಷತ್ರ",
            AppLanguage.MR to "नक्षत्र",
            AppLanguage.GU to "નક્ષત્ર",
            AppLanguage.BN to "নক্ষত্র",
            AppLanguage.ML to "നക്ഷത്രം",
            AppLanguage.OR_LANG to "ନକ୍ଷତ୍ର"
        ),
        "yoga" to mapOf(
            AppLanguage.EN to "Yoga",
            AppLanguage.HI to "योग",
            AppLanguage.TE to "యోగం",
            AppLanguage.TA to "யோகம்",
            AppLanguage.KN to "ಯೋಗ",
            AppLanguage.MR to "योग",
            AppLanguage.GU to "યોગ",
            AppLanguage.BN to "যোগ",
            AppLanguage.ML to "യോഗം",
            AppLanguage.OR_LANG to "ଯୋଗ"
        ),
        "karana" to mapOf(
            AppLanguage.EN to "Karana",
            AppLanguage.HI to "करण",
            AppLanguage.TE to "కరణం",
            AppLanguage.TA to "கரணம்",
            AppLanguage.KN to "ಕರಣ",
            AppLanguage.MR to "करण",
            AppLanguage.GU to "કરણ",
            AppLanguage.BN to "করণ",
            AppLanguage.ML to "കരണം",
            AppLanguage.OR_LANG to "କରଣ"
        ),
        "vara" to mapOf(
            AppLanguage.EN to "Vara (Day)",
            AppLanguage.HI to "वार (दिन)",
            AppLanguage.TE to "వారం (రోజు)",
            AppLanguage.TA to "வாரம் (கிழமை)",
            AppLanguage.KN to "ವಾರ (ದಿನ)",
            AppLanguage.MR to "वार (दिवस)",
            AppLanguage.GU to "વાર (દિવસ)",
            AppLanguage.BN to "বার (দিন)",
            AppLanguage.ML to "വാരം (ദിവസം)",
            AppLanguage.OR_LANG to "ବାର (ଦିନ)"
        ),
        "paksha" to mapOf(
            AppLanguage.EN to "Paksha",
            AppLanguage.HI to "पक्ष",
            AppLanguage.TE to "పక్షం",
            AppLanguage.TA to "பக்ஷம்",
            AppLanguage.KN to "ಪಕ್ಷ",
            AppLanguage.MR to "पक्ष",
            AppLanguage.GU to "પક્ષ",
            AppLanguage.BN to "পক্ষ",
            AppLanguage.ML to "പക്ഷം",
            AppLanguage.OR_LANG to "ପକ୍ଷ"
        ),
        "deity" to mapOf(
            AppLanguage.EN to "Deity",
            AppLanguage.HI to "देवता",
            AppLanguage.TE to "దైవం",
            AppLanguage.TA to "தெய்வம்",
            AppLanguage.KN to "ದೇವತೆ",
            AppLanguage.MR to "देवता",
            AppLanguage.GU to "દેવતા",
            AppLanguage.BN to "দেবতা",
            AppLanguage.ML to "ദേവത",
            AppLanguage.OR_LANG to "ଦେବତା"
        ),
        "ruler" to mapOf(
            AppLanguage.EN to "Ruler / Lord",
            AppLanguage.HI to "स्वामी ग्रह",
            AppLanguage.TE to "అధిపతి",
            AppLanguage.TA to "அதிபதி கிரகம்",
            AppLanguage.KN to "ಅಧಿಪತಿ ಗ್ರಹ",
            AppLanguage.MR to "स्वामी ग्रह",
            AppLanguage.GU to "સ્વામી ગ્રહ",
            AppLanguage.BN to "অধিপতি গ্রহ",
            AppLanguage.ML to "അധിപ ഗ്രഹം",
            AppLanguage.OR_LANG to "ଅଧିପତି ଗ୍ରହ"
        ),
        "ends_at" to mapOf(
            AppLanguage.EN to "Ends at",
            AppLanguage.HI to "समाप्ति काल",
            AppLanguage.TE to "ముగింపు సమయం",
            AppLanguage.TA to "முடிவு நேரம்",
            AppLanguage.KN to "ಮುಕ್ತಾಯ ಸಮಯ",
            AppLanguage.MR to "समाप्ती वेळ",
            AppLanguage.GU to "સમાપ્તિ સમય",
            AppLanguage.BN to "সমাপ্তি সময়",
            AppLanguage.ML to "അവസാന സമയം",
            AppLanguage.OR_LANG to "ସମାପ୍ତି ସମୟ"
        ),
        "pada" to mapOf(
            AppLanguage.EN to "Pada",
            AppLanguage.HI to "चरण / पद",
            AppLanguage.TE to "పాదం",
            AppLanguage.TA to "பாதம்",
            AppLanguage.KN to "ಪಾದ",
            AppLanguage.MR to "चरण",
            AppLanguage.GU to "ચરણ",
            AppLanguage.BN to "চরণ",
            AppLanguage.ML to "പാദം",
            AppLanguage.OR_LANG to "ପାଦ"
        ),

        // Astronomy & Sun/Moon
        "astronomy_sky" to mapOf(
            AppLanguage.EN to "ASTRONOMY & SKY TODAY",
            AppLanguage.HI to "आज का खगोल एवं आकाश",
            AppLanguage.TE to "ఖగోళం & నేటి ఆకాశం",
            AppLanguage.TA to "வானியல் மற்றும் இன்றைய வானம்",
            AppLanguage.KN to "ಖಗೋಳ ಮತ್ತು ಇಂದಿನ ಆಕಾಶ",
            AppLanguage.MR to "आजचे खगोल आणि आकाश",
            AppLanguage.GU to "આજનું ખગોળ અને આકાશ",
            AppLanguage.BN to "আজকের জ্যোতির্বিদ্যা ও আকাশ",
            AppLanguage.ML to "ഇന്നത്തെ ആകാശക്കാഴ്ച",
            AppLanguage.OR_LANG to "ଆଜିର ଖଗୋଳ ଓ ଆକାଶ"
        ),
        "surya_sun" to mapOf(
            AppLanguage.EN to "SURYA (SUN)",
            AppLanguage.HI to "सूर्य देव (SUN)",
            AppLanguage.TE to "సూర్యుడు (SUN)",
            AppLanguage.TA to "சூரியன் (SUN)",
            AppLanguage.KN to "ಸೂರ್ಯ (SUN)",
            AppLanguage.MR to "सूर्य (SUN)",
            AppLanguage.GU to "સૂર્ય (SUN)",
            AppLanguage.BN to "সূর্য (SUN)",
            AppLanguage.ML to "സൂര്യൻ (SUN)",
            AppLanguage.OR_LANG to "ସୂର୍ଯ୍ୟ (SUN)"
        ),
        "chandra_moon" to mapOf(
            AppLanguage.EN to "CHANDRA (MOON)",
            AppLanguage.HI to "चन्द्र देव (MOON)",
            AppLanguage.TE to "చంద్రుడు (MOON)",
            AppLanguage.TA to "சந்திரன் (MOON)",
            AppLanguage.KN to "ಚಂದ್ರ (MOON)",
            AppLanguage.MR to "चंद्र (MOON)",
            AppLanguage.GU to "ચંદ્ર (MOON)",
            AppLanguage.BN to "চন্দ্র (MOON)",
            AppLanguage.ML to "ചന്ദ്രൻ (MOON)",
            AppLanguage.OR_LANG to "ଚନ୍ଦ୍ର (MOON)"
        ),
        "sunrise" to mapOf(
            AppLanguage.EN to "Sunrise",
            AppLanguage.HI to "सूर्योदय",
            AppLanguage.TE to "సూర్యోదయం",
            AppLanguage.TA to "சூரிய உதயம்",
            AppLanguage.KN to "ಸೂರ್ಯೋದಯ",
            AppLanguage.MR to "सूर्योदय",
            AppLanguage.GU to "સૂર્યોદય",
            AppLanguage.BN to "সূর্যোদয়",
            AppLanguage.ML to "സൂര്യോദയം",
            AppLanguage.OR_LANG to "ସୂର୍ଯ୍ୟୋଦୟ"
        ),
        "sunset" to mapOf(
            AppLanguage.EN to "Sunset",
            AppLanguage.HI to "सूर्यास्त",
            AppLanguage.TE to "సూర్యాస్తమయం",
            AppLanguage.TA to "சூரிய அஸ்தமனம்",
            AppLanguage.KN to "ಸೂರ್ಯಾಸ್ತ",
            AppLanguage.MR to "सूर्यास्त",
            AppLanguage.GU to "સૂર્યાસ્ત",
            AppLanguage.BN to "সূর্যাস্ত",
            AppLanguage.ML to "സൂര്യാസ്തമയം",
            AppLanguage.OR_LANG to "ସୂର୍ଯ୍ୟାସ୍ତ"
        ),
        "moonrise" to mapOf(
            AppLanguage.EN to "Moonrise",
            AppLanguage.HI to "चन्द्रोदय",
            AppLanguage.TE to "చంద్రోదయం",
            AppLanguage.TA to "சந்திர உதயம்",
            AppLanguage.KN to "ಚಂದ್ರೋದಯ",
            AppLanguage.MR to "चंद्रोदय",
            AppLanguage.GU to "ચંદ્રોદય",
            AppLanguage.BN to "চন্দ্রোদয়",
            AppLanguage.ML to "ചന്ദ്രോദയം",
            AppLanguage.OR_LANG to "ଚନ୍ଦ୍ରୋଦୟ"
        ),
        "moonset" to mapOf(
            AppLanguage.EN to "Moonset",
            AppLanguage.HI to "चंद्रास्त",
            AppLanguage.TE to "చంద్రాస్తమయం",
            AppLanguage.TA to "சந்திர அஸ்தமனம்",
            AppLanguage.KN to "ಚಂದ್ರಾಸ್ತ",
            AppLanguage.MR to "चंद्रास्त",
            AppLanguage.GU to "ચંદ્રાસ્ત",
            AppLanguage.BN to "চন্দ্রাস্ত",
            AppLanguage.ML to "ചന്ദ്രാസ്തമയം",
            AppLanguage.OR_LANG to "ଚନ୍ଦ୍ରାସ୍ତ"
        ),
        "day_length" to mapOf(
            AppLanguage.EN to "Day Length",
            AppLanguage.HI to "दिनमान (अवधि)",
            AppLanguage.TE to "పగటి కాలం",
            AppLanguage.TA to "பகல் நேரம்",
            AppLanguage.KN to "ಹಗಲಿನ ಅವಧಿ",
            AppLanguage.MR to "दिवसाचा कालावधी",
            AppLanguage.GU to "દિવસનો સમય",
            AppLanguage.BN to "দিনের দৈর্ঘ্য",
            AppLanguage.ML to "പകൽ ദൈർഘ്യം",
            AppLanguage.OR_LANG to "ଦିନର ଅବଧି"
        ),
        "zodiac" to mapOf(
            AppLanguage.EN to "Zodiac",
            AppLanguage.HI to "राशि",
            AppLanguage.TE to "రాశి",
            AppLanguage.TA to "ராசி",
            AppLanguage.KN to "ರಾಶಿ",
            AppLanguage.MR to "राशी",
            AppLanguage.GU to "રાશિ",
            AppLanguage.BN to "রাশি",
            AppLanguage.ML to "രാശി",
            AppLanguage.OR_LANG to "ରାଶି"
        ),

        // Auspicious & Inauspicious Timings
        "auspicious_timings" to mapOf(
            AppLanguage.EN to "Auspicious Timings (शुभ मुहूर्त)",
            AppLanguage.HI to "शुभ मुहूर्त एवं अनुकूल समय",
            AppLanguage.TE to "శుభ సమయాలు & ముహూర్తాలు",
            AppLanguage.TA to "சுப முகூர்த்த நேரங்கள்",
            AppLanguage.KN to "ಶುಭ ಮುಹೂರ್ತ ಸಮಯಗಳು",
            AppLanguage.MR to "शुभ मुहूर्त व वेळ",
            AppLanguage.GU to "શુભ મુહૂર્ત અને સમય",
            AppLanguage.BN to "শুভ মুহূর্ত ও অনুকূল সময়",
            AppLanguage.ML to "ശുഭ മുഹൂർത്ത സമയം",
            AppLanguage.OR_LANG to "ଶୁଭ ମୁହୂର୍ତ୍ତ ସମୟ"
        ),
        "inauspicious_timings" to mapOf(
            AppLanguage.EN to "Inauspicious Timings (वर्ज्य समय)",
            AppLanguage.HI to "अशुभ समय (राहुकाल / यमगण्ड)",
            AppLanguage.TE to "అశుభ సమయాలు (రాహుకాలం / యమగండం)",
            AppLanguage.TA to "அசுப நேரங்கள் (ராகுகாலம் / எமகண்டம்)",
            AppLanguage.KN to "ಅಶುಭ ಸಮಯಗಳು (ರಾಹು ಕಾಲ / ಯಮಗಂಡ)",
            AppLanguage.MR to "अशुभ वेळ (राहु काळ / यमगंड)",
            AppLanguage.GU to "અશુભ સમય (રાહુ કાળ / યમગંડ)",
            AppLanguage.BN to "অশুভ সময় (রাহু কাল / যমগণ্ড)",
            AppLanguage.ML to "അശുഭ സമയം (രാഹു കാലം)",
            AppLanguage.OR_LANG to "ଅଶୁଭ ସମୟ (ରାହୁ କାଳ)"
        ),
        "rahu_kalam" to mapOf(
            AppLanguage.EN to "Rahu Kalam",
            AppLanguage.HI to "राहु काल",
            AppLanguage.TE to "రాహు కాలం",
            AppLanguage.TA to "இராகு காலம்",
            AppLanguage.KN to "ರಾಹು ಕಾಲ",
            AppLanguage.MR to "राहु काळ",
            AppLanguage.GU to "રાહુ કાળ",
            AppLanguage.BN to "রাহু কাল",
            AppLanguage.ML to "രാഹു കാലം",
            AppLanguage.OR_LANG to "ରାହୁ କାଳ"
        ),
        "yamaganda" to mapOf(
            AppLanguage.EN to "Yamaganda",
            AppLanguage.HI to "यमगण्ड",
            AppLanguage.TE to "యమగండం",
            AppLanguage.TA to "எமகண்டம்",
            AppLanguage.KN to "ಯಮಗಂಡ",
            AppLanguage.MR to "यमगंड",
            AppLanguage.GU to "યમગંડ",
            AppLanguage.BN to "যমগণ্ড",
            AppLanguage.ML to "യമഗണ്ഡം",
            AppLanguage.OR_LANG to "ଯମଗଣ୍ଡ"
        ),
        "gulika" to mapOf(
            AppLanguage.EN to "Gulika Kalam",
            AppLanguage.HI to "गुलिक काल",
            AppLanguage.TE to "గుళికా కాలం",
            AppLanguage.TA to "குளிகை காலம்",
            AppLanguage.KN to "ಗುಳಿಕ ಕಾಲ",
            AppLanguage.MR to "गुलिक काळ",
            AppLanguage.GU to "ગુલિક કાળ",
            AppLanguage.BN to "গুলিক কাল",
            AppLanguage.ML to "ഗുളിക കാലം",
            AppLanguage.OR_LANG to "ଗୁଳିକା କାଳ"
        ),
        "abhijit" to mapOf(
            AppLanguage.EN to "Abhijit Muhurta",
            AppLanguage.HI to "अभिजित मुहूर्त",
            AppLanguage.TE to "అభిజిత్ ముహూర్తం",
            AppLanguage.TA to "அபிஜித் முகூர்த்தம்",
            AppLanguage.KN to "ಅಭಿಜಿತ್ ಮುಹೂರ್ತ",
            AppLanguage.MR to "अभिजित मुहूर्त",
            AppLanguage.GU to "અભિજિત મુહૂર્ત",
            AppLanguage.BN to "অভিজিৎ মুহূর্ত",
            AppLanguage.ML to "അഭിജിത് മുഹൂർത്തം",
            AppLanguage.OR_LANG to "ଅଭିଜିତ ମୁହୂର୍ତ୍ତ"
        ),
        "brahma_muhurta" to mapOf(
            AppLanguage.EN to "Brahma Muhurta",
            AppLanguage.HI to "ब्रह्म मुहूर्त",
            AppLanguage.TE to "బ్రహ్మ ముహూర్తం",
            AppLanguage.TA to "பிரம்மா முகூர்த்தம்",
            AppLanguage.KN to "ಬ್ರಹ್ಮ ಮುಹೂರ್ತ",
            AppLanguage.MR to "ब्रह्म मुहूर्त",
            AppLanguage.GU to "બ્રહ્મ મુહૂર્ત",
            AppLanguage.BN to "ব্রাহ্ম মুহূর্ত",
            AppLanguage.ML to "ബ്രഹ്മ മുഹൂർത്തം",
            AppLanguage.OR_LANG to "ବ୍ରାହ୍ମ ମୁହୂର୍ତ୍ତ"
        ),
        "amrita_kalam" to mapOf(
            AppLanguage.EN to "Amrita Kalam",
            AppLanguage.HI to "अमृत काल",
            AppLanguage.TE to "అమృత కాలం",
            AppLanguage.TA to "அமிர்த காலம்",
            AppLanguage.KN to "ಅಮೃತ ಕಾಲ",
            AppLanguage.MR to "अमृत काळ",
            AppLanguage.GU to "અમૃત કાળ",
            AppLanguage.BN to "অমৃত কাল",
            AppLanguage.ML to "അമൃത കാലം",
            AppLanguage.OR_LANG to "ଅମୃତ କାଳ"
        ),
        "varjyam" to mapOf(
            AppLanguage.EN to "Varjyam",
            AppLanguage.HI to "वर्ज्यम्",
            AppLanguage.TE to "వర్జ్యం",
            AppLanguage.TA to "வர்ஜ்யம்",
            AppLanguage.KN to "ವರ್ಜ್ಯಂ",
            AppLanguage.MR to "वर्ज्यम्",
            AppLanguage.GU to "વર્જ્યમ",
            AppLanguage.BN to "বর্জ্যম",
            AppLanguage.ML to "വർജ്ജ്യം",
            AppLanguage.OR_LANG to "ବର୍ଜ୍ୟମ୍"
        ),
        "dur_muhurtam" to mapOf(
            AppLanguage.EN to "Dur Muhurtam",
            AppLanguage.HI to "दुर्मुहूर्त",
            AppLanguage.TE to "దుర్ముహూర్తం",
            AppLanguage.TA to "துர்முகூர்த்தம்",
            AppLanguage.KN to "ದುರ್ಮುಹೂರ್ತ",
            AppLanguage.MR to "दुर्मुहूर्त",
            AppLanguage.GU to "દુર્મુહૂર્ત",
            AppLanguage.BN to "দুর্মুহূর্ত",
            AppLanguage.ML to "ദുർമുഹൂർത്തം",
            AppLanguage.OR_LANG to "ଦୁର୍ମୁହୂର୍ତ୍ତ"
        ),

        // Choghadiya
        "choghadiya_title" to mapOf(
            AppLanguage.EN to "CHOGHADIYA (DAY & NIGHT)",
            AppLanguage.HI to "चौघड़िया (दिन एवं रात्रि)",
            AppLanguage.TE to "చౌఘడియా (పగలు & రాత్రి)",
            AppLanguage.TA to "சோகடியா (பகல் & இரவு)",
            AppLanguage.KN to "ಚೌಘಡಿಯಾ (ಹಗಲು & ರಾತ್ರಿ)",
            AppLanguage.MR to "चौघडिया (दिवस आणि रात्र)",
            AppLanguage.GU to "ચોઘડિયા (દિવસ અને રાત)",
            AppLanguage.BN to "চৌঘড়িয়া (দিন ও রাত)",
            AppLanguage.ML to "ചൗഘടിയ (പകലും രാത്രിയും)",
            AppLanguage.OR_LANG to "ଚୌଘଡ଼ିଆ (ଦିନ ଓ ରାତି)"
        ),
        "day_choghadiya" to mapOf(
            AppLanguage.EN to "☀️ Day Choghadiya",
            AppLanguage.HI to "☀️ दिन का चौघड़िया",
            AppLanguage.TE to "☀️ పగటి చౌఘడియా",
            AppLanguage.TA to "☀️ பகல் சோகடியா",
            AppLanguage.KN to "☀️ ಹಗಲಿನ ಚೌಘಡಿಯಾ",
            AppLanguage.MR to "☀️ दिवसाचे चौघडिया",
            AppLanguage.GU to "☀️ દિવસનું ચોઘડિયું",
            AppLanguage.BN to "☀️ দিনের চৌঘড়িয়া",
            AppLanguage.ML to "☀️ പകൽ ചൗഘടിയ",
            AppLanguage.OR_LANG to "☀️ ଦିନ ଚୌଘଡ଼ିଆ"
        ),
        "night_choghadiya" to mapOf(
            AppLanguage.EN to "🌙 Night Choghadiya",
            AppLanguage.HI to "🌙 रात्रि का चौघड़िया",
            AppLanguage.TE to "🌙 రాత్రి చౌఘడియా",
            AppLanguage.TA to "🌙 இரவு சோகடியா",
            AppLanguage.KN to "🌙 ರಾತ್ರಿಯ ಚೌಘಡಿಯಾ",
            AppLanguage.MR to "🌙 रात्रीचे चौघडिया",
            AppLanguage.GU to "🌙 રાત્રિનું ચોઘડિયું",
            AppLanguage.BN to "🌙 রাতের চৌঘড়িয়া",
            AppLanguage.ML to "🌙 രാത്രി ചൗഘടിയ",
            AppLanguage.OR_LANG to "🌙 ରାତି ଚୌଘଡ଼ିଆ"
        ),

        // Festival Categories & UI Labels
        "category_all" to mapOf(
            AppLanguage.EN to "All",
            AppLanguage.HI to "सभी",
            AppLanguage.TE to "అన్నీ",
            AppLanguage.TA to "அனைத்தும்",
            AppLanguage.KN to "ಎಲ್ಲವೂ",
            AppLanguage.MR to "सर्व",
            AppLanguage.GU to "તમામ",
            AppLanguage.BN to "সব",
            AppLanguage.ML to "എല്ലാം",
            AppLanguage.OR_LANG to "ସମସ୍ତ"
        ),
        "category_major" to mapOf(
            AppLanguage.EN to "Major Festivals",
            AppLanguage.HI to "प्रमुख त्योहार",
            AppLanguage.TE to "ముఖ్యమైన పండుగలు",
            AppLanguage.TA to "முக்கிய பண்டிகைகள்",
            AppLanguage.KN to "ಪ್ರಮುಖ ಹಬ್ಬಗಳು",
            AppLanguage.MR to "प्रमुख सण",
            AppLanguage.GU to "મુખ્ય તહેવારો",
            AppLanguage.BN to "প্রধান উৎসব",
            AppLanguage.ML to "പ്രധാന ഉത്സവങ്ങൾ",
            AppLanguage.OR_LANG to "ମୁଖ୍ୟ ପର୍ବ"
        ),
        "category_vrat" to mapOf(
            AppLanguage.EN to "Vrats & Fasting",
            AppLanguage.HI to "व्रत एवं उपवास",
            AppLanguage.TE to "వ్రతాలు & ఉపవాసాలు",
            AppLanguage.TA to "விரதங்கள்",
            AppLanguage.KN to "ವ್ರತಗಳು & ಉಪವಾಸ",
            AppLanguage.MR to "व्रते आणि उपवास",
            AppLanguage.GU to "વ્રત અને ઉપવાસ",
            AppLanguage.BN to "ব্রত ও উপবাস",
            AppLanguage.ML to "വ്രതങ്ങൾ",
            AppLanguage.OR_LANG to "ବ୍ରତ"
        ),
        "category_ekadashi" to mapOf(
            AppLanguage.EN to "Ekadashi",
            AppLanguage.HI to "एकादशी",
            AppLanguage.TE to "ఏకాదశి వ్రతాలు",
            AppLanguage.TA to "ஏகாதசி",
            AppLanguage.KN to "ಏಕಾದಶಿ",
            AppLanguage.MR to "एकादशी",
            AppLanguage.GU to "એકાદશી",
            AppLanguage.BN to "একাদশী",
            AppLanguage.ML to "ഏകാദശി",
            AppLanguage.OR_LANG to "ଏକାଦଶୀ"
        ),
        "category_jayanti" to mapOf(
            AppLanguage.EN to "Jayanti",
            AppLanguage.HI to "जयंती",
            AppLanguage.TE to "జయంతి ఉత్సవాలు",
            AppLanguage.TA to "ஜெயந்தி",
            AppLanguage.KN to "ಜಯಂತಿ",
            AppLanguage.MR to "जयंती",
            AppLanguage.GU to "જયંતિ",
            AppLanguage.BN to "জয়ন্তী",
            AppLanguage.ML to "ജയന്തി",
            AppLanguage.OR_LANG to "ଜୟନ୍ତୀ"
        ),
        "category_regional" to mapOf(
            AppLanguage.EN to "Regional",
            AppLanguage.HI to "क्षेत्रीय",
            AppLanguage.TE to "ప్రాంతీయ పండుగలు",
            AppLanguage.TA to "பிராந்திய",
            AppLanguage.KN to "ಪ್ರಾದೇಶಿಕ",
            AppLanguage.MR to "प्रादेशिक",
            AppLanguage.GU to "પ્રાદેશિક",
            AppLanguage.BN to "আঞ্চলিক",
            AppLanguage.ML to "പ്രദേശികം",
            AppLanguage.OR_LANG to "ଆଞ୍ଚଳିକ"
        ),
        "puja_muhurta" to mapOf(
            AppLanguage.EN to "Puja Muhurta",
            AppLanguage.HI to "पूजा मुहूर्त",
            AppLanguage.TE to "పూజా ముహూర్తం",
            AppLanguage.TA to "பூஜை முகூர்த்தம்",
            AppLanguage.KN to "ಪೂಜಾ ಮುಹೂರ್ತ",
            AppLanguage.MR to "पूजा मुहूर्त",
            AppLanguage.GU to "પૂજા મુહૂર્ત",
            AppLanguage.BN to "পূজা মুহূর্ত",
            AppLanguage.ML to "പൂജ മുഹൂർത്തം",
            AppLanguage.OR_LANG to "ପୂଜା ମୁହୂର୍ତ୍ତ"
        ),
        "parana_time" to mapOf(
            AppLanguage.EN to "Parana Time",
            AppLanguage.HI to "पारण समय",
            AppLanguage.TE to "పారణ సమయం",
            AppLanguage.TA to "பாரணை நேரம்",
            AppLanguage.KN to "ಪಾರಣೆ ಸಮಯ",
            AppLanguage.MR to "पारणा वेळ",
            AppLanguage.GU to "પારણા સમય",
            AppLanguage.BN to "পারণ সময়",
            AppLanguage.ML to "പാരണ സമയം",
            AppLanguage.OR_LANG to "ପାରଣା ସମୟ"
        ),
        "deity" to mapOf(
            AppLanguage.EN to "Deity",
            AppLanguage.HI to "इष्टदेव / देवता",
            AppLanguage.TE to "దైవం",
            AppLanguage.TA to "தெய்வம்",
            AppLanguage.KN to "ದೇವತೆ",
            AppLanguage.MR to "देवता",
            AppLanguage.GU to "દેવતા",
            AppLanguage.BN to "দেবতা",
            AppLanguage.ML to "ദൈവം",
            AppLanguage.OR_LANG to "ଦେବତା"
        ),
        "festivals_found_label" to mapOf(
            AppLanguage.EN to "festivals & vrats found for year",
            AppLanguage.HI to "वर्ष हेतु पंजीकृत त्यौहार एवं व्रत",
            AppLanguage.TE to "సంవత్సరంలో పండుగలు & వ్రతాలు",
            AppLanguage.TA to "ஆண்டின் பண்டிகைகள்",
            AppLanguage.KN to "ವರ್ಷದ ಹಬ್ಬಗಳು & ವ್ರತಗಳು",
            AppLanguage.MR to "वर्षातील सण व व्रते",
            AppLanguage.GU to "વર્ષના તહેવારો અને વ્રત",
            AppLanguage.BN to "বছরের উৎসব ও ব্রত",
            AppLanguage.ML to "വർഷത്തിലെ ഉത്സവങ്ങൾ",
            AppLanguage.OR_LANG to "ବର୍ଷର ପର୍ବ"
        ),
        "search_festivals_hint" to mapOf(
            AppLanguage.EN to "Search festivals, vrats, deities (e.g., Diwali, Ekadashi)...",
            AppLanguage.HI to "त्यौहार, व्रत, देवता खोजें (जैसे दीपावली, एकादशी)...",
            AppLanguage.TE to "పండుగలు, వ్రతాలు, దేవతలను వెతకండి... (ఉదా. దీపావళి, ఏకాదశి)",
            AppLanguage.TA to "பண்டிகைகள், விரதங்களை தேடுக... (எ.கா: தீபாவளி)",
            AppLanguage.KN to "ಹಬ್ಬಗಳು, ವ್ರತಗಳನ್ನು ಹುಡುಕಿ... (ಉದಾ: ದೀಪಾವಳಿ)",
            AppLanguage.MR to "सण, व्रत शोधा (उदा. दिवाळी)...",
            AppLanguage.GU to "તહેવાર, વ્રત શોધો (દા.ત. દિવાળી)...",
            AppLanguage.BN to "উৎসব ও ব্রত খুঁজুন (যেমন দীপাবলী)...",
            AppLanguage.ML to "ഉത്സവങ്ങൾ തിരയുക...",
            AppLanguage.OR_LANG to "ପର୍ବ ଖୋଜନ୍ତୁ..."
        ),
        "share_card" to mapOf(
            AppLanguage.EN to "Share Card",
            AppLanguage.HI to "कार्ड शेयर करें",
            AppLanguage.TE to "కార్డ్ షేర్ చేయండి",
            AppLanguage.TA to "கார்டு பகிரவும்",
            AppLanguage.KN to "ಕಾರ್ಡ್ ಶೇರ್ ಮಾಡಿ",
            AppLanguage.MR to "कार्ड शेअर करा",
            AppLanguage.GU to "કાર્ડ શેર કરો",
            AppLanguage.BN to "কার্ড শেয়ার করুন",
            AppLanguage.ML to "കാർഡ് പങ്കിടുക",
            AppLanguage.OR_LANG to "କାର୍ଡ ସେୟାର କରନ୍ତୁ"
        ),
        "view_details" to mapOf(
            AppLanguage.EN to "View Details",
            AppLanguage.HI to "विवरण देखें",
            AppLanguage.TE to "వివరాలు చూడండి",
            AppLanguage.TA to "விவரங்களை பார்க்க",
            AppLanguage.KN to "ವಿವರಗಳನ್ನು ನೋಡಿ",
            AppLanguage.MR to "सविस्तर पहा",
            AppLanguage.GU to "વિગતો જુઓ",
            AppLanguage.BN to "বিস্তারিত দেখুন",
            AppLanguage.ML to "വിവരങ്ങൾ കാണുക",
            AppLanguage.OR_LANG to "ବିବରଣୀ ଦେଖନ୍ତୁ"
        ),

        // Hindu Calendar Details
        "hindu_calendar_details" to mapOf(
            AppLanguage.EN to "HINDU CALENDAR DETAILS",
            AppLanguage.HI to "हिन्दू संवत्सर एवं मास विवरण",
            AppLanguage.TE to "హిందూ క్యాలెండర్ వివరాలు",
            AppLanguage.TA to "இந்து நாட்காட்டி விவரங்கள்",
            AppLanguage.KN to "ಹಿಂದೂ ಕ್ಯಾಲೆಂಡರ್ ವಿವರಗಳು",
            AppLanguage.MR to "हिंदू पंचांग तपशील",
            AppLanguage.GU to "હિન્દુ કેલેન્ડર વિગત",
            AppLanguage.BN to "হিন্দু পঞ্জিকার বিবরণ",
            AppLanguage.ML to "ഹിന്ദു കലണ്ടർ വിവരങ്ങൾ",
            AppLanguage.OR_LANG to "ହିନ୍ଦୁ ପଞ୍ଜିକା ବିବରଣୀ"
        ),
        "samvatsara" to mapOf(
            AppLanguage.EN to "Samvatsara (Year)",
            AppLanguage.HI to "संवत्सर (वर्ष)",
            AppLanguage.TE to "సంవత్సరం",
            AppLanguage.TA to "சம்வத்ஸரம் (ஆண்டு)",
            AppLanguage.KN to "ಸಂವತ್ಸರ (ವರ್ಷ)",
            AppLanguage.MR to "संवत्सर (वर्ष)",
            AppLanguage.GU to "સંવત્સર (વર્ષ)",
            AppLanguage.BN to "সংবৎসর (বছর)",
            AppLanguage.ML to "സംവത്സരം (വർഷം)",
            AppLanguage.OR_LANG to "ସମ୍ବତ୍ସର (ବର୍ଷ)"
        ),
        "ayanam" to mapOf(
            AppLanguage.EN to "Ayanam",
            AppLanguage.HI to "अयन",
            AppLanguage.TE to "అయనం",
            AppLanguage.TA to "அயனம்",
            AppLanguage.KN to "ಅಯನ",
            AppLanguage.MR to "अयन",
            AppLanguage.GU to "અયન",
            AppLanguage.BN to "অয়ন",
            AppLanguage.ML to "അയനം",
            AppLanguage.OR_LANG to "ଅୟନ"
        ),
        "ritu" to mapOf(
            AppLanguage.EN to "Ritu (Season)",
            AppLanguage.HI to "ऋतु (मौसम)",
            AppLanguage.TE to "ఋతువు",
            AppLanguage.TA to "ருது (பருவகாலம்)",
            AppLanguage.KN to "ಋತು",
            AppLanguage.MR to "ऋतू",
            AppLanguage.GU to "ઋતુ",
            AppLanguage.BN to "ঋতু",
            AppLanguage.ML to "ഋതു (കാലം)",
            AppLanguage.OR_LANG to "ଋତୁ"
        ),
        "masa" to mapOf(
            AppLanguage.EN to "Masa (Lunar Month)",
            AppLanguage.HI to "मास (चांद्र मास)",
            AppLanguage.TE to "మాసం",
            AppLanguage.TA to "மாதம் (சந்திர மாதம்)",
            AppLanguage.KN to "ಮಾಸ",
            AppLanguage.MR to "मास (महिना)",
            AppLanguage.GU to "માસ (મહિનો)",
            AppLanguage.BN to "মাস",
            AppLanguage.ML to "മാസം",
            AppLanguage.OR_LANG to "ମାସ"
        ),
        "shaka_era" to mapOf(
            AppLanguage.EN to "Shaka Samvat",
            AppLanguage.HI to "शक संवत",
            AppLanguage.TE to "శక సంవత్",
            AppLanguage.TA to "சக சம்வத்",
            AppLanguage.KN to "ಶಕ ಸಂವತ್",
            AppLanguage.MR to "शक संवत",
            AppLanguage.GU to "શક સંવત",
            AppLanguage.BN to "শকাব্দ",
            AppLanguage.ML to "ശക വർഷം",
            AppLanguage.OR_LANG to "ଶକାବ୍ଦ"
        ),
        "vikram_era" to mapOf(
            AppLanguage.EN to "Vikram Samvat",
            AppLanguage.HI to "विक्रम संवत",
            AppLanguage.TE to "విక్రమ సంవత్",
            AppLanguage.TA to "விக்ரம் சம்வத்",
            AppLanguage.KN to "ವಿಕ್ರಮ ಸಂವತ್",
            AppLanguage.MR to "विक्रम संवत",
            AppLanguage.GU to "વિક્રમ સંવત",
            AppLanguage.BN to "বিক্রমাব্দ",
            AppLanguage.ML to "വിക്രം വർഷം",
            AppLanguage.OR_LANG to "ବିକ୍ରମାବ୍ଦ"
        ),

        // Complete Panchang Accordion
        "view_complete_panchang" to mapOf(
            AppLanguage.EN to "View Complete Panchang",
            AppLanguage.HI to "सम्पूर्ण पंचांग देखें",
            AppLanguage.TE to "సంపూర్ణ పంచాంగం చూడండి",
            AppLanguage.TA to "முழு பஞ்சாங்கத்தைப் பார்க்கவும்",
            AppLanguage.KN to "ಸಂಪೂರ್ಣ ಪಂಚಾಂಗ ವೀಕ್ಷಿಸಿ",
            AppLanguage.MR to "संपूर्ण पंचांग पहा",
            AppLanguage.GU to "સંપૂર્ણ પંચાંગ જુઓ",
            AppLanguage.BN to "সম্পূর্ণ পঞ্জিকা দেখুন",
            AppLanguage.ML to "പൂർണ്ണ പഞ്ചാംഗം കാണുക",
            AppLanguage.OR_LANG to "ସମ୍ପୂର୍ଣ୍ଣ ପଞ୍ଜିକା ଦେଖନ୍ତୁ"
        ),
        "hide_complete_panchang" to mapOf(
            AppLanguage.EN to "Hide Complete Panchang",
            AppLanguage.HI to "सम्पूर्ण पंचांग छिपाएं",
            AppLanguage.TE to "పంచాంగం దాచండి",
            AppLanguage.TA to "பஞ்சாங்கத்தை மறைக்கவும்",
            AppLanguage.KN to "ಪಂಚಾಂಗ ಮರೆಮಾಡಿ",
            AppLanguage.MR to "पंचांग लपवा",
            AppLanguage.GU to "પંચાંગ છુપાવો",
            AppLanguage.BN to "পঞ্জিকা লুকান",
            AppLanguage.ML to "പഞ്ചാംഗം മറയ്ക്കുക",
            AppLanguage.OR_LANG to "ପଞ୍ଜିକା ଲୁଚାନ୍ତୁ"
        ),
        "todays_festivals" to mapOf(
            AppLanguage.EN to "Today's Festivals & Observances",
            AppLanguage.HI to "आज के त्यौहार व व्रत",
            AppLanguage.TE to "నేటి పండుగలు & ఆచారాలు",
            AppLanguage.TA to "இன்றைய பண்டிகைகள் & விரதங்கள்",
            AppLanguage.KN to "ಇಂದಿನ ಹಬ್ಬಗಳು ಮತ್ತು ಆಚರಣೆಗಳು",
            AppLanguage.MR to "आजचे सण व व्रते",
            AppLanguage.GU to "આજના તહેવારો અને વ્રત",
            AppLanguage.BN to "আজকের উৎসব ও ব্রত",
            AppLanguage.ML to "ഇന്നത്തെ ഉത്സവങ്ങളും വ്രതങ്ങളും",
            AppLanguage.OR_LANG to "ଆଜିର ପର୍ବ ଓ ବ୍ରତ"
        ),
        "no_festivals_today" to mapOf(
            AppLanguage.EN to "No major festivals today. Regular devotional activities recommended.",
            AppLanguage.HI to "आज कोई मुख्य त्यौहार नहीं है। नियमित पूजा-अर्चना का विधान है।",
            AppLanguage.TE to "ఈ రోజు ప్రధాన పండుగలు లేవు. నిత్య పూజా కార్యక్రమములు శుభప్రదం.",
            AppLanguage.TA to "இன்று முக்கிய பண்டிகைகள் இல்லை. வழக்கமான வழிபாடுகள் செய்யலாம்.",
            AppLanguage.KN to "ಇಂದು ಯಾವುದೇ ಪ್ರಮುಖ ಹಬ್ಬಗಳಿಲ್ಲ. ನಿತ್ಯ ಪೂಜೆ ಮಂಗಳಕರ.",
            AppLanguage.MR to "आज कोणताही प्रमुख सण नाही. नित्य पूजा करावी.",
            AppLanguage.GU to "આજે કોઈ મુખ્ય તહેવાર નથી. નિયમિત પૂજા શુભ છે.",
            AppLanguage.BN to "আজ কোনো প্রধান উৎসব নেই। নিয়মিত নিত্যপূজা বিধেয়।",
            AppLanguage.ML to "ഇന്ന് പ്രധാന ഉത്സവങ്ങളില്ല. നിത്യപൂജ നടത്താം.",
            AppLanguage.OR_LANG to "ଆଜି କୌଣସି ପ୍ରମୁଖ ପର୍ବ ନାହିଁ। ନିତ୍ୟ ପୂଜା ଶୁଭ।"
        ),

        // Festivals Screen
        "search_festivals_hint" to mapOf(
            AppLanguage.EN to "Search festivals, vrats, deities... (e.g. Diwali, Ekadashi)",
            AppLanguage.HI to "त्यौहार, व्रत, देवता खोजें... (जैसे दिवाली, एकादशी)",
            AppLanguage.TE to "పండుగలు, వ్రతాలు, దేవతలను వెతకండి... (ఉదా. దీపావళి, ఏకాదశి)",
            AppLanguage.TA to "பண்டிகைகள், விரதங்கள், தெய்வங்களைத் தேடுங்கள்... (எ.கா. தீபாவளி)",
            AppLanguage.KN to "ಹಬ್ಬಗಳು, ವ್ರತಗಳು, ದೇವತೆಗಳನ್ನು ಹುಡುಕಿ... (ಉದಾ: ದೀಪಾವಳಿ, ಏಕಾದಶಿ)",
            AppLanguage.MR to "सण, व्रते, देवता शोधा... (उदा. दिवाळी, एकादशी)",
            AppLanguage.GU to "તહેવારો, વ્રત, દેવતાઓ શોધો... (દા.ત. દિવાળી, એકાદશી)",
            AppLanguage.BN to "উৎসব, ব্রত, দেবতা অনুসন্ধান করুন... (যেমন দীপাবলি)",
            AppLanguage.ML to "ഉത്സവങ്ങൾ, വ്രതങ്ങൾ തിരയുക... (ഉദാ: ദീപാവലി)",
            AppLanguage.OR_LANG to "ପର୍ବ, ବ୍ରତ, ଦେବତା ଖୋଜନ୍ତୁ... (ଯଥା ଦୀପାବଳି)"
        ),
        "festivals_found_label" to mapOf(
            AppLanguage.EN to "festivals & vrats in",
            AppLanguage.HI to "त्यौहार एवं व्रत वर्ष",
            AppLanguage.TE to "సంవత్సరంలో పండుగలు & వ్రతాలు",
            AppLanguage.TA to "ஆண்டின் பண்டிகைகள் & விரதங்கள்",
            AppLanguage.KN to "ವರ್ಷದ ಹಬ್ಬಗಳು ಮತ್ತು ವ್ರತಗಳು",
            AppLanguage.MR to "वर्षातील सण व व्रते",
            AppLanguage.GU to "વર્ષના તહેવારો અને વ્રત",
            AppLanguage.BN to "বছরের উৎসব ও ব্রত",
            AppLanguage.ML to "വർഷത്തിലെ ഉത്സവങ്ങളും വ്രതങ്ങളും",
            AppLanguage.OR_LANG to "ବର୍ଷର ପର୍ବ ଓ ବ୍ରତ"
        ),
        "view_details" to mapOf(
            AppLanguage.EN to "View Details",
            AppLanguage.HI to "विवरण देखें",
            AppLanguage.TE to "వివరాలు చూడండి",
            AppLanguage.TA to "விவரங்கள்",
            AppLanguage.KN to "ವಿವರಗಳು",
            AppLanguage.MR to "तपशील पहा",
            AppLanguage.GU to "વિગત જુઓ",
            AppLanguage.BN to "বিবরণ দেখুন",
            AppLanguage.ML to "വിശദാംശങ്ങൾ",
            AppLanguage.OR_LANG to "ବିବରଣୀ ଦେଖନ୍ତୁ"
        ),
        "share_card" to mapOf(
            AppLanguage.EN to "Share Card",
            AppLanguage.HI to "कार्ड साझा करें",
            AppLanguage.TE to "కార్డ్ షేర్ చేయండి",
            AppLanguage.TA to "பகிர்",
            AppLanguage.KN to "ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.MR to "शेअर करा",
            AppLanguage.GU to "શેર કરો",
            AppLanguage.BN to "শেয়ার করুন",
            AppLanguage.ML to "പങ്കുവെക്കുക",
            AppLanguage.OR_LANG to "ସେୟାର କରନ୍ତୁ"
        ),

        // Muhurtha Screen
        "find_auspicious_muhurtha" to mapOf(
            AppLanguage.EN to "FIND AUSPICIOUS MUHURTHA",
            AppLanguage.HI to "शुभ मुहूर्त खोजें",
            AppLanguage.TE to "శుభ ముహూర్తం కనుగొనండి",
            AppLanguage.TA to "சுப முகூர்த்தம் காண்க",
            AppLanguage.KN to "ಶುಭ ಮುಹೂರ್ತ ಹುಡುಕಿ",
            AppLanguage.MR to "शुभ मुहूर्त शोधा",
            AppLanguage.GU to "શુભ મુહૂર્ત શોધો",
            AppLanguage.BN to "শুভ মুহূর্ত খুঁজুন",
            AppLanguage.ML to "ശുഭ മുഹൂർത്തം കണ്ടെത്തുക",
            AppLanguage.OR_LANG to "ଶୁଭ ମୁହୂର୍ତ୍ତ ଖୋଜନ୍ତୁ"
        ),
        "select_purpose_ceremony" to mapOf(
            AppLanguage.EN to "Select Purpose / Ceremony:",
            AppLanguage.HI to "कार्य / संस्कार चुनें:",
            AppLanguage.TE to "కార్యం / సంస్కారాన్ని ఎంచుకోండి:",
            AppLanguage.TA to "சுப காரியம் / சடங்கைத் தேர்ந்தெடுக்கவும்:",
            AppLanguage.KN to "ಕಾರ್ಯ / ಸಂಸ್ಕಾರ ಆಯ್ಕೆಮಾಡಿ:",
            AppLanguage.MR to "कार्य / संस्कार निवडा:",
            AppLanguage.GU to "કાર્ય / સંસ્કાર પસંદ કરો:",
            AppLanguage.BN to "অনুষ্ঠান / সংস্কার নির্বাচন করুন:",
            AppLanguage.ML to "ചടങ്ങ് തിരഞ്ഞെടുക്കുക:",
            AppLanguage.OR_LANG to "ସଂସ୍କାର ଚୟନ କରନ୍ତୁ:"
        ),
        "range" to mapOf(
            AppLanguage.EN to "Range:",
            AppLanguage.HI to "अवधि:",
            AppLanguage.TE to "పరిధి:",
            AppLanguage.TA to "கால அளவு:",
            AppLanguage.KN to "ಅವಧಿ:",
            AppLanguage.MR to "कालावधी:",
            AppLanguage.GU to "સમયગાળો:",
            AppLanguage.BN to "সময়সীমা:",
            AppLanguage.ML to "കാലപരിധി:",
            AppLanguage.OR_LANG to "ସମୟ ସୀମା:"
        ),
        "favorable_window" to mapOf(
            AppLanguage.EN to "Favorable Window:",
            AppLanguage.HI to "शुभ समय सीमा:",
            AppLanguage.TE to "శుభ సమయం:",
            AppLanguage.TA to "சுப நேரம்:",
            AppLanguage.KN to "ಶುಭ ಸಮಯ:",
            AppLanguage.MR to "शुभ वेळ:",
            AppLanguage.GU to "શુભ સમય:",
            AppLanguage.BN to "অনুকূল সময়সীমা:",
            AppLanguage.ML to "അനുകൂല സമയം:",
            AppLanguage.OR_LANG to "ଶୁଭ ସମୟ:"
        ),
        "favorable_factors" to mapOf(
            AppLanguage.EN to "Favorable Factors:",
            AppLanguage.HI to "अनुकूल योग व प्रभाव:",
            AppLanguage.TE to "అనుకూల అంశాలు:",
            AppLanguage.TA to "அனுகூல காரணிகள்:",
            AppLanguage.KN to "ಅನುಕೂಲ ಅಂಶಗಳು:",
            AppLanguage.MR to "अनुकूल घटक:",
            AppLanguage.GU to "અનુકૂળ પરિબળો:",
            AppLanguage.BN to "অনুকূল বিষয়সমূহ:",
            AppLanguage.ML to "അനുകൂല ഘടകങ്ങൾ:",
            AppLanguage.OR_LANG to "ଅନୁକୂଳ କାରକ:"
        ),
        "things_to_avoid" to mapOf(
            AppLanguage.EN to "Things to Avoid / Cautions:",
            AppLanguage.HI to "सावधानी एवं वर्जित काल:",
            AppLanguage.TE to "జాగ్రత్తలు / వర్జ్య సమయం:",
            AppLanguage.TA to "தவிர்க்க வேண்டியவை / எச்சரிக்கை:",
            AppLanguage.KN to "ಎಚ್ಚರಿಕೆ / ವರ್ಜ್ಯ ಸಮಯ:",
            AppLanguage.MR to "सावधगिरी आणि वर्ज्य वेळ:",
            AppLanguage.GU to "સાવચેતી અને વર્જ્ય સમય:",
            AppLanguage.BN to "বর্জ্য ও সতর্কতা:",
            AppLanguage.ML to "ശ്രദ്ധിക്കേണ്ട കാര്യങ്ങൾ:",
            AppLanguage.OR_LANG to "ବର୍ଜିତ ସମୟ ଓ ସାବଧାନତା:"
        ),

        // Dialogs & Actions
        "select_location" to mapOf(
            AppLanguage.EN to "Select City / Location",
            AppLanguage.HI to "स्थान / नगर चुनें",
            AppLanguage.TE to "నగరం / స్థలాన్ని ఎంచుకోండి",
            AppLanguage.TA to "நகரம் / இடத்தை தேர்ந்தெடுக்கவும்",
            AppLanguage.KN to "ನಗರ / ಸ್ಥಳ ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.MR to "शहर / स्थान निवडा",
            AppLanguage.GU to "શહેર / સ્થળ પસંદ કરો",
            AppLanguage.BN to "শহর / স্থান নির্বাচন করুন",
            AppLanguage.ML to "നഗരം തിരഞ്ഞെടുക്കുക",
            AppLanguage.OR_LANG to "ନଗର ଚୟନ କରନ୍ତୁ"
        ),
        "select_tradition" to mapOf(
            AppLanguage.EN to "Select Regional Tradition",
            AppLanguage.HI to "क्षेत्रीय पंचांग परंपरा चुनें",
            AppLanguage.TE to "ప్రాంతీయ పంచాంగ సంప్రదాయాన్ని ఎంచుకోండి",
            AppLanguage.TA to "பிராந்திய பஞ்சாங்க மரபை தேர்ந்தெடுக்கவும்",
            AppLanguage.KN to "ಪ್ರಾದೇಶಿಕ ಪಂಚಾಂಗ ಸಂಪ್ರದಾಯ ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.MR to "प्रादेशिक पंचांग परंपरा निवडा",
            AppLanguage.GU to "પ્રાદેશિક પંચાંગ પરંપરા પસંદ કરો",
            AppLanguage.BN to "আঞ্চলিক পঞ্জিকা ঐতিহ্য নির্বাচন করুন",
            AppLanguage.ML to "പഞ്ചാംഗ പാരമ്പര്യം തിരഞ്ഞെടുക്കുക",
            AppLanguage.OR_LANG to "ପଞ୍ଜିକା ପରମ୍ପରା ଚୟନ କରନ୍ତୁ"
        ),
        "select_language" to mapOf(
            AppLanguage.EN to "Select Language",
            AppLanguage.HI to "भाषा चुनें",
            AppLanguage.TE to "భాషను ఎంచుకోండి",
            AppLanguage.TA to "மொழியைத் தேர்ந்தெடுக்கவும்",
            AppLanguage.KN to "ಭಾಷೆ ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.MR to "भाषा निवडा",
            AppLanguage.GU to "ભાષા પસંદ કરો",
            AppLanguage.BN to "ভাষা নির্বাচন করুন",
            AppLanguage.ML to "ഭാഷ തിരഞ്ഞെടുക്കുക",
            AppLanguage.OR_LANG to "ଭାଷା ଚୟନ କରନ୍ତୁ"
        ),
        "close" to mapOf(
            AppLanguage.EN to "Close",
            AppLanguage.HI to "बंद करें",
            AppLanguage.TE to "మూసివేయి",
            AppLanguage.TA to "மூடு",
            AppLanguage.KN to "ಮುಚ್ಚಿ",
            AppLanguage.MR to "बंद करा",
            AppLanguage.GU to "બંધ કરો",
            AppLanguage.BN to "বন্ধ করুন",
            AppLanguage.ML to "അടയ്ക്കുക",
            AppLanguage.OR_LANG to "ବନ୍ଦ କରନ୍ତୁ"
        ),
        "copy_text" to mapOf(
            AppLanguage.EN to "Copy Text",
            AppLanguage.HI to "पाठ कॉपी करें",
            AppLanguage.TE to "కాపీ చేయండి",
            AppLanguage.TA to "நகலெடு",
            AppLanguage.KN to "ನಕಲಿಸಿ",
            AppLanguage.MR to "कॉपी करा",
            AppLanguage.GU to "કૉપિ કરો",
            AppLanguage.BN to "কপি করুন",
            AppLanguage.ML to "പകർപ്പുക",
            AppLanguage.OR_LANG to "କପି କରନ୍ତୁ"
        ),
        "share_with_friends" to mapOf(
            AppLanguage.EN to "Share with Friends",
            AppLanguage.HI to "मित्रों के साथ साझा करें",
            AppLanguage.TE to "స్నేహితులతో పంచుకోండి",
            AppLanguage.TA to "நண்பர்களுடன் பகிரவும்",
            AppLanguage.KN to "ಸ್ನೇಹಿತರೊಂದಿಗೆ ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.MR to "मित्रांसोबत शेअर करा",
            AppLanguage.GU to "મિત્રો સાથે શેર કરો",
            AppLanguage.BN to "বন্ধুদের সাথে শেয়ার করুন",
            AppLanguage.ML to "സുഹൃത്തുക്കളുമായി പങ്കിടുക",
            AppLanguage.OR_LANG to "ସାଙ୍ଗମାନଙ୍କ ସହ ସେୟାର କରନ୍ତୁ"
        ),
        "todays_special_occasion" to mapOf(
            AppLanguage.EN to "Today's Festival / Special Occasion",
            AppLanguage.HI to "आज का पर्व / विशेष अवसर",
            AppLanguage.TE to "ఈ రోజు పండుగ / ప్రత్యేక విశేషం",
            AppLanguage.TA to "இன்றைய பண்டிகை / சிறப்பு நிகழ்வு",
            AppLanguage.KN to "ಇಂದಿನ ಹಬ್ಬ / ವಿಶೇಷ ಆಚರಣೆ",
            AppLanguage.MR to "आजचा सण / विशेष प्रसंग",
            AppLanguage.GU to "આજનો તહેવાર / વિશેષ પ્રસંગ",
            AppLanguage.BN to "আজকের উৎসব / বিশেষ উপলক্ষ",
            AppLanguage.ML to "ഇന്നത്തെ ഉത്സവം / വിശേഷം",
            AppLanguage.OR_LANG to "ଆଜିର ପର୍ବ / ବିଶେଷ ଉତ୍ସବ"
        ),
        "major_festival" to mapOf(
            AppLanguage.EN to "Major Festival",
            AppLanguage.HI to "महापर्व",
            AppLanguage.TE to "ప్రధాన పండుగ",
            AppLanguage.TA to "முக்கிய பண்டிகை",
            AppLanguage.KN to "ಪ್ರಮುಖ ಹಬ್ಬ",
            AppLanguage.MR to "प्रमुख सण",
            AppLanguage.GU to "મુખ્ય તહેવાર",
            AppLanguage.BN to "প্রধান উৎসব",
            AppLanguage.ML to "പ്രധാന ഉത്സവം",
            AppLanguage.OR_LANG to "ପ୍ରମୁଖ ପର୍ବ"
        ),
        "share_festive_card" to mapOf(
            AppLanguage.EN to "Share Festive Card",
            AppLanguage.HI to "शुभकामना कार्ड भेजें",
            AppLanguage.TE to "శుభాకాంక్షల కార్డు పంపండి",
            AppLanguage.TA to "வாழ்த்து அட்டை பகிரவும்",
            AppLanguage.KN to "ಶುಭಾಶಯ ಕಾರ್ಡ್ ಕಳುಹಿಸಿ",
            AppLanguage.MR to "शुभेच्छा कार्ड पाठवा",
            AppLanguage.GU to "શુભેચ્છા કાર્ડ મોકલો",
            AppLanguage.BN to "শুভেচ্ছা কার্ড পাঠান",
            AppLanguage.ML to "ആശംസാ കാർഡ് അയക്കുക",
            AppLanguage.OR_LANG to "ଶୁଭେଚ୍ଛା କାର୍ଡ ପଠାନ୍ତୁ"
        ),
        "translation_meaning" to mapOf(
            AppLanguage.EN to "Translation & Meaning:",
            AppLanguage.HI to "भावार्थ एवं अर्थ:",
            AppLanguage.TE to "తాత్పర్యం & భావార్థం:",
            AppLanguage.TA to "பொருள் & விளக்கம்:",
            AppLanguage.KN to "ಅರ್ಥ ಮತ್ತು ವಿವರಣೆ:",
            AppLanguage.MR to "अर्थ आणि महत्त्व:",
            AppLanguage.GU to "અર્થ અને મહત્વ:",
            AppLanguage.BN to "অর্থ ও তাৎপর্য:",
            AppLanguage.ML to "അർത്ഥവും പ്രാധാന്യവും:",
            AppLanguage.OR_LANG to "ଅର୍ଥ ଓ ତାତ୍ପର୍ଯ୍ୟ:"
        ),
        "significance_benefits" to mapOf(
            AppLanguage.EN to "Spiritual Significance & Benefits:",
            AppLanguage.HI to "आध्यात्मिक महत्व एवं फल:",
            AppLanguage.TE to "ఆధ్యాత్మిక విశేషం & ఫలితాలు:",
            AppLanguage.TA to "ஆன்மீக சிறப்பு & பலன்கள்:",
            AppLanguage.KN to "ಆಧ್ಯಾತ್ಮಿಕ ಮಹತ್ವ ಮತ್ತು ಫಲ:",
            AppLanguage.MR to "आध्यात्मिक महत्त्व आणि लाभ:",
            AppLanguage.GU to "આધ્યાત્મિક મહત્વ અને લાભ:",
            AppLanguage.BN to "আধ্যাত্মিক তাৎপর্য ও ফল:",
            AppLanguage.ML to "ആത്മീയ പ്രാധാന്യവും ഫലങ്ങളും:",
            AppLanguage.OR_LANG to "ଆଧ୍ୟାତ୍ମିକ ମହତ୍ତ୍ୱ ଓ ଫଳ:"
        ),
        "event_title" to mapOf(
            AppLanguage.EN to "Event Title",
            AppLanguage.HI to "प्रसंग का शीर्षक",
            AppLanguage.TE to "వేడుక శీర్షిక",
            AppLanguage.TA to "நிகழ்வின் தலைப்பு",
            AppLanguage.KN to "ಕಾರ್ಯಕ್ರಮದ ಶೀರ್ಷಿಕೆ",
            AppLanguage.MR to "कार्यक्रमाचे शीर्षक",
            AppLanguage.GU to "કાર્યક્રમનું શીર્ષક",
            AppLanguage.BN to "অনুষ্ঠানের শিরোনাম",
            AppLanguage.ML to "ചടങ്ങിന്റെ പേര്",
            AppLanguage.OR_LANG to "କାର୍ଯ୍ୟକ୍ରମର ଶୀର୍ଷକ"
        ),
        "family_member_name" to mapOf(
            AppLanguage.EN to "Family Member Name",
            AppLanguage.HI to "परिवार के सदस्य का नाम",
            AppLanguage.TE to "కుటుంబ సభ్యుని పేరు",
            AppLanguage.TA to "குடும்ப உறுப்பினர் பெயர்",
            AppLanguage.KN to "ಕುಟುಂಬದ ಸದಸ್ಯರ ಹೆಸರು",
            AppLanguage.MR to "कुटुंबातील सदस्याचे नाव",
            AppLanguage.GU to "પરિવારના સભ્યનું નામ",
            AppLanguage.BN to "পরিবারের সদস্যের নাম",
            AppLanguage.ML to "കുടുംബാംഗത്തിന്റെ പേര്",
            AppLanguage.OR_LANG to "ପରିବାର ସଦସ୍ୟଙ୍କ ନାମ"
        ),
        "traditional_tithi_vrat" to mapOf(
            AppLanguage.EN to "Traditional Tithi / Vrat",
            AppLanguage.HI to "पारंपरिक तिथि / व्रत",
            AppLanguage.TE to "సాంప్రదాయ తిథి / వ్రతం",
            AppLanguage.TA to "பாரம்பரிய திதி / விரதம்",
            AppLanguage.KN to "ಸಾಂಪ್ರದಾಯಿಕ ತಿಥಿ / ವ್ರತ",
            AppLanguage.MR to "पारंपरिक तिथी / व्रत",
            AppLanguage.GU to "પરંપરાગત તિથિ / વ્રત",
            AppLanguage.BN to "ঐতিহ্যবাহী তিথি / ব্রত",
            AppLanguage.ML to "പരമ്പരാഗത തിഥി / വ്രതം",
            AppLanguage.OR_LANG to "ପାରମ୍ପରିକ ତିଥି / ବ୍ରତ"
        ),
        "historical_context_label" to mapOf(
            AppLanguage.EN to "Historical Context & Origin:",
            AppLanguage.HI to "ऐतिहासिक पृष्ठभूमि एवं उत्पत्ति:",
            AppLanguage.TE to "చారిత్రక నేపథ్యం & ఆవిర్భావ కథ:",
            AppLanguage.TA to "வரலாற்றுப் பின்னணி & பிறப்பு:",
            AppLanguage.KN to "ಐತಿಹಾಸಿಕ ಹಿನ್ನೆಲೆ ಮತ್ತು ಉಗಮ:",
            AppLanguage.MR to "ऐतिहासिक पार्श्वभूमी:",
            AppLanguage.GU to "ઐતિહાસિક પૃષ્ઠભૂમિ:",
            AppLanguage.BN to "ঐতিহাসিক প্রেক্ষাপট:",
            AppLanguage.ML to "ചരിത്ര പശ്ചാത്തലം:",
            AppLanguage.OR_LANG to "ଐତିହାସିକ ପୃଷ୍ଠଭୂମି:"
        ),
        "translation_meaning" to mapOf(
            AppLanguage.EN to "Translation & Meaning:",
            AppLanguage.HI to "अनुवाद एवं अर्थ:",
            AppLanguage.TE to "అనువాదం & భావం:",
            AppLanguage.TA to "மொழிபெயர்ப்பு & பொருள்:",
            AppLanguage.KN to "ಅನುವಾದ ಮತ್ತು ಅರ್ಥ:",
            AppLanguage.MR to "भाषांतर आणि अर्थ:",
            AppLanguage.GU to "અનુવાદ અને અર્થ:",
            AppLanguage.BN to "অনুবাদ ও অর্থ:",
            AppLanguage.ML to "വിവർത്തനവും അർത്ഥവും:",
            AppLanguage.OR_LANG to "ଅନୁବାଦ ଓ ଅର୍ଥ:"
        ),
        "spiritual_significance_benefits" to mapOf(
            AppLanguage.EN to "Spiritual Significance & Benefits:",
            AppLanguage.HI to "आध्यात्मिक महत्व एवं लाभ:",
            AppLanguage.TE to "ఆధ్యాత్మిక విశిష్టత & ఫలితాలు:",
            AppLanguage.TA to "ஆன்மீக முக்கியத்துவம் & நன்மைகள்:",
            AppLanguage.KN to "ಆಧ್ಯಾತ್ಮಿಕ ಮಹತ್ವ ಮತ್ತು ಫಲಗಳು:",
            AppLanguage.MR to "आध्यात्मिक महत्त्व आणि लाभ:",
            AppLanguage.GU to "આધ્યાત્મિક મહત્વ અને લાભ:",
            AppLanguage.BN to "আধ্যাত্মিক তাৎপর্য ও সুফল:",
            AppLanguage.ML to "ആത്മീയ പ്രാധാന്യവും ഗുണങ്ങളും:",
            AppLanguage.OR_LANG to "ଆଧ୍ୟାତ୍ମିକ ମହତ୍ତ୍ୱ ଓ ଫଳ:"
        ),
        "add_event" to mapOf(
            AppLanguage.EN to "Add Event",
            AppLanguage.HI to "प्रसंग जोड़ें",
            AppLanguage.TE to "వేడుక జోడించండి",
            AppLanguage.TA to "நிகழ்வு சேர்க்க",
            AppLanguage.KN to "ಪ್ರಸಂಗ ಸೇರಿಸಿ",
            AppLanguage.MR to "प्रसंग जोडा",
            AppLanguage.GU to "પ્રસંગ ઉમેરો",
            AppLanguage.BN to "অনুষ্ঠান যোগ করুন",
            AppLanguage.ML to "ചടങ്ങുകൾ ചേർക്കുക",
            AppLanguage.OR_LANG to "ଉତ୍ସବ ଯୋଡ଼ନ୍ତୁ"
        ),
        "no_family_events" to mapOf(
            AppLanguage.EN to "No family events added yet. Tap 'Add Event' above.",
            AppLanguage.HI to "कोई पारिवारिक प्रसंग नहीं जोड़ा गया। ऊपर 'प्रसंग जोड़ें' पर टैप करें।",
            AppLanguage.TE to "కుటుంబ వేడుకలు ఏవీ లేవు. పైన ఉన్న 'వేడుక జోడించండి' నొక్కండి.",
            AppLanguage.TA to "குடும்ப நிகழ்வுகள் எதுவும் இல்லை. மேலே உள்ள 'நிகழ்வு சேர்க்க' தொடவும்.",
            AppLanguage.KN to "ಯಾವುದೇ ಕೌಟುಂಬಿಕ ಪ್ರಸಂಗಗಳಿಲ್ಲ. 'ಪ್ರಸಂಗ ಸೇರಿಸಿ' ಒತ್ತಿ.",
            AppLanguage.MR to "कोणतेही कौटुंबिक प्रसंग जोडलेले नाहीत.",
            AppLanguage.GU to "કોઈ પારિવારિક પ્રસંગ ઉમેરેલ નથી.",
            AppLanguage.BN to "কোনো পারিবারিক ব্রত যোগ করা হয়নি।",
            AppLanguage.ML to "കുടുംബ ചടങ്ങുകൾ ഒന്നുമില്ല.",
            AppLanguage.OR_LANG to "କୌଣସି ପାରିବାରିକ ଉତ୍ସବ ଯୋଡ଼ାଯାଇନାହିଁ।"
        ),
        "member_label" to mapOf(
            AppLanguage.EN to "Member",
            AppLanguage.HI to "सदस्य",
            AppLanguage.TE to "సభ్యులు",
            AppLanguage.TA to "உறுப்பினர்",
            AppLanguage.KN to "ಸದಸ್ಯ",
            AppLanguage.MR to "सदस्य",
            AppLanguage.GU to "સભ્ય",
            AppLanguage.BN to "সদস্য",
            AppLanguage.ML to "അംഗം",
            AppLanguage.OR_LANG to "ସଦସ୍ୟ"
        ),
        "bell_chime" to mapOf(
            AppLanguage.EN to "Bell Chime 🔔",
            AppLanguage.HI to "घंटी ध्वनि 🔔",
            AppLanguage.TE to "గంట నాదం 🔔",
            AppLanguage.TA to "மணி ஓசை 🔔",
            AppLanguage.KN to "ಘಂಟಾನಾದ 🔔",
            AppLanguage.MR to "घंटा नाद 🔔",
            AppLanguage.GU to "ઘંટ નાદ 🔔",
            AppLanguage.BN to "ঘণ্টাধ্বনি 🔔",
            AppLanguage.ML to "മണിനാദം 🔔",
            AppLanguage.OR_LANG to "ଘଣ୍ଟା ଧ୍ୱନି 🔔"
        ),
        "summary_label" to mapOf(
            AppLanguage.EN to "Summary:",
            AppLanguage.HI to "संक्षिप्त विवरण:",
            AppLanguage.TE to "సారాంశం:",
            AppLanguage.TA to "சுருக்கம்:",
            AppLanguage.KN to "ಸಾರಾಂಶ:",
            AppLanguage.MR to "थोडक्यात:",
            AppLanguage.GU to "સારાંશ:",
            AppLanguage.BN to "সংক্ষিপ্ত বিবরণ:",
            AppLanguage.ML to "സംഗ്രഹം:",
            AppLanguage.OR_LANG to "ସଂକ୍ଷିପ୍ତ ବିବରଣୀ:"
        ),
        "significance_label" to mapOf(
            AppLanguage.EN to "Significance & Spiritual Essence:",
            AppLanguage.HI to "महत्व एवं आध्यात्मिक सार:",
            AppLanguage.TE to "విశిష్టత & ఆధ్యాత్మిక అంతరార్థం:",
            AppLanguage.TA to "முக்கியத்துவம் & ஆன்மீக விளக்கம்:",
            AppLanguage.KN to "ಮಹತ್ವ ಮತ್ತು ಆಧ್ಯಾತ್ಮಿಕ ಸಾರ:",
            AppLanguage.MR to "महत्त्व आणि आध्यात्मिक सार:",
            AppLanguage.GU to "મહત્વ અને આધ્યાત્મિક સાર:",
            AppLanguage.BN to "তাৎপর্য ও আধ্যাত্মিক মর্মার্থ:",
            AppLanguage.ML to "പ്രാധാന്യവും ആത്മീയ തത്വവും:",
            AppLanguage.OR_LANG to "ମହତ୍ତ୍ୱ ଓ ଆଧ୍ୟାତ୍ମିକ ସାର:"
        ),
        "fasting_rules_label" to mapOf(
            AppLanguage.EN to "Vrat & Fasting Rules:",
            AppLanguage.HI to "व्रत एवं उपवास के नियम:",
            AppLanguage.TE to "వ్రతము & ఉపవాస నియమాలు:",
            AppLanguage.TA to "விரத விதிகள்:",
            AppLanguage.KN to "ವ್ರತ ಮತ್ತು ಉಪವಾಸ ನಿಯಮಗಳು:",
            AppLanguage.MR to "व्रत आणि उपवासाचे नियम:",
            AppLanguage.GU to "વ્રત અને ઉપવાસના નિયમો:",
            AppLanguage.BN to "ব্রত ও উপবাসের নিয়ম:",
            AppLanguage.ML to "വ്രതാനുഷ്ഠാന നിയമങ്ങൾ:",
            AppLanguage.OR_LANG to "ବ୍ରତ ଓ ଉପବାସ ନିୟମ:"
        ),
        "sacred_story_label" to mapOf(
            AppLanguage.EN to "Sacred Story / Katha:",
            AppLanguage.HI to "पवित्र कथा / प्रसंग:",
            AppLanguage.TE to "పుణ్య కథ / వ్రత కథ:",
            AppLanguage.TA to "புனிதக் கதை:",
            AppLanguage.KN to "ಪುಣ್ಯ ಕಥೆ:",
            AppLanguage.MR to "पवित्र कथा:",
            AppLanguage.GU to "પવિત્ર કથા:",
            AppLanguage.BN to "পবিত্র ব্রতকথা:",
            AppLanguage.ML to "വിശുദ്ധ കഥ:",
            AppLanguage.OR_LANG to "ପବିତ୍ର ବ୍ରତକଥା:"
        ),
        "mantra_label" to mapOf(
            AppLanguage.EN to "Auspicious Mantra / Shloka:",
            AppLanguage.HI to "शुभ मंत्र / श्लोक:",
            AppLanguage.TE to "శుభ మంత్రం / శ్లోకం:",
            AppLanguage.TA to "மந்திரம் / சுலோகம்:",
            AppLanguage.KN to "ಮಂತ್ರ / ಶ್ಲೋಕ:",
            AppLanguage.MR to "मंत्र / श्लोक:",
            AppLanguage.GU to "મંત્ર / શ્લોક:",
            AppLanguage.BN to "শুভ মন্ত্র / শ্লোক:",
            AppLanguage.ML to "മന്ത്രം / ശ്ലോകം:",
            AppLanguage.OR_LANG to "ଶୁଭ ମନ୍ତ୍ର / ଶ୍ଳୋକ:"
        ),
        "today_special_festival" to mapOf(
            AppLanguage.EN to "Today's Sacred Occasion / Festival",
            AppLanguage.HI to "आज का विशेष पर्व / प्रसंग",
            AppLanguage.TE to "ఈ రోజు ప్రత్యేక పండుగ / విశేషం",
            AppLanguage.TA to "இன்றைய சிறப்பு பண்டிகை / நிகழ்வு",
            AppLanguage.KN to "ಇಂದಿನ ವಿಶೇಷ ಹಬ್ಬ / ಆಚರಣೆ",
            AppLanguage.MR to "आजचा विशेष सण / प्रसंग",
            AppLanguage.GU to "આજનો વિશેષ તહેવાર / પ્રસંગ",
            AppLanguage.BN to "আজকের বিশেষ উৎসব / অনুষ্ঠান",
            AppLanguage.ML to "ഇന്നത്തെ വിശേഷ ഉത്സവം",
            AppLanguage.OR_LANG to "ଆଜିର ବିଶେଷ ପର୍ବ / ଉତ୍ସବ"
        ),
        "major_festival_badge" to mapOf(
            AppLanguage.EN to "MAJOR FESTIVAL",
            AppLanguage.HI to "महापर्व",
            AppLanguage.TE to "ప్రధాన పండుగ",
            AppLanguage.TA to "முக்கிய பண்டிகை",
            AppLanguage.KN to "ಪ್ರಮುಖ ಹಬ್ಬ",
            AppLanguage.MR to "प्रमुख सण",
            AppLanguage.GU to "મુખ્ય તહેવાર",
            AppLanguage.BN to "প্রধান উৎসব",
            AppLanguage.ML to "പ്രധാന ഉത്സവം",
            AppLanguage.OR_LANG to "ପ୍ରମୁଖ ପର୍ବ"
        )
    )

    fun translateFestivalWish(festivalTitle: String, deityTitle: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.TE -> "శ్రీ $deityTitle వారి దివ్య అనుగ్రహం మరియు ఆశీస్సులు మీపై ఎల్లప్పుడూ ఉండుగాక! మీకు మరియు మీ కుటుంబ సభ్యులకు $festivalTitle హార్దిక శుభాకాంక్షలు."
            AppLanguage.HI -> "भगवान $deityTitle की असीम कृपा और आशीर्वाद आपके जीवन में सुख, शांति और समृद्धि लाए। आपको और आपके परिवार को $festivalTitle की हार्दिक शुभकामनाएं।"
            AppLanguage.TA -> "ஸ்ரீ $deityTitle அவர்களின் தெய்வீக அருளால் உங்கள் வாழ்வில் அமைதியும் மகிழ்ச்சியும் நிலைக்கட்டும். உங்களுக்கும் உங்கள் குடும்பத்தினருக்கும் $festivalTitle நல்வாழ்த்துகள்."
            AppLanguage.KN -> "ಶ್ರೀ $deityTitle ರವರ ದಿವ್ಯ ಅನುಗ್ರಹವು ನಿಮ್ಮ ಬಾಳಿನಲ್ಲಿ ಸಂತೋಷವನ್ನು ತರಲಿ. ತಮಗೆ ಮತ್ತು ತಮ್ಮ ಕುಟುಂಬಕ್ಕೆ $festivalTitle ಶುಭಾಶಯಗಳು."
            AppLanguage.MR -> "भगवान $deityTitle यांच्या कृपेने आपल्या जीवनात सुख, शांती व आरोग्य लाभो. $festivalTitle च्या हार्दिक शुभेच्छा."
            AppLanguage.GU -> "ભગવાન $deityTitle ના આશીર્વાદથી આપના જીવનમાં સુખ અને શાંતિ રહે. આપને $festivalTitle ની હાર્દિક શુભેચ્છાઓ."
            AppLanguage.BN -> "শ্রী $deityTitle এর কৃপায় আপনার জীবন সুখ ও শান্তিতে ভরে উঠুক। আপনাকে ও আপনার পরিবারকে $festivalTitle এর আন্তরিক শুভেচ্ছা।"
            AppLanguage.ML -> "ശ്രീ $deityTitle ന്റെ അനുഗ്രഹം നിങ്ങളുടെ ജീവിതത്തിൽ ഐശ്വര്യവും സമാധാനവും നൽകട്ടെ. $festivalTitle ആശംസകൾ."
            AppLanguage.OR_LANG -> "ପ୍ରଭୁ $deityTitle ଙ୍କ ଅଶେଷ କୃପା ଆପଣଙ୍କ ଜୀବନରେ ସୁଖ ଓ ଶାନ୍ତି ଭରିଦେଉ। $festivalTitle ର ହାର୍ଦ୍ଦିକ ଶୁଭେଚ୍ଛା।"
            else -> "May the divine grace of $deityTitle illuminate your life with infinite blessings, inner peace, and good health on this sacred $festivalTitle."
        }
    }

    fun get(key: String, language: AppLanguage): String {
        return translations[key]?.get(language)
            ?: translations[key]?.get(AppLanguage.EN)
            ?: key
    }

    // --- Dynamic Terms Translators ---

    // 1. Tithi Name Translator
    fun translateTithi(tithiName: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return tithiName

        val cleanName = tithiName.trim()
        if (cleanName.contains("నవమి") || cleanName.contains("అష్టమి") || cleanName.contains("పాడ్యమి") || 
            cleanName.contains("విదియ") || cleanName.contains("తదియ") || cleanName.contains("చవితి") || 
            cleanName.contains("పంచమి") || cleanName.contains("షష్ఠి") || cleanName.contains("సప్తమి") || 
            cleanName.contains("దశమి") || cleanName.contains("ఏకాదశి") || cleanName.contains("ద్వాదశి") || 
            cleanName.contains("త్రయోదశి") || cleanName.contains("చతుర్దశి") || cleanName.contains("పౌర్ణమి") || 
            cleanName.contains("పూర్ణిమ") || cleanName.contains("అమావాస్య")) {
            return cleanName
        }

        val isShukla = cleanName.startsWith("Shukla", ignoreCase = true) || cleanName.contains("Shukla ", ignoreCase = true)
        val isKrishna = cleanName.startsWith("Krishna", ignoreCase = true) || cleanName.contains("Krishna ", ignoreCase = true)
        
        val baseName = cleanName
            .replace("Shukla", "", ignoreCase = true)
            .replace("Krishna", "", ignoreCase = true)
            .trim()

        val teMap = mapOf(
            "Pratipada" to "పాడ్యమి",
            "Prathama" to "పాడ్యమి",
            "Dwitiya" to "విదియ",
            "Tritiya" to "తదియ",
            "Chaturthi" to "చవితి",
            "Panchami" to "పంచమి",
            "Shashthi" to "షష్ఠి",
            "Sashti" to "షష్ఠి",
            "Saptami" to "సప్తమి",
            "Ashtami" to "అష్టమి",
            "Navami" to "నవమి",
            "Dashami" to "దశమి",
            "Ekadashi" to "ఏకాదశి",
            "Dwadashi" to "ద్వాదశి",
            "Trayodashi" to "త్రయోదశి",
            "Chaturdashi" to "చతుర్దశి",
            "Purnima" to "పూర్ణిమ (పౌర్ణమి)",
            "Pournami" to "పూర్ణిమ (పౌర్ణమి)",
            "Amavasya" to "అమావాస్య"
        )

        val hiMap = mapOf(
            "Pratipada" to "प्रतिपदा",
            "Dwitiya" to "द्वितीया",
            "Tritiya" to "तृतीया",
            "Chaturthi" to "चतुर्थी",
            "Panchami" to "पंचमी",
            "Shashthi" to "षष्ठी",
            "Saptami" to "सप्तमी",
            "Ashtami" to "अष्टमी",
            "Navami" to "नवमी",
            "Dashami" to "दशमी",
            "Ekadashi" to "एकादशी",
            "Dwadashi" to "द्वादशी",
            "Trayodashi" to "त्रयोदशी",
            "Chaturdashi" to "चतुर्दशी",
            "Purnima" to "पूर्णिमा",
            "Amavasya" to "अमावस्या"
        )

        val taMap = mapOf(
            "Pratipada" to "பிரதமை",
            "Dwitiya" to "துவிதியை",
            "Tritiya" to "திருதியை",
            "Chaturthi" to "சதுர்த்தி",
            "Panchami" to "பஞ்சமி",
            "Shashthi" to "சஷ்டி",
            "Saptami" to "சப்தமி",
            "Ashtami" to "அஷ்டமி",
            "Navami" to "நவமி",
            "Dashami" to "தசமி",
            "Ekadashi" to "ஏகாதசி",
            "Dwadashi" to "துவாதசி",
            "Trayodashi" to "திரயோதசி",
            "Chaturdashi" to "சதுர்தசி",
            "Purnima" to "பௌர்ணமி",
            "Amavasya" to "அமாவாசை"
        )

        val knMap = mapOf(
            "Pratipada" to "ಪಾಡ್ಯ (ಪ್ರತಿಪದೆ)",
            "Dwitiya" to "ಬಿದಿಗೆ (ದ್ವಿತೀಯ)",
            "Tritiya" to "ತದಿಗೆ (ತೃತೀಯ)",
            "Chaturthi" to "ಚೌತಿ (ಚತುರ್ಥಿ)",
            "Panchami" to "ಪಂಚಮಿ",
            "Shashthi" to "ಷಷ್ಠಿ",
            "Saptami" to "ಸಪ್ತಮಿ",
            "Ashtami" to "ಅಷ್ಟಮಿ",
            "Navami" to "ನವಮಿ",
            "Dashami" to "ದಶಮಿ",
            "Ekadashi" to "ಏಕಾದಶಿ",
            "Dwadashi" to "ದ್ವಾದಶಿ",
            "Trayodashi" to "ತ್ರಯೋದಶಿ",
            "Chaturdashi" to "ಚತುರ್ದಶಿ",
            "Purnima" to "ಹುಣ್ಣಿಮೆ (ಪೂರ್ಣಿಮಾ)",
            "Amavasya" to "ಅಮಾವಾಸ್ಯೆ"
        )

        val prefix = when {
            isShukla -> when (lang) {
                AppLanguage.TE -> "శుక్ల "
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "शुक्ल "
                AppLanguage.TA -> "சுக்ல "
                AppLanguage.KN -> "ಶುಕ್ಲ "
                else -> "Shukla "
            }
            isKrishna -> when (lang) {
                AppLanguage.TE -> "కృష్ణ "
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "कृष्ण "
                AppLanguage.TA -> "கிருஷ்ண "
                AppLanguage.KN -> "ಕೃಷ್ಣ "
                else -> "Krishna "
            }
            else -> ""
        }

        val translatedBase = when (lang) {
            AppLanguage.TE -> teMap[baseName] ?: baseName
            AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> hiMap[baseName] ?: baseName
            AppLanguage.TA -> taMap[baseName] ?: baseName
            AppLanguage.KN -> knMap[baseName] ?: baseName
            else -> hiMap[baseName] ?: baseName
        }

        return prefix + translatedBase
    }

    // 2. Nakshatra Name Translator
    fun translateNakshatra(nakshatraName: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return nakshatraName

        var base = nakshatraName.trim()
        var padaSuffix = ""

        if (base.contains("(") && base.endsWith(")")) {
            val startIndex = base.indexOf("(")
            val contents = base.substring(startIndex + 1, base.length - 1).trim()
            base = base.substring(0, startIndex).trim()

            val padaNum = contents.replace("Pada", "", ignoreCase = true).trim()
            padaSuffix = when (lang) {
                AppLanguage.TE -> " (${padaNum}వ పాదం)"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> " (चरण $padaNum)"
                AppLanguage.TA -> " (பாதம் $padaNum)"
                AppLanguage.KN -> " (ಪಾದ $padaNum)"
                else -> " (Pada $padaNum)"
            }
        }

        val teMap = mapOf(
            "Ashwini" to "అశ్విని", "Asvini" to "అశ్విని",
            "Bharani" to "భరణి",
            "Krittika" to "కృత్తిక", "Kritika" to "కృత్తిక",
            "Rohini" to "రోహిణి",
            "Mrigashira" to "మృగశిర", "Mrigashirsha" to "మృగశిర", "Mrigasira" to "మృగశిర",
            "Ardra" to "ఆరుద్ర", "Arudra" to "ఆరుద్ర",
            "Punarvasu" to "పునర్వసు", "Punavasu" to "పునర్వసు",
            "Pushya" to "పుష్యమి", "Pushyami" to "పుష్యమి",
            "Ashlesha" to "ఆశ్లేష", "Aslesha" to "ఆశ్లేష",
            "Magha" to "మఖ", "Makha" to "మఖ",
            "Purva Phalguni" to "పూర్వ ఫల్గుణి (పుబ్బ)", "PurvaPhalguni" to "పూర్వ ఫల్గుణి (పుబ్బ)", "Pubba" to "పుబ్బ",
            "Uttara Phalguni" to "ఉత్తర ఫల్గుణి (ఉత్తర)", "UttaraPhalguni" to "ఉత్తర ఫల్గుణి (ఉత్తర)", "Uttara" to "ఉత్తర",
            "Hasta" to "హస్త",
            "Chitra" to "చిత్త",
            "Swati" to "స్వాతి", "Svati" to "స్వాతి",
            "Vishakha" to "విశాఖ", "Visakha" to "విశాఖ",
            "Anuradha" to "అనూరాధ",
            "Jyeshtha" to "జ్యేష్ఠ", "Jyeshta" to "జ్యేష్ఠ",
            "Mula" to "మూల", "Moola" to "మూల",
            "Purva Ashadha" to "పూర్వాషాఢ", "Purvashada" to "పూర్వాషాఢ", "PurvaAshadha" to "పూర్వాషాఢ",
            "Uttara Ashadha" to "ఉత్తరాషాఢ", "Uttarashada" to "ఉత్తరాషాఢ", "UttaraAshadha" to "ఉత్తరాషాఢ",
            "Shravana" to "శ్రవణం", "Sravana" to "శ్రవణం",
            "Dhanishta" to "ధనిష్ఠ", "Dhanista" to "ధనిష్ఠ",
            "Shatabhisha" to "శతభిషం", "Satabhisha" to "శతభిషం", "Shatabhishak" to "శతభిషం",
            "Purva Bhadrapada" to "పూర్వాభాద్ర", "Purvabhadra" to "పూర్వాభాద్ర", "PurvaBhadrapada" to "పూర్వాభాద్ర",
            "Uttara Bhadrapada" to "ఉత్తరాభాద్ర", "Uttarabhadra" to "ఉత్తరాభాద్ర", "UttaraBhadrapada" to "ఉత్తరాభాద్ర",
            "Revati" to "రేవతి"
        )

        val hiMap = mapOf(
            "Ashwini" to "अश्विनी", "Bharani" to "भरणी", "Krittika" to "कृत्तिका", "Rohini" to "रोहिणी",
            "Mrigashira" to "मृगशिरा", "Ardra" to "आर्द्रा", "Punarvasu" to "पुनर्वसु", "Pushya" to "पुष्य",
            "Ashlesha" to "आश्लेषा", "Magha" to "मघा", "Purva Phalguni" to "पूर्वा फाल्गुनी",
            "Uttara Phalguni" to "उत्तरा फाल्गुनी", "Hasta" to "हस्त", "Chitra" to "चित्रा",
            "Swati" to "स्वाति", "Vishakha" to "विशाखा", "Anuradha" to "अनुराधा", "Jyeshtha" to "ज्येष्ठा",
            "Mula" to "मूल", "Purva Ashadha" to "पूर्वाषाढ़ा", "Uttara Ashadha" to "उत्तराषाढ़ा",
            "Shravana" to "श्रवण", "Dhanishta" to "धनिष्ठा", "Shatabhisha" to "शतभिषा",
            "Purva Bhadrapada" to "पूर्व भाद्रपद", "Uttara Bhadrapada" to "उत्तर भाद्रपद", "Revati" to "रेवती"
        )

        val taMap = mapOf(
            "Ashwini" to "அஸ்வினி", "Bharani" to "பரணி", "Krittika" to "கிருத்திகை", "Rohini" to "ரோகிணி",
            "Mrigashira" to "மிருகசீரிஷம்", "Ardra" to "திருவாதிரை", "Punarvasu" to "புனர்பூசம்", "Pushya" to "பூசம்",
            "Ashlesha" to "ஆயில்யம்", "Magha" to "மகம்", "Purva Phalguni" to "பூரம்",
            "Uttara Phalguni" to "உத்திரம்", "Hasta" to "ஹஸ்தம்", "Chitra" to "சித்திரை",
            "Swati" to "சுவாதி", "Vishakha" to "விசாகம்", "Anuradha" to "அனுஷம்", "Jyeshtha" to "கேட்டை",
            "Mula" to "மூலம்", "Purva Ashadha" to "பூராடம்", "Uttara Ashadha" to "உத்திராடம்",
            "Shravana" to "திருவோணம்", "Dhanishta" to "அவிட்டம்", "Shatabhisha" to "சதயம்",
            "Purva Bhadrapada" to "பூரட்டாதி", "Uttara Bhadrapada" to "உத்திரட்டாதி", "Revati" to "ரேவதி"
        )

        val knMap = mapOf(
            "Ashwini" to "ಅಶ್ವಿನಿ", "Bharani" to "ಭರಣಿ", "Krittika" to "ಕೃತ್ತಿಕಾ", "Rohini" to "ರೋಹಿಣಿ",
            "Mrigashira" to "ಮೃಗಶಿರ", "Ardra" to "ಆರಿದ್ರಾ", "Punarvasu" to "ಪುನರ್ವಸು", "Pushya" to "ಪುಷ್ಯ",
            "Ashlesha" to "ಆಶ್ಲೇಷಾ", "Magha" to "ಮಖಾ", "Purva Phalguni" to "ಪುಬ್ಬಾ",
            "Uttara Phalguni" to "ಉತ್ತರಾ", "Hasta" to "ಹಸ್ತಾ", "Chitra" to "ಚಿತ್ತಾ",
            "Swati" to "ಸ್ವಾತಿ", "Vishakha" to "ವಿಶಾಖಾ", "Anuradha" to "ಅನುರಾಧಾ", "Jyeshtha" to "ಜ್ಯೇಷ್ಠಾ",
            "Mula" to "ಮೂಲಾ", "Purva Ashadha" to "ಪೂರ್ವಾಷಾಢಾ", "Uttara Ashadha" to "ಉತ್ತರಾಷಾಢಾ",
            "Shravana" to "ಶ್ರವಣಾ", "Dhanishta" to "ಧನಿಷ್ಠಾ", "Shatabhisha" to "ಶತಭಿಷಾ",
            "Purva Bhadrapada" to "ಪೂರ್ವಾಭಾದ್ರಾ", "Uttara Bhadrapada" to "ಉತ್ತರಾಭಾದ್ರಾ", "Revati" to "ರೇವತಿ"
        )

        val translatedBase = when (lang) {
            AppLanguage.TE -> teMap[base] ?: base
            AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> hiMap[base] ?: base
            AppLanguage.TA -> taMap[base] ?: base
            AppLanguage.KN -> knMap[base] ?: base
            else -> hiMap[base] ?: base
        }

        return translatedBase + padaSuffix
    }

    // 3. Paksha Translator
    fun translatePaksha(paksha: String, lang: AppLanguage): String {
        return if (paksha.contains("Shukla", ignoreCase = true)) {
            when (lang) {
                AppLanguage.TE -> "శుక్ల పక్షం"
                AppLanguage.HI, AppLanguage.MR -> "शुक्ल पक्ष"
                AppLanguage.TA -> "சுக்ல பக்ஷம் (வளர்பிறை)"
                AppLanguage.KN -> "ಶುಕ್ಲ ಪಕ್ಷ"
                AppLanguage.GU -> "શુક્લ પક્ષ"
                AppLanguage.BN -> "শুক্লপক্ষ"
                AppLanguage.ML -> "ശുക്ലപക്ഷം"
                AppLanguage.OR_LANG -> "ଶୁକ୍ଲ ପକ୍ଷ"
                else -> "Shukla Paksha"
            }
        } else {
            when (lang) {
                AppLanguage.TE -> "కృష్ణ పక్షం"
                AppLanguage.HI, AppLanguage.MR -> "कृष्ण पक्ष"
                AppLanguage.TA -> "கிருஷ்ண பக்ஷம் (தேய்பிறை)"
                AppLanguage.KN -> "ಕೃಷ್ಣ ಪಕ್ಷ"
                AppLanguage.GU -> "કૃષ્ણ પક્ષ"
                AppLanguage.BN -> "কৃষ্ণপক্ষ"
                AppLanguage.ML -> "കൃഷ്ണപക്ഷം"
                AppLanguage.OR_LANG -> "କୃଷ୍ଣ ପକ୍ଷ"
                else -> "Krishna Paksha"
            }
        }
    }

    // 4. Weekday (Vara) Translator
    fun translateVara(dayOfWeek: DayOfWeek, lang: AppLanguage): String {
        val te = listOf("ఆదివారం", "సోమవారం", "మంగళవారం", "బుధవారం", "గురువారం", "శుక్రవారం", "శనివారం")
        val hi = listOf("रविवार", "सोमवार", "मंगलवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार")
        val ta = listOf("ஞாயிற்றுக்கிழமை", "திங்கட்கிழமை", "செவ்வாய்க்கிழமை", "புதன்கிழமை", "வியாழக்கிழமை", "வெள்ளிக்கிழமை", "சனிக்கிழமை")
        val kn = listOf("ಭಾನುವಾರ", "ಸೋಮವಾರ", "ಮಂಗಳವಾರ", "ಬುಧವಾರ", "ಗುರುವಾರ", "ಶುಕ್ರವಾರ", "ಶನಿವಾರ")

        val idx = if (dayOfWeek == DayOfWeek.SUNDAY) 0 else dayOfWeek.value

        return when (lang) {
            AppLanguage.TE -> te[idx]
            AppLanguage.HI, AppLanguage.MR -> hi[idx]
            AppLanguage.TA -> ta[idx]
            AppLanguage.KN -> kn[idx]
            else -> dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    // 5. Hindu Month (Masa) Translator
    fun translateMasa(masa: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return "$masa Masa"
        val cleanMasa = masa.replace("మాసం", "").replace("Month", "").replace("Masa", "").replace("మాస", "").replace("மாதம்", "").replace("मास", "").trim()
        val te = mapOf(
            "Chaitra" to "చైత్ర మాసం", "Vaishakha" to "వైశాఖ మాసం", "Jyeshtha" to "జ్యేష్ఠ మాసం", "Ashadha" to "ఆషాఢ మాసం",
            "Shravana" to "శ్రావణ మాసం", "Bhadrapada" to "భాద్రపద మాసం", "Ashwina" to "ఆశ్వయుజ మాసం", "Kartika" to "కార్తీక మాసం",
            "Margashirsha" to "మార్గశిర మాసం", "Pausha" to "పుష్య మాసం", "Pushya" to "పుష్య మాసం", "Magha" to "మాఘ మాసం", "Phalguna" to "ఫాల్గుణ మాసం",
            "చైత్రం" to "చైత్ర మాసం", "వైశాఖం" to "వైశాఖ మాసం", "జ్యేష్ఠం" to "జ్యేష్ఠం మాసం", "ఆషాఢం" to "ఆషాఢ మాసం",
            "శ్రావణం" to "శ్రావణ మాసం", "భాద్రపదం" to "భాద్రపద మాసం", "ఆశ్వయుజం" to "ఆశ్వయుజ మాసం", "కార్తీకం" to "కార్తీక మాసం",
            "మార్గశిరం" to "మార్గశిర మాసం", "పుష్యం" to "పుష్య మాసం", "మాఘం" to "మాఘ మాసం", "ఫాల్గుణం" to "ఫాల్గుణ మాసం",
            "చైత్ర" to "చైత్ర మాసం", "వైశాఖ" to "వైశాఖ మాసం", "జ్యేష్ఠ" to "జ్యేష్ఠ మాసం", "ఆషాఢ" to "ఆషాఢ మాసం",
            "శ్రావణ" to "శ్రావణ మాసం", "భాద్రపద" to "భాద్రపద మాసం", "ఆశ్వయుజ" to "ఆశ్వయుజ మాసం", "కార్తీక" to "కార్తీక మాసం",
            "మార్గశిర" to "మార్గశిర మాసం", "పుష్య" to "పుష్య మాసం", "మాఘ" to "మాఘ మాసం", "ఫాల్గుణ" to "ఫాల్గుణ మాసం"
        )
        val hi = mapOf(
            "Chaitra" to "चैत्र मास", "Vaishakha" to "वैशाख मास", "Jyeshtha" to "ज्येष्ठ मास", "Ashadha" to "आषाढ़ मास",
            "Shravana" to "श्रावण मास", "Bhadrapada" to "भाद्रपद मास", "Ashwina" to "अश्विन मास", "Kartika" to "कार्तिक मास",
            "Margashirsha" to "मार्गशीर्ष मास", "Pausha" to "पौष मास", "Pushya" to "पौष मास", "Magha" to "माघ मास", "Phalguna" to "फाल्गुन मास"
        )
        val ta = mapOf(
            "Chaitra" to "சித்திரை மாதம்", "Vaishakha" to "வைகாசி மாதம்", "Jyeshtha" to "ஆனி மாதம்", "Ashadha" to "ஆடி மாதம்",
            "Shravana" to "ஆவணி மாதம்", "Bhadrapada" to "புரட்டாசி மாதம்", "Ashwina" to "ஐப்பசி மாதம்", "Kartika" to "கார்த்திகை மாதம்",
            "Margashirsha" to "மார்கழி மாதம்", "Pausha" to "தை மாதம்", "Pushya" to "தை மாதம்", "Magha" to "மாசி மாதம்", "Phalguna" to "பங்குனி மாதம்"
        )
        val kn = mapOf(
            "Chaitra" to "ಚೈತ್ರ ಮಾಸ", "Vaishakha" to "ವೈಶಾಖ ಮಾಸ", "Jyeshtha" to "ಜ್ಯೇಷ್ಠ ಮಾಸ", "Ashadha" to "ಆಷಾಢ ಮಾಸ",
            "Shravana" to "ಶ್ರಾವಣ ಮಾಸ", "Bhadrapada" to "ಭಾದ್ರಪದ ಮಾಸ", "Ashwina" to "ಆಶ್ವಯುಜ ಮಾಸ", "Kartika" to "ಕಾರ್ತಿಕ ಮಾಸ",
            "Margashirsha" to "ಮಾರ್ಗಶಿರ ಮಾಸ", "Pausha" to "ಪುಷ್ಯ ಮಾಸ", "Pushya" to "ಪುಷ್ಯ ಮಾಸ", "Magha" to "ಮಾಘ ಮಾಸ", "Phalguna" to "ಫಾಲ್ಗುಣ ಮಾಸ"
        )

        val mapped = when (lang) {
            AppLanguage.TE -> te[cleanMasa] ?: "$cleanMasa మాసం"
            AppLanguage.HI, AppLanguage.MR -> hi[cleanMasa] ?: "$cleanMasa मास"
            AppLanguage.TA -> ta[cleanMasa] ?: "$cleanMasa மாதம்"
            AppLanguage.KN -> kn[cleanMasa] ?: "$cleanMasa ಮಾಸ"
            else -> "$cleanMasa Masa"
        }
        return mapped
    }

    // 6. Rashi (Zodiac) Translator
    fun translateRashi(rashi: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return rashi
        val base = rashi.replace(" (Aries)", "").replace(" (Taurus)", "").replace(" (Gemini)", "")
            .replace(" (Cancer)", "").replace(" (Leo)", "").replace(" (Virgo)", "")
            .replace(" (Libra)", "").replace(" (Scorpio)", "").replace(" (Sagittarius)", "")
            .replace(" (Capricorn)", "").replace(" (Aquarius)", "").replace(" (Pisces)", "").trim()

        val te = mapOf(
            "Mesha" to "మేష రాశి", "Vrishabha" to "వృషభ రాశి", "Mithuna" to "మిథున రాశి",
            "Karkataka" to "కర్కాటక రాశి", "Simha" to "సింహ రాశి", "Kanya" to "కన్యా రాశి",
            "Tula" to "తులా రాశి", "Vrischika" to "వృశ్చిక రాశి", "Dhanu" to "ధనుస్సు రాశి",
            "Makara" to "మకర రాశి", "Kumbha" to "కుంభ రాశి", "Meena" to "మీన రాశి"
        )
        val hi = mapOf(
            "Mesha" to "मेष राशि", "Vrishabha" to "वृषभ राशि", "Mithuna" to "मिथुन राशि",
            "Karkataka" to "कर्क राशि", "Simha" to "सिंह राशि", "Kanya" to "कन्या राशि",
            "Tula" to "तुला राशि", "Vrischika" to "वृश्चिक राशि", "Dhanu" to "धनु राशि",
            "Makara" to "मकर राशि", "Kumbha" to "कुंभ राशि", "Meena" to "मीन राशि"
        )
        val ta = mapOf(
            "Mesha" to "மேஷம்", "Vrishabha" to "ரிஷபம்", "Mithuna" to "மிதுனம்",
            "Karkataka" to "கடகம்", "Simha" to "சிம்மம்", "Kanya" to "கன்னி",
            "Tula" to "துலாம்", "Vrischika" to "விருச்சிகம்", "Dhanu" to "தனுசு",
            "Makara" to "மகரம்", "Kumbha" to "கும்பம்", "Meena" to "மீனம்"
        )
        val kn = mapOf(
            "Mesha" to "ಮೇಷ ರಾಶಿ", "Vrishabha" to "ವೃಷಭ ರಾಶಿ", "Mithuna" to "ಮಿಥುನ ರಾಶಿ",
            "Karkataka" to "ಕರ್ಕಾಟಕ ರಾಶಿ", "Simha" to "ಸಿಂಹ ರಾಶಿ", "Kanya" to "ಕನ್ಯಾ ರಾಶಿ",
            "Tula" to "ತುಲಾ ರಾಶಿ", "Vrischika" to "ವೃಶ್ಚಿಕ ರಾಶಿ", "Dhanu" to "ಧನು ರಾಶಿ",
            "Makara" to "ಮಕರ ರಾಶಿ", "Kumbha" to "ಕುಂಭ ರಾಶಿ", "Meena" to "ಮೀನ ರಾಶಿ"
        )
        return when (lang) {
            AppLanguage.TE -> te[base] ?: base
            AppLanguage.HI, AppLanguage.MR -> hi[base] ?: base
            AppLanguage.TA -> ta[base] ?: base
            AppLanguage.KN -> kn[base] ?: base
            else -> base
        }
    }

    // 7. Choghadiya Slot Translator
    fun translateChoghadiya(name: String, lang: AppLanguage): String {
        val te = mapOf(
            "Amrit" to "అమృతం (చాలా శుభప్రదం)",
            "Shubh" to "శుభం (శుభప్రదం)",
            "Labh" to "లాభం (లాభదాయకం)",
            "Char" to "చర (సాధారణ చలనం)",
            "Rog" to "రోగం (అశుభం)",
            "Kaal" to "కాలం (అశుభం)",
            "Udveg" to "ఉద్వేగం (అశుభం)"
        )
        val hi = mapOf(
            "Amrit" to "अमृत (सर्वोत्तम शुभ)",
            "Shubh" to "शुभ (अनुकूल)",
            "Labh" to "लाभ (लाभकारी)",
            "Char" to "चर (सामान्य)",
            "Rog" to "रोग (अशुभ)",
            "Kaal" to "काल (हानिकारक)",
            "Udveg" to "उद्वेग (अशुभ)"
        )
        val ta = mapOf(
            "Amrit" to "அமிர்தம் (மிகச் சுபம்)",
            "Shubh" to "சுபம் (சுபமானது)",
            "Labh" to "லாபம் (நன்மை)",
            "Char" to "சரம் (நடுநிலை)",
            "Rog" to "ரோகம் (அசுபம்)",
            "Kaal" to "காலம் (அசுபம்)",
            "Udveg" to "உத்வேகம் (கவலை)"
        )
        val kn = mapOf(
            "Amrit" to "ಅಮೃತ (ಅತ್ಯಂತ ಶುಭ)",
            "Shubh" to "ಶುಭ (ಮಂಗಳಕರ)",
            "Labh" to "ಲಾಭ (ಲಾಭದಾಯಕ)",
            "Char" to "ಚರ (ಸಾಮಾನ್ಯ)",
            "Rog" to "ರೋಗ (ಅಶುಭ)",
            "Kaal" to "ಕಾಲ (ಅಶುಭ)",
            "Udveg" to "ಉದ್ವೇಗ (ಅಶುಭ)"
        )

        return when (lang) {
            AppLanguage.TE -> te[name] ?: name
            AppLanguage.HI, AppLanguage.MR -> hi[name] ?: name
            AppLanguage.TA -> ta[name] ?: name
            AppLanguage.KN -> kn[name] ?: name
            else -> name
        }
    }

    // 8. Localized Gregorian Date Formatter
    fun formatLocalizedDate(date: LocalDate, lang: AppLanguage): String {
        val vara = translateVara(date.dayOfWeek, lang)
        val day = date.dayOfMonth
        val year = date.year

        val teMonths = listOf("", "జనవరి", "ఫిబ్రవరి", "మార్చి", "ఏప్రిల్", "మే", "జూన్", "జూలై", "ఆగస్టు", "సెప్టెంబర్", "అక్టోబర్", "నవంబర్", "డిసెంబర్")
        val hiMonths = listOf("", "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितम्बर", "अक्टूबर", "नवम्बर", "दिसम्बर")
        val taMonths = listOf("", "ஜனவரி", "பிப்ரவரி", "மார்ச்", "ஏப்ரல்", "மே", "ஜூன்", "ஜூலை", "ஆகஸ்ட்", "செப்டம்பர்", "அக்டோபர்", "நவம்பர்", "டிசம்பர்")
        val knMonths = listOf("", "ಜನವರಿ", "ಫೆಬ್ರವರಿ", "ಮಾರ್ಚ್", "ಏಪ್ರಿಲ್", "ಮೇ", "ಜೂನ್", "ಜುಲೈ", "ಆಗಸ್ಟ್", "ಸೆಪ್ಟೆಂಬರ್", "ಅಕ್ಟೋಬರ್", "ನವೆಂಬರ್", "ಡಿಸೆಂಬರ್")

        val monthName = when (lang) {
            AppLanguage.TE -> teMonths[date.monthValue]
            AppLanguage.HI, AppLanguage.MR -> hiMonths[date.monthValue]
            AppLanguage.TA -> taMonths[date.monthValue]
            AppLanguage.KN -> knMonths[date.monthValue]
            else -> date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }

        return "$vara, $day $monthName $year"
    }

    // 9. Calendar Month Year Formatter
    fun formatMonthYear(yearMonth: YearMonth, lang: AppLanguage): String {
        val year = yearMonth.year
        val monthIdx = yearMonth.monthValue
        val teMonths = listOf("", "జనవరి", "ఫిబ్రవరి", "మార్చి", "ఏప్రిల్", "మే", "జూన్", "జూలై", "ఆగస్టు", "సెప్టెంబర్", "అక్టోబర్", "నవంబర్", "డిసెంబర్")
        val hiMonths = listOf("", "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितम्बर", "अक्टूबर", "नवम्बर", "दिसम्बर")
        val taMonths = listOf("", "ஜனவரி", "பிப்ரவரி", "மார்ச்", "ஏப்ரல்", "மே", "ஜூன்", "ஜூலை", "ஆகஸ்ட்", "செப்டம்பர்", "அக்டோபர்", "நவம்பர்", "டிசம்பர்")
        val knMonths = listOf("", "ಜನವರಿ", "ಫೆಬ್ರವರಿ", "ಮಾರ್ಚ್", "ಏಪ್ರಿಲ್", "ಮೇ", "ಜೂನ್", "ಜುಲೈ", "ಆಗಸ್ಟ್", "ಸೆಪ್ಟೆಂಬರ್", "ಅಕ್ಟೋಬರ್", "ನವೆಂಬರ್", "ಡಿಸೆಂಬರ್")

        val mName = when (lang) {
            AppLanguage.TE -> teMonths[monthIdx]
            AppLanguage.HI, AppLanguage.MR -> hiMonths[monthIdx]
            AppLanguage.TA -> taMonths[monthIdx]
            AppLanguage.KN -> knMonths[monthIdx]
            else -> yearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }
        return "$mName $year"
    }

    // 10. Calendar Grid Day of Week Header
    fun getDayOfWeekHeader(dayOfWeek: DayOfWeek, lang: AppLanguage): String {
        val idx = if (dayOfWeek == DayOfWeek.SUNDAY) 0 else dayOfWeek.value
        val te = listOf("ఆది", "సోమ", "మంగళ", "బుధ", "గురు", "శుక్ర", "శని")
        val hi = listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
        val ta = listOf("ஞாயிறு", "திங்கள்", "செவ்வாய்", "புதன்", "வியாழன்", "வெள்ளி", "சனி")
        val kn = listOf("ಭಾನು", "ಸೋಮ", "ಮಂಗಳ", "ಬುಧ", "ಗುರು", "ಶುಕ್ರ", "ಶನಿ")

        return when (lang) {
            AppLanguage.TE -> te[idx]
            AppLanguage.HI, AppLanguage.MR -> hi[idx]
            AppLanguage.TA -> ta[idx]
            AppLanguage.KN -> kn[idx]
            else -> dayOfWeek.name.take(3)
        }
    }

    // 11. Format Live Time with accurate localized AM/PM periods
    fun formatLiveTime(time: LocalTime, lang: AppLanguage): String {
        val hour12 = if (time.hour == 0) 12 else if (time.hour > 12) time.hour - 12 else time.hour
        val amPm = when {
            time.hour < 12 -> when (lang) {
                AppLanguage.TE -> "ఉదయం (AM)"
                AppLanguage.HI -> "प्रातः (AM)"
                AppLanguage.TA -> "காலை (AM)"
                AppLanguage.KN -> "ಬೆಳಿಗ್ಗೆ (AM)"
                AppLanguage.MR -> "सकाळ (AM)"
                AppLanguage.GU -> "સવાર (AM)"
                AppLanguage.BN -> "সকাল (AM)"
                AppLanguage.ML -> "രാവിലെ (AM)"
                AppLanguage.OR_LANG -> "ସକାଳ (AM)"
                else -> "AM"
            }
            time.hour in 12..15 -> when (lang) {
                AppLanguage.TE -> "మధ్యాహ్నం (PM)"
                AppLanguage.HI -> "दोपहर (PM)"
                AppLanguage.TA -> "மதியம் (PM)"
                AppLanguage.KN -> "ಮಧ್ಯಾಹ್ನ (PM)"
                AppLanguage.MR -> "दुपार (PM)"
                AppLanguage.GU -> "બપોર (PM)"
                AppLanguage.BN -> "দুপুর (PM)"
                AppLanguage.ML -> "ഉച്ചയ്ക്ക് (PM)"
                AppLanguage.OR_LANG -> "ମଧ୍ୟାହ୍ନ (PM)"
                else -> "PM"
            }
            time.hour in 16..19 -> when (lang) {
                AppLanguage.TE -> "సాయంత్రం (PM)"
                AppLanguage.HI -> "सायं (PM)"
                AppLanguage.TA -> "மாலை (PM)"
                AppLanguage.KN -> "ಸಂಜೆ (PM)"
                AppLanguage.MR -> "संध्याकाळ (PM)"
                AppLanguage.GU -> "સાંજ (PM)"
                AppLanguage.BN -> "সন্ধ্যা (PM)"
                AppLanguage.ML -> "വൈകുന്നേരം (PM)"
                AppLanguage.OR_LANG -> "ସନ୍ଧ୍ୟା (PM)"
                else -> "PM"
            }
            else -> when (lang) {
                AppLanguage.TE -> "రాత్రి (PM)"
                AppLanguage.HI -> "रात्रि (PM)"
                AppLanguage.TA -> "இரவு (PM)"
                AppLanguage.KN -> "ರಾತ್ರಿ (PM)"
                AppLanguage.MR -> "रात्र (PM)"
                AppLanguage.GU -> "રાત (PM)"
                AppLanguage.BN -> "রাত (PM)"
                AppLanguage.ML -> "രാത്രി (PM)"
                AppLanguage.OR_LANG -> "ରାତି (PM)"
                else -> "PM"
            }
        }
        val hourStr = String.format(Locale.ENGLISH, "%02d", hour12)
        val minStr = String.format(Locale.ENGLISH, "%02d", time.minute)
        val secStr = String.format(Locale.ENGLISH, "%02d", time.second)
        return "$hourStr:$minStr:$secStr $amPm"
    }

    // 12. Muhurtha Category Label Translator
    fun translateMuhurthaCategory(catTitle: String, lang: AppLanguage): String {
        val te = mapOf(
            "Vivah (Marriage)" to "వివాహ ముహూర్తం (పెళ్లి)",
            "Marriage (Vivah)" to "వివాహ ముహూర్తం (పెళ్లి)",
            "Griha Pravesh (House Warming)" to "గృహ ప్రవేశం",
            "House Warming (Griha Pravesh)" to "గృహ ప్రవేశం",
            "Vahan Kharidi (Vehicle Purchase)" to "వాహన కొనుగోలు",
            "Vehicle Purchase (Vahan)" to "వాహన కొనుగోలు",
            "Bhoomi Puja & Property" to "భూమి పూజ / ఆస్తి కొనుగోలు",
            "Property & Land (Bhoomi)" to "భూమి / ఆస్తి కొనుగోలు",
            "Vyapar Aarambh (Business Opening)" to "వ్యాపార ప్రారంభం / ప్రారంభోత్సవం",
            "Business Opening" to "వ్యాపార ప్రారంభం / ప్రారంభోత్సవం",
            "Namakaran (Naming Ceremony)" to "నామకరణ ముహూర్తం (బారసాల)",
            "Naming Ceremony (Namakaran)" to "నామకరణ ముహూర్తం (బారసాల)",
            "Annaprashana (First Solid Food)" to "అన్నప్రాశన ముహూర్తం",
            "First Feeding (Annaprashana)" to "అన్నప్రాశన ముహూర్తం",
            "Upanayana (Sacred Thread)" to "ఉపనయనం (ఒడుగు)",
            "Sacred Thread (Upanayana)" to "ఉపనయనం (ఒడుగు)",
            "Aksharabhyasam (First Learning)" to "అక్షరాభ్యాసం (విద్యారంభం)",
            "School Start (Aksharabhyasam)" to "అక్షరాభ్యాసం (విద్యారంభం)",
            "Yatra (Auspicious Travel)" to "యాత్రా ముహూర్తం (ప్రయాణం)",
            "Travel (Yatra)" to "యాత్రా ముహూర్తం (ప్రయాణం)"
        )
        val hi = mapOf(
            "Vivah (Marriage)" to "विवाह संस्कार (शादी)",
            "Marriage (Vivah)" to "विवाह संस्कार (शादी)",
            "Griha Pravesh (House Warming)" to "गृह प्रवेश",
            "House Warming (Griha Pravesh)" to "गृह प्रवेश",
            "Vahan Kharidi (Vehicle Purchase)" to "वाहन खरीद",
            "Vehicle Purchase (Vahan)" to "वाहन खरीद",
            "Bhoomi Puja & Property" to "भूमि पूजा / संपत्ति खरीद",
            "Property & Land (Bhoomi)" to "भूमि / संपत्ति खरीद",
            "Vyapar Aarambh (Business Opening)" to "व्यापार शुभारंभ / उद्घाटन",
            "Business Opening" to "व्यापार शुभारंभ / उद्घाटन",
            "Namakaran (Naming Ceremony)" to "नामकरण संस्कार",
            "Naming Ceremony (Namakaran)" to "नामकरण संस्कार",
            "Annaprashana (First Solid Food)" to "अन्नप्राशन संस्कार",
            "First Feeding (Annaprashana)" to "अन्नप्राशन संस्कार",
            "Upanayana (Sacred Thread)" to "यज्ञोपवीत (उपनयन)",
            "Sacred Thread (Upanayana)" to "यज्ञोपवीत (उपनयन)",
            "Aksharabhyasam (First Learning)" to "अक्षरारंभ / विद्यारंभ",
            "School Start (Aksharabhyasam)" to "अक्षरारंभ / विद्यारंभ",
            "Yatra (Auspicious Travel)" to "यात्रा मुहूर्त",
            "Travel (Yatra)" to "यात्रा मुहूर्त"
        )
        val ta = mapOf(
            "Vivah (Marriage)" to "திருமண முகூர்த்தம்",
            "Griha Pravesh (House Warming)" to "கிரஹப்பிரவேசம்",
            "Vahan Kharidi (Vehicle Purchase)" to "வாகனம் வாங்குதல்",
            "Bhoomi Puja & Property" to "பூமி பூஜை / நிலம்",
            "Vyapar Aarambh (Business Opening)" to "தொழில் துவக்கம்",
            "Namakaran (Naming Ceremony)" to "நாமகரணம்",
            "Annaprashana (First Solid Food)" to "அன்னப்ராசனம்",
            "Upanayana (Sacred Thread)" to "உபநயனம்",
            "Aksharabhyasam (First Learning)" to "அக்ஷராப்யாசம்",
            "Yatra (Auspicious Travel)" to "பயண முகூர்த்தம்"
        )
        val kn = mapOf(
            "Vivah (Marriage)" to "ವಿವಾಹ ಮುಹೂರ್ತ",
            "Griha Pravesh (House Warming)" to "ಗೃಹ ಪ್ರವೇಶ",
            "Vahan Kharidi (Vehicle Purchase)" to "ವಾಹನ ಖರೀದಿ",
            "Bhoomi Puja & Property" to "ಭೂಮಿ ಪೂಜೆ / ಆಸ್ತಿ",
            "Vyapar Aarambh (Business Opening)" to "ವ್ಯಾಪಾರ ಉದ್ಘಾಟನೆ",
            "Namakaran (Naming Ceremony)" to "ನಾಮಕರಣ",
            "Annaprashana (First Solid Food)" to "ಅನ್ನಪ್ರಾಶನ",
            "Upanayana (Sacred Thread)" to "ಉಪನಯನ",
            "Aksharabhyasam (First Learning)" to "ಅಕ್ಷರಾಭ್ಯಾಸ",
            "Yatra (Auspicious Travel)" to "ಪ್ರಯಾಣ ಮುಹೂರ್ತ"
        )

        return when (lang) {
            AppLanguage.TE -> te[catTitle] ?: catTitle
            AppLanguage.HI, AppLanguage.MR -> hi[catTitle] ?: catTitle
            AppLanguage.TA -> ta[catTitle] ?: catTitle
            AppLanguage.KN -> kn[catTitle] ?: catTitle
            else -> catTitle
        }
    }

    fun translateMuhurthaDescription(catTitle: String, lang: AppLanguage): String {
        val te = mapOf(
            "Vivah (Marriage)" to "వివాహ వేడుకకు మరియు దంపతుల సుఖసంతోషాలకు శ్రేష్ఠమైన లగ్న ముహూర్తములు",
            "Griha Pravesh (House Warming)" to "నూతన గృహ ప్రవేశానికి శుభ గ్రహ బలం కలిగిన దివ్య ముహూర్తములు",
            "Vahan Kharidi (Vehicle Purchase)" to "నూతన వాహన కొనుగోలు మరియు ప్రయాణ రక్షణకు అనుకూల సమయాలు",
            "Bhoomi Puja & Property" to "భూమి పూజ, శంకుస్థాపన మరియు నూతన రిజిస్ట్రేషన్ ముహూర్తాలు",
            "Vyapar Aarambh (Business Opening)" to "నూతన వ్యాపార ప్రారంభం, దుకాణ ప్రారంభోత్సవ శుభ సమయాలు",
            "Namakaran (Naming Ceremony)" to "శిశువు నామకరణ మహోత్సవ శుభ ముహూర్తాలు (బారసాల)",
            "Annaprashana (First Solid Food)" to "పాపాయి మొదటి అన్నప్రాశన మరియు కంఠాభరణ ధారణ ముహూర్తాలు",
            "Upanayana (Sacred Thread)" to "వటువు ఉపనయన సంస్కారం మరియు గాయత్రీ ఉపదేశ శుభ ముహూర్తాలు",
            "Aksharabhyasam (First Learning)" to "పిల్లల తొలి విద్యారంభం & శ్రీ సరస్వతీ పూజా ముహూర్తాలు",
            "Yatra (Auspicious Travel)" to "దిశ శూల వర్జితమైన శుభ ప్రయాణ సమయాలు"
        )
        val hi = mapOf(
            "Vivah (Marriage)" to "विवाह संस्कार एवं दांपत्य सुख के लिए सर्वोत्तम शुभ लग्न",
            "Griha Pravesh (House Warming)" to "नए घर में प्रवेश हेतु ग्रह-नक्षत्रों से संरक्षित शुभ समय",
            "Vahan Kharidi (Vehicle Purchase)" to "नवीन वाहन क्रय एवं वाहन पूजन हेतु उत्तम समय",
            "Bhoomi Puja & Property" to "भूमि पूजन, शिलान्यास एवं संपत्ति रजिस्ट्री मुहूर्त",
            "Vyapar Aarambh (Business Opening)" to "नए व्यापार, दुकान उद्घाटन एवं व्यावसायिक प्रतिष्ठान मुहूर्त",
            "Namakaran (Naming Ceremony)" to "शिशु के नामकरण संस्कार हेतु पावन मुहूर्त",
            "Annaprashana (First Solid Food)" to "शिशु के प्रथम अन्नप्राशन संस्कार हेतु शुभ मुहूर्त",
            "Upanayana (Sacred Thread)" to "यज्ञोपवीत एवं गायत्री उपदेश संस्कार मुहूर्त",
            "Aksharabhyasam (First Learning)" to "सरस्वती पूजन एवं प्रथम विद्यारंभ मुहूर्त",
            "Yatra (Auspicious Travel)" to "दिशा शूल रहित निर्विघ्न यात्रा मुहूर्त"
        )
        return when (lang) {
            AppLanguage.TE -> te[catTitle] ?: "శుభ గ్రహ బలం కలిగిన అనుకూల ముహూర్తములు"
            AppLanguage.HI -> hi[catTitle] ?: "शुभ ग्रह बल युक्त अनुकूल मुहूर्त"
            else -> "Auspicious planetary alignment for your chosen ceremony"
        }
    }

    // 13. Festival Category Label Translator
    fun translateFestivalCategory(label: String, lang: AppLanguage): String {
        return when (label) {
            "All" -> get("category_all", lang)
            "Major Festivals" -> get("category_major", lang)
            "Vrats & Fasting" -> get("category_vrat", lang)
            "Ekadashi" -> get("category_ekadashi", lang)
            "Jayanti" -> get("category_jayanti", lang)
            "Regional" -> get("category_regional", lang)
            else -> label
        }
    }

    // 14. Translate Month Range Label
    fun translateMonthRange(months: Int, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.TE -> "$months నెలలు"
            AppLanguage.HI, AppLanguage.MR -> "$months माह"
            AppLanguage.TA -> "$months மாதங்கள்"
            AppLanguage.KN -> "$months ತಿಂಗಳುಗಳು"
            else -> "$months Months"
        }
    }

    // 15. Translate Festival Names across languages
    fun cleanTeluguFestivalName(name: String): String {
        // If it contains brackets with Telugu text inside (e.g., "Vinayaka Chavithi (వినాయక చవితి)"):
        val teluguInParen = Regex("""\(([\u0C00-\u0C7F\s/&,.-]+)\)""").find(name)?.groupValues?.get(1)?.trim()
        if (!teluguInParen.isNullOrEmpty() && teluguInParen.any { it in '\u0C00'..'\u0C7F' }) {
            return teluguInParen
        }

        // If name contains Telugu text and brackets with English (e.g., "భాద్రపద శుక్ల ఏకాదశి (Ekadashi)"):
        var cleaned = name.replace(Regex("""\([A-Za-z0-9\s/&,.-]+\)"""), "").trim()

        // Strip pure English prefix if followed by slash or dash or brackets:
        if (cleaned.contains("/") && cleaned.any { it in '\u0C00'..'\u0C7F' }) {
            val parts = cleaned.split("/").map { it.trim() }
            val teluguParts = parts.filter { part -> part.any { it in '\u0C00'..'\u0C7F' } }
            if (teluguParts.isNotEmpty()) {
                cleaned = teluguParts.joinToString(" / ")
            }
        }

        // Remove any remaining stray English characters if Telugu characters exist
        if (cleaned.any { it in '\u0C00'..'\u0C7F' }) {
            cleaned = cleaned.replace(Regex("""[A-Za-z]+"""), "").replace(Regex("""\s+"""), " ").trim()
            cleaned = cleaned.trim(',', '/', '-', ' ')
        }
        return cleaned.ifEmpty { name }
    }

    fun translateFestivalName(name: String, lang: AppLanguage): String {
        val te = mapOf(
            "Ganesh Chaturthi (Vinayaka Chavithi)" to "వినాయక చవితి (గణేష్ చతుర్థి)",
            "Vinayaka Chavithi (వినాయక చవితి / గణేష్ చతుర్థి)" to "వినాయక చవితి (గణేష్ చతుర్థి)",
            "Makar Sankranti / Pongal" to "మకర సంక్రాంతి / పెద్ద పండుగ",
            "Makar Sankranti / Pongal / Pedda Panduga" to "మకర సంక్రాంతి / పెద్ద పండుగ",
            "Kanuma Panduga (కనుమ పండుగ)" to "కనుమ పండుగ",
            "Republic Day (గణతంత్ర దినోత్సవం)" to "గణతంత్ర దినోత్సవం",
            "Independence Day (స్వాతంత్ర్య దినోత్సవం)" to "స్వాతంత్ర్య దినోత్సవం",
            "Gandhi Jayanti (గాంధీ జయంతి)" to "గాంధీ జయంతి",
            "Lohri" to "లోహ్రీ పండుగ",
            "Shattila Ekadashi" to "షట్టిలా ఏకాదశి",
            "Vasant Panchami / Saraswati Puja" to "వసంత పంచమి (శ్రీ సరస్వతీ పూజ)",
            "Vasant Panchami / Saraswati Puja (వసంత పంచమి / శ్రీ పంచమి)" to "వసంత పంచమి (సరస్వతీ పూజ)",
            "Ratha Saptami" to "రథసప్తమి (సూర్య జయంతి)",
            "Ratha Saptami / Surya Jayanti (రథసప్తమి)" to "రథసప్తమి (సూర్య జయంతి)",
            "Maha Shivaratri" to "మహా శివరాత్రి",
            "Maha Shivaratri (మహా శివరాత్రి)" to "మహా శివరాత్రి",
            "Holi / Holika Dahan" to "హోలీ పండుగ / కామదహనం",
            "Kamadahana & Holi (కామదహనం & హోలీ)" to "కామదహనం & హోలీ",
            "Ugadi / Gudi Padwa / Chaitra Navratri" to "శ్రీ క్రోధి నామ ఉగాది",
            "Ugadi / Telugu New Year (ఉగాది)" to "శ్రీ క్రోధి నామ ఉగాది",
            "Sri Rama Navami" to "శ్రీరామనవమి (సీతారామ కళ్యాణం)",
            "Sri Rama Navami (శ్రీరామనవమి)" to "శ్రీరామనవమి (సీతారామ కళ్యాణం)",
            "Hanuman Jayanti" to "హనుమాన్ జయంతి",
            "Hanuman Jayanti (హనుమాన్ జయంతి)" to "హనుమాన్ జయంతి",
            "Akshaya Tritiya" to "అక్షయ తృతీయ",
            "Akshaya Tritiya (అక్షయ తృతీయ)" to "అక్షయ తృతీయ",
            "Narasimha Jayanti" to "శ్రీ నృసింహ జయంతి",
            "Narasimha Jayanti (నృసింహ జయంతి)" to "శ్రీ నృసింహ జయంతి",
            "Buddha Purnima & Kurma Jayanti" to "బుద్ధ పూర్ణిమ & కూర్మ జయంతి",
            "Ganga Dussehra (గంగా దసరా)" to "గంగా దసరా",
            "Vata Savitri Vratam & Eruvaka Purnima (వట సావిత్రి వ్రతం & ఏరువాక పౌర్ణమి)" to "వట సావిత్రి వ్రతం & ఏరువాక పౌర్ణమి",
            "Puri Jagannath Ratha Yatra" to "పూరీ జగన్నాథ రథయాత్ర",
            "Nirjala Ekadashi" to "నిర్జల ఏకాదశి",
            "Devshayani Ekadashi" to "దేవశయనీ ఏకాదశి (చాతుర్మాస్య ప్రారంభం)",
            "Tholi Ekadashi / Devshayani Ekadashi (తొలి ఏకాదశి)" to "తొలి ఏకాదశి (శయన ఏకాదశి)",
            "Guru Purnima (Vyasa Purnima)" to "గురు పూర్ణిమ (వ్యాస పూజ)",
            "Guru Purnima / Vyasa Puja (గురు పూర్ణిమ)" to "గురు పూర్ణిమ (వ్యాస పూజ)",
            "Nag Panchami" to "నాగ పంచమి",
            "Naga Panchami (నాగ పంచమి)" to "నాగ పంచమి",
            "Varalakshmi Vratam" to "శ్రావణ వరలక్ష్మీ వ్రతం",
            "Varalakshmi Vratam (వరలక్ష్మీ వ్రతం)" to "శ్రావణ వరలక్ష్మీ వ్రతం",
            "Raksha Bandhan / Shravani Upakarma" to "రాఖీ పౌర్ణమి (రక్షా బంధన్)",
            "Raksha Bandhan & Hayagriva Jayanti (రాఖీ పౌర్ణమి)" to "రాఖీ పౌర్ణమి (రక్షా బంధన్)",
            "Sri Krishna Janmashtami" to "శ్రీకృష్ణాష్టమి (గోకులాష్టమి)",
            "Sri Krishna Janmashtami / Gokulashtami (శ్రీకృష్ణాష్టమి)" to "శ్రీకృష్ణాష్టమి (గోకులాష్టమి)",
            "Rishi Panchami (ఋషి పంచమి)" to "ఋషి పంచమి",
            "Anantha Padmanabha Vratam (అనంత పద్మనాభ చతుర్దశి)" to "అనంత పద్మనాభ వ్రతం",
            "Anant Chaturdashi (Ganesh Visarjan)" to "అనంత చతుర్దశి (వినాయక నిమజ్జనం)",
            "Mahalaya Amavasya (Sarva Pitru Amavasya)" to "మహాలయ అమావాస్య (పితృ తర్పణం)",
            "Mahalaya Amavasya / Pitru Paksha (మహాలయ అమావాస్య)" to "మహాలయ అమావాస్య (పితృ తర్పణం)",
            "Sharad Navratri Ghatasthapana" to "దేవీ శరన్నవరాత్రులు ప్రారంభం",
            "Sharad Navratri / Devi Navaratrulu Start (శరన్నవరాత్రులు ప్రారంభం)" to "దేవీ శరన్నవరాత్రులు ప్రారంభం",
            "Maha Ashtami / Durgashtami" to "శ్రీ దుర్గాష్టమి",
            "Durgashtami / Bathukamma Panduga (దుర్గాష్టమి & సద్దుల బతుకమ్మ)" to "దుర్గాష్టమి & సద్దుల బతుకమ్మ",
            "Maha Navami / Ayudha Puja (మహానవమి / ఆయుధ పూజ)" to "మహానవమి (ఆయుధ పూజ)",
            "Vijaya Dashami (Dussehra)" to "విజయదశమి (దసరా / శమీ పూజ)",
            "Vijaya Dashami / Dussehra (విజయదశమి / దసరా)" to "విజయదశమి (దసరా)",
            "Atla Tadde / Karwa Chauth (అట్ల తద్దె)" to "అట్ల తద్దె",
            "Karwa Chauth" to "కర్వా చౌత్ వ్రతం",
            "Dhanteras (Dhanatrayodashi)" to "ధన త్రయోదశి (ధన్వంతరి జయంతి)",
            "Dhanteras / Dhantrayodashi (ధన త్రయోదశి)" to "ధన త్రయోదశి",
            "Naraka Chaturdashi (Choti Diwali)" to "నరక చతుర్దశి",
            "Naraka Chaturdashi (నరక చతుర్దశి)" to "నరక చతుర్దశి",
            "Diwali (Lakshmi Puja)" to "దీపావళి (శ్రీ మహాలక్ష్మీ పూజ)",
            "Deepavali / Lakshmi Puja (దీపావళి పండుగ)" to "దీపావళి పండుగ (మహాలక్ష్మీ పూజ)",
            "Govardhan Puja / Annakut" to "గోవర్ధన పూజ / అన్నకూట్",
            "Bhai Dooj (Yama Dwitiya)" to "భగినీ హస్తభోజనం / భాయ్ దూజ్",
            "Nagula Chavithi (నాగుల చవితి)" to "నాగుల చవితి",
            "Chhath Puja" to "ఛత్ పూజ (సూర్య షష్ఠి)",
            "Devutthana Ekadashi / Tulsi Vivah" to "ప్రబోధినీ ఏకాదశి / తులసీ కళ్యాణం",
            "Ksheerabdi Dwadashi / Tulsi Vivah (తులసీ వివాహం & క్షీరాబ్ధి ద్వాదశి)" to "తులసీ వివాహం & క్షీరాబ్ధి ద్వాదశి",
            "Kartik Purnima / Dev Diwali" to "కార్తీక పౌర్ణమి (జ్వాలా తోరణం)",
            "Kartika Purnima / Jwala Thoranam (కార్తీక పౌర్ణమి)" to "కార్తీక పౌర్ణమి (జ్వాలా తోరణం)",
            "Subramanya Sashti / Skanda Sashti (సుబ్రహ్మణ్య షష్ఠి)" to "సుబ్రహ్మణ్య షష్ఠి",
            "Gita Jayanti" to "శ్రీ భగవద్గీతా జయంతి",
            "Gita Jayanti & Mokshada Ekadashi (గీతా జయంతి)" to "గీతా జయంతి & మోక్షద ఏకాదశి",
            "Vaikuntha Ekadashi / Mukkoti (ముక్కోటి ఏకాదశి)" to "ముక్కోటి ఏకాదశి (వైకుంఠ ఏకాదశి)",
            "Bhishma Ekadashi (భీష్మ ఏకాదశి)" to "భీష్మ ఏకాదశి"
        )
        val hi = mapOf(
            "Ganesh Chaturthi (Vinayaka Chavithi)" to "श्री गणेश चतुर्थी (विनायक चतुर्थी)",
            "Makar Sankranti / Pongal" to "मकर संक्रांति / पोंगल",
            "Lohri" to "लोहड़ी",
            "Shattila Ekadashi" to "षट्तिला एकादशी",
            "Vasant Panchami / Saraswati Puja" to "बसंत पंचमी / सरस्वती पूजा",
            "Ratha Saptami" to "रथ सप्तमी",
            "Maha Shivaratri" to "महाशिवरात्रि",
            "Holi / Holika Dahan" to "होली / होलिका दहन",
            "Ugadi / Gudi Padwa / Chaitra Navratri" to "गुड़ी पड़वा / चैत्र नवरात्रि",
            "Sri Rama Navami" to "श्री राम नवमी",
            "Hanuman Jayanti" to "श्री हनुमान जयंती",
            "Akshaya Tritiya" to "अक्षय तृतीया",
            "Narasimha Jayanti" to "नरसिंह जयंती",
            "Nirjala Ekadashi" to "निर्जला एकादशी",
            "Devshayani Ekadashi" to "देवशयनी एकादशी",
            "Guru Purnima (Vyasa Purnima)" to "गुरु पूर्णिमा (व्यास पूर्णिमा)",
            "Nag Panchami" to "नाग पंचमी",
            "Varalakshmi Vratam" to "वरलक्ष्मी व्रत",
            "Raksha Bandhan / Shravani Upakarma" to "रक्षाबंधन / श्रावणी उपकर्म",
            "Sri Krishna Janmashtami" to "श्री कृष्ण जन्माष्टमी",
            "Anant Chaturdashi (Ganesh Visarjan)" to "अनंत चतुर्दशी (गणेश विसर्जन)",
            "Mahalaya Amavasya (Sarva Pitru Amavasya)" to "महालय अमावस्या (सर्वपितृ अमावस्या)",
            "Sharad Navratri Ghatasthapana" to "शारदीय नवरात्रि घटस्थापना",
            "Maha Ashtami / Durgashtami" to "महाष्टमी / दुर्गाष्टमी",
            "Vijaya Dashami (Dussehra)" to "विजयादशमी (दशहरा)",
            "Karwa Chauth" to "करवा चौथ",
            "Dhanteras (Dhanatrayodashi)" to "धनतेरस (धनत्रयोदशी)",
            "Naraka Chaturdashi (Choti Diwali)" to "नरक चतुर्दशी (छोटी दीवाली)",
            "Diwali (Lakshmi Puja)" to "दीपावली (महालक्ष्मी पूजन)",
            "Govardhan Puja / Annakut" to "गोवर्धन पूजा / अन्नकूट",
            "Bhai Dooj (Yama Dwitiya)" to "भाई दूज (यम द्वितीया)",
            "Chhath Puja" to "छठ पूजा (सूर्य षष्ठी)",
            "Devutthana Ekadashi / Tulsi Vivah" to "देवउठनी एकादशी / तुलसी विवाह",
            "Kartik Purnima / Dev Diwali" to "कार्तिक पूर्णिमा / देव दीपावली",
            "Gita Jayanti" to "श्रीमद्भगवद्गीता जयंती"
        )
        val ta = mapOf(
            "Ganesh Chaturthi (Vinayaka Chavithi)" to "விநாயகர் சதுர்த்தி",
            "Makar Sankranti / Pongal" to "தைப்பொங்கல் / மகர சங்கராந்தி",
            "Lohri" to "லோஹ்ரி",
            "Shattila Ekadashi" to "ஷட்டிலா ஏகாதசி",
            "Vasant Panchami / Saraswati Puja" to "வசந்த பஞ்சமி / சரஸ்வதி பூஜை",
            "Ratha Saptami" to "ரத சப்தமி",
            "Maha Shivaratri" to "மகா சிவராத்திரி",
            "Holi / Holika Dahan" to "ஹோலி பண்டிகை",
            "Ugadi / Gudi Padwa / Chaitra Navratri" to "யுகாதி பண்டிகை",
            "Sri Rama Navami" to "ஸ்ரீ ராம நவமி",
            "Hanuman Jayanti" to "ஹனுமான் ஜெயந்தி",
            "Akshaya Tritiya" to "அட்சய திருதியை",
            "Narasimha Jayanti" to "நரசிம்ம ஜெயந்தி",
            "Nirjala Ekadashi" to "நிர்ஜலா ஏகாதசி",
            "Devshayani Ekadashi" to "தேவசயனி ஏகாதசி",
            "Guru Purnima (Vyasa Purnima)" to "குரு பூர்ணிமா",
            "Nag Panchami" to "நாக பஞ்சமி",
            "Varalakshmi Vratam" to "வரலட்சுமி விரதம்",
            "Raksha Bandhan / Shravani Upakarma" to "ரக்ஷா பந்தன்",
            "Sri Krishna Janmashtami" to "கோகுலாஷ்டமி / கிருஷ்ண ஜெயந்தி",
            "Anant Chaturdashi (Ganesh Visarjan)" to "அனந்த சதுர்தசி",
            "Mahalaya Amavasya (Sarva Pitru Amavasya)" to "மஹாளய அமாவாசை",
            "Sharad Navratri Ghatasthapana" to "சாரதா நவராத்திரி ஆரம்பம்",
            "Maha Ashtami / Durgashtami" to "துர்காஷ்டமி",
            "Vijaya Dashami (Dussehra)" to "விஜயதசமி",
            "Karwa Chauth" to "கர்வா சௌத்",
            "Dhanteras (Dhanatrayodashi)" to "தன திரயோதசி",
            "Naraka Chaturdashi (Choti Diwali)" to "நரக சதுர்த்தசி",
            "Diwali (Lakshmi Puja)" to "தீபாவளி பண்டிகை",
            "Govardhan Puja / Annakut" to "கோவர்த்தன பூஜை",
            "Bhai Dooj (Yama Dwitiya)" to "பாய் தூஜ்",
            "Chhath Puja" to "சத் பூஜை",
            "Devutthana Ekadashi / Tulsi Vivah" to "துளசி கல்யாணம்",
            "Kartik Purnima / Dev Diwali" to "கார்த்திகை தீபம்",
            "Gita Jayanti" to "பகவத் கீதை ஜெயந்தி"
        )
        val kn = mapOf(
            "Ganesh Chaturthi (Vinayaka Chavithi)" to "ಗಣೇಶ ಚತುರ್ಥಿ (ವಿನಾಯಕ ಚೌತಿ)",
            "Makar Sankranti / Pongal" to "ಮಕರ ಸಂಕ್ರಾಂತಿ",
            "Lohri" to "ಲೋಹ್ರಿ",
            "Shattila Ekadashi" to "ಷಟ್ಟಿಲಾ ಏಕಾದಶಿ",
            "Vasant Panchami / Saraswati Puja" to "ವಸಂತ ಪಂಚಮಿ / ಸರಸ್ವತಿ ಪೂಜೆ",
            "Ratha Saptami" to "ರಥಸಪ್ತಮಿ",
            "Maha Shivaratri" to "ಮಹಾ ಶಿವರಾತ್ರಿ",
            "Holi / Holika Dahan" to "ಹೋಳಿ ಹಬ್ಬ",
            "Ugadi / Gudi Padwa / Chaitra Navratri" to "ಯುಗಾದಿ ಹಬ್ಬ",
            "Sri Rama Navami" to "ಶ್ರೀ ರಾಮ ನವಮಿ",
            "Hanuman Jayanti" to "ಹನುಮಾನ್ ಜಯಂತಿ",
            "Akshaya Tritiya" to "ಅಕ್ಷಯ ತೃತೀಯ",
            "Narasimha Jayanti" to "ನರಸಿಂಹ ಜಯಂತಿ",
            "Nirjala Ekadashi" to "ನಿರ್ಜಲಾ ಏಕಾದಶಿ",
            "Devshayani Ekadashi" to "ದೇವಶಯನಿ ಏಕಾದಶಿ",
            "Guru Purnima (Vyasa Purnima)" to "ಗುರು ಪೂರ್ಣಿಮೆ",
            "Nag Panchami" to "ನಾಗ ಪಂಚಮಿ",
            "Varalakshmi Vratam" to "ವರಮಹಾಲಕ್ಷ್ಮಿ ವ್ರತ",
            "Raksha Bandhan / Shravani Upakarma" to "ರಕ್ಷಾ ಬಂಧನ",
            "Sri Krishna Janmashtami" to "ಶ್ರೀ ಕೃಷ್ಣ ಜನ್ಮಾಷ್ಟಮಿ",
            "Anant Chaturdashi (Ganesh Visarjan)" to "ಅನಂತ ಚತುರ್ದಶಿ",
            "Mahalaya Amavasya (Sarva Pitru Amavasya)" to "ಮಹಾಲಯ ಅಮಾವಾಸ್ಯೆ",
            "Sharad Navratri Ghatasthapana" to "ಶರನ್ನವರಾತ್ರಿ ಆರಂಭ",
            "Maha Ashtami / Durgashtami" to "ದುರ್ಗಾಷ್ಟಮಿ",
            "Vijaya Dashami (Dussehra)" to "ವಿಜಯದಶಮಿ (ದಸರಾ)",
            "Karwa Chauth" to "ಕರ್ವಾ ಚೌತ್",
            "Dhanteras (Dhanatrayodashi)" to "ಧನ ತ್ರಯೋದಶಿ",
            "Naraka Chaturdashi (Choti Diwali)" to "ನರಕ ಚತುರ್ದಶಿ",
            "Diwali (Lakshmi Puja)" to "ದೀಪಾವಳಿ (ಲಕ್ಷ್ಮಿ ಪೂಜೆ)",
            "Govardhan Puja / Annakut" to "ಗೋವರ್ಧನ ಪೂಜೆ",
            "Bhai Dooj (Yama Dwitiya)" to "ಭಾಯಿ ದೂಜ್",
            "Chhath Puja" to "ಛತ್ ಪೂಜೆ",
            "Devutthana Ekadashi / Tulsi Vivah" to "ತುಳಸಿ ಪೂಜೆ / ಕಲ್ಯಾಣ",
            "Kartik Purnima / Dev Diwali" to "ಕಾರ್ತಿಕ ಹುಣ್ಣಿಮೆ",
            "Gita Jayanti" to "ಗೀತಾ ಜಯಂತಿ"
        )

        return when (lang) {
            AppLanguage.TE -> te[name] ?: cleanTeluguFestivalName(name)
            AppLanguage.HI, AppLanguage.MR -> hi[name] ?: name
            AppLanguage.TA -> ta[name] ?: name
            AppLanguage.KN -> kn[name] ?: name
            else -> name
        }
    }

    // 16. Translate Festival Summary
    fun translateFestivalSummary(id: String, defaultSummary: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) {
            if (id.startsWith("ganesh_chaturthi")) {
                return when (lang) {
                    AppLanguage.HI -> "विघ्नहर्ता भगवान गणेश का गृह प्रवेश, मोदक भोग एवं 21 दुर्वा अर्पण का महापर्व।"
                    AppLanguage.TA -> "விக்னங்களை தீர்க்கும் விநாயகர் அவதரித்த நன்னாள்; கொழுக்கட்டை நிவேதனம் மற்றும் சிறப்பு வழிபாடு."
                    AppLanguage.KN -> "ವಿಘ್ನನಾಶಕ ಗಣಪತಿಯ ಆಗಮನ, ಮೋದಕ ನೈವೇದ್ಯ ಮತ್ತು 21 ಪತ್ರೆಗಳಿಂದ ವೈಭವದ ಪೂಜೆ."
                    else -> defaultSummary
                }
            }
            if (id.startsWith("sankranti")) {
                return when (lang) {
                    AppLanguage.HI -> "सूर्य देव का मकर राशि में प्रवेश एवं उत्तरायण का पावन शुभारंभ।"
                    AppLanguage.TA -> "சூரியன் மகர ராசியில் பிரவேசிக்கும் தைப்பொங்கல் திருநாள்."
                    AppLanguage.KN -> "ಸೂರ್ಯನ ಮಕರ ಸಂಕ್ರಮಣ ಹಾಗೂ ಉತ್ತರಾಯಣ ಪುಣ್ಯಕಾಲ ಆರಂಭ."
                    else -> defaultSummary
                }
            }
            if (id.startsWith("krishna_janmashtami")) {
                return when (lang) {
                    AppLanguage.HI -> "भाद्रपद कृष्ण अष्टमी को रोहिणी नक्षत्र में भगवान श्री कृष्ण का प्राकट्योत्सव।"
                    AppLanguage.TA -> "ஸ்ரீகிருஷ்ணர் அவதரித்த நன்னாள்; நள்ளிரவு பூஜை மற்றும் உறியடி உற்சவம்."
                    AppLanguage.KN -> "ಶ್ರೀಕೃಷ್ಣ ಪರಮಾತ್ಮನ ಜನ್ಮದಿನೋತ್ಸವ; ಮಧ್ಯರಾತ್ರಿ ಪೂಜೆ ಮತ್ತು ಭಕ್ತಿ ಸಂಭ್ರಮ."
                    else -> defaultSummary
                }
            }
            if (id.startsWith("diwali")) {
                return when (lang) {
                    AppLanguage.HI -> "अमावस्या की रात्रि में दीप प्रज्वलन एवं माता महालक्ष्मी का मंगल पूजन।"
                    AppLanguage.TA -> "தீபங்களின் திருநாள்; லட்சுமி குபேர பூஜை மற்றும் மகிழ்ச்சி கொண்டாட்டம்."
                    AppLanguage.KN -> "ದೀಪಗಳ ಮಹೋತ್ಸವ; ಮಹಾಲಕ್ಷ್ಮಿ ಪೂಜೆಯಿಂದ ಸಕಲ ಸಮೃದ್ಧಿ ಪ್ರಾಪ್ತಿ."
                    else -> defaultSummary
                }
            }
            return defaultSummary
        }

        return when {
            id.startsWith("ganesh_chaturthi") -> "విఘ్నాలను తొలగించే గణనాథుని ప్రతిష్ఠ, మోదకాలు, 21 పత్రాల పూజలతో వైభవంగా జరుపుకునే 10 రోజుల మహోత్సవం."
            id.startsWith("sankranti") -> "సూర్య భగవానుడు మకర రాశిలోకి ప్రవేశించే పుణ్యకాలం; ఉత్తరాయణ పుణ్య ఘడియల ప్రారంభం."
            id.startsWith("krishna_janmashtami") -> "శ్రావణ బహుళ అష్టమి రోహిణీ నక్షత్రంలో శ్రీకృష్ణ పరమాత్ముని దివ్య జన్మదినోత్సవం."
            id.startsWith("diwali") -> "అమావాస్య చీకట్లను పారద్రోలే దీపాల పండుగ; శ్రీ మహాలక్ష్మి పూజలతో సకల సంపదల ప్రాప్తి."
            id.startsWith("ugadi") -> "తెలుగు నూతన సంవత్సరాది (విశ్వావసు/క్రోధి నామ సంవత్సరం); బ్రహ్మదేవుడు విశ్వ సృష్టికి శ్రీకారం చుట్టిన దివ్య సూర్యోదయ వేళ."
            id.startsWith("sri_rama_navami") -> "మర్యాదా పురుషోత్తముడు శ్రీరామచంద్రుని దివ్య అవతార దినోత్సవం; శ్రీ సీతారాముల దివ్య కళ్యాణ మహోత్సవం."
            id.startsWith("hanuman_jayanti") -> "చిరంజీవి అంజనా పుత్ర శ్రీ హనుమంతుని అవతార దినం; ధైర్యం, బలం మరియు భక్తి ప్రదాయక పర్వదినం."
            id.startsWith("maha_shivaratri") -> "లింగోద్భవ కాలంలో పరమశివునికి రాత్రి నాలుగు యామాల అభిషేకాలు, జాగరణ, శివార్చనలతో మోక్షం పొందే మహా పర్వదినం."
            id.startsWith("holi") -> "వసంత ఋతువుకు స్వాగతం పలుకుతూ ప్రకృతి సహజ రంగులతో, ఆనందోత్సాహాలతో జరుపుకునే వర్ణోత్సవం."
            id.startsWith("holika_dahan") -> "భక్త ప్రహ్లాదుని రక్షించి అధర్మాన్ని దహించిన కామదహనం / హోలికా దహన పవిత్ర జ్వాలారోపణం."
            id.startsWith("vasant_panchami") -> "విద్యలతల్లి చదువుల దేవత శ్రీ సరస్వతీ దేవి ప్రాకట్య దినోత్సవం; అక్షరాభ్యాసాలకు అత్యంత శుభ సమయం."
            id.startsWith("akshaya_tritiya") -> "త్రేతాయుగ ప్రారంభ దినం; తరగని అక్షయ సమృద్ధిని ప్రసాదించే శ్రీ లక్ష్మీ కుబేర పుణ్యకాలం."
            id.startsWith("ratha_saptami") -> "సూర్య జయంతి; ఆరోగ్యం, తేజస్సు ప్రసాదించే సూర్య భగవానుడు ఏడు గుర్రాల రథంపై లోకానికి దర్శనమిచ్చే పవిత్ర సప్తమి."
            id.startsWith("guru_purnima") -> "మహర్షి వేదవ్యాస జయంతి; మనోజ్ఞానాన్ని ప్రసాదించే గురుదేవులను పూజించే మహోన్నత పర్వదినం."
            id.startsWith("varalakshmi") -> "శ్రావణ శుక్రవారం నాడు సకల సౌభాగ్యాలు, అష్టైశ్వర్యాల కోసం ముత్తైదువులు ఆచరించే శ్రీ వరలక్ష్మీ వ్రతం."
            id.startsWith("raksha_bandhan") -> "అక్కాతమ్ముళ్లు, అన్నచెల్లెళ్ల అనురాగానికి ప్రతీకయైన రాఖీ పండుగ మరియు యజ్ఞోపవీత ధారణ శ్రావణి."
            id.startsWith("vijayadashami") -> "చెడుపై మంచి సాధించిన విజయానికి ప్రతీకగా శ్రీరాముని రావణ సంహారం మరియు విజయదుర్గ శమీ పూజల దసరా."
            id.startsWith("durga_ashtami") -> "నవరాత్రులలో అత్యంత శక్తివంతమైన మహర్షి సంహిత మహాష్టమి పూజ, కన్యాపూజ మరియు సంధి పూజల పర్వం."
            id.startsWith("sharad_navratri") -> "శరదృతువు ప్రారంభంలో జగన్మాత దుర్గాదేవి తొమ్మిది రూపాలను ఆరాధించే దివ్య నవరాత్రి మహోత్సవాల ప్రారంభం."
            id.startsWith("karwa_chauth") -> "పతిదీర్ఘాయుష్షు మరియు సౌభాగ్యం కోసం స్త్రీలు ఆచరించే నిర్జల కరవా చౌత్ వ్రతం."
            id.startsWith("dhanteras") -> "క్షీరసాగర మథనం నుండి అమృత కలశంతో ధన్వంతరి భగవానుడు ఉద్భవించిన ధన త్రయోదశి స్వర్ణ క్రయ దివస్."
            id.startsWith("govardhan_puja") -> "ఇంద్రుని గర్వభంగం చేస్తూ గోకుల ప్రజలను కాపాడటానికి శ్రీకృష్ణుడు గోవర్ధన గిరిని ఎత్తిన అనుకూల పూజ."
            id.startsWith("chhath_puja") -> "సూర్య భగవానునికి, ఛఠీ మైయాకు ప్రత్యక్ష నదీ తీరాలలో సూర్యాస్తమయ, సూర్యోదయ అర్ఘ్యాలు సమర్పించే కఠోర వ్రతం."
            id.startsWith("gita_jayanti") -> "కురుక్షేత్ర సంగ్రామంలో శ్రీకృష్ణ పరమాత్ముడు అర్జునునికి దివ్య భగవద్గీతను ఉపదేశించిన పవిత్ర గీతా జయంతి."
            id.startsWith("vaikuntha_ekadashi") -> "ముక్కోటి ఏకాదశి; వైకుంఠ ద్వారాలు తెరుచుకుని సకల దేవతలతో శ్రీ వేంకటేశ్వరుడు దర్శనమిచ్చే మోక్షదినం."
            id.startsWith("anant_chaturdashi") -> "వినాయక నిమజ్జనోత్సవం మరియు శ్రీ అనంత పద్మనాభ స్వామి పూజలతో కూడిన అత్యంత పవిత్ర భాద్రపద శుక్ల చతుర్దశి."
            id.startsWith("pitru_paksha_end") -> "పితృ పక్ష ముగింపు వేళ పూర్వీకుల దివ్య స్మరణకు మరియు ఆత్మశాంతికి అత్యంత పవిత్రమైన శ్రాద్ధ కర్మల అమావాస్య."
            id.startsWith("sharad_navratri_start") -> "శరన్నవరాత్రుల ప్రారంభ ఘట్టం; మంగళ కలశ స్థాపన మరియు నవదుర్గల దివ్య తొమ్మిది రోజుల ఆరాధనల ఆరంభం."
            else -> defaultSummary
        }
    }

    // 16b. Translate Festival Significance
    fun translateFestivalSignificance(id: String, defaultSignificance: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return defaultSignificance

        return when {
            id.startsWith("ganesh_chaturthi") -> "మట్టి గణపతి ప్రతిష్ఠ, 21 పత్రాల (ఏకవింశతి పత్ర) సమర్పణ, సిద్ధ వినాయక కథాపారాయణం మరియు మోదకాల నైవేద్యం."
            id.startsWith("sankranti") -> "షడ్రుచుల సమన్వయం, నువ్వులు-బెల్లం పిండివంటలు, హరిదాసు కీర్తనలు, భోగి మంటలు, ధాన్య లక్ష్మి పూజల పంటల పండుగ."
            id.startsWith("krishna_janmashtami") -> "అర్ధరాత్రి శ్రీకృష్ణ జన్మాభిషేకం, ఉరియడి (దహి హండి) సంబరాలు, వెన్న మఖన్ నైవేద్యం మరియు గీతా పారాయణం."
            id.startsWith("diwali") -> "మహాలక్ష్మి-కుబేర నిశా పూజ, ప్రమిదలలో నువ్వుల నూనె దీపాల వెలుగులు, నరకాసుర వధ విజయ స్మరణ, ధనలక్ష్మి ఆవాహన."
            id.startsWith("ugadi") -> "ఉగాది పచ్చడి (చేదు, తీపి, వగరు, కారం, ఉప్పు, పులుపు) ఆస్వాదించడం ద్వారా కష్టసుఖాలను సమానంగా స్వీకరించాలనే జీవిత సందేశం."
            id.startsWith("sri_rama_navami") -> "శ్రీరామ నవమి నాడు శ్రీ సీతారామ కల్యాణం తిలకించడం, వడపప్పు-పానకం పంపిణీ మరియు శ్రీరామ రక్షా స్తోత్ర పారాయణం."
            id.startsWith("hanuman_jayanti") -> "హనుమాన్ చాలీసా 108 సార్లు పారాయణం చేయడం, సుందరకాండ పఠనం, తమలపాకుల మాల మరియు సిందూర సమర్పణ."
            id.startsWith("maha_shivaratri") -> "లింగాష్టకం పఠనం, రాత్రి జాగరణ, పంచామృతాభిషేకం మరియు బిల్వపత్రార్చన ద్వారా జన్మరాహిత్యం సకల పాపక్షయం."
            id.startsWith("holi") -> "ప్రకృతిలో రంగుల కేళి, ద్వేషాలను మరచి స్నేహ సంబంధాలను పునరుద్ధరించుకోవడానికి ప్రతీకగా జరుపుకునే హర్షోత్సవం."
            id.startsWith("holika_dahan") -> "సాత్విక భక్తి ముందు దుష్ట శక్తులు నశిస్తాయని నిరూపించిన హోలికా దహన పవిత్ర భస్మ తిలక ధారణ."
            id.startsWith("vasant_panchami") -> "పసుపు వర్ణ దుస్తులు ధరించి శ్రీ సరస్వతీ దేవి పూజ, పుస్తకాలు, వాద్య పరికరాల పూజ మరియు అక్షరాభ్యాసం."
            id.startsWith("akshaya_tritiya") -> "దానం, బంగారు నాణేలు లేదా వస్తువుల కొనుగోలు, జలదానం, నూతన కార్యారంభం ద్వారా లక్ష్మీ కటాక్ష సిద్ధి."
            id.startsWith("ratha_saptami") -> "జిల్లేడు ఆకులు (అర్క పత్రాలు) తలపై ఉంచుకుని నదీ స్నానం చేయడం ద్వారా సకల ఆరోగ్య ప్రాప్తి మరియు నేత్ర తేజస్సు."
            id.startsWith("guru_purnima") -> "గురు పాదపూజ, గురుగీత పారాయణం, విద్యాదాతలకు వస్త్ర సత్కారం మరియు ఆత్మజ్ఞాన సాధన."
            id.startsWith("varalakshmi") -> "కలశ స్థాపన, తోరబంధనం (9 ముడుల పసుపు దారం), వరలక్ష్మీ అష్టోత్తర పూజ మరియు వాయన దానాలు."
            id.startsWith("raksha_bandhan") -> "సోదరి సోదరునికి పవిత్ర రాఖీ కట్టి రక్షణ కోరడం; బ్రాహ్మణుల నూతన యజ్ఞోపవీత శ్రావణి ధారణ."
            id.startsWith("vijayadashami") -> "శమీ పూజ (జమ్మి చెట్టు ఆకులు బంగారంలా పంచుకోవడం), ఆయుధ పూజ, వాహన పూజ మరియు విజయముహూర్తంలో నూతన ప్రారంభాలు."
            id.startsWith("durga_ashtami") -> "9 మంది చిన్న పిల్లలకు (కన్యకలకు) పీఠం వేసి కాళ్లు కడిగి పూజించే కన్యా పూజ మరియు 108 దీపారాధన."
            id.startsWith("sharad_navratri") -> "కలశ స్థాపన, దుర్గా సప్తశతి పారాయణం, గర్బా/దండియా నృత్యాలు మరియు నిత్య అలంకార పూజలు."
            id.startsWith("karwa_chauth") -> "రోజంతా నిర్జల ఉపవాసం ఉండి, చంద్రోదయం వేళ జల్లెడలో చంద్రుడిని, భర్తను చూసి అర్ఘ్యమిచ్చి ఉపవాస విరమణ."
            id.startsWith("dhanteras") -> "ఇంటి ముంగిట యమదీపం వెలిగించడం, రాగి/ఇత్తడి రత్న కొనుగోళ్లు మరియు ధన్వంతరి వైభవ పూజ."
            id.startsWith("govardhan_puja") -> "56 రకాల పిండివంటల (ఛప్పన్ భోగ్) అన్నకూట నివేదన మరియు గోమాతలకు, ప్రకృతికి కృతజ్ఞతా పూజ."
            id.startsWith("chhath_puja") -> "పవిత్ర నది నీటిలో నిలబడి సూర్యదేవునికి అర్ఘ్యం సమర్పించడం, తెకువా ప్రసాద నివేదన."
            id.startsWith("gita_jayanti") -> "భగవద్గీతలోని 18 అధ్యాయాల పారాయణం, కర్మయోగ, భక్తియోగ స్మరణ మరియు ధర్మ ప్రచారం."
            id.startsWith("vaikuntha_ekadashi") -> "వైకుంఠ ద్వార ప్రవేశం, విష్ణు సహస్రనామ పారాయణం, రాత్రి జాగరణ ద్వారా వైకుంఠ ప్రాప్తి."
            id.startsWith("anant_chaturdashi") -> "శ్రీహరి అనుగ్రహం, కష్టాల నివారణ కోసం హస్తానికి 14 ముడుల అనంత దారాల రక్షణను ధరించి వ్రతం ఆచరించడం."
            id.startsWith("pitru_paksha_end") -> "నల్లనువ్వులతో తిల తర్పణాలు, పితృ పిండ ప్రదానాలు మరియు బ్రాహ్మణులకు, మూగజీవాలకు అన్నదానం చేయడం."
            id.startsWith("sharad_navratri_start") -> "దుష్టశిక్షణ, శిష్టరక్షణ కోసం లోకమాత తొమ్మిది రూపాలలో పూజలందుకునే దేవీ నవరాత్రి మహోత్సవాల ప్రారంభం."
            else -> defaultSignificance
        }
    }

    // 16c. Translate Festival Historical Context
    fun translateFestivalHistory(id: String, defaultHistory: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return defaultHistory

        return when {
            id.startsWith("ganesh_chaturthi") -> "పార్వతీదేవి తన నలుగు పిండితో రూపుదిద్దిన బాల గణపతికి పరమశివుడు గజముఖాన్ని అమర్చి, సకల దేవతల కంటే ముందుగా పూజలందుకునే ప్రథమ పూజ్య వరం ప్రసాదించిన పవిత్ర పౌరాణిక ఘట్టం."
            id.startsWith("sankranti") -> "సూర్య భగవానుడు ధనూ రాశి నుండి మకర రాశిలోకి ప్రవేశించి ఉత్తరాయణ పుణ్యకాలాన్ని ప్రారంభిస్తాడు. భీష్మాచార్యుడు అంబరంలో తన ప్రాణాలను త్యజించడానికి ఈ ఉత్తరాయణ పుణ్యఘడియల కొరకే వేచి చూశాడు."
            id.startsWith("krishna_janmashtami") -> "ద్వాపర యుగంలో కంసుని బంధనాగారంలో దేవకీ వసుదేవులకు శ్రావణ బహుళ అష్టమి అర్ధరాత్రి రోహిణీ నక్షత్రంలో శ్రీకృష్ణ పరమాత్ముడు జన్మించి లోకానికే దివ్య రక్షణ అందించాడు."
            id.startsWith("diwali") -> "14 సంవత్సరాల అరణ్యవాసము, రావణాసురుని సంహారం తర్వాత శ్రీరాముడు సీతా లక్ష్మణ సమేతంగా అయోధ్యకు తిరిగి వచ్చిన రోజు. అయోధ్య ప్రజలు ఆనందంతో నగరమంతా ప్రమిదల దీపాలతో అలంకరించారు."
            id.startsWith("ugadi") -> "బ్రహ్మ పురాణం ప్రకారం చైత్ర శుద్ధ పాడ్యమి నాడు సూర్యోదయ సమయంలో బ్రహ్మదేవుడు విశ్వ కాల చక్రాన్ని, సృష్టి సమస్తాన్ని ప్రారంభించిన మహోన్నత చారిత్రక దినం."
            id.startsWith("sri_rama_navami") -> "త్రేతాయుగంలో అయోధ్య రాజైన దశరథ మహారాజు కౌసల్యాదేవికి పుత్రకామేష్టి యజ్ఞ ఫలితంగా చైత్ర శుద్ధ నవమి నాడు శ్రీరాముడు పునర్వసు నక్షత్రంలో జన్మించాడు."
            id.startsWith("hanuman_jayanti") -> "చైత్ర పూర్ణిమ నాడు అంజనాదేవికి పరమశివుని రుద్రాంశ సంభూతుడిగా వాయుదేవుని అనుగ్రహంతో అనంత శక్తివంతుడైన హనుమంతుడు ఉద్భవించాడు."
            id.startsWith("maha_shivaratri") -> "క్షీరసాగర మథనంలో ఉద్భవించిన హాలాహల విషాన్ని లోక రక్షణార్థం పరమశివుడు తన కంఠంలో ధరించి నీలకంఠుడిగా మారిన దివ్య రాత్రి."
            id.startsWith("holi") -> "శ్రీకృష్ణుడు రాధాదేవి మరియు గోపికలతో కలసి బృందావనంలో వసంత ఉత్సవాన్ని రంగులతో వేడుకగా జరుపుకున్న సంతోషకరమైన పౌరాణిక సంప్రదాయం."
            id.startsWith("holika_dahan") -> "రాక్షస రా జైన హిరణ్యకశిపుని సోదరి హోలికా అగ్నిలో భక్త ప్రహ్లాదుడిని దహనం చేయాలని చూసినప్పుడు, భగవదనుగ్రహంతో హోలికా దహనమై ప్రహ్లాదుడు సురక్షితంగా మిగిలాడు."
            id.startsWith("vasant_panchami") -> "సృష్టికి జ్ఞానం, వాక్కు, సంగీతం ప్రసాదించడానికి బ్రహ్మదేవుని ముఖం నుండి వీణాపాణి అయిన శ్రీ సరస్వతీ దేవి ఉద్భవించిన పవిత్ర దివసం."
            id.startsWith("akshaya_tritiya") -> "మహాభారత రచన ప్రారంభమైన దినం; వేదవ్యాసుడు చెప్తుండగా వినాయకుడు భారత కావ్యాన్ని లిఖించడం ప్రారంభించిన అక్షయ పుణ్య ఘడియలు."
            id.startsWith("ratha_saptami") -> "విశ్వానికి కాంతిని, జీవాన్ని ఇచ్చే సూర్య భగవానుడు ఏడు అశ్వాల (ఏడు వర్ణాలు) తేజో రథంపై లోకానికి దర్శనమిచ్చి సూర్య జయంతిగా వెలిగిన రోజు."
            id.startsWith("guru_purnima") -> "నాలుగు వేదాలను విభజించి, 18 పురాణాలను, మహాభారతాన్ని మానవాళికి అందించిన మహర్షి వేదవ్యాసుడు జన్మించిన పవిత్ర వ్యాస పూర్ణిమ."
            id.startsWith("varalakshmi") -> "శివుడు పార్వతీదేవికి ఉపదేశించిన వ్రతం; చారుమతీ దేవి అనే పతివ్రతకు కలకత్తా/కుండిన నగరంలో శ్రీలక్ష్మీదేవి స్వప్నంలో కనిపించి వ్రత విధానాన్ని తెలియజేసింది."
            id.startsWith("raksha_bandhan") -> "దేవాసుర యుద్ధంలో ఇంద్రునికి ఇంద్రాణి రక్షా సూత్రం కట్టడం మరియు శ్రీకృష్ణునికి ద్రౌపది తన చీర కొంగు తుంచి కట్టిన రక్షణ బంధం."
            id.startsWith("vijayadashami") -> "పాండవులు అజ్ఞాతవాసం పూర్తిచేసి శమీ చెట్టుపై దాచిన తమ దివ్య ఆయుధాలను తీసి పూజించి విజయం సాధించిన శుభ సందర్భం."
            id.startsWith("durga_ashtami") -> "చాముండేశ్వరీ రూపాన్ని ధరించిన జగన్మాత దుర్గాదేవి రక్తాక్షులను, చండముండులను సంహరించిన అత్యంత शक्तिవంతమైన రోజిన పూజ."
            id.startsWith("karwa_chauth") -> "పతివ్రత వీరావతి కథ మరియు సావిత్రి యమధర్మరాజు నుండి సత్యవంతుని ప్రాణాలను తిరిగి దక్కించుకున్న పవిత్ర పురాణ గాథ."
            id.startsWith("dhanteras") -> "దేవదానవులు అమృతం కోసం సమద్ర మథనం చేసినప్పుడు వైద్య శాస్త్ర పితామహుడు ధన్వంతరి అమృత కలశంతో లోకానికి ప్రత్యక్షమయ్యాడు."
            id.startsWith("diwali") -> "శ్రీరాముడు రావణాసురుని వధించి 14 సంవత్సరాల తర్వాత అయోధ్యకు తిరిగొచ్చిన వేడుకతో పాటు నరకాసుర వధ ముగిసిన ఆనందోత్సవం."
            id.startsWith("gita_jayanti") -> "కురుక్షేత్ర రణరంగంలో శోకమోహితుడైన అర్జునునికి మోక్షమార్గాన్ని, కర్మ సిద్ధాంతాన్ని వివరిస్తూ శ్రీకృష్ణుడు భగవద్గీతను అందించిన ఘట్టం."
            id.startsWith("vaikuntha_ekadashi") -> "శ్రీమహావిష్ణువు వైకుంఠ ద్వారాలు తెరచి సకల దేవతలతో కలసి ముక్కోటి దేవతలకు దర్శనమిచ్చిన పవిత్ర ముక్కోటి ఏకాదశి."
            id.startsWith("anant_chaturdashi") -> "మహాభారతంలో పాండవులు అరణ్యవాస కష్టాల నుండి విముక్తి పొందడానికి శ్రీకృష్ణుని ఆదేశానుసారం ఈ అనంత పద్మనాభ స్వామి వ్రతాన్ని ఆచరించి పూర్వ వైభవాన్ని పొందారు."
            id.startsWith("pitru_paksha_end") -> "మహాభారతంలో కర్ణుడు స్వర్గానికి చేరినప్పుడు కేవలం బంగారం, రత్నాలు లభించగా, పితృ పక్షంలో భూమిపై తిరిగి వచ్చి అన్నదానం ఆచరించడం వల్ల ఆయన ఆకలి తీరి తృప్తి పొందాడనేది పురాణ కథ."
            id.startsWith("sharad_navratri_start") -> "శ్రీరాముడు లంకా విజయం కోసం శరదృతువులో దేవీ నవరాత్రులను నిష్టతో పూజించి దుర్గామాత అనుగ్రహం పొంది రావణాసురుని సంహరించాడు."
            else -> defaultHistory
        }
    }

    // 17. Translate Deity
    fun translateDeity(deity: String, lang: AppLanguage): String {
        val te = mapOf(
            "Lord Vighnaharta Ganesha" to "శ్రీ విఘ్నేశ్వరుడు (గణపతి)",
            "Lord Ganesha" to "శ్రీ గణపతి",
            "Surya Deva" to "సూర్య భగవానుడు",
            "Surya & Agni" to "సూర్యుడు & అగ్నిదేవుడు",
            "Lord Vishnu" to "శ్రీ మహావిష్ణువు",
            "Maa Saraswati" to "శ్రీ సరస్వతీ దేవి",
            "Surya / Ratha Saptami" to "సూర్య భగవానుడు",
            "Lord Shiva" to "పరమశివుడు",
            "Lord Shiva & Parvati" to "శివపార్వతులు",
            "Sri Rama" to "శ్రీరామచంద్ర ప్రభువు",
            "Sri Rama & Sita" to "సీతారాములు",
            "Lord Hanuman" to "శ్రీ ఆంజనేయ స్వామి",
            "Maa Lakshmi & Kubera" to "శ్రీ లక్ష్మీ కుబేరులు",
            "Lord Narasimha" to "శ్రీ లక్ష్మీ నృసింహ స్వామి",
            "Goddess Varalakshmi" to "శ్రీ వరలక్ష్మీ దేవి",
            "Lord Krishna" to "శ్రీకృష్ణ భగవానుడు",
            "Bhagavan Sri Krishna" to "శ్రీకృష్ణ పరమాత్ముడు",
            "Maa Durga" to "శ్రీ దుర్గాదేవి",
            "Maa Mahalakshmi" to "శ్రీ మహాలక్ష్మి",
            "Ancestors (Pitras) & Yama" to "పితృదేవతలు & యమధర్మరాజు",
            "Lord Venkateswara" to "శ్రీ వేంకటేశ్వర స్వామి",
            "Lord Vishnu & Ganesha" to "శ్రీ మహావిష్ణువు & శ్రీ విఘ్నేశ్వరుడు"
        )
        val hi = mapOf(
            "Lord Vighnaharta Ganesha" to "भगवान श्री विघ्नहर्ता गणेश",
            "Lord Ganesha" to "भगवान गणेश",
            "Surya Deva" to "भगवान सूर्य देव",
            "Surya & Agni" to "सूर्य एवं अग्नि देव",
            "Lord Vishnu" to "भगवान श्री विष्णु",
            "Maa Saraswati" to "माता सरस्वती",
            "Surya / Ratha Saptami" to "भगवान सूर्य देव",
            "Lord Shiva" to "भगवान शिव",
            "Lord Shiva & Parvati" to "शिव-पार्वती",
            "Sri Rama" to "मर्यादा पुरुषोत्तम श्री राम",
            "Sri Rama & Sita" to "श्री सीता-राम",
            "Lord Hanuman" to "श्री हनुमान जी",
            "Maa Lakshmi & Kubera" to "माता लक्ष्मी एवं कुबेर देव",
            "Lord Narasimha" to "भगवान श्री नृसिंह",
            "Goddess Varalakshmi" to "माता वरलक्ष्मी",
            "Lord Krishna" to "भगवान श्री कृष्ण",
            "Bhagavan Sri Krishna" to "भगवान श्री कृष्ण",
            "Maa Durga" to "माँ दुर्गा",
            "Maa Mahalakshmi" to "माता महालक्ष्मी",
            "Ancestors (Pitras) & Yama" to "पितृदेव एवं यमराज",
            "Lord Venkateswara" to "भगवान श्री वेंकटेश्वर",
            "Lord Vishnu & Ganesha" to "भगवान विष्णु एवं गणेश"
        )
        val ta = mapOf(
            "Lord Vighnaharta Ganesha" to "ஸ்ரீ விநாயகப் பெருமான்",
            "Lord Ganesha" to "ஸ்ரீ கணபதி",
            "Surya Deva" to "சூரிய பகவான்",
            "Lord Vishnu" to "ஸ்ரீ மகாவிஷ்ணு",
            "Maa Saraswati" to "சரஸ்வதி தாயார்",
            "Lord Shiva" to "சிவபெருமான்",
            "Lord Shiva & Parvati" to "சிவன் & பார்வதி",
            "Sri Rama" to "ஸ்ரீ ராமர்",
            "Lord Hanuman" to "ஸ்ரீ அனுமன்",
            "Goddess Varalakshmi" to "வரலட்சுமி தாயார்",
            "Lord Krishna" to "ஸ்ரீ கிருஷ்ணர்",
            "Bhagavan Sri Krishna" to "ஸ்ரீ கிருஷ்ணர்",
            "Maa Durga" to "துர்க்கை அம்மன்",
            "Maa Mahalakshmi" to "மகாலட்சுமி தாயார்"
        )
        val kn = mapOf(
            "Lord Vighnaharta Ganesha" to "ಶ್ರೀ ವಿಘ್ನನಿವಾರಕ ಗಣೇಶ",
            "Lord Ganesha" to "ಶ್ರೀ ಗಣಪತಿ",
            "Surya Deva" to "ಸೂರ್ಯ ಭಗವಾನ್",
            "Lord Vishnu" to "ಶ್ರೀ ಮಹಾವಿಷ್ಣು",
            "Maa Saraswati" to "ಸರಸ್ವತೀ ದೇವಿ",
            "Lord Shiva" to "ಪರಮಶಿವ",
            "Lord Shiva & Parvati" to "ಶಿವ-ಪಾರ್ವತಿ",
            "Sri Rama" to "ಶ್ರೀ ರಾಮಚಂದ್ರ",
            "Lord Hanuman" to "ಶ್ರೀ ಆಂಜನೇಯ",
            "Goddess Varalakshmi" to "ವರಮಹಾಲಕ್ಷ್ಮಿ ದೇವಿ",
            "Lord Krishna" to "ಶ್ರೀ ಕೃಷ್ಣ",
            "Bhagavan Sri Krishna" to "ಶ್ರೀ ಕೃಷ್ಣ ಪರಮಾತ್ಮ",
            "Maa Durga" to "ದುರ್ಗಾದೇವಿ",
            "Maa Mahalakshmi" to "ಮಹಾಲಕ್ಷ್ಮಿ"
        )

        return when (lang) {
            AppLanguage.TE -> te[deity] ?: deity
            AppLanguage.HI, AppLanguage.MR -> hi[deity] ?: deity
            AppLanguage.TA -> ta[deity] ?: deity
            AppLanguage.KN -> kn[deity] ?: deity
            else -> deity
        }
    }

    // 18. Samvatsara Translator
    fun translateSamvatsara(samvatsara: String, lang: AppLanguage): String {
        val mapped = when (lang) {
            AppLanguage.TE -> {
                val mapping = mapOf(
                    "Prabhava" to "ప్రభవ", "Vibhava" to "విభవ", "Shukla" to "శుక్ల", "Pramoda" to "ప్రమోదూత", "Prajapati" to "ప్రజోత్పత్తి",
                    "Angirasa" to "ఆంగీరస", "Shrimukha" to "శ్రీముఖ", "Bhava" to "భవ", "Yuva" to "యువ", "Dhatri" to "ధాత",
                    "Ishwara" to "ఈశ్వర", "Bahudhanya" to "బహుధాన్య", "Pramathi" to "ప్రమాది", "Vikrama" to "విక్రమ", "Vrisha" to "వృష",
                    "Chitrabhanu" to "చిత్రభాను", "Subhanu" to "స్వభాను", "Tarana" to "తారణ", "Parthiva" to "పార్థివ", "Vyaya" to "వ్యయ",
                    "Sarvajit" to "సర్వజిత్తు", "Sarvadhari" to "సర్వధారి", "Virodhi" to "విరోధి", "Vikrita" to "వికృతి", "Khara" to "ఖర",
                    "Nandana" to "నందన", "Vijaya" to "విజయ", "Jaya" to "జయ", "Manmatha" to "మన్మథ", "Durmukha" to "దుర్ముఖి",
                    "Hemalamba" to "హేవిళంబి", "Vilamba" to "విళంబి", "Vikari" to "వికారి", "Sharvari" to "శార్వరి", "Plava" to "ప్లవ",
                    "Shubhakrit" to "శుభకృతు", "Sobhakrit" to "శోభకృతు", "Krodhi" to "క్రోధి", "Vishwavasu" to "విశ్వావసు", "Parabhava" to "పరాభవ",
                    "Plavanga" to "ప్లవంగ", "Kilaka" to "కీలక", "Saumya" to "సౌమ్య", "Sadharana" to "సాధారణ", "Virodhakrit" to "విరోధికృతు",
                    "Paridhavi" to "పరీధావి", "Pramadicha" to "ప్రమాదీచ", "Ananda" to "ఆనంద", "Rakshasa" to "రాక్షస", "Anala" to "నల",
                    "Pingala" to "పింగళ", "Kalayukta" to "కాళయుక్తి", "Siddharthi" to "సిద్ధార్థి", "Raudri" to "రౌద్రి", "Durmati" to "దుర్మతి",
                    "Dundubhi" to "దుందుభి", "Rudhrodgari" to "రుధిరోద్గారి", "Raktakshi" to "రక్తాక్షి", "Krodhana" to "క్రోధన", "Kshaya" to "అక్షయ"
                )
                mapping[samvatsara] ?: samvatsara
            }
            AppLanguage.HI -> {
                val mapping = mapOf(
                    "Prabhava" to "प्रभव", "Vibhava" to "विभव", "Shukla" to "शुक्ल", "Pramoda" to "प्रमोद", "Prajapati" to "प्रजापति",
                    "Angirasa" to "अंगिरस", "Shrimukha" to "श्रीमुख", "Bhava" to "भव", "Yuva" to "युवा", "Dhatri" to "धाता",
                    "Ishwara" to "ईश्वर", "Bahudhanya" to "बहुधान्य", "Pramathi" to "प्रमाथी", "Vikrama" to "विक्रम", "Vrisha" to "वृष",
                    "Chitrabhanu" to "चित्रभानु", "Subhanu" to "सुभानु", "Tarana" to "तारण", "Parthiva" to "पार्थिव", "Vyaya" to "व्यय",
                    "Sarvajit" to "सर्वजित", "Sarvadhari" to "सर्वधारी", "Virodhi" to "विरोधी", "Vikrita" to "विकृत", "Khara" to "खर",
                    "Nandana" to "नन्दन", "Vijaya" to "विजय", "Jaya" to "जय", "Manmatha" to "मन्मथ", "Durmukha" to "दुर्मुख",
                    "Hemalamba" to "हेमलम्ब", "Vilamba" to "विलम्ब", "Vikari" to "विकारी", "Sharvari" to "शार्वरी", "Plava" to "प्लव",
                    "Shubhakrit" to "शुभकृत", "Sobhakrit" to "शोभकृत", "Krodhi" to "क्रोधी", "Vishwavasu" to "विश्ववसु", "Parabhava" to "पराभव",
                    "Plavanga" to "प्लवंग", "Kilaka" to "कीलक", "Saumya" to "सौम्य", "Sadharana" to "साधारण", "Virodhakrit" to "विरोधकृत",
                    "Paridhavi" to "परिधावी", "Pramadicha" to "प्रमादीचा", "Ananda" to "आनन्द", "Rakshasa" to "राक्षस", "Anala" to "अनल",
                    "Pingala" to "पिंगल", "Kalayukta" to "कालयुक्त", "Siddharthi" to "सिद्धार्थी", "Raudri" to "रौद्र", "Durmati" to "दुर्मति",
                    "Dundubhi" to "दुन्दुभि", "Rudhrodgari" to "रुधिरोद्गारी", "Raktakshi" to "रक्ताक्ष", "Krodhana" to "क्रोधन", "Kshaya" to "क्षय"
                )
                mapping[samvatsara] ?: samvatsara
            }
            AppLanguage.TA -> {
                val mapping = mapOf(
                    "Prabhava" to "பிரபவ", "Vibhava" to "விபவ", "Shukla" to "சுக்ல", "Pramoda" to "பிரமோதூத", "Prajapati" to "பிரஜாபதி",
                    "Angirasa" to "ஆங்கீரச", "Shrimukha" to "ஸ்ரீமுக", "Bhava" to "பவ", "Yuva" to "யுவ", "Dhatri" to "தாது",
                    "Ishwara" to "ஈஸ்வர", "Bahudhanya" to "பஹுதான்ய", "Pramathi" to "பிரமாதி", "Vikrama" to "விக்ரம", "Vrisha" to "விருஷ",
                    "Chitrabhanu" to "சித்ரபானு", "Subhanu" to "சுபானு", "Tarana" to "தாரண", "Parthiva" to "பார்திவ", "Vyaya" to "வ்யய",
                    "Sarvajit" to "சர்வஜித்", "Sarvadhari" to "சர்வதாரி", "Virodhi" to "விரோதி", "Vikrita" to "விக்ருதி", "Khara" to "கர",
                    "Nandana" to "நந்தன", "Vijaya" to "விஜய", "Jaya" to "ஜய", "Manmatha" to "மன்மத", "Durmukha" to "துர்முகி",
                    "Hemalamba" to "ஹேவிளம்பி", "Vilamba" to "விளம்பி", "Vikari" to "விகாரி", "Sharvari" to "சார்வரி", "Plava" to "பிலவ",
                    "Shubhakrit" to "சுபகிருது", "Sobhakrit" to "சோபகிருது", "Krodhi" to "க்ரோதி", "Vishwavasu" to "விசுவாசு", "Parabhava" to "பராபவ",
                    "Plavanga" to "பிலவங்க", "Kilaka" to "கீலக", "Saumya" to "சௌமிய", "Sadharana" to "சாதாரண", "Virodhakrit" to "விரோதகிருது",
                    "Paridhavi" to "பரிதாபி", "Pramadicha" to "பிரமாதீச", "Ananda" to "ஆனந்த", "Rakshasa" to "ராட்சஸ", "Anala" to "நள",
                    "Pingala" to "பிங்கள", "Kalayukta" to "காளயுக்தி", "Siddharthi" to "சித்தார்த்தி", "Raudri" to "ரௌத்திரி", "Durmati" to "துர்மதி",
                    "Dundubhi" to "துந்துபி", "Rudhrodgari" to "ருத்ரோத்காரி", "Raktakshi" to "ரக்தாட்சி", "Krodhana" to "க்ரோதன", "Kshaya" to "அட்சய"
                )
                mapping[samvatsara] ?: samvatsara
            }
            AppLanguage.KN -> {
                val mapping = mapOf(
                    "Prabhava" to "ಪ್ರಭವ", "Vibhava" to "ವಿಭವ", "Shukla" to "ಶುಕ್ಲ", "Pramoda" to "ಪ್ರಮೋದೂತ", "Prajapati" to "ಪ್ರಜೋತ್ಪತ್ತಿ",
                    "Angirasa" to "ಆಂಗೀರಸ", "Shrimukha" to "ಶ್ರೀಮುಖ", "Bhava" to "ಭವ", "Yuva" to "ಯುವ", "Dhatri" to "ಧಾತ",
                    "Ishwara" to "ಈಶವರ", "Bahudhanya" to "ಬಹುಧಾನ್ಯ", "Pramathi" to "ಪ್ರಮಾದಿ", "Vikrama" to "ವಿಕ್ರಮ", "Vrisha" to "ವೃಷ",
                    "Chitrabhanu" to "ಚಿತ್ರಭಾನು", "Subhanu" to "ಸ್ವಭಾನು", "Tarana" to "ತಾರಣ", "Parthiva" to "ಪಾರ್ಥಿವ", "Vyaya" to "ವ್ಯಯ",
                    "Sarvajit" to "ಸರ್ವಜಿತ್ತು", "Sarvadhari" to "ಸರ್ವಧಾರಿ", "Virodhi" to "ವಿರೋಧಿ", "Vikrita" to "ವಿಕೃತಿ", "Khara" to "ಖರ",
                    "Nandana" to "ನಂದನ", "Vijaya" to "ವಿಜಯ", "Jaya" to "ಜಯ", "Manmatha" to "ಮನ್ಮಥ", "Durmukha" to "ದುರ್ಮುಖಿ",
                    "Hemalamba" to "ಹೇವಿಳಂಬಿ", "Vilamba" to "ವಿಳಂಬಿ", "Vikari" to "ವಿಕಾರಿ", "Sharvari" to "ಶಾರ್ವರಿ", "Plava" to "ಪ್ಲವ",
                    "Shubhakrit" to "ಶುಭಕೃತು", "Sobhakrit" to "ಶೋಭಕೃತು", "Krodhi" to "ಕ್ರೋಧಿ", "Vishwavasu" to "ವಿಶ್ವಾವಸು", "Parabhava" to "ಪರಾಭವ",
                    "Plavanga" to "ಪ್ಲವಂಗ", "Kilaka" to "ಕೀಲಕ", "Saumya" to "ಸೌಮ್ಯ", "Sadharana" to "ಸಾಧಾರಣ", "Virodhakrit" to "ವಿರೋಧಿಕೃತು",
                    "Paridhavi" to "ಪರೀಧಾವಿ", "Pramadicha" to "ಪ್ರಮಾದೀಚ", "Ananda" to "ಆನಂದ", "Rakshasa" to "ರಾಕ್ಷಸ", "Anala" to "ನಳ",
                    "Pingala" to "ಪಿಂಗಳ", "Kalayukta" to "ಕಾಳಯುಕ್ತಿ", "Siddharthi" to "ಸಿದ್ಧಾರ್ಥಿ", "Raudri" to "ರೌದ್ರಿ", "Durmati" to "ದುರ್ಮತಿ",
                    "Dundubhi" to "ದುಂದುಭಿ", "Rudhrodgari" to "ರುಧಿರೋದ್ಗಾರಿ", "Raktakshi" to "ರಕ್ತಾಕ್ಷಿ", "Krodhana" to "ಕ್ರೋಧನ", "Kshaya" to "ಅಕ್ಷಯ"
                )
                mapping[samvatsara] ?: samvatsara
            }
            else -> samvatsara
        }

        return when (lang) {
            AppLanguage.TE -> "$mapped నామ సంవత్సరం"
            AppLanguage.HI -> "$mapped संवत्सर"
            AppLanguage.TA -> "$mapped வருடம்"
            AppLanguage.KN -> "$mapped ಸಂವತ್ಸರ"
            else -> "$mapped Samvatsara"
        }
    }

    // 19. Ayana Translator
    fun translateAyana(ayana: String, lang: AppLanguage): String {
        return when {
            ayana.contains("Dakshinayana", ignoreCase = true) || ayana.contains("దక్షిణాయనం") -> when (lang) {
                AppLanguage.TE -> "దక్షిణాయనం"
                AppLanguage.HI -> "दक्षिणायन"
                AppLanguage.TA -> "தட்சிணாயணம்"
                AppLanguage.KN -> "ದಕ್ಷಿಣಾಯನ"
                else -> "Dakshinayana"
            }
            ayana.contains("Uttarayana", ignoreCase = true) || ayana.contains("ఉత్తరాయణం") -> when (lang) {
                AppLanguage.TE -> "ఉత్తరాయణం"
                AppLanguage.HI -> "उत्तरायण"
                AppLanguage.TA -> "உத்தராயணம்"
                AppLanguage.KN -> "ಉತ್ತರಾಯಣ"
                else -> "Uttarayana"
            }
            else -> ayana
        }
    }

    // 20. Ritu Translator
    fun translateRitu(ritu: String, lang: AppLanguage): String {
        val isVasanta = ritu.contains("Vasanta", ignoreCase = true) || ritu.contains("వసంత")
        val isGrishma = ritu.contains("Grishma", ignoreCase = true) || ritu.contains("గ్రీష్మ")
        val isVarsha = ritu.contains("Varsha", ignoreCase = true) || ritu.contains("వర్ష")
        val isSharad = ritu.contains("Sharad", ignoreCase = true) || ritu.contains("శరద్")
        val isHemanta = ritu.contains("Hemanta", ignoreCase = true) || ritu.contains("హేమంత")
        val isShishira = ritu.contains("Shishira", ignoreCase = true) || ritu.contains("శిశిర")

        return when {
            isVasanta -> when (lang) {
                AppLanguage.TE -> "వసంత ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "वसंत ऋतु"
                AppLanguage.TA -> "வசந்த ருது"
                AppLanguage.KN -> "ವಸಂತ ಋತು"
                else -> "Vasanta Ritu"
            }
            isGrishma -> when (lang) {
                AppLanguage.TE -> "గ్రీష్మ ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "ग्रीष्म ऋतु"
                AppLanguage.TA -> "கிரீஷ்ம ருது"
                AppLanguage.KN -> "ಗ್ರೀಷ್ಮ ಋತು"
                else -> "Grishma Ritu"
            }
            isVarsha -> when (lang) {
                AppLanguage.TE -> "వర్ష ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "वर्षा ऋतु"
                AppLanguage.TA -> "வர்ஷ ருது"
                AppLanguage.KN -> "ವರ್ಷ ಋತು"
                else -> "Varsha Ritu"
            }
            isSharad -> when (lang) {
                AppLanguage.TE -> "శరద్ ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "शरद ऋतु"
                AppLanguage.TA -> "சரத் ருது"
                AppLanguage.KN -> "ಶರದೃತು"
                else -> "Sharad Ritu"
            }
            isHemanta -> when (lang) {
                AppLanguage.TE -> "హేమంత ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "हेमंत ऋतु"
                AppLanguage.TA -> "ஹேமந்த ருது"
                AppLanguage.KN -> "ಹೇಮಂತ ಋತು"
                else -> "Hemanta Ritu"
            }
            isShishira -> when (lang) {
                AppLanguage.TE -> "శిశిర ఋతువు"
                AppLanguage.HI, AppLanguage.MR -> "शिशिर ऋतु"
                AppLanguage.TA -> "சிசிர ருது"
                AppLanguage.KN -> "ಶಿಶಿರ ಋತು"
                else -> "Shishira Ritu"
            }
            else -> ritu
        }
    }

    // 21. Yoga & Nature Translator
    fun translateYoga(yoga: String, lang: AppLanguage): String {
        val te = mapOf(
            "Vishkambha" to "విష్కంభ", "Priti" to "ప్రీతి", "Ayushman" to "ఆయుష్మాన్", "Saubhagya" to "సౌభాగ్య",
            "Shobhana" to "శోభన", "Atiganda" to "అతిగండ", "Sukarma" to "సుకర్మ", "Dhriti" to "ధృతి",
            "Shula" to "శూల", "Ganda" to "గండ", "Vriddhi" to "వృద్ధి", "Dhruva" to "ధ్రువ",
            "Vyaghata" to "వ్యాఘాత", "Harshana" to "హర్షణ", "Vajra" to "వజ్ర", "Siddhi" to "సిద్ధి",
            "Vyatipata" to "వ్యతీపాత", "Variyan" to "వరీయాన్", "Parigha" to "పరిగ", "Shiva" to "శివ",
            "Siddha" to "సిద్ధ", "Sadhya" to "సాధ్య", "Shubha" to "శుభ", "Shukla" to "శుక్ల",
            "Brahma" to "బ్రహ్మ", "Indra" to "ఇంద్ర", "Vaidhriti" to "వైధృతి"
        )
        val hi = mapOf(
            "Vishkambha" to "विष्कम्भ", "Priti" to "प्रीति", "Ayushman" to "आयुष्मान", "Saubhagya" to "सौभाग्य",
            "Shobhana" to "शोभन", "Atiganda" to "अतिगण्ड", "Sukarma" to "सुकर्मा", "Dhriti" to "धृति",
            "Shula" to "शूल", "Ganda" to "गण्ड", "Vriddhi" to "वृद्धि", "Dhruva" to "ध्रुव",
            "Vyaghata" to "व्याघात", "Harshana" to "हर्षण", "Vajra" to "वज्र", "Siddhi" to "सिद्धि",
            "Vyatipata" to "व्यतीपात", "Variyan" to "वरीयान्", "Parigha" to "परिघ", "Shiva" to "शिव",
            "Siddha" to "सिद्ध", "Sadhya" to "साध्य", "Shubha" to "शुभ", "Shukla" to "शुक्ल",
            "Brahma" to "ब्रह्म", "Indra" to "इन्द्र", "Vaidhriti" to "वैधृति"
        )
        return when (lang) {
            AppLanguage.TE -> te[yoga] ?: yoga
            AppLanguage.HI, AppLanguage.MR -> hi[yoga] ?: yoga
            else -> yoga
        }
    }

    fun translateYogaNature(nature: String, lang: AppLanguage): String {
        return when {
            nature.contains("Auspicious", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "శుభ యోగం"
                AppLanguage.HI -> "शुभ योग"
                AppLanguage.TA -> "சுப யோகம்"
                AppLanguage.KN -> "ಶುಭ ಯೋಗ"
                else -> "Auspicious"
            }
            nature.contains("Inauspicious", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "అశుభ యోగం"
                AppLanguage.HI -> "अशुभ योग"
                AppLanguage.TA -> "அசுப யோகம்"
                AppLanguage.KN -> "ಅಶುಭ ಯೋಗ"
                else -> "Inauspicious"
            }
            else -> nature
        }
    }

    // 22. Karana & Category Translator
    fun translateKarana(karana: String, lang: AppLanguage): String {
        val te = mapOf(
            "Bava" to "బవ", "Balava" to "బాలవ", "Kaulava" to "కౌలవ", "Taitila" to "తైతిల",
            "Gara" to "గర", "Vanija" to "వణిజ", "Vishti" to "విష్టి (భద్ర)", "Shakuni" to "శకుని",
            "Chatushpada" to "చతుష్పాద", "Naga" to "నాగ", "Kimstughna" to "కింస్తుఘ్న"
        )
        val hi = mapOf(
            "Bava" to "बव", "Balava" to "बालव", "Kaulava" to "कौलव", "Taitila" to "तैतिल",
            "Gara" to "गर", "Vanija" to "वणिज", "Vishti" to "विष्टि (भद्रा)", "Shakuni" to "शकुनि",
            "Chatushpada" to "चतुष्पद", "Naga" to "नाग", "Kimstughna" to "किंस्तुघ्न"
        )
        return when (lang) {
            AppLanguage.TE -> te[karana] ?: karana
            AppLanguage.HI, AppLanguage.MR -> hi[karana] ?: karana
            else -> karana
        }
    }

    fun translateKaranaCategory(cat: String, lang: AppLanguage): String {
        return when {
            cat.contains("Movable", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "చర కరణం"
                AppLanguage.HI -> "चर करण"
                AppLanguage.TA -> "சர கரணம்"
                AppLanguage.KN -> "ಚರ ಕರಣ"
                else -> "Movable Karana"
            }
            cat.contains("Fixed", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "స్థిర కరణం"
                AppLanguage.HI -> "स्थिर करण"
                AppLanguage.TA -> "ஸ்திர கரணம்"
                AppLanguage.KN -> "ಸ್ಥಿರ ಕರಣ"
                else -> "Fixed Karana"
            }
            else -> cat
        }
    }

    // 23. Moon Phase Translator
    fun translateMoonPhase(phase: String, lang: AppLanguage): String {
        return when {
            phase.contains("Waxing", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "శుక్ల పక్షం (చంద్రవృద్ధి)"
                AppLanguage.HI -> "शुक्ल पक्ष (चंद्र वृद्धि)"
                AppLanguage.TA -> "வளர்பிறை"
                AppLanguage.KN -> "ಶುಕ್ಲ ಪಕ್ಷ (ಬೆಳೆಯುವ ಚಂದ್ರ)"
                else -> phase
            }
            phase.contains("Waning", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "కృష్ణ పక్షం (చంద్రక్షీణత)"
                AppLanguage.HI -> "कृष्ण पक्ष (चंद्र क्षय)"
                AppLanguage.TA -> "தேய்பிறை"
                AppLanguage.KN -> "ಕೃಷ್ಣ ಪಕ್ಷ (ಕ್ಷೀಣಿಸುವ ಚಂದ್ರ)"
                else -> phase
            }
            phase.contains("Full Moon", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "పూర్ణిమ (పూర్ణ చంద్రుడు)"
                AppLanguage.HI -> "पूर्णिमा (पूर्ण चंद्र)"
                AppLanguage.TA -> "பௌர்ணமி"
                AppLanguage.KN -> "ಹುಣ್ಣಿಮೆ"
                else -> phase
            }
            phase.contains("New Moon", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "అమావాస్య (నవ చంద్రుడు)"
                AppLanguage.HI -> "अमावस्या (नव चंद्र)"
                AppLanguage.TA -> "அமாவாசை"
                AppLanguage.KN -> "ಅಮಾವಾಸ್ಯೆ"
                else -> phase
            }
            else -> phase
        }
    }

    // 24. Timing Note Translator
    fun translateTimingNote(note: String, lang: AppLanguage): String {
        val te = mapOf(
            "Most auspicious midday window" to "మధ్యాహ్న అత్యంత శుభ ముహూర్తం",
            "Supreme for meditation & morning prayers" to "ధ్యానం & ప్రాతః సంధ్యావందనానికి సర్వోత్కృష్టం",
            "Auspicious nectar period" to "అమృత కాలం, సర్వకార్య సిద్ధి ప్రదం",
            "Avoid commencing important new work" to "ముఖ్యమైన నూతన కార్యాలు ప్రారంభించవద్దు",
            "Inauspicious period" to "అశుభ కాలం, జాగ్రత్త అవసరం",
            "Saturnian period" to "శని ప్రభావ కాలం",
            "Discarded period of current Nakshatra" to "నక్షత్ర వర్జ్యం, నిషేధ సమయం",
            "Afflicted daytime Muhurta" to "దిన ప్రమాణ వర్జ్య ముహూర్తం"
        )
        val hi = mapOf(
            "Most auspicious midday window" to "मध्याह्न का सर्वाधिक शुभ मुहूर्त",
            "Supreme for meditation & morning prayers" to "ध्यान एवं प्रातः पूजन हेतु सर्वश्रेष्ठ वेला",
            "Auspicious nectar period" to "अमृत काल, समस्त शुभ कार्यों के लिए उत्तम",
            "Avoid commencing important new work" to "महत्वपूर्ण नवीन कार्य प्रारंभ न करें",
            "Inauspicious period" to "अशुभ काल, विशेष सावधानी अपेक्षित",
            "Saturnian period" to "शनि प्रभाव युक्त काल",
            "Discarded period of current Nakshatra" to "नक्षत्र का त्याज्य काल",
            "Afflicted daytime Muhurta" to "दूषित दिन मुहूर्त"
        )
        val ta = mapOf(
            "Most auspicious midday window" to "மதியத்தின் மிகச் சிறந்த சுபவேளை",
            "Supreme for meditation & morning prayers" to "தியானம் மற்றும் காலை பூஜைக்கு உகந்தது",
            "Auspicious nectar period" to "அமிர்த காலம், சுப காரியங்களுக்கு நன்று",
            "Avoid commencing important new work" to "சுப காரியங்களைத் தவிர்க்கவும்",
            "Inauspicious period" to "அசுப காலம்",
            "Saturnian period" to "குளிக காலம்",
            "Discarded period of current Nakshatra" to "வர்ஜ்யம், விலக்கப்பட வேண்டிய நேரம்",
            "Afflicted daytime Muhurta" to "துர்முகூர்த்தம்"
        )
        val kn = mapOf(
            "Most auspicious midday window" to "ಮಧ್ಯಾಹ್ನದ ಅತ್ಯಂತ ಶುಭ ಮುಹೂರ್ತ",
            "Supreme for meditation & morning prayers" to "ಧ್ಯಾನ ಹಾಗೂ ಪ್ರಾತಃ ಪೂಜೆಗೆ ಅತ್ಯುತ್ತಮ",
            "Auspicious nectar period" to "ಅಮೃತ ಕಾಲ, ಸರ್ವಕಾರ್ಯ ಸಿದ್ಧಿ",
            "Avoid commencing important new work" to "ಹೊಸ ಕೆಲಸಗಳನ್ನು ಪ್ರಾರಂಭಿಸಬೇಡಿ",
            "Inauspicious period" to "ಅಶುಭ ಕಾಲ",
            "Saturnian period" to "ಗುಳಿಕ ಕಾಲ",
            "Discarded period of current Nakshatra" to "ವರ್ಜ್ಯ ಸಮಯ",
            "Afflicted daytime Muhurta" to "ದುರ್ಮುಹೂರ್ತ"
        )

        return when (lang) {
            AppLanguage.TE -> te[note] ?: note
            AppLanguage.HI, AppLanguage.MR -> hi[note] ?: note
            AppLanguage.TA -> ta[note] ?: note
            AppLanguage.KN -> kn[note] ?: note
            else -> note
        }
    }

    // 25. City Names & States Translator
    fun translateCityName(cityId: String, defaultName: String, lang: AppLanguage): String {
        val cleanKey = cityId.lowercase().trim()
        val cleanDefault = defaultName.lowercase().trim()

        val te = mapOf(
            "hyd" to "హైదరాబాద్", "hyderabad" to "హైదరాబాద్",
            "tpt" to "తిరుపతి", "tirupati" to "తిరుపతి",
            "vga" to "విజయవాడ", "vijayawada" to "విజయవాడ",
            "del" to "న్యూఢిల్లీ", "delhi" to "న్యూఢిల్లీ", "new delhi" to "న్యూఢిల్లీ",
            "vns" to "వారణాసి (కాశీ)", "varanasi" to "వారణాసి (కాశీ)", "varanasi (kashi)" to "వారణాసి (కాశీ)",
            "bom" to "ముంబై", "mumbai" to "ముంబై",
            "maa" to "చెన్నై", "chennai" to "చెన్నై",
            "blr" to "బెంగళూరు", "bengaluru" to "బెంగళూరు", "bangalore" to "బెంగళూరు",
            "ccu" to "కోల్‌కతా", "kolkata" to "కోల్‌కతా", "calcutta" to "కోల్‌కతా",
            "amd" to "అహ్మదాబాద్", "ahmedabad" to "అహ్మదాబాద్",
            "pnq" to "పూణే", "pune" to "పూణే",
            "ujn" to "ఉజ్జయిని", "ujjain" to "ఉజ్జయిని",
            "jpr" to "జైపూర్", "jaipur" to "జైపూర్",
            "cok" to "కొచ్చి", "kochi" to "కొచ్చి",
            "bbi" to "భువనేశ్వర్", "bhubaneswar" to "భువనేశ్వర్",
            "ayodhya" to "అయోధ్య",
            "puri" to "పూరీ (జగన్నాథ ధామం)",
            "haridwar" to "హరిద్వార్",
            "srinagar" to "శ్రీనగర్",
            "guwahati" to "గౌహతి",
            "madurai" to "మదురై",
            "thiruvananthapuram" to "తిరువనంతపురం",
            "visakhapatnam" to "విశాఖపట్నం", "vizag" to "విశాఖపట్నం",
            "warangal" to "వరంగల్", "guntur" to "గుంటూరు", "nellore" to "నెల్లూరు", "kurnool" to "కర్నూలు",
            "rajahmundry" to "రాజమండ్రి", "kakinada" to "కాకినాడ", "anantapur" to "అనంతపురం", "kadapa" to "కడప",
            "karimnagar" to "కరీంనగర్", "khammam" to "ఖమ్మం", "nizamabad" to "నిజామాబాద్",
            "lon" to "లండన్", "london" to "లండన్",
            "nyc" to "న్యూయార్క్", "new_york" to "న్యూయార్క్", "new york" to "న్యూయార్క్",
            "sfo" to "శాన్ జోస్ / బే ఏరియా", "san_francisco" to "శాన్ ఫ్రాన్సిస్కో", "san jose / bay area" to "శాన్ జోస్ / బే ఏరియా",
            "dallas" to "డల్లాస్",
            "sin" to "సింగపూర్", "singapore" to "సింగపూర్",
            "syd" to "సిడ్నీ", "sydney" to "సిడ్నీ",
            "dxb" to "దుబాయ్", "dubai" to "దుబాయ్",
            "tor" to "టొరంటో", "toronto" to "టొరంటో"
        )
        val hi = mapOf(
            "hyd" to "हैदराबाद", "hyderabad" to "हैदराबाद",
            "tpt" to "तिरुपति", "tirupati" to "तिरुपति",
            "vga" to "विजयवाड़ा", "vijayawada" to "विजयवाड़ा",
            "del" to "नई दिल्ली", "delhi" to "नई दिल्ली", "new delhi" to "नई दिल्ली",
            "vns" to "वाराणसी (काशी)", "varanasi" to "वाराणसी (काशी)", "varanasi (kashi)" to "वाराणसी (काशी)",
            "bom" to "मुंबई", "mumbai" to "मुंबई",
            "maa" to "चेन्नई", "chennai" to "चेन्नई",
            "blr" to "बेंगलुरु", "bengaluru" to "बेंगलुरु",
            "ccu" to "कोलकाता", "kolkata" to "कोलकाता",
            "amd" to "अहमदाबाद", "ahmedabad" to "अहमदाबाद",
            "pnq" to "पुणे", "pune" to "पुणे",
            "ujn" to "उज्जैन", "ujjain" to "उज्जैन",
            "jpr" to "जयपुर", "jaipur" to "जयपुर",
            "cok" to "कोच्चि", "kochi" to "कोच्चि",
            "bbi" to "भुवनेश्वर", "bhubaneswar" to "भुवनेश्वर",
            "ayodhya" to "अयोध्या", "puri" to "पुरी (जगन्नाथ धाम)", "haridwar" to "हरिद्वार", "srinagar" to "श्रीनगर", "guwahati" to "गुवाहाटी",
            "madurai" to "मदुरै", "thiruvananthapuram" to "तिरुवनंतपुरम",
            "lon" to "लंदन", "london" to "लंदन",
            "nyc" to "न्यूयॉर्क", "new_york" to "न्यूयॉर्क", "new york" to "न्यूयॉर्क",
            "sfo" to "सैन फ्रांसिस्को / बे एरिया", "san_francisco" to "सैन फ्रांसिस्को",
            "dallas" to "डलास", "sin" to "सिंगापुर", "singapore" to "सिंगापुर",
            "syd" to "सिडनी", "sydney" to "सिडनी", "dxb" to "दुबई", "dubai" to "दुबई",
            "tor" to "टोरंटो", "toronto" to "टोरंटो"
        )
        val ta = mapOf(
            "hyd" to "ஹைதராபாத்", "hyderabad" to "ஹைதராபாத்",
            "tpt" to "திருப்பதி", "tirupati" to "திருப்பதி",
            "vga" to "விஜயவாடா", "vijayawada" to "விஜயவாடா",
            "del" to "புது தில்லி", "delhi" to "புது தில்லி",
            "vns" to "வாரணாசி (காசி)", "varanasi" to "வாரணாசி (காசி)",
            "bom" to "மும்பை", "mumbai" to "மும்பை",
            "maa" to "சென்னை", "chennai" to "சென்னை",
            "blr" to "பெங்களூரு", "bengaluru" to "பெங்களூரு",
            "ccu" to "கொல்கத்தா", "kolkata" to "கொல்கத்தா",
            "madurai" to "மதுரை", "thiruvananthapuram" to "திருவனந்தபுரம்",
            "lon" to "லண்டன்", "london" to "லண்டன்",
            "nyc" to "நியூயார்க்", "new_york" to "நியூயார்க்",
            "dxb" to "துபாய்", "dubai" to "துபாய்",
            "sin" to "சிங்கப்பூர்", "singapore" to "சிங்கப்பூர்",
            "syd" to "சிட்னி", "sydney" to "சிட்னி",
            "tor" to "டொராண்டோ", "toronto" to "டொராண்டோ"
        )
        val kn = mapOf(
            "hyd" to "ಹೈದರಾಬಾದ್", "hyderabad" to "ಹೈದರಾಬಾದ್",
            "tpt" to "ತಿರುಪತಿ", "tirupati" to "ತಿರುಪತಿ",
            "vga" to "ವಿಜಯವಾಡ", "vijayawada" to "ವಿಜಯವಾಡ",
            "del" to "ನವದೆಹಲಿ", "delhi" to "ನವದೆಹಲಿ",
            "vns" to "ವಾರಣಾಸಿ", "varanasi" to "ವಾರಣಾಸಿ",
            "bom" to "ಮುಂಬೈ", "mumbai" to "ಮುಂಬೈ",
            "maa" to "ಚೆನ್ನೈ", "chennai" to "ಚೆನ್ನೈ",
            "blr" to "ಬೆಂಗಳೂರು", "bengaluru" to "ಬೆಂಗಳೂರು",
            "ccu" to "ಕೋಲ್ಕತ್ತಾ", "kolkata" to "ಕೋಲ್ಕತ್ತಾ"
        )

        return when (lang) {
            AppLanguage.TE -> {
                te[cleanKey] 
                    ?: te[cleanDefault] 
                    ?: IndianDistrictsRepository.ALL_DISTRICTS.find { it.id.equals(cleanKey, ignoreCase = true) || it.nameEn.equals(cleanDefault, ignoreCase = true) }?.nameTe 
                    ?: defaultName
            }
            AppLanguage.HI, AppLanguage.MR -> hi[cleanKey] ?: hi[cleanDefault] ?: defaultName
            AppLanguage.TA -> ta[cleanKey] ?: ta[cleanDefault] ?: defaultName
            AppLanguage.KN -> kn[cleanKey] ?: kn[cleanDefault] ?: defaultName
            else -> defaultName
        }
    }

    // 26. Tradition Translator
    fun translateTradition(tradition: CalendarTradition, lang: AppLanguage): String {
        return when (tradition) {
            CalendarTradition.TELUGU -> when (lang) {
                AppLanguage.TE -> "తెలుగు పంచాంగం (అమావాస్యాంత)"
                AppLanguage.HI -> "तेलुगु पंचांग (अमावस्यांत)"
                AppLanguage.TA -> "தெலுங்கு பஞ்சாங்கம்"
                AppLanguage.KN -> "ತೆಲುಗು ಪಂಚಾಂಗ (ಅಮಾವಾಸ್ಯಾಂತ)"
                else -> tradition.title
            }
            CalendarTradition.NORTH_INDIAN -> when (lang) {
                AppLanguage.TE -> "ఉత్తర భారత పద్ధతి (పూర్ణిమాంత పंचांग)"
                AppLanguage.HI -> "उत्तर भारतीय पंचांग (पूर्णिमांत)"
                AppLanguage.TA -> "வட இந்திய பஞ்சாங்கம் (பூர்ணிமாந்த)"
                AppLanguage.KN -> "ಉತ್ತರ ಭಾರತೀಯ ಪದ್ಧತಿ (ಪೂರ್ಣಿಮಾಂತ)"
                else -> tradition.title
            }
            CalendarTradition.TAMIL -> when (lang) {
                AppLanguage.TE -> "తమిళ సౌర పంచాంగం (తిరుక్కణిదం)"
                AppLanguage.HI -> "तमिल सौर पंचांग (तिरुक्कणिद)"
                AppLanguage.TA -> "தமிழ் பஞ்சாங்கம் (திருக்கணிதம்)"
                AppLanguage.KN -> "ತಮಿಳು ಸೌರ ಪಂಚಾಂಗ"
                else -> tradition.title
            }
            CalendarTradition.BENGALI -> when (lang) {
                AppLanguage.TE -> "బెంగాలీ సౌర పంజికా"
                AppLanguage.HI -> "बंगाली सौर पंजिका"
                AppLanguage.TA -> "வங்காள பஞ்சாங்கம்"
                AppLanguage.KN -> "ಬೆಂಗಾಲಿ ಸೌರ ಪಂಜಿಕಾ"
                else -> tradition.title
            }
            CalendarTradition.MALAYALAM -> when (lang) {
                AppLanguage.TE -> "మలయాళ కొల్లవర్షం క్యాలెండర్"
                AppLanguage.HI -> "मलयालम कोल्लम वर्ष पंचांग"
                AppLanguage.TA -> "மலையாள கொல்லவர்ஷம்"
                AppLanguage.KN -> "ಮಲಯಾಳಂ ಕೊಲ್ಲವರ್ಷಂ"
                else -> tradition.title
            }
            CalendarTradition.KANNADA -> when (lang) {
                AppLanguage.TE -> "కన్నడ పంచాంగం (అమావాస్యాంత)"
                AppLanguage.HI -> "कन्नड़ पंचांग (अमावस्यांत)"
                AppLanguage.TA -> "கன்னட பஞ்சாங்கம்"
                AppLanguage.KN -> "ಕನ್ನಡ ಪಂಚಾಂಗ (ಅಮಾವಾಸ್ಯಾಂತ)"
                else -> tradition.title
            }
            CalendarTradition.GUJARATI -> when (lang) {
                AppLanguage.TE -> "గుజరాతీ పంచాంగం"
                AppLanguage.HI -> "गुजराती पंचांग (कार्तिक शुक्ल आरंभ)"
                AppLanguage.TA -> "குஜராத்தி பஞ்சாங்கம்"
                AppLanguage.KN -> "ಗುಜರಾತಿ ಪಂಚಾಂಗ"
                else -> tradition.title
            }
            CalendarTradition.MARATHI -> when (lang) {
                AppLanguage.TE -> "మరాఠీ పంచాంగం"
                AppLanguage.HI -> "मराठी पंचांग"
                AppLanguage.TA -> "மராத்தி பஞ்சாங்கம்"
                AppLanguage.KN -> "ಮರಾಠಿ ಪಂಚಾಂಗ"
                else -> tradition.title
            }
            CalendarTradition.ODIA -> when (lang) {
                AppLanguage.TE -> "ఒడియా పంజికా"
                AppLanguage.HI -> "ओड़िया कोहिनूर पंजिका"
                AppLanguage.TA -> "ஒடியா பஞ்சாங்கம்"
                AppLanguage.KN -> "ಒಡಿಯಾ ಪಂಜಿಕಾ"
                else -> tradition.title
            }
        }
    }

    fun translateTraditionShort(tradition: CalendarTradition, lang: AppLanguage): String {
        return when (tradition) {
            CalendarTradition.TELUGU -> when (lang) {
                AppLanguage.TE -> "తెలుగు 🕉️"
                AppLanguage.HI -> "तेलुगु 🕉️"
                AppLanguage.TA -> "தெலுங்கு 🕉️"
                AppLanguage.KN -> "ತೆಲುಗು 🕉️"
                else -> "Telugu"
            }
            CalendarTradition.NORTH_INDIAN -> when (lang) {
                AppLanguage.TE -> "ఉత్తర భారత 🕉️"
                AppLanguage.HI -> "उत्तर भारतीय 🕉️"
                AppLanguage.TA -> "வட இந்தியா 🕉️"
                AppLanguage.KN -> "ಉತ್ತರ ಭಾರತ 🕉️"
                else -> "North India"
            }
            CalendarTradition.TAMIL -> when (lang) {
                AppLanguage.TE -> "తమిళం 🕉️"
                AppLanguage.HI -> "तमिल 🕉️"
                AppLanguage.TA -> "தமிழ் 🕉️"
                AppLanguage.KN -> "ತಮಿಳು 🕉️"
                else -> "Tamil"
            }
            CalendarTradition.KANNADA -> when (lang) {
                AppLanguage.TE -> "కన్నడ 🕉️"
                AppLanguage.HI -> "कन्नड़ 🕉️"
                AppLanguage.TA -> "கன்னடம் 🕉️"
                AppLanguage.KN -> "ಕನ್ನಡ 🕉️"
                else -> "Kannada"
            }
            else -> tradition.title.replace(" Panchangam", "").replace(" Panchanga", "").replace(" Calendar", "")
        }
    }

    fun translateTraditionDesc(tradition: CalendarTradition, lang: AppLanguage): String {
        return when (tradition) {
            CalendarTradition.TELUGU -> when (lang) {
                AppLanguage.TE -> "ఆంధ్రప్రదేశ్ మరియు తెలంగాణ ప్రాంతాలకు ప్రామాణిక అమావాస్యాంత పద్ధతి"
                AppLanguage.HI -> "आंध्र प्रदेश एवं तेलंगाना हेतु मानक अमावस्यांत परंपरा"
                AppLanguage.TA -> "ஆந்திரா மற்றும் தெலுங்கானா மாநிலங்களுக்கான பாரம்பரியம்"
                AppLanguage.KN -> "ಆಂಧ್ರ ಮತ್ತು ತೆಲಂಗಾಣ ಪ್ರದೇಶದ ಪದ್ಧತಿ"
                else -> tradition.regionDescription
            }
            CalendarTradition.NORTH_INDIAN -> when (lang) {
                AppLanguage.TE -> "ఉత్తరప్రదేశ్, బిహార్, రాజస్థాన్, మధ్యప్రదేశ్ పౌర్ణమి అంతమయ్యే పద్ధతి"
                AppLanguage.HI -> "उत्तर भारत, राजस्थान, मध्य प्रदेश, बिहार एवं दिल्ली हेतु पूर्णिमांत परंपरा"
                AppLanguage.TA -> "வட இந்திய மாநிலங்களுக்கான பூர்ணிமாந்த முறை"
                AppLanguage.KN -> "ಉತ್ತರ ಭಾರತದ ಪೂರ್ಣಿಮಾಂತ ಪದ್ಧತಿ"
                else -> tradition.regionDescription
            }
            CalendarTradition.TAMIL -> when (lang) {
                AppLanguage.TE -> "తమిళనాడు సౌరమాన తిరుక్కణిదం"
                AppLanguage.HI -> "तमिलनाडु हेतु मानक सौर गणना"
                AppLanguage.TA -> "தமிழ்நாடு மற்றும் புலம்பெயர்ந்தோருக்கான திருக்கணித முறை"
                AppLanguage.KN -> "ತಮಿಳುನಾಡು ಸೌರ ಪದ್ಧತಿ"
                else -> tradition.regionDescription
            }
            else -> tradition.regionDescription
        }
    }

    fun translateTraditionTitle(title: String, lang: AppLanguage): String {
        val matchedTradition = CalendarTradition.entries.find { it.title.equals(title, ignoreCase = true) }
        return if (matchedTradition != null) translateTradition(matchedTradition, lang) else title
    }

    fun translateNextEvent(eventStr: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return eventStr
        val trimmed = eventStr.trim()
        
        return when {
            trimmed.contains("Fasting Day", ignoreCase = true) && trimmed.contains("Ekadashi", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "ఉపవాస దినం: ఏకాదశి వ్రతం ప్రస్తుతం అమలులో ఉంది 🌾"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "व्रत का दिन: एकादशी व्रत वर्तमान में सक्रिय है 🌾"
                AppLanguage.TA -> "விரத நாள்: ஏகாதசி விரதம் தற்போது செயல்பாட்டில் உள்ளது 🌾"
                AppLanguage.KN -> "ಉಪವಾಸ ದಿನ: ಏಕಾದಶಿ ವ್ರತವು ಪ್ರಸ್ತುತ ಜಾರಿಯಲ್ಲಿದೆ 🌾"
                else -> eventStr
            }
            trimmed.contains("Full Moon", ignoreCase = true) || trimmed.contains("Purnima", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "పౌర్ణమి (పూర్ణిమ) — శ్రీ సత్యనారాయణ స్వామి పూజకు అత్యంత పవిత్రమైనది 🌕"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "पूर्णिमा — श्री सत्यनारायण पूजन एवं व्रत के लिए अत्यंत शुभ 🌕"
                AppLanguage.TA -> "பௌர்ணமி — ஸ்ரீ சத்யநாராயண பூஜைக்கு உகந்த நாள் 🌕"
                AppLanguage.KN -> "ಹುಣ್ಣಿಮೆ (ಪೂರ್ಣಿಮಾ) — ಶ್ರೀ ಸತ್ಯನಾರಾಯಣ ಪೂಜೆಗೆ ಅತ್ಯಂತ ಪವಿತ್ರ 🌕"
                else -> eventStr
            }
            trimmed.contains("New Moon", ignoreCase = true) || trimmed.contains("Amavasya", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "అమావాస్య — పితృ తర్పణ మరియు శ్రద్ధాదుల దినం 🌑"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "अमावस्या — पितृ तर्पण एवं दान-पुण्य का पावन दिन 🌑"
                AppLanguage.TA -> "அமாவாசை — பித்ரு தர்ப்பணத்திற்கான புனித நாள் 🌑"
                AppLanguage.KN -> "ಅಮಾವಾಸ್ಯೆ — ಪಿತೃ ತರ್ಪಣ ಮತ್ತು ದಾನ-ಧರ್ಮಕ್ಕೆ ಪ್ರಶಸ್ತ 🌑"
                else -> eventStr
            }
            trimmed.contains("Sankashti", ignoreCase = true) || trimmed.contains("Chaturthi", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "సంకష్టహర / వినాయక చతుర్థి పూజా వ్రతములు 🕉️"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "संकष्टी / विनायक चतुर्थी व्रत एवं गणेश पूजा 🕉️"
                AppLanguage.TA -> "சங்கடஹர / விநாயக சதுர்த்தி விரத வழிபாடுகள் 🕉️"
                AppLanguage.KN -> "ಸಂಕಷ್ಟಹರ / ವಿನಾಯಕ ಚತುರ್ಥಿ ವ್ರತದ ಆಚರಣೆಗಳು 🕉️"
                else -> eventStr
            }
            trimmed.contains("Pradosham", ignoreCase = true) -> when (lang) {
                AppLanguage.TE -> "ప్రదోష కాలం సంధ్యా సమయ శివ పూజ 🔱"
                AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "प्रदोष काल संध्या शिव पूजन समय 🔱"
                AppLanguage.TA -> "பிரதோஷ காலம் மாலை சிவ வழிபாடு 🔱"
                AppLanguage.KN -> "ಪ್ರದೋಷ ಕಾಲ ಸಂಧ್ಯಾ ಶಿವ ಪೂಜೆ 🔱"
                else -> eventStr
            }
            trimmed.startsWith("Next transition:", ignoreCase = true) -> {
                val timePart = trimmed.substringAfter("Next transition:").replace("Up to", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "తదుపరి తిథి ముగింపు సమయం: $timePart వరకు"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "अगला परिवर्तन: $timePart तक"
                    AppLanguage.TA -> "அடுத்த மாற்றம்: $timePart வரை"
                    AppLanguage.KN -> "ಮುಂದಿನ ಬದಲಾವಣೆ: $timePart ರವರೆಗೆ"
                    else -> eventStr
                }
            }
            else -> eventStr
        }
    }

    fun translateNextTransition(transitionStr: String, lang: AppLanguage): String {
        if (lang == AppLanguage.EN) return transitionStr
        val trimmed = transitionStr.trim()
        
        return when {
            trimmed.startsWith("Sunrise in ", ignoreCase = true) -> {
                val dur = trimmed.replace("Sunrise in", "", ignoreCase = true).replace("m", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "సూర్యోదయం $dur నిమిషాలలో 🌅"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "सूर्योदय $dur मिनट में 🌅"
                    AppLanguage.TA -> "சூரியோதயம் $dur நிமிடங்களில் 🌅"
                    AppLanguage.KN -> "ಸೂರ್ಯೋದಯ $dur ನಿಮಿಷಗಳಲ್ಲಿ 🌅"
                    else -> transitionStr
                }
            }
            trimmed.startsWith("Rahu Kalam starts in ", ignoreCase = true) -> {
                val dur = trimmed.replace("Rahu Kalam starts in", "", ignoreCase = true).replace("m", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "రాహుకాలం $dur నిమిషాలలో ప్రారంభం ⚠️"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "राहुकाल $dur मिनट में शुरू ⚠️"
                    AppLanguage.TA -> "ராகுகாலம் $dur நிமிடங்களில் ஆரம்பம் ⚠️"
                    AppLanguage.KN -> "ರಾಹುಕಾಲ $dur ನಿಮಿಷಗಳಲ್ಲಿ ಆರಂಭ ⚠️"
                    else -> transitionStr
                }
            }
            trimmed.startsWith("Rahu Kalam ends in ", ignoreCase = true) -> {
                val dur = trimmed.replace("Rahu Kalam ends in", "", ignoreCase = true).replace("m", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "రాహుకాలం $dur నిమిషాలలో ముగుస్తుంది ✅"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "राहुकाल $dur मिनट में समाप्त ✅"
                    AppLanguage.TA -> "ராகுகாலம் $dur நிமிடங்களில் முடியும் ✅"
                    AppLanguage.KN -> "ರಾಹುಕಾಲ $dur ನಿಮಿಷಗಳಲ್ಲಿ ಮುಕ್ತಾಯ ✅"
                    else -> transitionStr
                }
            }
            trimmed.startsWith("Sunset in ", ignoreCase = true) -> {
                val dur = trimmed.replace("Sunset in", "", ignoreCase = true).replace("m", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "సూర్యాస్తమయం $dur నిమిషాలలో 🌇"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "सूर्यास्त $dur मिनट में 🌇"
                    AppLanguage.TA -> "சூரியாஸ்தமனம் $dur நிமிடங்களில் 🌇"
                    AppLanguage.KN -> "ಸೂರ್ಯಾಸ್ತ $dur ನಿಮಿಷಗಳಲ್ಲಿ 🌇"
                    else -> transitionStr
                }
            }
            trimmed.startsWith("Next Sunrise at ", ignoreCase = true) -> {
                val time = trimmed.replace("Next Sunrise at", "", ignoreCase = true).trim()
                when (lang) {
                    AppLanguage.TE -> "రేపటి సూర్యోదయం: $time"
                    AppLanguage.HI, AppLanguage.MR, AppLanguage.GU -> "कल का सूर्योदय: $time"
                    AppLanguage.TA -> "அடுத்த சூரியோதயம்: $time"
                    AppLanguage.KN -> "ಮುಂದಿನ ಸೂರ್ಯೋದಯ: $time"
                    else -> transitionStr
                }
            }
            else -> transitionStr
        }
    }

    // 27. Day Special Occasion Resolver
    fun getFestivalOrOccasionForDay(
        date: LocalDate,
        panchanga: DayPanchanga,
        lang: AppLanguage
    ): DaySpecialOccasion {
        val festivals = FestivalRepository.getFestivalsForDate(date)
        if (festivals.isNotEmpty()) {
            val mainFest = festivals.first()
            return DaySpecialOccasion(
                title = translateFestivalName(mainFest.name, lang),
                deity = translateDeity(mainFest.deity, lang),
                summary = translateFestivalSummary(mainFest.id, mainFest.summary, lang),
                significance = translateFestivalSignificance(mainFest.id, mainFest.significance, lang),
                historicalContext = translateFestivalHistory(mainFest.id, mainFest.historicalContext ?: "", lang),
                pujaMuhurta = mainFest.pujaMuhurta,
                isMajorFestival = mainFest.category == FestivalCategory.MAJOR_FESTIVALS,
                iconEmoji = mainFest.iconEmoji,
                festivalItem = mainFest
            )
        }

        val tithiName = panchanga.tithi.name.lowercase()
        val dayOfWeek = date.dayOfWeek

        val (title, deity, summary, emoji) = when {
            tithiName.contains("ekadashi") -> Quad(
                when (lang) {
                    AppLanguage.TE -> "ఏకాదశీ వ్రతం (శ్రీ మహావిష్ణు పూజ)"
                    AppLanguage.HI -> "एकादशी व्रत (श्री हरि विष्णु पूजन)"
                    AppLanguage.TA -> "ஏகாதசி விரதம் (மகாவிஷ்ணு வழிபாடு)"
                    AppLanguage.KN -> "ಏಕಾದಶಿ ವ್ರತ (ಶ್ರೀ ವಿಷ್ಣು ಪೂಜೆ)"
                    else -> "Ekadashi Vrata (Sri Vishnu Puja)"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ మహావిష్ణువు"
                    AppLanguage.HI -> "भगवान श्री विष्णु"
                    AppLanguage.TA -> "ஸ்ரீ மகாவிஷ்ணு"
                    AppLanguage.KN -> "ಶ್ರೀ ಮಹಾವಿಷ್ಣು"
                    else -> "Lord Maha Vishnu"
                },
                when (lang) {
                    AppLanguage.TE -> "పుణ్యప్రదమైన ఏకాదశి ఉపవాస వ్రతం, పాపహరణం, మోక్షదాయకం."
                    AppLanguage.HI -> "पापनाशिनी एकादशी उपवास, आध्यात्मिक शांति एवं पुण्य प्राप्ति।"
                    AppLanguage.TA -> "புண்ணியம் தரும் ஏகாதசி விரதம், பாவங்களை நீக்கும்."
                    AppLanguage.KN -> "ಪುಣ್ಯಪ್ರದವಾದ ಏಕಾದಶಿ ಉಪವಾಸ ವ್ರತ, ಮೋಕ್ಷದಾಯಕ."
                    else -> "Sacred Ekadashi fast dedicated to Lord Vishnu for spiritual upliftment."
                },
                "🙏"
            )
            tithiName.contains("pradosham") || tithiName.contains("trayodashi") -> Quad(
                when (lang) {
                    AppLanguage.TE -> "ప్రదోష వ్రతం (శివారాధన)"
                    AppLanguage.HI -> "प्रदोष व्रत (शिव आराधना)"
                    AppLanguage.TA -> "பிரதோஷ விரதம் (சிவ வழிபாடு)"
                    AppLanguage.KN -> "ಪ್ರದೋಷ ವ್ರತ (ಶಿವ ಪೂಜೆ)"
                    else -> "Pradosha Vratam (Lord Shiva)"
                },
                when (lang) {
                    AppLanguage.TE -> "పరమశివుడు & పార్వతీదేవి"
                    AppLanguage.HI -> "भगवान शिव एवं माता पार्वती"
                    AppLanguage.TA -> "சிவபெருமான் & பார்வதி தேவி"
                    AppLanguage.KN -> "ಪರಮಶಿವ & ಪಾರ್ವತಿದೇವಿ"
                    else -> "Lord Shiva & Parvati"
                },
                when (lang) {
                    AppLanguage.TE -> "సాయంకాల ప్రదోష వేళ శివాభిషేకం సర్వ సంకట నివారిణి."
                    AppLanguage.HI -> "संध्याकाल प्रदोष वेला में शिव अभिषेक से सर्व मनोरथ सिद्ध होते हैं।"
                    AppLanguage.TA -> "மாலை பிரதோஷ காலத்தில் சிவதரிசனம் சகல தோஷங்களையும் போக்கும்."
                    AppLanguage.KN -> "ಸಂಜೆ ಪ್ರದೋಷ ಕಾಲದಲ್ಲಿ ಶಿವ ಪೂಜೆಯಿಂದ ಸರ್ವ ಸಂಕಷ್ಟ ಪರಿಹಾರ."
                    else -> "Evening twilight worship of Lord Shiva for relief from distress."
                },
                "🔱"
            )
            tithiName.contains("purnima") -> Quad(
                when (lang) {
                    AppLanguage.TE -> "పూర్ణిమ (సత్యనారాయణ వ్రతం)"
                    AppLanguage.HI -> "पूर्णिमा (सत्यनारायण व्रत)"
                    AppLanguage.TA -> "பௌர்ணமி (சத்யநாராயண பூஜை)"
                    AppLanguage.KN -> "ಹುಣ್ಣಿಮೆ (ಸತ್ಯನಾರಾಯಣ ವ್ರತ)"
                    else -> "Purnima (Satyanarayan Vrat)"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ సత్యనారాయణ స్వామి"
                    AppLanguage.HI -> "श्री सत्यनारायण भगवान"
                    AppLanguage.TA -> "ஸ்ரீ சத்யநாராயணர்"
                    AppLanguage.KN -> "ಶ್ರೀ ಸತ್ಯನಾರಾಯಣ ಸ್ವಾಮಿ"
                    else -> "Lord Satyanarayana"
                },
                when (lang) {
                    AppLanguage.TE -> "పూర్ణ చంద్రుని దర్శనం, సత్యనారాయణ వ్రతానికి అత్యంత శ్రేష్ఠం."
                    AppLanguage.HI -> "सत्यनारायण कथा एवं पूर्ण चंद्र दर्शन का परम पुण्यकारी दिन।"
                    AppLanguage.TA -> "பௌர்ணமி நிலவு தரிசனம் மற்றும் சத்யநாராயண விரதத்திற்கு உகந்தது."
                    AppLanguage.KN -> "ಹುಣ್ಣಿಮೆಯ ಚಂದ್ರ ದರ್ಶನ ಹಾಗೂ ಸತ್ಯನಾರಾಯಣ ಕಥಾ ಶ್ರವಣ ಅತ್ಯಂತ ಮಂಗಳಕರ."
                    else -> "Auspicious full moon day ideal for Sri Satyanarayana Vrata."
                },
                "🌕"
            )
            tithiName.contains("amavasya") -> Quad(
                when (lang) {
                    AppLanguage.TE -> "అమావాస్య (పితృ తర్పణం)"
                    AppLanguage.HI -> "अमावस्या (पितृ तर्पण एवं शांति)"
                    AppLanguage.TA -> "அமாவாசை (பித்ரு தர்ப்பணம்)"
                    AppLanguage.KN -> "ಅಮಾವಾಸ್ಯೆ (ಪಿತೃ ತರ್ಪಣ)"
                    else -> "Amavasya (Pitru Tarpana)"
                },
                when (lang) {
                    AppLanguage.TE -> "పితృదేవతలు"
                    AppLanguage.HI -> "पितृदेव"
                    AppLanguage.TA -> "பித்ருக்கள்"
                    AppLanguage.KN -> "ಪಿತೃದೇವತೆಗಳು"
                    else -> "Pitru Devatas (Ancestors)"
                },
                when (lang) {
                    AppLanguage.TE -> "పితృదేవతల ఆశీస్సుల కొరకు తర్పణం, అన్నదానం ప్రశస్తం."
                    AppLanguage.HI -> "पितरों की तृप्ति हेतु तर्पण, दान एवं शांति अनुष्ठान का दिन।"
                    AppLanguage.TA -> "முன்னோர் ஆசி பெற தர்ப்பணம் மற்றும் அன்னதானம் செய்ய உகந்தது."
                    AppLanguage.KN -> "ಪಿತೃದೇವತೆಗಳ ತೃಪ್ತಿಗೆ ತರ್ಪಣ ಮತ್ತು ಅನ್ನದಾನ ಪ್ರಶಸ್ತ."
                    else -> "Sacred new moon day for ancestral offerings and charity."
                },
                "🌑"
            )
            tithiName.contains("sankashti") || tithiName.contains("chaturthi") -> Quad(
                when (lang) {
                    AppLanguage.TE -> "సంకష్టహర / వినాయక చతుర్థి"
                    AppLanguage.HI -> "संकष्टी / विनायक चतुर्थी"
                    AppLanguage.TA -> "சங்கடஹர சதுர்த்தி"
                    AppLanguage.KN -> "ಸಂಕಷ್ಟಹರ ಚತುರ್ಥಿ"
                    else -> "Sankashti / Vinayaka Chaturthi"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ గణపతి"
                    AppLanguage.HI -> "भगवान श्री गणेश"
                    AppLanguage.TA -> "ஸ்ரீ விநாயகர்"
                    AppLanguage.KN -> "ಶ್ರೀ ಗಣಪತಿ"
                    else -> "Lord Ganesha"
                },
                when (lang) {
                    AppLanguage.TE -> "విఘ్నాలు తొలగి కార్యసిద్ధి కొరకు వినాయక పూజ."
                    AppLanguage.HI -> "विघ्न निवारण एवं मनोकामना पूर्ति हेतु गणेश आराधना।"
                    AppLanguage.TA -> "விக்னங்கள் நீங்கி நலம் பெற விநாயகர் வழிபாடு."
                    AppLanguage.KN -> "ವಿಘ್ನ ನಿವಾರಣೆಗೆ ಶ್ರೀ ಗಣೇಶನ ಪೂಜೆ."
                    else -> "Prayer to Lord Ganesha for removing obstacles and distress."
                },
                "🐘"
            )
            dayOfWeek == DayOfWeek.MONDAY -> Quad(
                when (lang) {
                    AppLanguage.TE -> "సోమవార శివారాధన"
                    AppLanguage.HI -> "सोमवार शिव पूजन"
                    AppLanguage.TA -> "திங்கட்கிழமை சிவபூஜை"
                    AppLanguage.KN -> "ಸೋಮವಾರ ಶಿವ ಪೂಜೆ"
                    else -> "Somavara Shiva Aradhana"
                },
                when (lang) {
                    AppLanguage.TE -> "పరమేశ్వరుడు"
                    AppLanguage.HI -> "भगवान शिव"
                    AppLanguage.TA -> "சிவபெருமான்"
                    AppLanguage.KN -> "ಶಿವ ಪರಮಾತ್ಮ"
                    else -> "Lord Shiva"
                },
                when (lang) {
                    AppLanguage.TE -> "సోమవార వేళ శివలింగార్చన, ఓం నమః శివాయ జపం అత్యంత శ్రేయస్కరం."
                    AppLanguage.HI -> "शिवलिंग पर जलाभिषेक एवं ॐ नमः शिवाय जप से मानसिक शांति मिलती है।"
                    AppLanguage.TA -> "சிவபெருமானுக்கு அபிஷேகம் செய்து நலம் பெற உகந்த நாள்."
                    AppLanguage.KN -> "ಶಿವಲಿಂಗಕ್ಕೆ ಜಲಾಭಿಷೇಕ ಮತ್ತು ಶಿವ ನಾಮಸ್ಮರಣೆ ಮಂಗಳಕರ."
                    else -> "Auspicious Monday dedicated to Lord Shiva and holy meditation."
                },
                "🔱"
            )
            dayOfWeek == DayOfWeek.TUESDAY -> Quad(
                when (lang) {
                    AppLanguage.TE -> "మంగళవార హనుమద్ & సుబ్రహ్మణ్య పూజ"
                    AppLanguage.HI -> "मंगलवार हनुमान एवं मंगल पूजन"
                    AppLanguage.TA -> "செவ்வாய்க்கிழமை முருகன் வழிபாடு"
                    AppLanguage.KN -> "ಮಂಗಳವಾರ ಹನುಮಂತ & ಸುಬ್ರಹ್ಮಣ್ಯ ಪೂಜೆ"
                    else -> "Tuesday Hanuman & Murugan Puja"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ హనుమాన్ / సుబ్రహ్మణ్య స్వామి"
                    AppLanguage.HI -> "श्री हनुमान जी"
                    AppLanguage.TA -> "ஸ்ரீ முருகன் & அனுமன்"
                    AppLanguage.KN -> "ಶ್ರೀ ಆಂಜನೇಯ / ಸುಬ್ರಹ್ಮಣ್ಯ"
                    else -> "Lord Hanuman & Kartikeya"
                },
                when (lang) {
                    AppLanguage.TE -> "హనుమాన్ చాలీసా పారాయణం, శక్తి, ధైర్య ప్రసాదం."
                    AppLanguage.HI -> "हनुमान चालीसा पाठ एवं सुंदरकांड से संकट दूर होते हैं।"
                    AppLanguage.TA -> "முருகன் மற்றும் அனுமன் வழிபாட்டால் தைரியம் பெருகும்."
                    AppLanguage.KN -> "ಹನುಮಾನ್ ಚಾಲೀಸಾ ಪಠಣ, ಧೈರ್ಯ ಮತ್ತು ಶಕ್ತಿ ವೃದ್ಧಿ."
                    else -> "Auspicious for recitation of Hanuman Chalisa for strength and protection."
                },
                "🚩"
            )
            dayOfWeek == DayOfWeek.FRIDAY -> Quad(
                when (lang) {
                    AppLanguage.TE -> "శుక్రవార మహాలక్ష్మీ పూజ"
                    AppLanguage.HI -> "शुक्रवार महालक्ष्मी पूजन"
                    AppLanguage.TA -> "வெள்ளிக்கிழமை லட்சுமி வழிபாடு"
                    AppLanguage.KN -> "ಶುಕ್ರವಾರ ಮಹಾಲಕ್ಷ್ಮೀ ಪೂಜೆ"
                    else -> "Friday Mahalakshmi Puja"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ మహాలక్ష్మీ దేవి"
                    AppLanguage.HI -> "माता महालक्ष्मी"
                    AppLanguage.TA -> "மகாலட்சுமி தாயார்"
                    AppLanguage.KN -> "ಶ್ರೀ ಮಹಾಲಕ್ಷ್ಮಿ ದೇವಿ"
                    else -> "Goddess Mahalakshmi"
                },
                when (lang) {
                    AppLanguage.TE -> "లక్ష్మీదేవి అనుగ్రహం కొరకు దీపారాధన, అష్టలక్ష్మి స్తోత్ర పఠనం."
                    AppLanguage.HI -> "महालक्ष्मी अष्टकम पाठ एवं दीप प्रज्वलन से ऐश्वर्य वृद्धि होती है।"
                    AppLanguage.TA -> "லட்சுமி தேவியின் அருளால் இல்லத்தில் ஐஸ்வர்யம் நிலைக்கும்."
                    AppLanguage.KN -> "ಮನೆಯಲ್ಲಿ ದೀಪಾರಾಧನೆ ಮತ್ತು ಲಕ್ಷ್ಮೀ ಕೃಪೆಗೆ ಪ್ರಶಸ್ತ ದಿನ."
                    else -> "Friday worship of Goddess Mahalakshmi for wealth and peace."
                },
                "🪷"
            )
            dayOfWeek == DayOfWeek.SATURDAY -> Quad(
                when (lang) {
                    AppLanguage.TE -> "శనివార వేంకటేశ్వర & శని ఆరాధన"
                    AppLanguage.HI -> "शनिवार श्री वेंकटेश्वर एवं शनि आराधना"
                    AppLanguage.TA -> "சனிக்கிழமை வெங்கடேஸ்வரர் வழிபாடு"
                    AppLanguage.KN -> "ಶನಿವಾರ ಶ್ರೀ ವೆಂಕಟೇಶ್ವರ / ಶನಿ ಪೂಜೆ"
                    else -> "Saturday Venkateswara / Shani Puja"
                },
                when (lang) {
                    AppLanguage.TE -> "శ్రీ వేంకటేశ్వర స్వామి & శనిదేవుడు"
                    AppLanguage.HI -> "भगवान वेंकटेश्वर एवं शनिदेव"
                    AppLanguage.TA -> "ஸ்ரீ வெங்கடாசலபதி & சனீஸ்வரர்"
                    AppLanguage.KN -> "ಶ್ರೀ ವೆಂಕಟೇಶ್ವರ ಸ್ವಾಮಿ & ಶನಿದೇವ"
                    else -> "Lord Venkateswara & Shani Deva"
                },
                when (lang) {
                    AppLanguage.TE -> "గోవింద నామస్మరణ, నువ్వుల నూనెతో దీపారాధన శ్రేష్ఠం."
                    AppLanguage.HI -> "तिल तेल का दीपक एवं गोविंद नाम जप से शांति मिलती है।"
                    AppLanguage.TA -> "எள் தீபம் ஏற்றி கோவிந்த நாம சங்கீர்த்தனம் செய்வது நன்று."
                    AppLanguage.KN -> "ಎಳ್ಳೆಣ್ಣೆ ದೀಪ ಮತ್ತು ಗೋವಿಂದ ನಾಮಸ್ಮರಣೆಯಿಂದ ಶುಭಫಲ."
                    else -> "Worship of Lord Venkateswara and lighting sesame oil lamps."
                },
                "🙏"
            )
            dayOfWeek == DayOfWeek.SUNDAY -> Quad(
                when (lang) {
                    AppLanguage.TE -> "ఆదివార సూర్య నమస్కారాలు"
                    AppLanguage.HI -> "रविवार सूर्य उपासना"
                    AppLanguage.TA -> "ஞாயிற்றுக்கிழமை சூரிய வழிபாடு"
                    AppLanguage.KN -> "ಭಾನುವಾರ ಸೂರ್ಯ ನಮಸ್ಕಾರ"
                    else -> "Sunday Surya Upasana"
                },
                when (lang) {
                    AppLanguage.TE -> "ప్రత్యక్ష దైవం సూర్య భగవానుడు"
                    AppLanguage.HI -> "भगवान सूर्य देव"
                    AppLanguage.TA -> "சூரிய பகவான்"
                    AppLanguage.KN -> "ಸೂರ್ಯ ಭಗವಾನ್"
                    else -> "Surya Deva (The Sun God)"
                },
                when (lang) {
                    AppLanguage.TE -> "ఆదిత్య హృదయ స్తోత్ర పఠనం, సూర్య నమస్కారాలు ఆరోగ్యప్రదం."
                    AppLanguage.HI -> "आदित्य हृदय स्तोत्र पाठ एवं अर्घ्य समर्पण से तेज एवं आरोग्य की प्राप्ति।"
                    AppLanguage.TA -> "ஆதித்ய ஹிருதய ஸ்தோத்திரம் மற்றும் சூரிய நமஸ்காரம் நலம் தரும்."
                    AppLanguage.KN -> "ಆದಿತ್ಯ ಹೃದಯ ಸ್ತೋತ್ರ ಪಠಣ ಮತ್ತು ಸೂರ್ಯ ನಮಸ್ಕಾರ ಆರೋಗ್ಯಕರ."
                    else -> "Surya Namaskar and Aditya Hrudaya Stotra for health and vitality."
                },
                "☀️"
            )
            else -> Quad(
                when (lang) {
                    AppLanguage.TE -> "${translateVara(dayOfWeek, lang)} దిన విశేష పూజ"
                    AppLanguage.HI -> "${translateVara(dayOfWeek, lang)} विशेष आराधना"
                    AppLanguage.TA -> "${translateVara(dayOfWeek, lang)} தின வழிபாடு"
                    AppLanguage.KN -> "${translateVara(dayOfWeek, lang)} ವಿಶೇಷ ಆರಾಧನೆ"
                    else -> "Daily Vedic Worship"
                },
                when (lang) {
                    AppLanguage.TE -> "ఇష్టదైవం & గ్రహ మండలము"
                    AppLanguage.HI -> "इष्टदेव एवं नवग्रह"
                    AppLanguage.TA -> "இஷ்ட தெய்வம்"
                    AppLanguage.KN -> "ಇಷ್ಟದೇವತೆ"
                    else -> "Ishta Devata & Navagraha"
                },
                when (lang) {
                    AppLanguage.TE -> "దినచర్యలో భాగంగా సంధ్యావందనం, గాయత్రీ మంత్ర జపం."
                    AppLanguage.HI -> "दैनिक संध्यावंदन, गायत्री मंत्र जप एवं शुभ कर्म।"
                    AppLanguage.TA -> "தினசரி காயத்ரி மந்திர ஜபம் மற்றும் இஷ்ட தெய்வ வழிபாடு."
                    AppLanguage.KN -> "ದೈನಂದಿನ ಗಾಯತ್ರೀ ಜಪ ಮತ್ತು ಇಷ್ಟದೇವತಾ ಆರಾಧನೆ."
                    else -> "Daily Gayatri Mantra chanting and prayer."
                },
                "🪔"
            )
        }

        return DaySpecialOccasion(
            title = title,
            deity = deity,
            summary = summary,
            pujaMuhurta = null,
            isMajorFestival = false,
            iconEmoji = emoji,
            festivalItem = null
        )
    }

    fun translateEndTime(endTimeStr: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return endTimeStr
        val trimmed = endTimeStr.trim()
        if (trimmed.isBlank() || trimmed == "-") return "-"
        if (trimmed.contains("Next half-tithi transition", ignoreCase = true)) {
            return "తదుపరి కరణం మార్పు వరకు"
        }
        var clean = trimmed
            .replace("Up to", "", ignoreCase = true)
            .replace("Next transition:", "", ignoreCase = true)
            .replace("వరకు", "")
            .trim()

        if (clean.isBlank()) return ""

        // Check for AM/PM formatting
        val isPm = clean.endsWith("PM", ignoreCase = true)
        val isAm = clean.endsWith("AM", ignoreCase = true)

        if (isPm || isAm) {
            val timeDigits = clean.replace("AM", "", ignoreCase = true).replace("PM", "", ignoreCase = true).trim()
            val parts = timeDigits.split(":")
            if (parts.size == 2) {
                val hour = parts[0].toIntOrNull() ?: 12
                val min = parts[1]
                val period = if (isAm) {
                    if (hour in 4..11) "ఉదయం" else "రాత్రి"
                } else {
                    when (hour) {
                        12, in 1..3 -> "మధ్యాహ్నం"
                        in 4..7 -> "సాయంత్రం"
                        else -> "రాత్రి"
                    }
                }
                return "$period ${String.format(Locale.ENGLISH, "%02d:%s", hour, min)} వరకు"
            }
        }

        return "$clean వరకు"
    }

    fun translatePlanet(planet: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return planet
        return when (planet.trim()) {
            "Mars" -> "కుజుడు (అంగారకుడు)"
            "Venus" -> "శుక్రుడు"
            "Mercury" -> "బుధుడు"
            "Moon" -> "చంద్రుడు"
            "Sun" -> "సూర్యుడు"
            "Jupiter" -> "గురుడు (బృహస్పతి)"
            "Saturn" -> "శని"
            else -> planet
        }
    }

    fun translateElement(element: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return element
        return when (element.trim()) {
            "Fire" -> "అగ్ని"
            "Earth" -> "భూమి"
            "Air" -> "వాయువు"
            "Water" -> "జలం"
            else -> element
        }
    }

    fun translateSyllables(syllables: String, lang: AppLanguage): String {
        if (lang != AppLanguage.TE) return syllables
        return when (syllables.trim()) {
            "A, L, I" -> "అ, ల, ఇ"
            "U, O, Va, Vi" -> "ఉ, ఓ, వా, వి"
            "Ka, Ki, Ku, Gha" -> "కా, కి, కు, ఘ"
            "Hi, Hu, He, Ho" -> "హి, హు, హే, హో"
            "Ma, Mi, Mu, Me" -> "మ, మి, ము, మే"
            "To, Pa, Pi, Pu" -> "తో, ప, పి, పు"
            "Ra, Ri, Ru, Re" -> "రా, రి, రు, రే"
            "To, Na, Ni, Nu" -> "తో, నా, ని, ను"
            "Ye, Yo, Bha, Bhi" -> "యే, యో, భ, భి"
            "Bho, Ja, Ji, Khi" -> "భో, జా, జి, ఖి"
            "Go, Ge, Go, Sa" -> "గో, గే, గో, సా"
            "Di, Du, Tha, Jha" -> "దీ, దూ, థా, ఝా"
            else -> syllables
        }
    }

    fun translateRating(rating: String, lang: AppLanguage): String {
        return when (rating.trim()) {
            "Highly Auspicious" -> when (lang) {
                AppLanguage.TE -> "అతి శ్రేష్ఠమైనది"
                AppLanguage.HI -> "अत्यंत शुभ"
                else -> "Highly Auspicious"
            }
            "Auspicious" -> when (lang) {
                AppLanguage.TE -> "శుభ సమయం"
                AppLanguage.HI -> "शुभ समय"
                else -> "Auspicious"
            }
            "Good" -> when (lang) {
                AppLanguage.TE -> "మంచిది"
                AppLanguage.HI -> "अच्छा"
                else -> "Good"
            }
            else -> rating
        }
    }

    fun getTeluguMonthName(month: Int): String {
        return when (month) {
            1 -> "జనవరి"
            2 -> "ఫిబ్రవరి"
            3 -> "మార్చి"
            4 -> "ఏప్రిల్"
            5 -> "మే"
            6 -> "జూన్"
            7 -> "జూలై"
            8 -> "ఆగస్టు"
            9 -> "సెప్టెంబర్"
            10 -> "అక్టోబర్"
            11 -> "నవంబర్"
            12 -> "డిసెంబర్"
            else -> "నెల"
        }
    }

    fun getTeluguDayOfWeek(dow: DayOfWeek): String {
        return when (dow) {
            DayOfWeek.SUNDAY -> "ఆదివారము"
            DayOfWeek.MONDAY -> "సోమవారము"
            DayOfWeek.TUESDAY -> "మంగళవారము"
            DayOfWeek.WEDNESDAY -> "బుధవారము"
            DayOfWeek.THURSDAY -> "గురువారము"
            DayOfWeek.FRIDAY -> "శుక్రవారము"
            DayOfWeek.SATURDAY -> "శనివారము"
        }
    }

    fun getSanskritVara(dow: DayOfWeek): String {
        return when (dow) {
            DayOfWeek.SUNDAY -> "భాను వాసరః"
            DayOfWeek.MONDAY -> "ఇందు వాసరః"
            DayOfWeek.TUESDAY -> "భౌమ వాసరః"
            DayOfWeek.WEDNESDAY -> "సౌమ్య వాసరః"
            DayOfWeek.THURSDAY -> "గురు వాసరః"
            DayOfWeek.FRIDAY -> "భృగు వాసరః"
            DayOfWeek.SATURDAY -> "స్థిర వాసరః"
        }
    }

    fun getShortDayName(dow: DayOfWeek, lang: AppLanguage = AppLanguage.TE): String {
        return when (dow) {
            DayOfWeek.SUNDAY -> if (lang == AppLanguage.TE) "ఆది" else "Sun"
            DayOfWeek.MONDAY -> if (lang == AppLanguage.TE) "సోమ" else "Mon"
            DayOfWeek.TUESDAY -> if (lang == AppLanguage.TE) "మంగళ" else "Tue"
            DayOfWeek.WEDNESDAY -> if (lang == AppLanguage.TE) "బుధ" else "Wed"
            DayOfWeek.THURSDAY -> if (lang == AppLanguage.TE) "గురు" else "Thu"
            DayOfWeek.FRIDAY -> if (lang == AppLanguage.TE) "శుక్ర" else "Fri"
            DayOfWeek.SATURDAY -> if (lang == AppLanguage.TE) "శని" else "Sat"
        }
    }

    fun getShortTithiTelugu(tithiNum: Int): String {
        val num = ((tithiNum - 1) % 15) + 1
        return when (num) {
            1 -> "పాడ్యమి"
            2 -> "విదియ"
            3 -> "తదియ"
            4 -> "చవితి"
            5 -> "పంచమి"
            6 -> "షష్ఠి"
            7 -> "సప్తమి"
            8 -> "అష్టమి"
            9 -> "నవమి"
            10 -> "దశమి"
            11 -> "ఏకాదశి"
            12 -> "ద్వాదశి"
            13 -> "త్రయోదశి"
            14 -> "చతుర్దశి"
            15 -> if (tithiNum == 15) "పౌర్ణమి" else "అమావాస్య"
            else -> "తిథి"
        }
    }

    private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
}

data class DaySpecialOccasion(
    val title: String,
    val deity: String,
    val summary: String,
    val significance: String? = null,
    val historicalContext: String? = null,
    val pujaMuhurta: String? = null,
    val isMajorFestival: Boolean = false,
    val iconEmoji: String = "🪔",
    val festivalItem: FestivalItem? = null
)
