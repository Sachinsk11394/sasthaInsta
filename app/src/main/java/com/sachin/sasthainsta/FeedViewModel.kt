package com.sachin.sasthainsta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class FeedViewModel(private val repository: FeedActivityRepository) : ViewModel() {

    // Get dog images as stories
    fun stories() : LiveData<NetworkResponse<Tags>> {
        val liveData = MutableLiveData<NetworkResponse<Tags>>()
        viewModelScope.launch {
            liveData.postValue(repository.tags())
        }
        return liveData
    }

    fun galleryTop() : LiveData<NetworkResponse<Feed>> {
        val liveData = MutableLiveData<NetworkResponse<Feed>>()
        viewModelScope.launch {
            liveData.postValue(repository.galleryTop())
        }
        return liveData
    }

    fun galleryHot() : LiveData<NetworkResponse<Feed>> {
        val liveData = MutableLiveData<NetworkResponse<Feed>>()
        viewModelScope.launch {
            liveData.postValue(repository.galleryHot())
        }
        return liveData
    }
}