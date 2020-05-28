package com.funnelmik.spreadly

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair as UtilPair
import android.os.Build
import kotlinx.android.synthetic.main.menu_group.*

val TITLE_TRANSITION = "Menu:Detail:Transition:Title"
val IMAGE_TRANSITION = "Menu:Detail:Transition:Image"


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
        val menuHeaders = menu.keys.toList()
        listAdapter = ExpandableListAdapter(this, menu, menuHeaders)
        listView.setAdapter(listAdapter)

        listView.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
            val intent: Intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra(NAME, menu[menuHeaders[groupPosition]]?.get(childPosition)?.name)
                putExtra()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val imageView: View = view.findViewById(R.id.menuItemImage)
                val textView: View = view.findViewById(R.id.menuItemTitle)
                val activityOptions: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        UtilPair.create(imageView, IMAGE_TRANSITION),
                        UtilPair.create(textView, TITLE_TRANSITION)
                    )

                ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
            } else {
                startActivity(intent, null)
            }

            true
        }
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
                        type,
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
