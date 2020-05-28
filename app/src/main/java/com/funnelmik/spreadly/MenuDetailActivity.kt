package com.funnelmik.spreadly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MenuDetailActivity : AppCompatActivity() {

    private var itemImageView: ImageView? = null
    private var itemTitleView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_detail)

        itemImageView = findViewById(R.id.menuDetailImageView)
        itemTitleView = findViewById(R.id.menuDetailTextView)


    }

    private fun loadUI() {

    }
}
