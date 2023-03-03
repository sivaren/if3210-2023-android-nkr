package com.example.majika.model

import com.squareup.moshi.Json

data class BranchResponse (
    @Json(name = "data")
    val data: List<BranchItem>
)
