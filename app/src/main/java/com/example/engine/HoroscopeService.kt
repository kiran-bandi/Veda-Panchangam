package com.example.engine

import com.example.model.AppLanguage
import com.example.model.Rashi
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDate
import java.util.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HoroscopeService {

    private val client = OkHttpClient()

    suspend fun fetchDailyHoroscope(
        rashi: Rashi,
        date: LocalDate,
        language: AppLanguage
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "Generate a highly accurate daily Vedic astrology horoscope guidance for the zodiac sign ${rashi.englishName} on ${date} in ${language.displayName}. Keep it inspiring, concise, and focused on career, wellness, and relationships. Keep the response limited to 2-3 sentences. Do not use markdown. Translate to ${language.displayName}."
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                
                val jsonPayload = JSONObject().apply {
                    put("contents", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", org.json.JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            })
                        })
                    })
                }

                val body = jsonPayload.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (!responseBody.isNullOrBlank()) {
                            val json = JSONObject(responseBody)
                            val text = json.getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text")
                            if (text.isNotBlank()) {
                                return@withContext text.trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Fall back gracefully on network or API failures
            }
        }
        
        // Fallback to offline dynamic guidance generator
        return@withContext generateLocalHoroscope(rashi, date, language)
    }

    private fun generateLocalHoroscope(rashi: Rashi, date: LocalDate, language: AppLanguage): String {
        val seed = date.toEpochDay() + rashi.ordinal * 31
        val random = Random(seed)

        val enTransits = listOf(
            "The planetary lord ${rashi.planet} rules your day with strong protective vibes, enhancing",
            "A cosmic connection between the Moon and Jupiter opens up new pathways for",
            "As the celestial energy shifts, you will find special inspiration and clarity in",
            "Vedic planetary alignments encourage a balanced and deeply thoughtful approach to",
            "A wave of cosmic radiance boosts your inner strength today, paving the way for",
            "Planetary positions today favor focus and mental equilibrium, which directly supports"
        )

        val enCareers = listOf(
            " career progress and long-term financial stability. Seek advice from mentors.",
            " professional breakthroughs and creative solutions to pending projects.",
            " sudden gains and constructive discussions with business colleagues.",
            " academic and technical pursuits. Dedication will yield beautiful results.",
            " identifying lucrative opportunities that align with your spiritual purpose."
        )

        val enPersonal = listOf(
            " Spend time in peaceful meditation and keep an eye on nutrition.",
            " Radiate kindness in family relationships to dissolve minor misunderstandings.",
            " An evening walk or listening to spiritual mantras will completely rejuvenate your spirit.",
            " Trust your intuition; it is guided by the stars. Be mindful of your words today.",
            " Wear a touch of your lucky color or perform a small act of charity for peace."
        )

        val teTransits = listOf(
            "ఈ రోజు మీ రాశ్యాధిపతి అయిన ${rashi.teluguName} గ్రహ ప్రభావం వల్ల అద్భుతమైన శక్తి లభిస్తుంది, ఇది",
            "చంద్ర మరియు గురు గ్రహాల అనుకూల వీక్షణం కారణంగా నూతన మార్గాలు సుగమం అవుతాయి, ఇది",
            "నక్షత్ర గమన మార్పుల వల్ల మీలో కొత్త ఉత్సాహం మరియు సృజనాత్మకత పెంపొందుతాయి, ఇది",
            "గ్రహాల సానుకూల స్థానాల వల్ల మానసిక ప్రశాంతత మరియు ఏకాగ్రత లభిస్తాయి, ఇది",
            "దివ్య నక్షత్రాల అనుకూలత మీలో ఆత్మవిశ్వాసాన్ని మరియు ధైర్యాన్ని పెంచుతుంది, ఇది"
        )

        val teCareers = listOf(
            " ఉద్యోగ మరియు వ్యాపార వ్యవహారాలలో మంచి పురోగతికి దారితీస్తుంది. పెద్దల సలహాలు మేలు చేస్తాయి.",
            " చాలా కాలంగా పెండింగ్‌లో ఉన్న పనులు విజయవంతంగా పూర్తి కావడానికి దోహదపడుతుంది.",
            " ఆర్థిక లాభాలను మరియు సహోద్యోగులతో సత్संबंधాలను మెరుగుపరుస్తుంది.",
            " విద్యా రంగంలో ఉన్న వారికి అద్భుతమైన ఫలితాలను మరియు కీర్తిని చేకూరుస్తుంది.",
            " నూతన పెట్టుబడులు మరియు వ్యాపార విస్తరణకు చక్కటి అవకాశాలను కలిగిస్తుంది."
        )

        val tePersonal = listOf(
            " సాయంత్రం ధ్యానం లేదా దైవ ప్రార్థన చేయడం వల్ల మరింత మనశ్శాంతి లభిస్తుంది.",
            " కుటుంబ సభ్యులతో మృదువుగా మాట్లాడటం వల్ల బంధాలు మరింత బలపడతాయి.",
            " ఆరోగ్యంపై ప్రత్యేక శ్రద్ధ వహించండి మరియు మిత ఆహారాన్ని తీసుకోండి.",
            " మీ అంతఃచేతనను నమ్మండి, అది మీకు సరైన దిశను చూపుతుంది.",
            " ఈ రోజు పేదలకు సహాయం చేయడం వల్ల మీ నూతన పనులు నిర్విఘ్నంగా సాగుతాయి."
        )

        if (language == AppLanguage.TE) {
            val transit = teTransits[random.nextInt(teTransits.size)]
            val career = teCareers[random.nextInt(teCareers.size)]
            val personal = tePersonal[random.nextInt(tePersonal.size)]
            return "$transit$career$personal"
        } else {
            val transit = enTransits[random.nextInt(enTransits.size)]
            val career = enCareers[random.nextInt(enCareers.size)]
            val personal = enPersonal[random.nextInt(enPersonal.size)]
            return "$transit$career$personal"
        }
    }
}
