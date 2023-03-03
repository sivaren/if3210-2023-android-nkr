package com.example.majika.model

data class SectionHeader (
    val name: String
)

object Datasource {
    fun getFoodTitle() = SectionHeader("Makanan")
    fun getDrinkTitle() = SectionHeader("Minuman")
}