package com.example.majika.model

import com.squareup.moshi.Json

data class MenuResponse (
    @Json(name = "data") val data: List<MenuItem>
)