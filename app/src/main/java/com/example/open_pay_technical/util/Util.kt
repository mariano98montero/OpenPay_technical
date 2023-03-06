package com.example.open_pay_technical.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.open_pay_technical.R
import java.io.ByteArrayOutputStream
import java.util.UUID

object Util {

    fun isDeviceOnline(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }

    fun getImageUri(context: Context, image: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, image, UUID.randomUUID().toString(),
            null)
        return Uri.parse(path)
    }

    fun ImageView.showImage(context: Context, imageURL: String){
        Glide.with(context)
            .load("${Constants.SERVICE_IMAGE_URL}${imageURL}")
            .placeholder(R.drawable.poster_placeholder)
            .circleCrop()
            .into(this)
    }
}
