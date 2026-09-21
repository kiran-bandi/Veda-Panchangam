package com.example.engine

import com.example.model.AppLanguage
import com.example.model.FestivalCategory
import com.example.model.FestivalItem
import java.time.LocalDate

object FestivalWishHelper {

    /**
     * Checks if a given FestivalItem is a Major Festival for displaying devotional wishes.
     */
    fun isMajorFestival(item: FestivalItem): Boolean {
        if (item.category == FestivalCategory.GOVERNMENT_HOLIDAYS) return false

        if (item.category == FestivalCategory.MAJOR_FESTIVALS) return true

        val id = item.id.lowercase()
        val name = item.name.lowercase()

        val keywords = listOf(
            "ugadi", "sankranti", "sri_rama_navami", "vinayaka_chavithi",
            "vijayadashami", "durgashtami", "mahanavami", "ayudha_puja", "kalasha_sthapana", "navaratri",
            "deepavali", "naraka_chaturdashi",
            "dhanteras", "maha_shivaratri", "sri_krishna_janmashtami", "rakhi_purnima",
            "varalakshmi_vratam", "nagula_chavithi", "hanuman_jayanti", "holi",
            "vaikuntha_ekadashi", "vasant_panchami", "ratha_saptami", "kartika_purnima",
            "akshaya_tritiya", "tholi_ekadashi", "guru_purnima", "bonalu", "naga_panchami"
        )

        return keywords.any { id.contains(it) || name.contains(it) }
    }

    /**
     * Returns the primary major festival for the given date, or null if no major festival occurs.
     */
    fun getMajorFestivalForDate(date: LocalDate, dayFestivals: List<FestivalItem>): FestivalItem? {
        val majorList = dayFestivals.filter { isMajorFestival(it) }
        if (majorList.isNotEmpty()) {
            // Prioritize category MAJOR_FESTIVALS first if available
            return majorList.firstOrNull { it.category == FestivalCategory.MAJOR_FESTIVALS } ?: majorList.first()
        }
        return null
    }

    /**
     * Returns custom localized devotional wish messages in Telugu or English for major festivals.
     */
    fun getDevotionalWishMessage(festival: FestivalItem, language: AppLanguage): String {
        val id = festival.id.lowercase()
        val isTe = language == AppLanguage.TE

        return when {
            id.contains("ugadi") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు శ్రీ ఉగాది (నూతన సంవత్సరాది) శుభాకాంక్షలు! ఈ నూతన సంవత్సరంలో ఆయురారోగ్యాలు, అష్టైశ్వర్యాలు మరియు సకల కార్యవిజయాలు కలుగాలని ఆకాంక్షిస్తున్నాము. 🥭✨"
            } else {
                "Wishing you and your family a very Happy Ugadi! May this New Year bring health, wealth, peace, and divine prosperity to your home. 🥭✨"
            }

            id.contains("sankranti") || id.contains("lohri") || id.contains("kanuma") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు మకర సంక్రాంతి పండుగ శుభాకాంక్షలు! ఆ సూర్య భగవానుని దివ్య కటాక్షంతో మీ ఇంట సుఖశాంతులు, ధాన్య సమృద్ధి కలకాలం నిండాలని కోరుకుంటున్నాము. 🪁🌾"
            } else {
                "Wishing you and your family a joyous Makar Sankranti! May the Sun God shower your home with warmth, health, and endless abundance. 🪁🌾"
            }

            id.contains("sri_rama_navami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు శ్రీరామనవమి శుభాకాంక్షలు! శ్రీ సీతారామచంద్ర స్వామి వారి దివ్య ఆశీస్సులతో మీ కుటుంబంలో సదా ఆనందం, ప్రశాంతత, ధర్మం నిలవాలని ప్రార్థిస్తున్నాము. 🏹🌸"
            } else {
                "Wishing you and your family a blessed Sri Rama Navami! May Lord Sita Ramachandra grant you wisdom, peace, and eternal happiness. 🏹🌸"
            }

