package com.funnelmik.spreadly

const val NAME: String = "name"
const val TYPE: String = "type"
const val PRICE: String = "price"
const val DESCRIPTION: String = "description"
const val INGREDIENTS: String = "ingredients"
const val SIDES: String = "sides"
const val PESCATARIAN: String = "pescatarian"
const val GF: String = "gf"
const val VEGAN: String = "vegan"
const val VEGETARIAN: String = "vegetarian"

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