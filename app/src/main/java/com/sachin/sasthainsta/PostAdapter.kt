package com.sachin.sasthainsta

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView


class PostAdapter(private val activity: AppCompatActivity, private val post: Post) :
    PagerAdapter() {

    override fun getCount(): Int {
        return post.mediaList?.size ?: 1
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.media, container, false)

        val imageView = view.findViewById<ImageView>(R.id.image)
        val videoView = view.findViewById<PlayerView>(R.id.video)
        val progressBarView = view.findViewById<ProgressBar>(R.id.progressBar)

        val type = if (post.isAlbum == true) {
            post.mediaList?.get(position)?.type
        } else {
            post.type
        }
        val link = if (post.isAlbum == true) {
            post.mediaList?.get(position)?.link
        } else {
            post.link
        } ?: ""

        if (type == "video/mp4") {
            videoView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            videoView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
            val player = SimpleExoPlayer.Builder(activity).build()
            videoView.player = player
            val mediaItem = MediaItem.fromUri(link)
            player.setMediaItem(mediaItem)
            player.volume = 0f
            player.playWhenReady = true
            player.prepare()
        } else {
            imageView.visibility = View.VISIBLE
            videoView.visibility = View.GONE
            progressBarView.visibility = View.VISIBLE

            Glide.with(activity.baseContext)
                .load(link)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBarView.visibility = View.GONE
                        return false
                    }

                })
                .into(imageView)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}