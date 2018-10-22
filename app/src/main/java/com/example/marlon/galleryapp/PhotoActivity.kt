package com.example.marlon.galleryapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class PhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        // Creates a new fragment
        val photoFragment = PhotoFragment()
        // Send data to the fragment
        photoFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().replace(R.id.container_layout, photoFragment).commit()
    }

}
