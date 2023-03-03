package com.example.majika.model

import com.squareup.moshi.Json

data class BranchItem (
    @Json(name = "name")
    val name: String,
    @Json(name = "popular_food")
    val popular_food: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "contact_person")
    val contact_person: String,
    @Json(name = "phone_number")
    val phone_number: String,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "latitude")
    val latitude: Double,
)