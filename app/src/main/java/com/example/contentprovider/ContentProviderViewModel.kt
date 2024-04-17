package com.example.contentprovider

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ContentProviderViewModel : ViewModel() {
    var imagesState = mutableStateOf(emptyList<ContentProviderDataItem>())

    fun updateImages(images: List<ContentProviderDataItem>) {
        imagesState.value = images
    }
}


data class ContentProviderDataItem(
    val id: Long,
    val name: String,
    val contentUri: Uri
)