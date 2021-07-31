package me.lengthmin.coshelper.ui.refresh_rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RefreshRateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is refresh rate Fragment"
    }
    val text: LiveData<String> = _text
}