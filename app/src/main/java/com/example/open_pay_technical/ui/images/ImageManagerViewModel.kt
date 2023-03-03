package com.example.open_pay_technical.ui.images

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ImageManagerViewModel @Inject constructor(
) : ViewModel() {

    private val liveData = MutableLiveData<ImageManagerData>()

    private val storage = FirebaseStorage.getInstance().reference

    fun getLiveData(): LiveData<ImageManagerData> = liveData

    fun uploadImage(imageData: List<Uri>) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            imageData.forEach { image ->
                val ref: StorageReference = storage.child("/images/${UUID.randomUUID()}.jpg");
                ref.putFile(image)
                    .addOnSuccessListener {
                        if (imageData.last() == image)
                            liveData.postValue(ImageManagerData(state = ImageManagerState.IMAGE_UPLOAD_SUCCESS))
                    }
                    .addOnFailureListener {
                        liveData.postValue(ImageManagerData(state = ImageManagerState.IMAGE_UPLOAD_FAILED))
                    }
                    .addOnProgressListener {
                        liveData.postValue(ImageManagerData(state = ImageManagerState.SHOW_LOADER))
                    }
            }

        }
    }
}

data class ImageManagerData(
    var state: ImageManagerState
)

enum class ImageManagerState {
    IMAGE_UPLOAD_SUCCESS,
    IMAGE_UPLOAD_FAILED,
    SHOW_LOADER
}

