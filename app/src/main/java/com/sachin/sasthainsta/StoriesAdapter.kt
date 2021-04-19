package com.sachin.sasthainsta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class StoriesAdapter(
    private val activity: AppCompatActivity, private val positionClicked: MutableLiveData<Int>,
    private val storiesList: Tags,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.tag, parent, false)
        return Stories(activity, positionClicked, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Stories).bindView(storiesList.tagsData?.postList?.get(position), position)
    }

    override fun getItemCount(): Int {
        return storiesList.tagsData?.postList?.size ?: 0
    }
}

class Stories(
    private val activity: AppCompatActivity,
    private val positionClicked: MutableLiveData<Int>,
    private val view: View
) :
    RecyclerView.ViewHolder(view) {
    fun bindView(post: Post?, position: Int) {
        val image = view.findViewById<ImageView>(R.id.image)
        val link = if (post?.isAlbum == true) {
            post.mediaList?.get(0)?.link
        } else {
            post?.link
        } ?: ""

        Glide.with(activity.baseContext)
            .load(link)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)

        image.setOnClickListener{
            positionClicked.value = position
        }
    }
}