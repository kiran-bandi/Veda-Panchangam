package com.example.engine

import java.time.LocalDate

data class DevotionalQuote(
    val id: Int,
    val titleTelugu: String,
    val titleEnglish: String,
    val slokaOrSukti: String,
    val source: String,
    val meaningTelugu: String,
    val meaningEnglish: String,
    val deityOrContext: String,
    val icon: String = "🪔"
)

object DevotionalQuoteRepository {

    val DAILY_QUOTES = listOf(
        DevotionalQuote(
            id = 1,
            titleTelugu = "కర్మ సిద్ధాంతం & కర్తవ్య నిష్ఠ",
            titleEnglish = "Duty without Attachment",
            slokaOrSukti = "కర్మణ్యేవాధికారస్తే మా ఫలేషు కదాచన ।\nమా కర్మఫలహేతుర్భూర్మా తే సఙ్గోఽస్త్వకర్మణి ॥",
            source = "శ్రీమద్భగవద్గీత - 2.47",
            meaningTelugu = "నీ కర్తవ్యాన్ని నిష్కామంగా చేయడం వరకే నీ అధికారం; ఫలితాలపై ఎన్నడూ వ్యామోహం ఉండరాదు. ఫలితాపేక్షతో పని చేయవద్దు, అలాగని కర్మను త్యజించి సోమరిగా ఉండవద్దు.",
            meaningEnglish = "You have a right to perform your prescribed duty, but you are not entitled to the fruits of actions. Never consider yourself the cause of results, nor be attached to inaction.",
            deityOrContext = "శ్రీకృష్ణ పరమాత్మ బోధ",
            icon = "🪔"
        ),
        DevotionalQuote(
            id = 2,
            titleTelugu = "సత్య సాధన",
            titleEnglish = "The Path of Truth",
            slokaOrSukti = "సత్యమేవ జయతే నానృతం\nసత్యేన పంథా వితతో దేవయానః ।",
            source = "ముండకోపనిషత్ - 3.1.6",
            meaningTelugu = "సత్యమే అంతిమంగా జయిస్తుంది, అసత్యం ఎప్పటికీ నిలబడదు. పరమ సత్యం ద్వారానే దైవికమైన మార్గం విశాలమవుతుంది.",
            meaningEnglish = "Truth alone triumphs, never falsehood. By truth is laid out the divine path across all realms.",
            deityOrContext = "ఉపనిషత్ అమృత వాక్కు",
            icon = "🕉️"
        ),
        DevotionalQuote(
            id = 3,
            titleTelugu = "సర్వ మానవ శాంతి & కల్యాణం",
            titleEnglish = "Universal Wellbeing & Peace",
            slokaOrSukti = "సర్వే భవంతు సుఖినః సర్వే సంతు నిరామయాః ।\nసర్వే భద్రాణి పశ్యంతు మా కశ్చిద్దుఃఖభాగ్‌భవేత్ ॥",
            source = "బృహదారణ్యకోపనిషత్",
            meaningTelugu = "సకల ప్రాణులు సుఖసంతోషాలతో ఉండుగాక; అందరూ రోగరహితులై ఆరోగ్యంగా ఉండుగాక; అందరికీ శుభములు కలుగుగాక; ఎవ్వరూ ఎటువంటి దుఃఖాన్ని అనుభవించకుండుగాక.",
            meaningEnglish = "May all sentient beings be joyful and happy. May everyone be healthy and free from illness. May all perceive auspiciousness, and may none suffer misery.",
            deityOrContext = "శాంతి మంత్రం",
            icon = "🌸"
        ),
        DevotionalQuote(
            id = 4,
            titleTelugu = "యోగక్షేమ రక్షణ",
            titleEnglish = "Supreme Divine Protection",
            slokaOrSukti = "అనన్యాశ్చింతయంతో మాం యే జనాః పర్యుపాసతే ।\nతేషాం నిత్యాభియుక్తానాం యోగక్షేమం వహామ్యహమ్ ॥",
            source = "శ్రీమద్భగవద్గీత - 9.22",
            meaningTelugu = "ఎవరైతే అనన్య భక్తితో నన్నే స్మరిస్తూ ఉపాసిస్తారో, నిరంతరం నాయందే మనస్సు నిలిపిన ఆ భక్తుల యోగక్షేమాలను నేనే స్వయంగా వహిస్తాను.",
            meaningEnglish = "To those devotees who constantly worship Me with complete devotion, meditating on Me, I personally carry their security, sustenance, and preservation.",
            deityOrContext = "శ్రీమన్నారాయణ అభయ వాక్కు",
            icon = "🚩"
        ),
        DevotionalQuote(
            id = 5,
            titleTelugu = "ఉదార హృదయం - వసుధైవ కుటుంబకమ్",
            titleEnglish = "The Global Family",
            slokaOrSukti = "అయం నిజః పరో వేతి గణనా లఘుచేతసామ్ ।\nఉదారచరితానాం తు వసుధైవ కుటుంబకమ్ ॥",
            source = "మహోపనిషత్ - 6.71",
            meaningTelugu = "ఇతను నావాడు, అతడు పరాయివాడు అనే భేదభావం సంకుచిత మనస్కులకు మాత్రమే ఉంటుంది. ఉదాత్త హృదయులకు ఈ సమస్త విశ్వమే ఒకే ఒక్క కుటుంబం.",
            meaningEnglish = "This one is mine, that one is a stranger – this is the calculation of narrow minds. For those of noble character, the entire cosmos is one loving family.",
            deityOrContext = "సనాతన ధర్మ సందేశం",
            icon = "✨"
        ),
        DevotionalQuote(
            id = 6,
            titleTelugu = "ధర్మ రక్షణ",
            titleEnglish = "Protect Dharma, Dharma Protects",
            slokaOrSukti = "ధర్మ ఏవ హతో హంతి ధర్మో రక్షతి రక్షితః ।\nతస్మాద్ధర్మో న త్యక్తవ్యో మా నో ధర్మో హతోఽవధీత్ ॥",
            source = "మనుస్మృతి / మహాభారతం",
            meaningTelugu = "నశింపజేయబడిన ధర్మం మనిషినే నాశనం చేస్తుంది; రక్షింపబడిన ధర్మం మనిషిని సర్వదా రక్షిస్తుంది. కావున ధర్మాన్ని ఎన్నడూ వదలరాదు.",
            meaningEnglish = "Dharma destroyed destroys its destroyer; Dharma safeguarded safeguards its protector. Hence, Dharma must never be abandoned.",
            deityOrContext = "ధర్మ శాస్త్ర సూత్రం",
            icon = "⚖️"
        ),
        DevotionalQuote(
            id = 7,
            titleTelugu = "పరిపూర్ణ శరణాగతి",
            titleEnglish = "Absolute Surrender to the Divine",
            slokaOrSukti = "సర్వధర్మాన్ పరిత్యజ్య మామేకం శరణం వ్రజ ।\nఅహం త్వా సర్వపాపేభ్యో మోక్షయిష్యామి మా శుచః ॥",
            source = "శ్రీమద్భగవద్గీత - 18.66",
            meaningTelugu = "సమస్త లౌకిక, మానసిక బంధనాలను నాయందు అర్పించి, నన్నొక్కడినే శరణు వేడుము. నిన్ను సమస్త పాపాల నుండి విముక్తుడిని చేస్తాను, శోకింపకు.",
            meaningEnglish = "Abandon all varieties of attachment and simply surrender unto Me alone. I shall deliver you from all sinful reactions; do not grieve.",
            deityOrContext = "చరమ శ్లోకం",
            icon = "🪷"
        ),
        DevotionalQuote(
            id = 8,
            titleTelugu = "నిజమైన విద్య & వినయం",
            titleEnglish = "True Wisdom & Humility",
            slokaOrSukti = "విద్యా దదాతి వినయం వినయాద్యాతి పాత్రతామ్ ।\nపాత్రత్వాద్ధనమాప్నోతి ధనాద్ధర్మం తతః సుఖమ్ ॥",
            source = "హితోపదేశం",
            meaningTelugu = "సద్విద్య వినయాన్ని ఇస్తుంది; వినయం వల్ల అర్హత వస్తుంది; అర్హత వల్ల సన్మార్గ ధనం లభిస్తుంది; ఆ ధనంతో ధర్మకార్యాలు ఆచరించడం ద్వారా శాశ్వత సుఖం ప్రాప్తిస్తుంది.",
            meaningEnglish = "True knowledge bestows humility; humility creates worthiness; worthiness brings wealth ethically; wealth practiced through Dharma leads to enduring peace and happiness.",
            deityOrContext = "ఆచార్య నీతి",
            icon = "📚"
        ),
        DevotionalQuote(
            id = 9,
            titleTelugu = "ఆత్మజ్ఞానం & నిర్భయత్వం",
            titleEnglish = "The Eternal Soul",
            slokaOrSukti = "నైనం ఛిందంతి శస్త్రాణి నైనం దహతి పావకః ।\nన చైనం క్లేదయంత్యాపో న శోషయతి మారుతః ॥",
            source = "శ్రీమద్భగవద్గీత - 2.23",
            meaningTelugu = "ఆత్మను ఏ ఆయుధాలూ ఛేదించలేవు; అగ్ని దహించలేదు; నీరు తడపలేదు; వాయువు ఆర్పలేదు. ఆత్మ అమరమైనది మరియు నాశనం లేనిది.",
            meaningEnglish = "Weapons cannot cleave the soul, fire cannot burn it, water cannot wet it, nor can the wind wither it. The soul is imperishable and eternal.",
            deityOrContext = "సాంఖ్య యోగం",
            icon = "☀️"
        ),
        DevotionalQuote(
            id = 10,
            titleTelugu = "సమత్వమే యోగం",
            titleEnglish = "Equanimity is Yoga",
            slokaOrSukti = "యోగస్థః కురు కర్మాణి సంగం త్యక్త్వా ధనంజయ ।\nసిద్ధ్యసిద్ధ్యోః సమో భూత్వా సమత్వం యోగ ఉచ్యతే ॥",
            source = "శ్రీమద్భగవద్గీత - 2.48",
            meaningTelugu = "ఓ ధనంజయ! ఫలితాలపై వ్యామోహాన్ని వీడి, విజయం-అపజయాలలో సమానమైన చిత్తాన్ని కలిగి కర్తవ్యాలను ఆచరించు. ఈ మానసిక సమత్వమే 'యోగం'.",
            meaningEnglish = "Be steadfast in yoga, perform your duty without attachment to success or failure. Such evenness of mind is called Yoga.",
            deityOrContext = "కర్మయోగ ప్రబోధం",
            icon = "🧘"
        ),
        DevotionalQuote(
            id = 11,
            titleTelugu = "పరోపకార గుణం",
            titleEnglish = "Selfless Service to Others",
            slokaOrSukti = "పరోపకారాయ ఫలంతి వృక్షాః పరోపకారాయ వహంతి నద్యః ।\nపరోపకారాయ దుహంతి గావః పరోపకారార్థమిదం శరీరమ్ ॥",
            source = "సుభాషిత రత్నాకరం",
            meaningTelugu = "వృక్షాలు పరుల కోసమే పండ్లను ఇస్తాయి; నదులు ఇతరుల కోసమే ప్రవహిస్తాయి; ఆవులు పరుల కోసమే పాలిస్తాయి; అలాగే ఈ మానవ శరీరమూ ఇతరులకు ఉపకారం చేయడానికే ఏర్పడింది.",
            meaningEnglish = "Trees bear fruit for others; rivers flow for others; cows give milk for others; this human body itself is meant for the service and welfare of others.",
            deityOrContext = "సుభాషిత అమృతం",
            icon = "🌳"
        ),
        DevotionalQuote(
            id = 12,
            titleTelugu = "పరిశుద్ధ భక్తి నైవేద్యం",
            titleEnglish = "A Loving Offering to the Divine",
            slokaOrSukti = "పత్రం పుష్పం ఫలం తోయం యో మే భక్త్యా ప్రయచ్ఛతి ।\nతదహం భక్త్యుపహృతమశ్నామి ప్రయతాత్మనః ॥",
            source = "శ్రీమద్భగవద్గీత - 9.26",
            meaningTelugu = "ఎవరైనా నిర్మలమైన భక్తితో నాకు ఒక ఆకు, పువ్వు, పండు లేదా కొద్దిగా నీరు సమర్పించినా... ఆ నిష్కల్మష భక్తిపూర్వక నైవేద్యాన్ని నేను ప్రీతితో స్వీకరిస్తాను.",
            meaningEnglish = "If one offers Me with love and pure devotion even a leaf, a flower, a fruit, or water, I accept that loving offering with immense joy.",
            deityOrContext = "భక్తి యోగం",
            icon = "🌺"
        ),
        DevotionalQuote(
            id = 13,
            titleTelugu = "కార్య సాధన & పరిశ్రమ",
            titleEnglish = "Diligence Brings Achievement",
            slokaOrSukti = "ఉద్యమేన హి సిధ్యంతి కార్యాణి న మనోరథైః ।\nన హి సుప్తస్య సింహస్య ప్రవిశంతి ముఖే మృగాః ॥",
            source = "పంచతంత్రం",
            meaningTelugu = "ఏ పనైనా శ్రమ మరియు ప్రయత్నం వల్లనే సిద్ధిస్తుంది కానీ కేవలం ఆలోచించడం వల్ల కాదు. నిద్రిస్తున్న సింహం నోటిలోకి జింకలు వాటంతట అవే వచ్చి చేరవు కదా!",
            meaningEnglish = "Tasks are accomplished only through sincere enterprise and effort, never through mere daydreams. Deer do not walk into the mouth of a sleeping lion.",
            deityOrContext = "నీతి శాస్త్రం",
            icon = "🦁"
        ),
        DevotionalQuote(
            id = 14,
            titleTelugu = "క్షమా గుణం & శాంతి",
            titleEnglish = "The Shield of Forgiveness",
            slokaOrSukti = "క్షమా శస్త్రం కరే యస్య దుర్జనః కిం కరిష్యతి ।\nఅతృణే పతితో వహ్నిః స్వయమేవోపశామ్యతి ॥",
            source = "మహాభారతం - ఉద్యోగ పర్వం",
            meaningTelugu = "ఎవరి చేతిలో అయితే 'ఓర్పు, క్షమ' అనే ఆయుధం ఉంటుందో అతడిని దుష్టులు ఏమీ చేయలేరు. గడ్డి లేని నేలపై పడిన నిప్పు రవ్వ దానంతట అదే ఆరిపోతుంది.",
            meaningEnglish = "What harm can an adversary do to one whose weapon is patience and forgiveness? Fire falling on ground without grass extinguishes on its own.",
            deityOrContext = "విదుర ప్రబోధం",
            icon = "🕊️"
        ),
        DevotionalQuote(
            id = 15,
            titleTelugu = "తల్లిదండ్రులు & గురువు పూజనీయులు",
            titleEnglish = "Revering Elders and Masters",
            slokaOrSukti = "మాతృదేవో భవ । పితృదేవో భవ ।\nఆచార్యదేవో భవ । అతిథిదేవో భవ ॥",
            source = "తైత్తిరీయోపనిషత్ - శీక్షావల్లీ",
            meaningTelugu = "తల్లిని దైవంగా భావించు, తండ్రిని దైవంగా సేవించు, జ్ఞానాన్నిచ్చే గురువును దైవంగా కొలువు, గృహానికి వచ్చిన అతిథిని దైవసమానంగా ఆదరించు.",
            meaningEnglish = "Treat your mother as divine; treat your father as divine; honor your spiritual teacher as divine; welcome your guest as an embodiment of the divine.",
            deityOrContext = "ఉపనిషత్ ఆదర్శాలు",
            icon = "🙏"
        ),
        DevotionalQuote(
            id = 16,
            titleTelugu = "నిరంతర జ్ఞానార్జన & శ్రద్ధ",
            titleEnglish = "Faith Bestows True Knowledge",
            slokaOrSukti = "శ్రద్ధావాఁల్లభతే జ్ఞానం తత్పరః సంయతేంద్రియః ।\nజ్ఞానం లబ్ధ్వా పరాం శాంతిమచిరేణాధిగచ్ఛతి ॥",
            source = "శ్రీమద్భగవద్గీత - 4.39",
            meaningTelugu = "తీవ్రమైన శ్రద్ధ మరియు ఇంద్రియ నిగ్రహం కలిగిన వ్యక్తియే దివ్య జ్ఞానాన్ని పొందుతాడు. అటువంటి జ్ఞానాన్ని సాధించిన వెంటనే పరమ శాంతిని చేరుకుంటాడు.",
            meaningEnglish = "A person with unwavering faith and mastered senses attains divine wisdom. Having attained this sacred knowledge, one immediately achieves supreme inner peace.",
            deityOrContext = "జ్ఞాన కర్మ సన్యాస యోగం",
            icon = "🕯️"
        ),
        DevotionalQuote(
            id = 17,
            titleTelugu = "సత్య దర్శనం & దివ్య ప్రార్థన",
            titleEnglish = "Lead from Darkness into Light",
            slokaOrSukti = "అసతో మా సద్గమయ । తమసో మా జ్యోతిర్గమయ ।\nమృత్యోర్మా అమృతం గమయ ॥",
            source = "బృహదారణ్యకోపనిషత్ - 1.3.28",
            meaningTelugu = "నన్ను అసత్యం నుండి సత్యము వైపుకు నడుపుము; అజ్ఞానమనే చీకటి నుండి జ్ఞానమనే దివ్య కాంతి వైపుకు నడుపుము; మరణభయం నుండి అమరత్వము వైపుకు నడుపుము.",
            meaningEnglish = "Lead us from the unreal to the real; lead us from the darkness of ignorance to the illumination of knowledge; lead us from mortality to eternal truth.",
            deityOrContext = "పవిత్ర ప్రార్థనా మంత్రం",
            icon = "🪔"
        ),
        DevotionalQuote(
            id = 18,
            titleTelugu = "సర్వవ్యాపక పరమాత్మ దర్శనం",
            titleEnglish = "The Omnipresent Divine Spark",
            slokaOrSukti = "ఈశావాస్యమిదం సర్వం యత్కించ జగత్యాం జగత్ ।\nతేన త్యక్తేన భుంజీథా మా గృధః కస్యస్విద్ధనమ్ ॥",
            source = "ఈశావాస్యోపనిషత్ - 1",
            meaningTelugu = "ఈ అనంత సృష్టిలో కదిలేవి, కదలనివి అన్నీ పరమాత్మ నివాస స్థానాలే. త్యాగభావంతో జీవనాన్ని అనుభవించుము; ఇతరుల సంపదపై దురాశ పడవద్దు.",
            meaningEnglish = "All this creation, whatsoever moves in this moving world, is pervaded by the Supreme Being. Enjoy life with detachment; do not covet anyone's wealth.",
            deityOrContext = "ఈశోపనిషత్ ప్రథమ మంత్రం",
            icon = "🪐"
        ),
        DevotionalQuote(
            id = 19,
            titleTelugu = "ధర్మ స్థాపన ప్రతిజ్ఞ",
            titleEnglish = "Descent for the Protection of Good",
            slokaOrSukti = "యదా యదా హి ధర్మస్య గ్లానిర్భవతి భారత ।\nఅభ్యుత్థానమధర్మస్య తదాత్మానం సృజామ్యహమ్ ॥",
            source = "శ్రీమద్భగవద్గీత - 4.7",
            meaningTelugu = "ఓ అర్జునా! ఎప్పుడెప్పుడు ధర్మానికి హాని కలుగుతుందో, అధర్మం ప్రబలుతుందో... అప్పుడప్పుడు ధర్మోద్ధరణ కోసం నేను అవతరిస్తూ ఉంటాను.",
            meaningEnglish = "Whenever and wherever there is a decline in Dharma and a rise in unrighteousness, at that time I manifest Myself upon this earth.",
            deityOrContext = "అవతార తత్త్వం",
            icon = "🏹"
        ),
        DevotionalQuote(
            id = 20,
            titleTelugu = "సద్గురు పాద వందనం",
            titleEnglish = "Salutations to the Supreme Guide",
            slokaOrSukti = "గురుర్బ్రహ్మా గురుర్విష్ణుః గురుర్దేవో మహేశ్వరః ।\nగురుస్సాక్షాత్ పరబ్రహ్మ తస్మై శ్రీగురవే నమః ॥",
            source = "శ్రీ గురు గీత",
            meaningTelugu = "గురువే సృష్టికర్తయైన బ్రహ్మ, స్థితికారుడైన విష్ణువు మరియు లయకారుడైన పరమేశ్వరుడు. గురువే సాక్షాత్ పరబ్రహ్మ స్వరూపము; అట్టి గురుదేవునికి ప్రణామములు.",
            meaningEnglish = "The Guru is Brahma the creator, Vishnu the preserver, and Maheshwara the transformer. The Guru is verily the Supreme Reality; salutations to that revered Master.",
            deityOrContext = "గురు వందనం",
            icon = "🪷"
        ),
        DevotionalQuote(
            id = 21,
            titleTelugu = "సంతోషమే పరమ సంపద",
            titleEnglish = "Contentment is the Highest Wealth",
            slokaOrSukti = "సంతోషామృతతృప్తానాం యత్సుఖం శాంతచేతసామ్ ।\nన చ తద్ధనలుబ్ధానామితశ్చేతశ్చ ధావతామ్ ॥",
            source = "చాణక్య నీతి",
            meaningTelugu = "సంతోషమనే అమృతాన్ని ఆస్వాదించి శాంత చిత్తులైన వారు పొందే అలౌకిక ఆనందం, ధనాశతో అటూఇటూ పరుగులు తీసే లోభులకు ఎన్నటికీ దొరకదు.",
            meaningEnglish = "The sublime bliss experienced by those who are satisfied with the nectar of contentment can never be experienced by greedy minds chasing material riches.",
            deityOrContext = "జీవన సత్యం",
            icon = "💎"
        ),
        DevotionalQuote(
            id = 22,
            titleTelugu = "గాయత్రీ దివ్య ప్రకాశం",
            titleEnglish = "Awakening Divine Intellect",
            slokaOrSukti = "ఓం భూర్భువః స్వః తత్సవితుర్వరేణ్యం\nభర్గో దేవస్య ధీమహి ధియో యో నః ప్రచోదయాత్ ॥",
            source = "ఋగ్వేదం - 3.62.10",
            meaningTelugu = "భూః, భువః, సువః లోకాలను వెలిగించే తేజోమయుడైన సూర్యభగవానుని దివ్య తేజాన్ని ధ్యానిస్తున్నాము. ఆయన మన బుద్ధులను సన్మార్గంలో ప్రేరేపించుగాక!",
            meaningEnglish = "We meditate upon the supreme radiant light of the divine sun, who illuminates all three realms. May that sacred grace inspire and illuminate our intellect.",
            deityOrContext = "వేదమాత గాయత్రీ మంత్రం",
            icon = "🌞"
        ),
        DevotionalQuote(
            id = 23,
            titleTelugu = "మనస్సు నిగ్రహం & విజయం",
            titleEnglish = "Mastery Over the Mind",
            slokaOrSukti = "బంధురాత్మాత్మనస్తస్య యేనాత్మైవాత్మనా జితః ।\nఅనాత్మనస్తు శత్రుత్వే వర్తేతాత్మైవ శత్రువత్ ॥",
            source = "శ్రీమద్భగవద్గీత - 6.6",
            meaningTelugu = "ఎవరైతే తన మనస్సును జయించారో వారికి మనస్సే గొప్ప మిత్రుడు. మనస్సును నిగ్రహించలేని వ్యక్తికి తన మనస్సే బద్ధ శత్రువుగా మారి నాశనం చేస్తుంది.",
            meaningEnglish = "For one who has conquered the mind, the mind is the dearest friend; but for one who has failed to do so, the mind remains the greatest enemy.",
            deityOrContext = "ఆత్మసంయమ యోగం",
            icon = "🛡️"
        ),
        DevotionalQuote(
            id = 24,
            titleTelugu = "సకల దైవ సమర్పణ",
            titleEnglish = "Thou Art Everything to Me",
            slokaOrSukti = "త్వమేవ మాతా చ పితా త్వమేవ త్వమేవ బంధుశ్చ సఖా త్వమేవ ।\nత్వమేవ విద్యా ద్రవిణం త్వమేవ త్వమేవ సర్వం మమ దేవదేవ ॥",
            source = "పాండవ గీత",
            meaningTelugu = "ఓ దేవాదిదేవా! నీవే నా తల్లివి, తండ్రివి, బంధువువు మరియు ప్రాణమిత్రుడవు. నా విద్యవు, సంపదవు సర్వస్వము నీవే అయి ఉన్నావు.",
            meaningEnglish = "You alone are my mother, my father, my kin, and my true friend. You alone are my knowledge and my treasure; You are everything to me, O Lord of Lords.",
            deityOrContext = "భక్తి ప్రపత్తి",
            icon = "💖"
        ),
        DevotionalQuote(
            id = 25,
            titleTelugu = "శాంతియుత విష్ణు దర్శనం",
            titleEnglish = "Ode to the Universal Guardian",
            slokaOrSukti = "శాంతాకారం భుజగశయనం పద్మనాభం సురేశం\nవిశ్వాధారం గగనసదృశం మేఘవర్ణం శుభాంగమ్ ।\nవందే విష్ణుం భవభయహరం సర్వలోకైకనాథమ్ ॥",
            source = "శ్రీ విష్ణు సహస్రనామ పూర్వపీఠిక",
            meaningTelugu = "శాంతమూర్తి, ఆదిశేషునిపై శయనించినవాడు, విశ్వానికి మూలాధారమైనవాడు, సమస్త సంసార భయాలను దూరం చేసే సకలలోక నాథుడైన శ్రీమహావిష్ణువునకు నమస్కారములు.",
            meaningEnglish = "I bow to Lord Vishnu, the serene one resting on Adishesha, the support of the universe, who dispels the fear of worldly sorrows, the sole sovereign of all realms.",
            deityOrContext = "విష్ణు స్తుతి",
            icon = "🐚"
        ),
        DevotionalQuote(
            id = 26,
            titleTelugu = "అంతర్గత దివ్య స్వరూపం",
            titleEnglish = "I Am Pure Consciousness",
            slokaOrSukti = "మనోబుద్ధ్యహంకార చిత్తాని నాహం\nన చ శ్రోత్రజిహ్వే న చ ఘ్రాణనేత్రే ।\nచిదానందరూపః శివోఽహం శివోఽహమ్ ॥",
            source = "నిర్వాణషట్కమ్ - ఆదిశంకరాచార్య",
            meaningTelugu = "నేను మనస్సు, బుద్ధి, అహంకారం, చిత్తం కాను; కన్ను, చెవి, ముక్కు, నాలుక కాను. నేను నిత్య సచ్చిదానంద స్వరూపమైన పరమశివుడను.",
            meaningEnglish = "I am not mind, intellect, ego, or thought; nor ears, tongue, nose, or eyes. I am pure bliss and radiant consciousness; I am Shiva, I am Shiva.",
            deityOrContext = "అద్వైత వేదాంత దర్శనం",
            icon = "🔱"
        ),
        DevotionalQuote(
            id = 27,
            titleTelugu = "ఆలస్యమే అతిపెద్ద శత్రువు",
            titleEnglish = "Procrastination is the Foe",
            slokaOrSukti = "ఆలస్యం హి మనుష్యాణాం శరీరస్థో మహాన్ రిపుః ।\nనాస్త్యుద్యమసమో బంధుః కృత్వా యం నావసీదతి ॥",
            source = "భర్తృహరి నీతి శతకం",
            meaningTelugu = "మనిషి శరీరంలో ఉండే సోమరితనమే అతడి అతిపెద్ద శత్రువు. పరిశ్రమ, కృషి కంటే గొప్ప మిత్రుడు మరొకడు లేడు; కష్టపడేవాడు ఎన్నడూ అధోగతి పాలవ్వడు.",
            meaningEnglish = "Laziness lurking inside the body is the greatest enemy of humankind. There is no friend equal to honest hard work, pursuing which one never falls.",
            deityOrContext = "సుభాషిత రత్నం",
            icon = "⚡"
        ),
        DevotionalQuote(
            id = 28,
            titleTelugu = "సన్మార్గ ప్రార్థన",
            titleEnglish = "Lead Us by the Noble Path",
            slokaOrSukti = "అగ్నే నయ సుపథా రాయే అస్మాన్\nవిశ్వాని దేవ వయునాని విద్వాన్ ।\nయుయోధ్యస్మజ్జుహురాణమేనో\nభూయిష్ఠాం తే నమ ఉక్తిం విధేమ ॥",
            source = "ఈశావాస్యోపనిషత్ - 18",
            meaningTelugu = "ఓ అగ్నిదేవా! సమస్త కర్మలను ఎరిగిన స్వామీ! మమ్మల్ని శుభప్రదమైన, నీతివంతమైన సన్మార్గంలో నడిపించుము; మాలోని కల్మషాలను తొలగించుము. నీకు మా హృదయపూర్వక నమస్కారములు.",
            meaningEnglish = "O Agni, who knows all deeds! Lead us along the right path to auspicious abundance; remove from us any deceitful sin. We offer our deepest homage unto You.",
            deityOrContext = "వేద ప్రార్థన",
            icon = "🔥"
        ),
        DevotionalQuote(
            id = 29,
            titleTelugu = "సుఖదుఃఖాలలో సమభావం",
            titleEnglish = "Fortitude in Joy and Sorrow",
            slokaOrSukti = "సుఖదుఃఖే సమే కృత్వా లాభాలాభౌ జయాజయౌ ।\nతతో యుద్ధాయ యుజ్యస్వ నైవం పాపమవాప్స్యసి ॥",
            source = "శ్రీమద్భగవద్గీత - 2.38",
            meaningTelugu = "సుఖదుఃఖాలను, లాభనష్టాలను, గెలుపోటములను సమానంగా భావించి నీ కర్తవ్యాన్ని నిర్వహించుము. అలా నిష్కామంగా చేయడం వల్ల నీకు ఎటువంటి పాపమూ అంటదు.",
            meaningEnglish = "Holding pleasure and pain, gain and loss, victory and defeat as equal, engage wholeheartedly in your duty; thus you shall incur no sin.",
            deityOrContext = "గీతా సందేశం",
            icon = "🏹"
        ),
        DevotionalQuote(
            id = 30,
            titleTelugu = "కర్మ నిగూఢతత్త్వం",
            titleEnglish = "The Deep Mysteries of Action",
            slokaOrSukti = "కర్మణో హ్యపి బోద్ధవ్యం బోద్ధవ్యం చ వికర్మణః ।\nఅకర్మణశ్చ బోద్ధవ్యం గహనా కర్మణో గతిః ॥",
            source = "శ్రీమద్భగవద్గీత - 4.17",
            meaningTelugu = "సత్కర్మ అంటే ఏమిటో, నిషిద్ధ కర్మ అంటే ఏమిటో, కర్మరాహిత్యం అంటే ఏమిటో తెలుసుకోవాలి. కర్మ గతి ఎంతో లోతైనది మరియు గహనమైనది.",
            meaningEnglish = "One must understand the nature of right action, wrongful action, and inaction. Truly profound and mysterious is the trajectory of Karma.",
            deityOrContext = "కర్మ వివేకం",
            icon = "☸️"
        ),
        DevotionalQuote(
            id = 31,
            titleTelugu = "సకల పాపహర హరి నామస్మరణ",
            titleEnglish = "The Cleansing Light of Divine Remembrance",
            slokaOrSukti = "హరే రామ హరే రామ రామ రామ హరే హరే ।\nహరే కృష్ణ హరే కృష్ణ కృష్ణ కృష్ణ హరే హరే ॥",
            source = "కలిసంతారణోపనిషత్",
            meaningTelugu = "కలియుగంలో మనస్సును పవిత్రం చేసి, సకల పాపాలను పోగొట్టి, దైవానుగ్రహాన్ని ప్రసాదించే పరమ పవిత్ర తారక నామ మహా మంత్రం.",
            meaningEnglish = "The supreme Maha-Mantra revealed in the Kali-Santarana Upanishad that cleanses the mind, dissolves negativities, and bestows sublime spiritual grace.",
            deityOrContext = "మహా మంత్రం",
            icon = "🪘"
        )
    )

