package com.example.majika.model

import com.squareup.moshi.Json

data class PaymentResponse (
    @Json(name = "status") val status: String
)