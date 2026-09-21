package com.example.model

data class IndianDistrict(
    val id: String,
    val nameEn: String,
    val nameTe: String,
    val stateEn: String,
    val stateTe: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String = "Asia/Kolkata"
) {
    fun toCityLocation(): CityLocation {
        return CityLocation(
            id = this.id,
            name = this.nameEn,
            stateOrCountry = "${this.stateEn}, India",
            latitude = this.latitude,
            longitude = this.longitude,
            timezoneId = this.timezone
        )
    }
}

object IndianDistrictsRepository {
    val ALL_DISTRICTS: List<IndianDistrict> = listOf(
        // ==================== TELANGANA (33 Districts) ====================
        IndianDistrict("tg_hyd", "Hyderabad", "హైదరాబాద్", "Telangana", "తెలంగాణ", 17.3850, 78.4867),
        IndianDistrict("tg_sec", "Secunderabad", "సికింద్రాబాద్", "Telangana", "తెలంగాణ", 17.4399, 78.4983),
        IndianDistrict("tg_adi", "Adilabad", "ఆదిలాబాద్", "Telangana", "తెలంగాణ", 19.6641, 78.5320),
        IndianDistrict("tg_bha", "Bhadradri Kothagudem", "భద్రాద్రి కొత్తగూడెం (భద్రాచలం)", "Telangana", "తెలంగాణ", 17.5528, 80.6178),
        IndianDistrict("tg_han", "Hanumakonda", "హనుమకొండ", "Telangana", "తెలంగాణ", 18.0138, 79.5447),
        IndianDistrict("tg_jgt", "Jagtial", "జగిత్యాల", "Telangana", "తెలంగాణ", 18.7946, 78.9129),
        IndianDistrict("tg_jan", "Jangaon", "జనగామ", "Telangana", "తెలంగాణ", 17.7259, 79.1558),
        IndianDistrict("tg_jay", "Jayashankar Bhupalpally", "జయశంకర్ భూపాలపల్లి", "Telangana", "తెలంగాణ", 18.4319, 79.8659),
        IndianDistrict("tg_jog", "Jogulamba Gadwal", "జోగులాంబ గద్వాల (ఆలంపూర్)", "Telangana", "తెలంగాణ", 16.2323, 77.8078),
        IndianDistrict("tg_kam", "Kamareddy", "కామారెడ్డి", "Telangana", "తెలంగాణ", 18.3248, 78.3400),
        IndianDistrict("tg_krm", "Karimnagar", "కరీంనగర్", "Telangana", "తెలంగాణ", 18.4386, 79.1288),
        IndianDistrict("tg_kham", "Khammam", "ఖమ్మం", "Telangana", "తెలంగాణ", 17.2473, 80.1514),
        IndianDistrict("tg_kbr", "Kumuram Bheem Asifabad", "కుమ్రం భీమ్ ఆసిఫాబాద్", "Telangana", "తెలంగాణ", 19.3621, 79.2890),
        IndianDistrict("tg_mhb_b", "Mahabubabad", "మహబూబాబాద్", "Telangana", "తెలంగాణ", 17.5986, 80.0041),
        IndianDistrict("tg_mhb_n", "Mahabubnagar", "మహబూబ్ నగర్", "Telangana", "తెలంగాణ", 16.7488, 77.9845),
        IndianDistrict("tg_man", "Mancherial", "మంచిర్యాల", "Telangana", "తెలంగాణ", 18.8679, 79.4639),
        IndianDistrict("tg_med", "Medak", "మెదక్", "Telangana", "తెలంగాణ", 18.0456, 78.2625),
        IndianDistrict("tg_mdc", "Medchal Malkajgiri", "మేడ్చల్ మల్కాజిగిరి", "Telangana", "తెలంగాణ", 17.6297, 78.4814),
        IndianDistrict("tg_mul", "Mulugu", "ములుగు", "Telangana", "తెలంగాణ", 18.1916, 79.9419),
        IndianDistrict("tg_nag", "Nagarkurnool", "నాగర్ కర్నూల్", "Telangana", "తెలంగాణ", 16.4866, 78.3090),
        IndianDistrict("tg_nal", "Nalgonda", "నల్గొండ", "Telangana", "తెలంగాణ", 17.0575, 79.2684),
        IndianDistrict("tg_nrp", "Narayanpet", "నారాయణపేట", "Telangana", "తెలంగాణ", 16.7369, 77.4987),
        IndianDistrict("tg_nrm", "Nirmal", "నిర్మల్", "Telangana", "తెలంగాణ", 19.0964, 78.3429),
        IndianDistrict("tg_nzb", "Nizamabad", "నిజామాబాద్", "Telangana", "తెలంగాణ", 18.6725, 78.0941),
        IndianDistrict("tg_ped", "Peddapalli", "పెద్దపల్లి", "Telangana", "తెలంగాణ", 18.6163, 79.3776),
        IndianDistrict("tg_raj", "Rajanna Sircilla", "రాజన్న సిరిసిల్ల (వేములవాడ)", "Telangana", "తెలంగాణ", 18.3888, 78.8354),
        IndianDistrict("tg_ran", "Ranga Reddy", "రంగారెడ్డి", "Telangana", "తెలంగాణ", 17.1883, 78.4357),
        IndianDistrict("tg_san", "Sangareddy", "సంగారెడ్డి", "Telangana", "తెలంగాణ", 17.6190, 78.0814),
        IndianDistrict("tg_sid", "Siddipet", "సిద్దిపేట", "Telangana", "తెలంగాణ", 18.1018, 78.8520),
        IndianDistrict("tg_sur", "Suryapet", "సూర్యాపేట", "Telangana", "తెలంగాణ", 17.1439, 79.6239),
        IndianDistrict("tg_vik", "Vikarabad", "వికారాబాద్", "Telangana", "తెలంగాణ", 17.3366, 77.9048),
        IndianDistrict("tg_wan", "Wanaparthy", "వనపర్తి", "Telangana", "తెలంగాణ", 16.3624, 78.0628),
        IndianDistrict("tg_war", "Warangal", "వరంగల్", "Telangana", "తెలంగాణ", 17.9689, 79.5941),
        IndianDistrict("tg_yad", "Yadadri Bhuvanagiri", "యాదాద్రి భువనగిరి (యాదగిరిగుట్ట)", "Telangana", "తెలంగాణ", 17.5123, 78.8872),

        // ==================== ANDHRA PRADESH (26 Districts) ====================
        IndianDistrict("ap_ank", "Anakapalli", "అనకాపల్లి", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 17.6896, 83.0033),
        IndianDistrict("ap_atp", "Ananthapuramu", "అనంతపురం", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 14.6819, 77.6006),
        IndianDistrict("ap_anm", "Annamayya", "అన్నమయ్య (రాయచోటి)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 14.0583, 78.7523),
        IndianDistrict("ap_bap", "Bapatla", "బాపట్ల", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 15.9042, 80.4674),
        IndianDistrict("ap_cht", "Chittoor", "చిత్తూరు", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 13.2172, 79.1003),
        IndianDistrict("ap_egd", "East Godavari", "తూర్పు గోదావరి (రాజమహేంద్రవరం)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 17.0005, 81.8040),
        IndianDistrict("ap_elu", "Eluru", "ఏలూరు", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.7107, 81.0952),
        IndianDistrict("ap_gun", "Guntur", "గుంటూరు", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.3067, 80.4365),
        IndianDistrict("ap_kak", "Kakinada", "కాకినాడ", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.9891, 82.2475),
        IndianDistrict("ap_kon", "Dr. B.R. Ambedkar Konaseema", "డా. బి.ఆర్. అంబేడ్కర్ కోనసీమ (అమలాపురం)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.5787, 82.0061),
        IndianDistrict("ap_kri", "Krishna", "కృష్ణా (మచిలీపట్నం)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.1875, 81.1389),
        IndianDistrict("ap_kur", "Kurnool", "కర్నూలు", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 15.8281, 78.0373),
        IndianDistrict("ap_man", "Parvathipuram Manyam", "పార్వతీపురం మన్యం", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 18.7796, 83.4287),
        IndianDistrict("ap_nan", "Nandyal", "నంద్యాల (శ్రీశైలం)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 15.4889, 78.4836),
        IndianDistrict("ap_ntr", "NTR (Vijayawada)", "ఎన్టీఆర్ (విజయవాడ)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.5062, 80.6480),
        IndianDistrict("ap_pal", "Palnadu", "పల్నాడు (నరసరావుపేట)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.2361, 80.0537),
        IndianDistrict("ap_pra", "Prakasam", "ప్రకాశం (ఒంగోలు)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 15.5057, 80.0499),
        IndianDistrict("ap_srk", "Srikakulam", "శ్రీకాకుళం", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 18.2949, 83.8938),
        IndianDistrict("ap_nel", "SPSR Nellore", "శ్రీ పొట్టి శ్రీరాములు నెల్లూరు", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 14.4426, 79.9865),
        IndianDistrict("ap_sss", "Sri Sathya Sai", "శ్రీ సత్యసాయి (పుట్టపర్తి)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 14.1652, 77.8109),
        IndianDistrict("ap_tpt", "Tirupati", "తిరుపతి", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 13.6288, 79.4192),
        IndianDistrict("ap_vsp", "Visakhapatnam", "విశాఖపట్నం (వైజాగ్)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 17.6868, 83.2185),
        IndianDistrict("ap_vzm", "Vizianagaram", "విజయనగరం", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 18.1067, 83.3956),
        IndianDistrict("ap_wgd", "West Godavari", "పశ్చిమ గోదావరి (భీమవరం)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 16.5449, 81.5212),
        IndianDistrict("ap_kad", "YSR Kadapa", "వైఎస్సార్ కడప", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 14.4673, 78.8242),
        IndianDistrict("ap_asr", "Alluri Sitharama Raju", "అల్లూరి సీతారామరాజు (పాడేరు)", "Andhra Pradesh", "ఆంధ్రప్రదేశ్", 18.0833, 82.6667),

        // ==================== KARNATAKA (31 Districts) ====================
        IndianDistrict("ka_blr_u", "Bengaluru Urban", "బెంగళూరు అర్బన్", "Karnataka", "కర్ణాటక", 12.9716, 77.5946),
        IndianDistrict("ka_blr_r", "Bengaluru Rural", "బెంగళూరు రూరల్", "Karnataka", "కర్ణాటక", 13.2382, 77.5385),
        IndianDistrict("ka_mys", "Mysuru (Mysore)", "మైసూరు", "Karnataka", "కర్ణాటక", 12.2958, 76.6394),
        IndianDistrict("ka_mng", "Mangaluru (Dakshina Kannada)", "మంగళూరు (దక్షిణ కన్నడ)", "Karnataka", "కర్ణాటక", 12.9141, 74.8560),
        IndianDistrict("ka_hub", "Hubballi-Dharwad", "హుబ్బళ్ళి - ధార్వాడ్", "Karnataka", "కర్ణాటక", 15.3647, 75.1240),
        IndianDistrict("ka_bel", "Belagavi (Belgaum)", "బెలగావి (బెల్గాం)", "Karnataka", "కర్ణాటక", 15.8497, 74.4977),
        IndianDistrict("ka_kal", "Kalaburagi (Gulbarga)", "కలబురగి (గుల్బర్గా)", "Karnataka", "కర్ణాటక", 17.3297, 76.8343),
        IndianDistrict("ka_bal", "Ballari (Bellary)", "బళ్లారి", "Karnataka", "కర్ణాటక", 15.1394, 76.9214),
        IndianDistrict("ka_dav", "Davanagere", "దావణగెరె", "Karnataka", "కర్ణాటక", 14.4644, 75.9218),
        IndianDistrict("ka_shi", "Shivamogga (Shimoga)", "శివమొగ్గ", "Karnataka", "కర్ణాటక", 13.9299, 75.5681),
        IndianDistrict("ka_udu", "Udupi", "ఉడుపి (శ్రీకృష్ణ మఠం)", "Karnataka", "కర్ణాటక", 13.3409, 74.7421),
        IndianDistrict("ka_tum", "Tumakuru (Tumkur)", "తుమకూరు", "Karnataka", "కర్ణాటక", 13.3379, 77.1173),
        IndianDistrict("ka_has", "Hassan", "హాసన్ (బేలూరు/హళేబీడు)", "Karnataka", "కర్ణాటక", 13.0033, 76.1004),
        IndianDistrict("ka_bid", "Bidar", "బీదర్", "Karnataka", "కర్ణాటక", 17.9104, 77.5199),
        IndianDistrict("ka_rai", "Raichur", "రాయచూర్ (మంత్రాలయం)", "Karnataka", "కర్ణాటక", 16.2120, 77.3439),
        IndianDistrict("ka_vij", "Vijayapura (Bijapur)", "విజయపుర (బీజాపూర్)", "Karnataka", "కర్ణాటక", 16.8302, 75.7100),
        IndianDistrict("ka_bag", "Bagalkote", "బాగల్‌కోట్ (బాదామి)", "Karnataka", "కర్ణాటక", 16.1875, 75.6980),
        IndianDistrict("ka_chk", "Chikkamagaluru", "చిక్కమగళూరు (శృంగేరి)", "Karnataka", "కర్ణాటక", 13.3161, 75.7720),
        IndianDistrict("ka_man", "Mandya", "మండ్య", "Karnataka", "కర్ణాటక", 12.5244, 76.8958),
        IndianDistrict("ka_kol", "Kolar", "కోలార్", "Karnataka", "కర్ణాటక", 13.1367, 78.1291),
        IndianDistrict("ka_utk", "Uttara Kannada (Gokarna)", "ఉత్తర కన్నడ (గోకర్ణ/కార్వార్)", "Karnataka", "కర్ణాటక", 14.8185, 74.1300),

        // ==================== TAMIL NADU (38 Districts) ====================
        IndianDistrict("tn_che", "Chennai", "చెన్నై (మద్రాస్)", "Tamil Nadu", "తమిళనాడు", 13.0827, 80.2707),
        IndianDistrict("tn_cbe", "Coimbatore", "కోయంబత్తూరు", "Tamil Nadu", "తమిళనాడు", 11.0168, 76.9558),
        IndianDistrict("tn_mad", "Madurai", "మదురై (మీనాక్షి అమ్మవారి ఆలయం)", "Tamil Nadu", "తమిళనాడు", 9.9252, 78.1198),
        IndianDistrict("tn_tri", "Tiruchirappalli (Srirangam)", "తిరుచిరాపల్లి (శ్రీరంగం)", "Tamil Nadu", "తమిళనాడు", 10.7905, 78.7047),
        IndianDistrict("tn_sal", "Salem", "సేలం", "Tamil Nadu", "తమిళనాడు", 11.6643, 78.1460),
        IndianDistrict("tn_tir", "Tirunelveli", "తిరునెల్వేలి", "Tamil Nadu", "తమిళనాడు", 8.7139, 77.7567),
        IndianDistrict("tn_vel", "Vellore", "వెల్లూరు (శ్రీపురం స్వర్ణ దేవాలయం)", "Tamil Nadu", "తమిళనాడు", 12.9165, 79.1325),
        IndianDistrict("tn_ero", "Erode", "ఈరోడ్", "Tamil Nadu", "తమిళనాడు", 11.3410, 77.7172),
        IndianDistrict("tn_tha", "Thanjavur", "తంజావూరు (బృహదీశ్వరాలయం)", "Tamil Nadu", "తమిళనాడు", 10.7870, 79.1378),
        IndianDistrict("tn_kan", "Kanchipuram", "కాంచీపురం (కామాక్షి ఆలయం)", "Tamil Nadu", "తమిళనాడు", 12.8342, 79.7036),
        IndianDistrict("tn_tirup", "Tiruppur", "తిరుప్పూర్", "Tamil Nadu", "తమిళనాడు", 11.1085, 77.3411),
        IndianDistrict("tn_kan_k", "Kanyakumari", "కన్యాకుమారి (వివేకానంద రాక్)", "Tamil Nadu", "తమిళనాడు", 8.0883, 77.5385),
        IndianDistrict("tn_ram", "Ramanathapuram (Rameshwaram)", "రామనాథపురం (రామేశ్వరం ధామం)", "Tamil Nadu", "తమిళనాడు", 9.3639, 78.8395),
        IndianDistrict("tn_din", "Dindigul (Palani)", "దిండిగల్ (పళని మురుగన్ ఆలయం)", "Tamil Nadu", "తమిళనాడు", 10.3673, 77.9803),
        IndianDistrict("tn_cud", "Cuddalore (Chidambaram)", "కడలూరు (చిదంబరం నటరాజ ఆలయం)", "Tamil Nadu", "తమిళనాడు", 11.7480, 79.7714),
        IndianDistrict("tn_tiruv", "Tiruvannamalai", "తిరువణ్ణామలై (అరుణాచలం)", "Tamil Nadu", "తమిళనాడు", 12.2253, 79.0747),

        // ==================== MAHARASHTRA ====================
        IndianDistrict("mh_mum", "Mumbai", "ముంబై (సిద్ధివినాయక)", "Maharashtra", "మహారాష్ట్ర", 19.0760, 72.8777),
        IndianDistrict("mh_pun", "Pune", "పూణే", "Maharashtra", "మహారాష్ట్ర", 18.5204, 73.8567),
        IndianDistrict("mh_nag", "Nagpur", "నాగపూర్", "Maharashtra", "మహారాష్ట్ర", 21.1458, 79.0882),
        IndianDistrict("mh_nas", "Nashik (Trimbakeshwar & Shirdi)", "నాసిక్ (త్రయంబకేశ్వర్ / షిర్డీ)", "Maharashtra", "మహారాష్ట్ర", 19.9975, 73.7898),
        IndianDistrict("mh_aur", "Chhatrapati Sambhajinagar (Ellora/Grishneshwar)", "ఛత్రపతి శంభాజీనగర్ (ఘృష్ణేశ్వర్)", "Maharashtra", "మహారాష్ట్ర", 19.8762, 75.3433),
        IndianDistrict("mh_tha", "Thane", "థానే", "Maharashtra", "మహారాష్ట్ర", 19.2183, 72.9781),
        IndianDistrict("mh_sol", "Solapur (Pandharpur)", "సోలాపూర్ (పంఢరీపురం విఠలేశ్వర)", "Maharashtra", "మహారాష్ట్ర", 17.6599, 75.9064),
        IndianDistrict("mh_kol", "Kolhapur", "కొల్హాపూర్ (మహాలక్ష్మి ఆలయం)", "Maharashtra", "మహారాష్ట్ర", 16.7050, 74.2433),
        IndianDistrict("mh_nan", "Nanded", "నాందేడ్", "Maharashtra", "మహారాష్ట్ర", 19.1383, 77.3210),
        IndianDistrict("mh_amr", "Amravati", "అమరావతి (మహారాష్ట్ర)", "Maharashtra", "మహారాష్ట్ర", 20.9374, 77.7796),
        IndianDistrict("mh_jal", "Jalgaon", "జల్గావ్", "Maharashtra", "మహారాష్ట్ర", 21.0077, 75.5626),
        IndianDistrict("mh_sat", "Satara", "సతారా", "Maharashtra", "మహారాష్ట్ర", 17.6805, 74.0183),

        // ==================== GUJARAT ====================
        IndianDistrict("gj_amd", "Ahmedabad", "అహ్మదాబాద్", "Gujarat", "గుజరాత్", 23.0225, 72.5714),
        IndianDistrict("gj_sur", "Surat", "సూరత్", "Gujarat", "గుజరాత్", 21.1702, 72.8311),
        IndianDistrict("gj_vad", "Vadodara (Baroda)", "వడోదర (బరోడా)", "Gujarat", "గుజరాత్", 22.3072, 73.1812),
        IndianDistrict("gj_raj", "Rajkot", "రాజ్‌కోట్", "Gujarat", "గుజరాత్", 22.3039, 70.8022),
        IndianDistrict("gj_bha", "Bhavnagar", "భావ్‌నగర్", "Gujarat", "గుజరాత్", 21.7645, 72.1519),
        IndianDistrict("gj_gan", "Gandhinagar", "గాంధీనగర్ (అక్షరధామ్)", "Gujarat", "గుజరాత్", 23.2156, 72.6369),
        IndianDistrict("gj_jam", "Jamnagar", "జామ్‌నగర్", "Gujarat", "గుజరాత్", 22.4707, 70.0577),
        IndianDistrict("gj_jun", "Junagadh (Gir)", "జునాగఢ్", "Gujarat", "గుజరాత్", 21.5222, 70.4579),
        IndianDistrict("gj_som", "Somnath (Gir Somnath)", "సోమనాథ్ (జ్యోతిర్లింగం)", "Gujarat", "గుజరాత్", 20.9056, 70.4012),
        IndianDistrict("gj_dwa", "Dwarka (Devbhumi Dwarka)", "ద్వారక (శ్రీకృష్ణ మందిరం)", "Gujarat", "గుజరాత్", 22.2442, 68.9685),
        IndianDistrict("gj_kch", "Kutch (Bhuj)", "కచ్ (భుజ్)", "Gujarat", "గుజరాత్", 23.2420, 69.6669),

        // ==================== KERALA ====================
        IndianDistrict("kl_tvm", "Thiruvananthapuram (Padmanabhaswamy)", "తిరువనంతపురం (పద్మనాభస్వామి)", "Kerala", "కేరళ", 8.5241, 76.9366),
        IndianDistrict("kl_cok", "Kochi (Ernakulam)", "కొచ్చి (ఎర్నాకులం)", "Kerala", "కేరళ", 9.9312, 76.2673),
        IndianDistrict("kl_koz", "Kozhikode (Calicut)", "కోజికోడ్ (కాలికట్)", "Kerala", "కేరళ", 11.2588, 75.7804),
        IndianDistrict("kl_thr", "Thrissur (Guruvayur)", "త్రిస్సూర్ (గురువాయూర్)", "Kerala", "కేరళ", 10.5276, 76.2144),
        IndianDistrict("kl_kol", "Kollam", "కొల్లం", "Kerala", "కేరళ", 8.8932, 76.6141),
        IndianDistrict("kl_pal", "Palakkad", "పాలక్కాడ్", "Kerala", "కేరళ", 10.7867, 76.6548),
        IndianDistrict("kl_kan", "Kannur", "కన్నూర్", "Kerala", "కేరళ", 11.8745, 75.3704),
        IndianDistrict("kl_kot", "Kottayam", "కొట్టాయం", "Kerala", "కేరళ", 9.5916, 76.5222),
        IndianDistrict("kl_ala", "Alappuzha (Alleppey)", "అలప్పుళ (అలెప్పి)", "Kerala", "కేరళ", 9.4981, 76.3388),
        IndianDistrict("kl_pat", "Pathanamthitta (Sabarimala)", "పతనంతిట్ట (శబరిమల అయ్యప్ప స్వామి)", "Kerala", "కేరళ", 9.2648, 76.7870),

        // ==================== ODISHA ====================
        IndianDistrict("od_bbi", "Bhubaneswar (Lingaraj)", "భువనేశ్వర్ (లింగరాజ మందిరం)", "Odisha", "ఒడిశా", 20.2961, 85.8245),
        IndianDistrict("od_cut", "Cuttack", "కటక్", "Odisha", "ఒడిశా", 20.4625, 85.8828),
        IndianDistrict("od_pur", "Puri (Jagannath Dham)", "పూరీ (శ్రీ జగన్నాథ ధామం)", "Odisha", "ఒడిశా", 19.8135, 85.8312),
        IndianDistrict("od_ber", "Berhampur (Ganjam)", "బరంపురం (గంజాం)", "Odisha", "ఒడిశా", 19.3150, 84.7941),
        IndianDistrict("od_rou", "Rourkela (Sundargarh)", "రూర్కెలా", "Odisha", "ఒడిశా", 22.2604, 84.8536),
        IndianDistrict("od_sam", "Sambalpur", "సంబల్‌పూర్ (సమలేశ్వరి)", "Odisha", "ఒడిశా", 21.4669, 83.9812),
        IndianDistrict("od_kor", "Koraput", "కోరాపుట్", "Odisha", "ఒడిశా", 18.8135, 82.7123),

        // ==================== DELHI NCR ====================
        IndianDistrict("dl_del", "New Delhi", "న్యూఢిల్లీ", "Delhi", "ఢిల్లీ", 28.6139, 77.2090),
        IndianDistrict("dl_noi", "Noida (Gautam Buddha Nagar)", "నోయిడా", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 28.5355, 77.3910),
        IndianDistrict("dl_gur", "Gurugram (Gurgaon)", "గురుగ్రామ్ (గుర్గావ్)", "Haryana", "హర్యానా", 28.4595, 77.0266),
        IndianDistrict("dl_far", "Faridabad", "ఫరీదాబాద్", "Haryana", "హర్యానా", 28.4089, 77.3178),
        IndianDistrict("dl_gha", "Ghaziabad", "ఘజియాబాద్", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 28.6692, 77.4538),

        // ==================== UTTAR PRADESH ====================
        IndianDistrict("up_vns", "Varanasi (Kashi Vishwanath)", "వారణాసి (కాశీ విశ్వనాథ్)", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 25.3176, 82.9739),
        IndianDistrict("up_luc", "Lucknow", "లక్నో", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 26.8467, 80.9462),
        IndianDistrict("up_pra", "Prayagraj (Triveni Sangam)", "ప్రయాగ్‌రాజ్ (అలహాబాద్ - త్రివేణి సంగమం)", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 25.4358, 81.8463),
        IndianDistrict("up_kan", "Kanpur", "కాన్పూర్", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 26.4499, 80.3319),
        IndianDistrict("up_agr", "Agra", "ఆగ్రా", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 27.1767, 78.0081),
        IndianDistrict("up_ayo", "Ayodhya", "అయోధ్య (శ్రీరామ జన్మభూమి)", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 26.7922, 82.1998),
        IndianDistrict("up_mat", "Mathura (Vrindavan)", "మథుర (బృందావనం)", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 27.4924, 77.6737),
        IndianDistrict("up_mee", "Meerut", "మీరట్", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 28.9845, 77.7064),
        IndianDistrict("up_gor", "Gorakhpur (Gorakhnath)", "గోరఖ్‌పూర్ (గోరఖ్‌నాథ్)", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 26.7606, 83.3732),
        IndianDistrict("up_bar", "Bareilly", "బరేలీ", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 28.3670, 79.4304),
        IndianDistrict("up_ali", "Aligarh", "అలీఘర్", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 27.8974, 78.0880),
        IndianDistrict("up_jha", "Jhansi", "ఝాన్సీ", "Uttar Pradesh", "ఉత్తర ప్రదేశ్", 25.4484, 78.5685),

        // ==================== MADHYA PRADESH ====================
        IndianDistrict("mp_ujn", "Ujjain (Mahakaleshwar)", "ఉజ్జయిని (మహాకాళేశ్వర్ జ్యోతిర్లింగం)", "Madhya Pradesh", "మధ్య ప్రదేశ్", 23.1765, 75.7885),
        IndianDistrict("mp_ind", "Indore", "ఇండోర్", "Madhya Pradesh", "మధ్య ప్రదేశ్", 22.7196, 75.8577),
        IndianDistrict("mp_bho", "Bhopal", "భోపాల్", "Madhya Pradesh", "మధ్య ప్రదేశ్", 23.2599, 77.4126),
        IndianDistrict("mp_gwa", "Gwalior", "గ్వాలియర్", "Madhya Pradesh", "మధ్య ప్రదేశ్", 26.2183, 78.1828),
        IndianDistrict("mp_jab", "Jabalpur", "జబల్‌పూర్ (భేడాఘాట్ నర్మదా)", "Madhya Pradesh", "మధ్య ప్రదేశ్", 23.1815, 79.9864),
        IndianDistrict("mp_omk", "Omkareshwar (Khandwa)", "ఓంకారేశ్వర్ (జ్యోతిర్లింగం)", "Madhya Pradesh", "మధ్య ప్రదేశ్", 22.2458, 76.1517),

        // ==================== WEST BENGAL ====================
        IndianDistrict("wb_kol", "Kolkata (Kalighat & Dakshineswar)", "కోల్‌కతా (కాళీఘాట్ / దక్షిణేశ్వరం)", "West Bengal", "పశ్చిమ బెంగాల్", 22.5726, 88.3639),
        IndianDistrict("wb_how", "Howrah", "హౌరా (బేలూర్ మఠం)", "West Bengal", "పశ్చిమ బెంగాల్", 22.5958, 88.2636),
        IndianDistrict("wb_sil", "Siliguri (Darjeeling)", "సిలిగురి (డార్జిలింగ్)", "West Bengal", "పశ్చిమ బెంగాల్", 26.7271, 88.3953),
        IndianDistrict("wb_asn", "Asansol", "అసన్సోల్", "West Bengal", "పశ్చిమ బెంగాల్", 23.6739, 86.9524),
        IndianDistrict("wb_nad", "Nadia (Mayapur ISKCON)", "నదియా (మాయాపూర్ శ్రీ చైతన్య ధామం)", "West Bengal", "పశ్చిమ బెంగాల్", 23.4233, 88.3934),

        // ==================== RAJASTHAN ====================
        IndianDistrict("rj_jai", "Jaipur", "జైపూర్ (పింక్ సిటీ)", "Rajasthan", "రాజస్థాన్", 26.9124, 75.7873),
        IndianDistrict("rj_jod", "Jodhpur", "జోధ్‌పూర్", "Rajasthan", "రాజస్థాన్", 26.2389, 73.0243),
        IndianDistrict("rj_uda", "Udaipur", "ఉదయ్‌పూర్ (సరస్సుల నగరం)", "Rajasthan", "రాజస్థాన్", 24.5854, 73.7125),
        IndianDistrict("rj_kot", "Kota", "కోటా", "Rajasthan", "రాజస్థాన్", 25.2138, 75.8648),
        IndianDistrict("rj_ajm", "Ajmer (Pushkar Brahma Temple)", "అజ్మీర్ (పుష్కర్ బ్రహ్మదేవ మందిరం)", "Rajasthan", "రాజస్థాన్", 26.4499, 74.6399),
        IndianDistrict("rj_bik", "Bikaner", "బికనీర్", "Rajasthan", "రాజస్థాన్", 28.0229, 73.3119),
        IndianDistrict("rj_nat", "Nathdwara (Rajsamand)", "నాథద్వారా (శ్రీనాథ్ జీ మందిరం)", "Rajasthan", "రాజస్థాన్", 24.9317, 73.8188),

        // ==================== BIHAR ====================
        IndianDistrict("br_pat", "Patna", "పాట్నా (పాటలీపుత్ర)", "Bihar", "బీహార్", 25.5941, 85.1376),
        IndianDistrict("br_gay", "Gaya (Bodh Gaya & Vishnu Pad)", "గయ (బోధగయ / విష్ణుపాద మందిరం)", "Bihar", "బీహార్", 24.7914, 85.0002),
        IndianDistrict("br_muz", "Muzaffarpur", "ముజఫర్‌పూర్", "Bihar", "బీహార్", 26.1209, 85.3647),
        IndianDistrict("br_bha", "Bhagalpur", "భాగల్‌పూర్", "Bihar", "బీహార్", 25.2425, 86.9842),

        // ==================== PUNJAB & HARYANA ====================
        IndianDistrict("pb_amr", "Amritsar (Golden Temple)", "అమృత్‌సర్ (స్వర్ణ దేవాలయం)", "Punjab", "పంజాబ్", 31.6340, 74.8723),
        IndianDistrict("pb_lud", "Ludhiana", "లూధియానా", "Punjab", "పంజాబ్", 30.9010, 75.8573),
        IndianDistrict("pb_jal", "Jalandhar", "జలంధర్", "Punjab", "పంజాబ్", 31.3260, 75.5762),
        IndianDistrict("ch_chd", "Chandigarh", "చండీగఢ్", "Chandigarh", "చండీగఢ్", 30.7333, 76.7794),
        IndianDistrict("hr_pan", "Panipat", "పానిపట్", "Haryana", "హర్యానా", 29.3909, 76.9635),
        IndianDistrict("hr_amb", "Ambala", "అంబాలా", "Haryana", "హర్యానా", 30.3782, 76.7767),
        IndianDistrict("hr_kur", "Kurukshetra (Jyotisar)", "కురుక్షేత్ర (జ్యోతిసర్ గీతోపదేశ స్థలి)", "Haryana", "హర్యానా", 29.9695, 76.8783),

        // ==================== UTTARAKHAND & HIMACHAL ====================
        IndianDistrict("uk_har", "Haridwar (Ganga Aarti)", "హరిద్వార్ (గంగా హారతి)", "Uttarakhand", "ఉత్తరాఖండ్", 29.9457, 78.1642),
        IndianDistrict("uk_ris", "Rishikesh", "రిషికేశ్", "Uttarakhand", "ఉత్తరాఖండ్", 30.0869, 78.2676),
        IndianDistrict("uk_deh", "Dehradun", "డెహ్రాడూన్", "Uttarakhand", "ఉత్తరాఖండ్", 30.3165, 78.0322),
        IndianDistrict("uk_ked", "Rudraprayag (Kedarnath Dham)", "రుద్రప్రయాగ్ (కేదార్‌నాథ్ ధామం)", "Uttarakhand", "ఉత్తరాఖండ్", 30.2844, 78.9811),
        IndianDistrict("uk_bad", "Chamoli (Badrinath Dham)", "చమోలీ (బద్రీనాథ్ ధామం)", "Uttarakhand", "ఉత్తరాఖండ్", 30.7433, 79.4938),
        IndianDistrict("hp_shi", "Shimla", "సిమ్లా", "Himachal Pradesh", "హిమాచల్ ప్రదేశ్", 31.1048, 77.1734),
        IndianDistrict("hp_dha", "Dharamshala (Kangra)", "ధర్మశాల (కాంగ్రా)", "Himachal Pradesh", "హిమాచల్ ప్రదేశ్", 32.2190, 76.3234),
        IndianDistrict("hp_kul", "Kullu - Manali", "కులు - మనాలి", "Himachal Pradesh", "హిమాచల్ ప్రదేశ్", 31.9579, 77.1095),

        // ==================== JAMMU & KASHMIR & LADAKH ====================
        IndianDistrict("jk_sri", "Srinagar (Shankaracharya Temple)", "శ్రీనగర్ (శంకరాచార్య కొండ)", "Jammu and Kashmir", "జమ్మూ & కాశ్మీర్", 34.0837, 74.7973),
        IndianDistrict("jk_jam", "Jammu", "జమ్ము", "Jammu and Kashmir", "జమ్మూ & కాశ్మీర్", 32.7266, 74.8570),
        IndianDistrict("jk_kat", "Katra (Vaishno Devi Dham)", "కత్రా (శ్రీ వైష్ణోదేవి ధామం)", "Jammu and Kashmir", "జమ్మూ & కాశ్మీర్", 32.9934, 74.9317),
        IndianDistrict("la_leh", "Leh (Ladakh)", "లేహ్ (లడఖ్)", "Ladakh", "లడఖ్", 34.1526, 77.5771),

        // ==================== JHARKHAND & CHHATTISGARH ====================
        IndianDistrict("jh_ran", "Ranchi", "రాంచీ", "Jharkhand", "జార్ఖండ్", 23.3441, 85.3096),
        IndianDistrict("jh_jam", "Jamshedpur (East Singhbhum)", "జంషెడ్‌పూర్", "Jharkhand", "జార్ఖండ్", 22.8046, 86.2029),
        IndianDistrict("jh_deo", "Deoghar (Baidyanath Dham)", "దేవ్‌ఘర్ (బైద్యనాథ్ జ్యోతిర్లింగం)", "Jharkhand", "జార్ఖండ్", 24.4826, 86.6997),
        IndianDistrict("cg_rai", "Raipur", "రాయ్‌పూర్", "Chhattisgarh", "ఛత్తీస్‌గఢ్", 21.2514, 81.6296),
        IndianDistrict("cg_bil", "Bilaspur", "బిలాస్‌పూర్", "Chhattisgarh", "ఛత్తీస్‌గఢ్", 22.0797, 82.1409),

        // ==================== ASSAM & NORTHEAST ====================
        IndianDistrict("as_guw", "Guwahati (Kamakhya Temple)", "గువాహటి (శ్రీ కామాఖ్య దేవి ఆలయం)", "Assam", "అస్సాం", 26.1445, 91.7362),
        IndianDistrict("tr_aga", "Agartala (Tripura Sundari)", "అగర్తల (త్రిపుర సుందరి శక్తిపీఠం)", "Tripura", "త్రిపుర", 23.8315, 91.2868),
        IndianDistrict("sk_gan", "Gangtok", "గ్యాంగ్‌టక్", "Sikkim", "సిక్కిం", 27.3389, 88.6065),
        IndianDistrict("ml_shi", "Shillong", "షిల్లాంగ్", "Meghalaya", "మేఘాలయ", 25.5788, 91.8933),

        // ==================== GOA & UTs ====================
        IndianDistrict("ga_pan", "Panaji (Goa)", "పనాజీ (గోవా)", "Goa", "గోవా", 15.4909, 73.8278),
        IndianDistrict("py_pud", "Puducherry (Auroville)", "పుదుచ్చేరి (పాండిచ్చేరి)", "Puducherry", "పుదుచ్చేరి", 11.9416, 79.8083),
        IndianDistrict("an_por", "Port Blair (Andaman)", "పోర్ట్ బ్లెయిర్ (అండమాన్)", "Andaman and Nicobar", "అండమాన్ & నికోబార్", 11.6234, 92.7265),

        // ==================== GLOBAL NRI HUBS ====================
        IndianDistrict("glob_nyc", "New York", "న్యూయార్క్", "USA", "అమెరికా", 40.7128, -74.0060, "America/New_York"),
        IndianDistrict("glob_sfo", "San Jose / Bay Area", "శాన్ జోస్ / బే ఏరియా", "USA", "అమెరికా", 37.3382, -121.8863, "America/Los_Angeles"),
        IndianDistrict("glob_dal", "Dallas / Texas", "డల్లాస్ (టెక్సాస్)", "USA", "అమెరికా", 32.7767, -96.7970, "America/Chicago"),
        IndianDistrict("glob_lon", "London", "లండన్", "United Kingdom", "బ్రిటన్", 51.5074, -0.1278, "Europe/London"),
        IndianDistrict("glob_dxb", "Dubai", "దుబాయ్", "United Arab Emirates", "దుబాయ్ / యుఎఇ", 25.2048, 55.2708, "Asia/Dubai"),
        IndianDistrict("glob_sin", "Singapore", "సింగపూర్", "Singapore", "సింగపూర్", 1.3521, 103.8198, "Asia/Singapore"),
        IndianDistrict("glob_syd", "Sydney", "సిడ్నీ", "Australia", "ఆస్ట్రేలియా", -33.8688, 151.2093, "Australia/Sydney"),
        IndianDistrict("glob_tor", "Toronto", "టొరంటో", "Canada", "కెనడా", 43.6532, -79.3832, "America/Toronto")
    )

    fun findDistrictById(id: String): IndianDistrict {
        return ALL_DISTRICTS.find { it.id.equals(id, ignoreCase = true) } ?: ALL_DISTRICTS.first()
    }

    fun getAllStates(lang: AppLanguage = AppLanguage.TE): List<Pair<String, String>> {
        val statesMap = linkedMapOf<String, String>()
        ALL_DISTRICTS.forEach { district ->
            if (!statesMap.containsKey(district.stateEn)) {
                val displayName = if (lang == AppLanguage.TE) district.stateTe else district.stateEn
                statesMap[district.stateEn] = displayName
            }
        }
        return statesMap.map { Pair(it.key, it.value) }
    }

    fun searchDistricts(query: String): List<IndianDistrict> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return ALL_DISTRICTS
        return ALL_DISTRICTS.filter { district ->
            district.nameEn.lowercase().contains(q) ||
                    district.nameTe.contains(q) ||
                    district.stateEn.lowercase().contains(q) ||
                    district.stateTe.contains(q)
        }
    }

    fun filterDistricts(selectedStateEn: String?, query: String): List<IndianDistrict> {
        val q = query.trim().lowercase()
        return ALL_DISTRICTS.filter { district ->
            val matchesState = selectedStateEn.isNullOrEmpty() || district.stateEn.equals(selectedStateEn, ignoreCase = true)
            val matchesQuery = q.isEmpty() ||
                    district.nameEn.lowercase().contains(q) ||
                    district.nameTe.contains(q) ||
                    district.stateEn.lowercase().contains(q) ||
                    district.stateTe.contains(q)
            matchesState && matchesQuery
        }
    }

    /**
     * Finds the nearest IndianDistrict based on latitude and longitude coordinates.
     */
    fun findNearestDistrict(lat: Double, lng: Double): IndianDistrict {
        return ALL_DISTRICTS.minByOrNull { dist ->
            val latDiff = Math.toRadians(dist.latitude - lat)
            val lngDiff = Math.toRadians(dist.longitude - lng)
            val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(dist.latitude)) *
                    Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            6371.0 * c // Earth radius in km
        } ?: ALL_DISTRICTS.first()
    }
}
