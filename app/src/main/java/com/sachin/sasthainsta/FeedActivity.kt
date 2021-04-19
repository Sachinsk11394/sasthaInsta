package com.sachin.sasthainsta

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.stories_toolbar.*
import javax.inject.Inject

class FeedActivity : AppCompatActivity() {

    /**
     * TagsData members
     */
    @Inject
    lateinit var mViewModelFactory: FeedViewModelFactory
    private val mTopFeedFragment: FeedFragment = FeedFragment()
    private val mHotFeedFragment: FeedFragment = FeedFragment()
    private val storiesFragment: ViewStoriesFragment = ViewStoriesFragment()
    private var mActiveFragment: FeedFragment = mTopFeedFragment

    private val mViewModel: FeedViewModel by lazy {
        ViewModelProvider(
            this@FeedActivity,
            mViewModelFactory
        ).get(FeedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        supportActionBar?.hide()

        DaggerCoreComponent.builder().coreModule(CoreModule(this@FeedActivity)).build()
            .injectMainActivity(this@FeedActivity)

        setStories()
        setFeed()
        setBottomNav()
    }

    private fun setStories() {
        val storiesList = mViewModel.stories()
        val viewPostPosition: MutableLiveData<Int> = MutableLiveData()
        storiesList.observe(this@FeedActivity, Observer {
            when (it) {
                is NetworkResponse.Success -> {
                    stories.adapter = StoriesAdapter(this@FeedActivity, viewPostPosition, it.data)
                }
                is NetworkResponse.Failure -> {
                    Toast.makeText(this@FeedActivity, "API error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        supportFragmentManager.beginTransaction()
            .add(R.id.storiesContainer, storiesFragment, "storiesFragment")
            .hide(storiesFragment).commit()
        storiesContainer.visibility = View.GONE
        viewPostPosition.observe(this@FeedActivity, Observer { position ->
            bottomNavigation.visibility = View.GONE
            storiesContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().show(storiesFragment).commit()
            storiesFragment.setStories(
                (storiesList.value as NetworkResponse.Success).data,
                position
            )
        })

        stories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setFeed() {
        supportFragmentManager.beginTransaction().add(R.id.feedContainer, mHotFeedFragment, "2")
            .hide(mHotFeedFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.feedContainer, mTopFeedFragment, "1")
            .commit()

        val topImages = mViewModel.galleryTop()
        topImages.observe(this@FeedActivity, Observer {
            when (it) {
                is NetworkResponse.Success -> {
                    mTopFeedFragment.setFeed(it.data)
                    progressBar.visibility = View.GONE
                }
                is NetworkResponse.Failure -> {
                    Toast.makeText(this@FeedActivity, "API error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val hotImages = mViewModel.galleryHot()
        hotImages.observe(this@FeedActivity, Observer {
            when (it) {
                is NetworkResponse.Success -> {
                    mHotFeedFragment.setFeed(it.data)
                }
                is NetworkResponse.Failure -> {
                    Toast.makeText(this@FeedActivity, "API error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setBottomNav() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_top -> {
                    supportFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(mTopFeedFragment).commit()
                    mActiveFragment = mTopFeedFragment
                }
                R.id.action_hot -> {
                    supportFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(mHotFeedFragment).commit()
                    mActiveFragment = mHotFeedFragment
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        val storiesFragment: Fragment? = supportFragmentManager.findFragmentByTag("storiesFragment")
        if (storiesFragment != null && storiesFragment.isVisible) {
            bottomNavigation.visibility = View.VISIBLE
            storiesContainer.visibility = View.GONE
            supportFragmentManager.beginTransaction().hide(storiesFragment).commit()
        } else {
            super.onBackPressed()
        }
    }
}