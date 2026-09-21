package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemotePanchangResponse(
    @Json(name = "status") val status: String? = "success",
    @Json(name = "date") val date: String? = null,
    @Json(name = "location") val locationName: String? = null,
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null,
    @Json(name = "tithi") val tithi: RemoteTithiData? = null,
    @Json(name = "nakshatra") val nakshatra: RemoteNakshatraData? = null,
    @Json(name = "yoga") val yoga: RemoteYogaData? = null,
    @Json(name = "karana") val karana: RemoteKaranaData? = null,
    @Json(name = "masa") val masa: String? = null,
    @Json(name = "paksha") val paksha: String? = null,
    @Json(name = "samvatsara") val samvatsara: String? = null,
    @Json(name = "sun_times") val sunTimes: RemoteSunTimes? = null,
    @Json(name = "source") val source: String? = "drikpanchang.com"
)

@JsonClass(generateAdapter = true)
data class RemoteTithiData(
    @Json(name = "number") val number: Int? = 1,
    @Json(name = "name") val name: String? = "",
    @Json(name = "paksha") val paksha: String? = "",
    @Json(name = "end_time") val endTime: String? = "",
    @Json(name = "deity") val deity: String? = ""
)

@JsonClass(generateAdapter = true)
data class RemoteNakshatraData(
    @Json(name = "number") val number: Int? = 1,
    @Json(name = "name") val name: String? = "",
    @Json(name = "pada") val pada: Int? = 1,
    @Json(name = "rashi") val rashi: String? = "",
    @Json(name = "ruler") val ruler: String? = "",
    @Json(name = "end_time") val endTime: String? = ""
)

@JsonClass(generateAdapter = true)
data class RemoteYogaData(
    @Json(name = "number") val number: Int? = 1,
    @Json(name = "name") val name: String? = "",
    @Json(name = "end_time") val endTime: String? = ""
)

@JsonClass(generateAdapter = true)
data class RemoteKaranaData(
    @Json(name = "number") val number: Int? = 1,
    @Json(name = "name") val name: String? = "",
    @Json(name = "end_time") val endTime: String? = ""
)

@JsonClass(generateAdapter = true)
data class RemoteSunTimes(
    @Json(name = "sunrise") val sunrise: String? = "",
    @Json(name = "sunset") val sunset: String? = "",
    @Json(name = "moonrise") val moonrise: String? = "",
    @Json(name = "moonset") val moonset: String? = ""
)
