package com.funnelmik.spreadly

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
const val URL = "url"
const val IMAGE = "image"
const val EMPTY_STRING = ""

const val TWO_MB: Long = 1024 * 1024 * 2


val db = Firebase.firestore
val storageRef = Firebase.storage


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
) {
    var image: ByteArray? = null
}