    /**
     * Returns a quote uniquely mapped for the given calendar date.
     * Guaranteed never to throw an index error.
     */
    fun getDailyQuote(date: LocalDate): DevotionalQuote {
        val totalDays = date.toEpochDay()
        val index = ((totalDays % DAILY_QUOTES.size + DAILY_QUOTES.size) % DAILY_QUOTES.size).toInt()
        return DAILY_QUOTES[index]
    }

    /**
     * Formats the devotional quote into a clean, WhatsApp-friendly devotional share text.
     */
    fun formatShareText(quote: DevotionalQuote, date: LocalDate): String {
        return buildString {
            appendLine("🚩 నిత్య భక్తి సూక్తి (Daily Devotional Quote)")
            appendLine("📅 తేదీ: ${date.dayOfMonth}-${date.monthValue}-${date.year}")
            appendLine("✨ ${quote.titleTelugu} (${quote.titleEnglish})")
            appendLine("🪔 మూల శ్లోకం / సూక్తి:")
            appendLine(quote.slokaOrSukti)
            appendLine("📖 ఆధారం: ${quote.source} | ${quote.deityOrContext}")
            appendLine()
            appendLine("🌺 తాత్పర్యం:")
            appendLine(quote.meaningTelugu)
            appendLine()
            appendLine("English Essence:")
            appendLine(quote.meaningEnglish)
            appendLine()
            appendLine("🕉️ శ్రీ వేద తెలుగు పంచాంగం (Veda Panchangam)")
        }
    }
}
