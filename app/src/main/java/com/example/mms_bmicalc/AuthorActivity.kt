package com.example.mms_bmicalc

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class AuthorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_author)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "About the Author"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}