package com.example.quantum.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.quantum.Helper.ImageLoader
import com.example.quantum.ImageData
import com.example.quantum.R

class ImageAdapter(private val images: List<ImageData>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item, parent, false) as ImageView
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position].url
        ImageLoader.loadImage(imageUrl, holder.imageView)
    }

    override fun getItemCount() = images.size
}
