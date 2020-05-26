package com.funnelmik.spreadly

data class MenuItem(
    val name: String,
    val type: String,
    val price: String,
    val description: String = "",
    val ingredients: List<String> = listOf<String>(),
    val sides: List<String> = listOf<String>(),
    val pescatarian: Boolean = false,
    val gf: Boolean = false,
    val vegan: Boolean = false,
    val vegetarian: Boolean = false,
    val imageURL: String = ""
)