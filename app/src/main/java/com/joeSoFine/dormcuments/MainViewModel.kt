package com.joeSoFine.dormcuments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel: ViewModel() {


    public override fun onCleared() {
        super.onCleared()
        Log.e("MainViewModel", "OnCleared mainViewModel")
    // Dispose All your Subscriptions to avoid memory leaks
    }
}

