package com.example.marlon.galleryapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.gallery_fragment_layout.view.*

class PhotoFragment : Fragment(), PhotoGalleryAdapter.SelectedPhoto {
    override fun clickItem(position: Int) {
        if (fullScreen) {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }

    private var photos: ArrayList<NasaPhoto>? = null
    private var position = 0
    private var fullScreen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(KEY_POSITION)
            photos = it.getParcelableArrayList(KEY_PHOTOS)
        }
    }

    private var recyclerView: RecyclerView? = null

    private lateinit var viewManager: LinearLayoutManager

    private lateinit var viewAdapter: PhotoGalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        hideSystemUI()

        val view = inflater.inflate(R.layout.gallery_fragment_layout, container, false)
        viewManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        // Sets data to the recycler view
        viewAdapter = PhotoGalleryAdapter(photos, this, false, this)
        recyclerView = view.photo_gallery.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter
            adapter = viewAdapter
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        viewManager.scrollToPosition(position)
        return view
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        fullScreen = false
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        fullScreen = true
    }
}
