package com.example.open_pay_technical.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.open_pay_technical.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentLocationBinding

    private val moviesViewModel: LocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(layoutInflater)
        /*
        moviesViewModel.getLiveData().observe({ lifecycle }, ::updateUI)
        if (context?.let { Util.isDeviceOnline(it) } == true) {
            moviesViewModel.fetchInitData()
        } else {
            showExceptionDialogFragment(getString(R.string.no_internet_error_message_text))
            moviesViewModel.loadTopRatedMoviesFromLocal()
        }
        ˘*/
        return binding.root
    }

}
