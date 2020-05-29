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
import java.lang.Exception
import java.util.concurrent.CountDownLatch

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



        readFirebaseFirestore(menuId, listView!!) { count, listView ->
            readFirebaseStorage(count, listView)
        }
    }


    private fun updateUI(listView: ExpandableListView) {
        val menuHeaders = menu.keys.toList()
        listAdapter = ExpandableListAdapter(this, menu, menuHeaders)
        listView.setAdapter(listAdapter)

        listView.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
            val intent: Intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra(NAME, menu[menuHeaders[groupPosition]]?.get(childPosition)?.name)
                // TODO: Find way to send image to next activity -- probably just send URL
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

    private fun readFirebaseStorage(imageCount: Int, listView: ExpandableListView) {
        val latch: CountDownLatch = CountDownLatch(imageCount)
        Log.i("CHECK***", "1")
        for ((type: String, itemList: List<MenuItem>) in menu) {
            for (item: MenuItem in itemList) {
                if (item.imageURL != EMPTY_STRING) {
                    val imageRef = storageRef.getReferenceFromUrl(item.imageURL)
                    Log.i("CHECK***", imageRef.path)
                    Log.i("CHECK", imageRef.toString())
                    val test = storageRef.reference.child(imageRef.path)
                    test.getBytes(TWO_MB).addOnSuccessListener { data: ByteArray ->
                        Log.i("CHECK***", "2")
                        item.image = data
                        latch.countDown()
                    }.addOnFailureListener { exception: Exception ->
                        Log.i("CHECK***", "3")
                        Log.e("FIREBASE_STORAGE_ERROR", exception.toString())
                        latch.countDown()
                    }.addOnCompleteListener {
                        Log.i("CHECK***", "test")
                    }.addOnCanceledListener {
                        Log.i("CHECK", "DID we get cancelled?")
                    }
                }
            }
        }
        latch.await()
        Log.i("CHECK***", "4")
        updateUI(listView)
    }



    private fun readFirebaseFirestore(menuId: String, listView: ExpandableListView, callback: (Int, ExpandableListView) -> Unit) {
        db.collection("clients").document(menuId).collection("menu").get()
            .addOnSuccessListener { documents ->
                var imageCount: Int = 0
                for (document in documents) {
                    val name = document.id
                    val data = document.data
                    val type = data[TYPE] as String
                    val imageURL = if (data.containsKey(IMAGE)) data[IMAGE] as String else EMPTY_STRING

                    if (imageURL != EMPTY_STRING) {
                        imageCount+=1
                    }

                    val menuItem = MenuItem(
                        name = name,
                        type = type,
                        price = data[PRICE] as String,
                        description = if (data.containsKey(DESCRIPTION)) data[DESCRIPTION] as String else "",
                        ingredients = if (data.containsKey(INGREDIENTS)) data[INGREDIENTS] as List<String> else listOf<String>(),
                        sides = if (data.containsKey(SIDES)) data[SIDES] as List<String> else listOf<String>(),
                        pescatarian = if (data.containsKey(PESCATARIAN)) data[PESCATARIAN] as Boolean else false,
                        gf = if (data.containsKey(GF)) data [GF] as Boolean else false,
                        vegan = if (data.containsKey(VEGAN)) data[VEGAN] as Boolean else false,
                        vegetarian = if (data.containsKey(VEGETARIAN)) data[VEGETARIAN] as Boolean else false,
                        imageURL = imageURL
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
                // TODO: Insert Read Images here
                Log.i("CHECK", "End of Firestore Read")
                callback(imageCount, listView)
                //updateUI(listView)
            }
            .addOnFailureListener { exception ->
                // Error Getting documents from Firebase
                Log.w("ERROR", "Error getting documents: $exception")
            }

    }
}
