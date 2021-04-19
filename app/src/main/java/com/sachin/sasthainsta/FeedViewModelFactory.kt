package com.sachin.sasthainsta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedViewModelFactory(private val repository: FeedActivityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(repository) as T
        } else {
            throw IllegalArgumentException("view model not available")
        }
    }
}