package com.funnelmik.spreadly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log



@Suppress("UNCHECKED_CAST")
class MenuActivity : AppCompatActivity() {

    var menu: MutableMap<String, Array<MenuItem>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        val menuId = intent.getStringExtra(URL).replace("spreadly://?", "", true)

        readFirebase(menuId)
    }

    private fun readFirebase(menuId: String) {
        DB.collection("clients").document(menuId).collection("menu").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.id
                    val data = document.data
                    val type = data["type"] as String

                    val menuItem = MenuItem(
                        name,
                        data["type"] as String,
                        data["price"] as String,
                        if (data.containsKey("description")) data["description"] as String else "",
                        if (data.containsKey("ingredients")) data["ingredients"] as List<String> else listOf<String>(),
                        if (data.containsKey("sides")) data["sides"] as List<String> else listOf<String>(),
                        if (data.containsKey("pescatarian")) data["pescatarian"] as Boolean else false,
                        if (data.containsKey("gf")) data["gf"] as Boolean else false,
                        if (data.containsKey("vegan")) data["vegan"] as Boolean else false,
                        if (data.containsKey("vegetarian")) data["vegetarian"] as Boolean else false,
                        if (data.containsKey("image")) data["image"] as String else ""
                    )

                    if (menu.containsKey(type)) {
                        var section = menu[type] as Array<MenuItem>
                        section += menuItem
                        menu[type] = section
                    } else {
                        menu[type] = arrayOf<MenuItem>(menuItem)
                    }

                    // Log.d("DATA", menuItem.toString())
                }
            }
            .addOnFailureListener { exception ->
                // Error Getting documents from Firebase
                Log.w("ERROR", "Error getting documents: $exception")
            }

    }
}