            id.contains("vinayaka_chavithi") || id.contains("ganesh") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు వినాయక చవితి శుభాకాంక్షలు! ఆ విఘ్నేశ్వరుని దివ్య అనుగ్రహంతో మీ సకల విఘ్నాలు తొలగి, తలపెట్టిన కార్యాలు విజయవంతం కావాలని కోరుకుంటున్నాము. 🐘✨"
            } else {
                "Wishing you and your family a very Happy Vinayaka Chavithi! May Lord Ganesha remove all obstacles and bless you with health, success, and wisdom. 🐘✨"
            }

            id.contains("mahanavami") || id.contains("ayudha_puja") || id.contains("maha_navami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు మహానవమి & ఆయుధ పూజ శుభాకాంక్షలు! శ్రీ సరస్వతీ దేవి, కనకదుర్గాదేవి దివ్య అనుగ్రహంతో మీ సమస్త యంత్ర, వాహన, వృత్తి వ్యాపారాలలో విజయం చేకూరాలని కోరుకుంటున్నాము. 🛠️✨"
            } else {
                "Wishing you and your family a blessed Maha Navami & Ayudha Puja! May Goddess Saraswati and Durga bless your work, tools, and endeavors with immense success. 🛠️✨"
            }

            id.contains("durgashtami") || id.contains("bathukamma") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు దుర్గాష్టమి శుభాకాంక్షలు! శ్రీ దుర్గాదేవి దివ్య ఆశీస్సులతో మీకు సకల ధైర్యం, ఆయురారోగ్యాలు, ఐశ్వర్యం కలగాలని ప్రార్థిస్తున్నాము. 🔱✨"
            } else {
                "Wishing you and your family a sacred Durga Ashtami! May Goddess Durga bless you with strength, protection, and prosperity. 🔱✨"
            }

            id.contains("kalasha_sthapana") || id.contains("navaratri_start") || id.contains("navaratri_prarambham") || (id.contains("navaratri") && !id.contains("vijayadashami")) -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు దేవీ శరన్నవరాత్రుల ప్రారంభం & కలశస్థాపన శుభాకాంక్షలు! శ్రీ కనకదుర్గాదేవి దివ్య అనుగ్రహంతో మీ ఇంట సకల మంగళకరమైన శుభాలు నిండాలని కోరుకుంటున్నాము. 🔱✨"
            } else {
                "Wishing you and your family a blessed Sharan Navaratri & Kalasha Sthapana! May Goddess Durga shower health, joy, and divine grace upon your family. 🔱✨"
            }

            id.contains("vijayadashami") || id.contains("dussehra") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు విజయదశమి (దసరా) శుభాకాంక్షలు! శ్రీ కనకదుర్గాదేవి దివ్య అనుగ్రహంతో మీరు తలపెట్టే సమస్త కార్యాలలో విజయాలు సాధించాలని, ఆయురారోగ్యాలు వర్ధిల్లాలని ఆకాంక్షిస్తున్నాము. 🔱✨"
            } else {
                "Wishing you and your family a victorious Vijayadashami (Dussehra)! May Goddess Durga bless you with triumph over all challenges, strength, and endless happiness. 🔱✨"
            }

            id.contains("deepavali") || id.contains("naraka_chaturdashi") || id.contains("dhanteras") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు దీపావళి పండుగ శుభాకాంక్షలు! ఈ దివ్య దీప కాంతులు మీ జీవితంలోని చీకట్లను తొలగించి, శ్రీ మహాలక్ష్మి దేవి అనుగ్రహంతో అష్టైశ్వర్యాలు ప్రసాదించాలని కోరుకుంటున్నాము. 🪔✨"
            } else {
                "Wishing you and your family a bright and joyful Diwali! May the sacred lights illuminate your life and Goddess Lakshmi bestow eternal wealth and peace. 🪔✨"
            }

            id.contains("maha_shivaratri") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు మహా శివరాత్రి శుభాకాంక్షలు! ఆ పరమేశ్వరుని దివ్య అనుగ్రహంతో మీకు ఆయురారోగ్యాలు, ప్రశాంతత, భక్తి మరియు సకల శుభాలు కలుగాలని ప్రార్థిస్తున్నాము. 🔱🕉️"
            } else {
                "Wishing you and your family a holy Maha Shivaratri! May Supreme Lord Shiva bless you with good health, inner peace, and spiritual devotion. 🔱🕉️"
            }

            id.contains("sri_krishna_janmashtami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు శ్రీకృష్ణాష్టమి శుభాకాంక్షలు! భగవాన్ శ్రీకృష్ణుని దివ్య ఆశీస్సులతో మీ గృహంలో ఆనంద అమృతం, సంతోషం నిరంతరం కురియాలని ఆకాంక్షిస్తున్నాము. 🦚✨"
            } else {
                "Wishing you and your family a blissful Sri Krishna Janmashtami! May Lord Krishna fill your home with divine love, joy, and peace. 🦚✨"
            }

            id.contains("rakhi_purnima") || id.contains("raksha_bandhan") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు రాఖీ పౌర్ణమి (రక్షాబంధన్) శుభాకాంక్షలు! ప్రేమానురాగాల సోదర బంధం ఎల్లవేళలా వర్ధిల్లాలని, సర్వ రక్షణ కలుగాలని కోరుకుంటున్నాము. 🧶✨"
            } else {
                "Wishing you and your family a Happy Raksha Bandhan! May the pure bond of love and protection keep your family safe and happy. 🧶✨"
            }

            id.contains("varalakshmi") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు శ్రీ వరలక్ష్మీ వ్రతం శుభాకాంక్షలు! ఆ వరలక్ష్మీ దేవి దివ్య కటాక్షంతో మీ యింట సౌభాగ్యం, ధనధాన్య వృద్ధి, మంగళప్రదమైన సిరిసంపదలు కలుగాలని ఆకాంక్షిస్తున్నాము. 🪷✨"
            } else {
                "Wishing you and your family a divine Varalakshmi Vratam! May Goddess Varalakshmi grace your household with unending prosperity and happiness. 🪷✨"
            }

            id.contains("nagula_chavithi") || id.contains("naga_panchami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు నాగుల చవితి శుభాకాంక్షలు! నాగేంద్ర స్వామి వారి దివ్య ఆశీస్సులతో సమస్త భయాలు తొలగి, కుటుంబ రక్షణ, ఆరోగ్యం కలగాలని ప్రార్థిస్తున్నాము. 🐍✨"
            } else {
                "Wishing you a blessed Nagula Chavithi! May Lord Nagendra protect your family and grant good health, peace, and prosperity. 🐍✨"
            }

            id.contains("holi") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు హోలీ పండుగ శుభాకాంక్షలు! ఈ వసంత రంగుల సంతోషం మరియు ఉత్సాహం మీ జీవితంలో ఎల్లప్పుడూ నిండాలని ఆకాంక్షిస్తున్నాము. 🎨✨"
            } else {
                "Wishing you and your family a vibrant and Happy Holi! May your life be filled with bright colors of joy, love, and health. 🎨✨"
            }

            id.contains("hanuman_jayanti") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు హనుమాన్ జయంతి శుభాకాంక్షలు! శ్రీ ఆంజనేయ స్వామి వారి అనుగ్రహంతో మీకు ధైర్యం, బుద్ధి, శక్తి, సకల జయములు కలగాలని ప్రార్థిస్తున్నాము. 🚩✨"
            } else {
                "Wishing you a blessed Hanuman Jayanti! May Lord Hanuman bestow immense strength, wisdom, courage, and health upon you. 🚩✨"
            }

            id.contains("kartika_purnima") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు కార్తీక పౌర్ణమి శుభాకాంక్షలు! జ్వాలాతోరణ దర్శనంతో, దీపారాధన పుణ్యఫలంతో శివానుగ్రహం, సకల శుభాలు చేకూరాలని కోరుకుంటున్నాము. 🔥✨"
            } else {
                "Wishing you a auspicious Kartika Purnima! May the divine lights of Kartika Deepam illuminate your life with peace and Shiva's grace. 🔥✨"
            }

            id.contains("vaikuntha_ekadashi") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు ముక్కోటి (వైకుంఠ) ఏకాదశి శుభాకాంక్షలు! శ్రీ వేంకటేశ్వర స్వామి వారి ఉత్తర ద్వార దర్శన పుణ్యఫలంతో సర్వ శ్రేయస్సులు కలగాలని ఆకాంక్షిస్తున్నాము. 🚪✨"
            } else {
                "Wishing you a holy Vaikuntha Ekadashi! May Lord Venkateswara grant you spiritual devotion, joy, and peace. 🚪✨"
            }

            id.contains("vasant_panchami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు వసంత పంచమి శుభాకాంక్షలు! శ్రీ సరస్వతీ అమ్మవారి దివ్య అనుగ్రహంతో విద్య, జ్ఞానవృద్ధి, సరస్వతీ కటాక్షం కలగాలని కోరుకుంటున్నాము. 🪕✨"
            } else {
                "Wishing you a Happy Vasant Panchami! May Goddess Saraswati bless you with wisdom, learning, and success. 🪕✨"
            }

            id.contains("ratha_saptami") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు రథసప్తమి (సూర్య జయంతి) శుభాకాంక్షలు! సూర్య భగవానుని దివ్య అనుగ్రహంతో మీకు ఆరోగ్య సమృద్ధి, దీర్ఘాయుష్షు కలగాలని ప్రార్థిస్తున్నాము. ☀️✨"
            } else {
                "Wishing you a auspicious Ratha Saptami! May Sun God Surya Dev bless you with good health, energy, and happiness. ☀️✨"
            }

            id.contains("akshaya_tritiya") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు అక్షయ తృతీయ శుభాకాంక్షలు! శ్రీ మహాలక్ష్మి దేవి, కుబేరుని దివ్య అనుగ్రహంతో మీ యింట అక్షయమైన సంపద, సమృద్ధి చేకూరాలని ఆకాంక్షిస్తున్నాము. 🪙✨"
            } else {
                "Wishing you a prosperous Akshaya Tritiya! May Goddess Lakshmi and Kubera bless you with endless fortune, wealth, and joy. 🪙✨"
            }

            id.contains("bonalu") -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు ఆషాఢ బోనాల పండుగ శుభాకాంక్షలు! శ్రీ మహాంకాళి అమ్మవారి దివ్య అనుగ్రహంతో మీ కుటుంబానికి సకల రక్షణ, ఆయురారోగ్యాలు కలగాలని కోరుకుంటున్నాము. 🌺✨"
            } else {
                "Wishing you and your family a Happy Bonalu! May Goddess Mahankali bless you with health, protection, and prosperity. 🌺✨"
            }

            else -> if (isTe) {
                "మీకు మరియు మీ కుటుంబ సభ్యులకు ${festival.name} హృదయపూర్వక శుభాకాంక్షలు! భగవంతుని దివ్య అనుగ్రహంతో మీ ఇంట సదా సుఖశాంతులు, ఆయురారోగ్యాలు వర్ధిల్లాలని ప్రార్థిస్తున్నాము. ${festival.iconEmoji}✨"
            } else {
                "Wishing you and your family a blessed ${festival.name}! May divine blessings bring peace, happiness, and prosperity to your home. ${festival.iconEmoji}✨"
            }
        }
    }
}
