package com.example.marlon.galleryapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Creates a new fragment
        val galleryFragment = GalleryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container_layout, galleryFragment).commit()
    }
}
