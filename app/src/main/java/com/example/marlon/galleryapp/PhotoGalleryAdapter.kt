package com.example.marlon.galleryapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import androidx.core.app.ActivityCompat.startPostponedEnterTransition
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.image_item.view.*
import kotlinx.android.synthetic.main.photo_item.view.*


class PhotoGalleryAdapter(
    private var photos: ArrayList<NasaPhoto?>?,
    private val selectedPhoto: SelectedPhoto,
    private val grid: Boolean,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // callback interface
    interface SelectedPhoto {
        fun clickItem(position: Int,photo:ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val view: View = if (grid) {
            LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        }
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(view)

    }

    fun setData(newList: ArrayList<NasaPhoto?>?) {
        photos = newList ?: photos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photos?.size ?: -1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // - get element from your dataSet at this position
        // - replace the contents of the view with that element
        bindView(holder, position)

    }

    // Set values to the view
    private fun bindView(holder: RecyclerView.ViewHolder, position: Int): RecyclerView.ViewHolder {
        val photo: ImageView
        val progress: ProgressBar

        if (grid) {
            // If the layout is a grid, uses these item properties names
            photo = holder.itemView.photo_thumbnail
            progress = holder.itemView.progress_thumbnail
        } else {
            // If the layout is not a grid, uses these item properties names
            photo = holder.itemView.photo
            progress = holder.itemView.progress
            val sheetBehavior = BottomSheetBehavior.from<LinearLayout>(holder.itemView.bottom_sheet)
            /**
             * bottom sheet state change listener
             * we are changing button text when sheet changed state
             * */
            sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            val nasaPhoto = photos?.get(position)
                            // Sets the data in the bottom sheet view
                            bottomSheet.name.text = nasaPhoto?.camera?.name
                            bottomSheet.launch_date.text = nasaPhoto?.rover?.launchDate
                            bottomSheet.landing_date.text = nasaPhoto?.rover?.landingDate
                            // Hide bottom sheet button
                            bottomSheet.hide.setOnClickListener { expandCloseSheet(sheetBehavior) }
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            photo.setOnLongClickListener { expandCloseSheet(sheetBehavior) }
        }
        // Opens a new activity if the image view is clicked
        photo.setOnClickListener { selectedPhoto.clickItem(position,photo) }
        photo.transitionName= "$position"
        // Download and display image or display progress bar in the image view
        GlideApp.with(fragment)
            .load(photos?.get(position)?.imgSrc)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.GONE
                    startPostponedEnterTransition(fragment.activity!!)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.GONE
                    startPostponedEnterTransition(fragment.activity!!)
                    return false
                }
            })
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(photo)

        return holder
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun expandCloseSheet(sheetBehavior: BottomSheetBehavior<LinearLayout>): Boolean {
        // Changes the bottom sheet behavior state
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return true
    }
}
