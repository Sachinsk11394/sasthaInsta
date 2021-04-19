package com.sachin.sasthainsta

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_view_stories.*
import java.util.*


class ViewStoriesFragment : Fragment() {
    private var mCurrentPage = 0
    private var mTimer: Timer = Timer()
    private val DELAY_MS: Long = 4000
    private val mPostLiveData: MutableLiveData<Post> = MutableLiveData()
    private var mPosition: MutableLiveData<Int> = MutableLiveData()
    private var mStartDragX = 0f
    private var isDragging = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_stories, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setStories(storiesList: Tags, position: Int) {
        mPostLiveData.observe(this@ViewStoriesFragment, { post ->
            viewpager.adapter = null
            val postAdapter = PostAdapter(activity as AppCompatActivity, post)
            viewpager.adapter = postAdapter
            tabDots.visibility = View.VISIBLE
            tabDots.setupWithViewPager(viewpager, true)
            viewpager.pageMargin = 2

            /*After setting the adapter use the timer */
            val handler = Handler()
            val update = Runnable {
                if (mCurrentPage == (post.mediaList?.size ?: 1) - 1) {
                    mCurrentPage = 0
                    mPosition.value = mPosition.value!! + 1
                } else {
                    viewpager.setCurrentItem(++mCurrentPage, true)
                }
            }

            viewpager.setOnTouchListener { _, ev ->
                val x = ev.x
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> mStartDragX = x
                    MotionEvent.ACTION_MOVE -> isDragging = true
                    MotionEvent.ACTION_UP -> {
                        if (isDragging) {
                            isDragging = false
                            if (mStartDragX < x && viewpager.currentItem == 0) {
                                mCurrentPage = 0
                                mPosition.value = mPosition.value?.minus(1)
                            } else if (mStartDragX > x && viewpager.currentItem == postAdapter!!.count - 1) {
                                mCurrentPage = 0
                                mPosition.value = mPosition.value?.plus(1)
                            }
                        }
                    }
                }

                return@setOnTouchListener viewpager.onTouchEvent(ev)
            }

            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    // intentionally left blank
                }

                override fun onPageSelected(position: Int) {
                    mCurrentPage = position
                    setTimer(handler, update)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    // intentionally left blank
                }
            })

            setTimer(handler, update)
        })
        mPosition.observe(this@ViewStoriesFragment, {
            mPostLiveData.value = storiesList.tagsData?.postList?.get(it)
        })
        mPosition.value = position
    }

    private fun setTimer(handler: Handler, update: Runnable) {
        mTimer.cancel()
        mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS)
    }
}