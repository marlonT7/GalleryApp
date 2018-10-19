package com.example.marlon.galleryapp

import android.graphics.drawable.Drawable
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
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
import kotlinx.android.synthetic.main.image_item.view.*
import kotlinx.android.synthetic.main.photo_item.view.*


class PhotoGalleryAdapter(
    private var photos: ArrayList<NasaPhoto>?,
    private val selectedPhoto: SelectedPhoto,
    private val grid: Boolean,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // callback interface
    interface SelectedPhoto {
        fun clickItem(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val view: View = if (grid) {
            LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        }
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(view = view)

    }

    fun setData(newList: ArrayList<NasaPhoto>?) {
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
            photo = holder.itemView.photo_thumbnail
            progress = holder.itemView.progress_thumbnail
        } else {
            photo = holder.itemView.photo
            progress = holder.itemView.progress
            val sheetBehavior = BottomSheetBehavior.from<LinearLayout>(holder.itemView.bottom_sheet)
            /**
             * bottom sheet state change listener
             * we are changing button text when sheet changed state
             * */
            sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            val nasaPhoto = photos?.get(position)
                            bottomSheet.name.text = nasaPhoto?.camera?.name
                            bottomSheet.launch_date.text = nasaPhoto?.rover?.launchDate
                            bottomSheet.landing_date.text = nasaPhoto?.rover?.landingDate
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
        photo.setOnClickListener { selectedPhoto.clickItem(position) }
        GlideApp.with(fragment)
            .load(photos?.get(position)?.imgSrc)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.GONE
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
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private fun expandCloseSheet(sheetBehavior: BottomSheetBehavior<LinearLayout>): Boolean {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return true
    }
}
