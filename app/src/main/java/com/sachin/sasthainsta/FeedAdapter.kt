package com.sachin.sasthainsta

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class FeedAdapter(private val activity: AppCompatActivity, private val feed: Feed) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.post, parent, false)
        return FeedHolder(activity, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeedHolder).bindView(feed.postListList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return feed.postListList?.size ?: 0
    }
}

class FeedHolder(private val activity: AppCompatActivity, private val view: View) :
    RecyclerView.ViewHolder(view) {
    fun bindView(post: Post) {
        val pager = view.findViewById<ViewPager>(R.id.viewpager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabDots)
        pager.adapter = PostAdapter(activity, post)
        if(post.mediaList?.size ?: 0 > 1) {
            tabLayout.visibility = View.VISIBLE
            tabLayout.setSelectedTabIndicator(null)
            tabLayout.setupWithViewPager(pager, true)
            pager.pageMargin = 2
        } else {
            tabLayout.visibility = View.GONE
        }
    }
}