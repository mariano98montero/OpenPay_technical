package com.example.open_pay_technical.ui.images

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.open_pay_technical.R
import com.example.open_pay_technical.databinding.FragmentImageManagerBinding
import com.example.open_pay_technical.util.ExceptionDialogFragment
import com.example.open_pay_technical.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageManagerFragment : Fragment() {

    private lateinit var binding: FragmentImageManagerBinding

    private val imageManagerViewModel: ImageManagerViewModel by viewModels()

    private var imageList = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageManagerBinding.inflate(layoutInflater)
        imageManagerViewModel.getLiveData().observe({ lifecycle }, ::updateUI)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageManagerFragmentTakePhotoButton.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        }
        binding.imageManagerFragmentGetFromGalleryButton.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, RESULT_LOAD_IMG) //one can be replaced with any action code
        }

        updateScreenImageCounter()

        binding.imageManagerFragmentUploadSelectedImagesButton.setOnClickListener {
            if (imageList.isNotEmpty()) {
                val list: List<Uri> = imageList
                imageManagerViewModel.uploadImage(list)
            } else {
                showExceptionDialogFragment(getString(R.string.image_manager_fragment_no_image_selected_warning))
            }
        }

        if (context?.let { Util.isDeviceOnline(it) } == false) {
            showExceptionDialogFragment(getString(R.string.no_internet_error_message_text))
        }

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } == PackageManager
                .PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun updateUI(data: ImageManagerData) {
        when (data.state) {
            ImageManagerState.IMAGE_UPLOAD_SUCCESS -> {
                showExceptionDialogFragment(
                    getString(
                        R.string
                            .image_manager_fragment_image_uploaded_success
                    )
                )
                imageList = mutableListOf()
                updateScreenImageCounter()
            }
            ImageManagerState.IMAGE_UPLOAD_FAILED -> {
                showExceptionDialogFragment(
                    getString(
                        R.string
                            .image_manager_fragment_image_uploaded_failed
                    )
                )
                binding.loaderAnimation.visibility = View.GONE
            }
            ImageManagerState.SHOW_LOADER -> binding.loaderAnimation.visibility = View.VISIBLE
        }
    }

    private fun updateScreenImageCounter() {
        binding.loaderAnimation.visibility = View.GONE
        binding.imageManagerFragmentImagesSelectedTextView.text = getString(
            R.string
                .image_manager_images_selected_text, imageList.size.toString()
        )
    }

    private fun showExceptionDialogFragment(title: String) {
        ExceptionDialogFragment.newInstance(title)
            .show(
                parentFragmentManager,
                ExceptionDialogFragment.EXCEPTION_DIALOG_FRAGMENT
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                data?.extras?.let {
                    val bitmap: Bitmap = it.get("data") as Bitmap
                    val imageUri = Util.getImageUri(requireContext(), bitmap)
                    imageUri?.let {
                        imageList.add(it)
                    }
                }
                updateScreenImageCounter()
            }
            1 -> if (resultCode == RESULT_OK) {
                data?.data?.let {
                    imageList.add(it)
                    updateScreenImageCounter()
                }
            }
        }
    }

    companion object {
        private const val RESULT_LOAD_IMG = 1
        private const val RESULT_OK = -1
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
