package com.funnelmik.spreadly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView


@Suppress("UNCHECKED_CAST")
class MenuActivity : AppCompatActivity() {

    private var menu: MutableMap<String, List<MenuItem>> = mutableMapOf()
    private var listView: ExpandableListView? = null
    private var listAdapter: ExpandableListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        val menuId = intent.getStringExtra(URL)?.replace("spreadly://?", "", true) ?: "Error ID"
        listView = findViewById<ExpandableListView>(R.id.MenuView)



        readFirebase(menuId, listView!!)
    }

    private fun updateUI(listView: ExpandableListView) {
        listAdapter = ExpandableListAdapter(this, menu, menu.keys.toList())
        listView.setAdapter(listAdapter)

    }

    private fun readFirebase(menuId: String, listView: ExpandableListView) {
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
                        val section = (menu[type] as List<MenuItem>).toMutableList()
                        section += menuItem
                        menu[type] = section
                    } else {
                        menu[type] = listOf<MenuItem>(menuItem)
                    }

                    // Log.d("DATA", menuItem.toString())
                }
                updateUI(listView)
            }
            .addOnFailureListener { exception ->
                // Error Getting documents from Firebase
                Log.w("ERROR", "Error getting documents: $exception")
            }

    }
}
