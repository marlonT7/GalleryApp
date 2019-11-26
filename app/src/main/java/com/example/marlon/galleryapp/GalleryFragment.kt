package com.example.marlon.galleryapp

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.gallery_fragment_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference


const val COLUMNS = 3
const val KEY_PHOTOS = "key photos"
const val KEY_POSITION = "key position"

class GalleryFragment : Fragment(), PhotoGalleryAdapter.SelectedPhoto {

    private lateinit var viewManager: GridLayoutManager
    private lateinit var viewAdapter: PhotoGalleryAdapter
    private var photos: Photos? = Photos(arrayListOf())
    private var service: NasaService? = null
    private var page = 0
    private var photoCall: Call<Photos?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Creates service request data
        context?.let {
            service = ApiNasa.getApi(WeakReference(it))?.create(NasaService::class.java)
            callApi() }

    }

    // Uses the service request the next data page
    fun callApi() {
        page++
        photoCall = service?.getPhotos(50, page, API_KEY)
        photoCall?.enqueue(object : Callback<Photos?> {
            override fun onResponse(call: Call<Photos?>, response: Response<Photos?>) {
                response.body()?.let {
                    photos?.photos?.addAll(it.photos)
                    viewAdapter.setData(photos?.photos) }
            }

            override fun onFailure(call: Call<Photos?>, t: Throwable) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.gallery_fragment_layout, container, false)
        viewManager =
            GridLayoutManager(context, COLUMNS)
        // Sets data to the recycler view
        viewAdapter = PhotoGalleryAdapter(photos?.photos, this, true, this)
        view.photo_gallery.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = viewManager
            // specify an viewAdapter
            adapter = viewAdapter
            // Add the pagination, loads more data when cant scroll down
            addOnScrollListener(OnScrollListener(this@GalleryFragment))
        }
        return view
    }

    override fun clickItem(position: Int, photo: ImageView) {
        // Sends data to the new activity
        val bundle = Bundle().apply {
            putParcelableArrayList(KEY_PHOTOS, photos?.photos)
            putInt(KEY_POSITION, position)
        }
        val intent = Intent(context, PhotoActivity::class.java)
        intent.putExtras(bundle)
        // Open the new activity whit animation
        activity?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it, photo , photo.transitionName)
            startActivity( intent, options.toBundle()) }

    }

    // Pagination if need more elements from the api
    class OnScrollListener(private val fragment: GalleryFragment) : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            recyclerView.let {
                if (!(it.canScrollVertically(1))) {
                fragment.callApi()
            } }

        }
    }
}