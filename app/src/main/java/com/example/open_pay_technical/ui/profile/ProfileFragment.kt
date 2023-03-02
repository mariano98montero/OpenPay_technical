package com.example.open_pay_technical.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.open_pay_technical.R
import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.databinding.FragmentProfileBinding
import com.example.open_pay_technical.ui.adapter.ProfileScreenMoviesAdapter
import com.example.open_pay_technical.util.Constants.SERVICE_IMAGE_URL
import com.example.open_pay_technical.util.ExceptionDialogFragment
import com.example.open_pay_technical.util.ExceptionDialogFragment.Companion.EXCEPTION_DIALOG_FRAGMENT
import com.example.open_pay_technical.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val moviesAdapter = ProfileScreenMoviesAdapter()

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel.getLiveData().observe({ lifecycle }, ::updateUI)

        if (context?.let { Util.isDeviceOnline(it) } == true) {
            profileViewModel.fetchInitData()
        } else {
            profileViewModel.getData
            showExceptionDialogFragment(getString(R.string.no_internet_error_message_text))
        }
        return binding.root
    }

    private fun updateUI(data: ProfileViewModel.ProfileScreenData) {
        when (data.state) {
            ProfileViewModel.ProfileScreenState.ACTOR_DATA_SUCCESS -> {
                binding.loaderAnimation.visibility = View.GONE
                data.actor?.let {
                    profileViewModel.saveActorOnLocal(it)
                    profileViewModel.getCombinedCredits(it.id)
                    populateActorUI(it)
                }
                binding.loaderAnimation.visibility = View.GONE
            }
            ProfileViewModel.ProfileScreenState.SERVICE_ERROR -> {
                binding.loaderAnimation.visibility = View.GONE
                showExceptionDialogFragment(data.exception)
            }
            ProfileViewModel.ProfileScreenState.SHOW_CREDIT_MOVIES -> {
                binding.profileFragmentRecyclerView.adapter = moviesAdapter
                binding.profileFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
                moviesAdapter.updateList(data.movies)
            }
            ProfileViewModel.ProfileScreenState.ACTOR_SAVED_SUCCESS -> Toast.makeText(
                context, getString(R.string.database_saved_successfully_message_text), Toast
                    .LENGTH_SHORT
            ).show()
            ProfileViewModel.ProfileScreenState.ACTOR_SAVING_ERROR -> showExceptionDialogFragment(data.exception)
            ProfileViewModel.ProfileScreenState.SHOW_LOADER -> binding.loaderAnimation.visibility = View.VISIBLE
        }
    }

    private fun showExceptionDialogFragment(title: String) {
        ExceptionDialogFragment.newInstance(title)
            .show(
                parentFragmentManager,
                EXCEPTION_DIALOG_FRAGMENT
            )
    }

    private fun populateActorUI(actor: Actor) {
        with(binding) {
            profileFragmentActorNameTextView.text = actor.name
            profileFragmentActorBiographyTextView.text = resources.getString(
                R.string.profile_actor_biography_text, actor.biography
            )
            profileFragmentActorOriginTextView.text = resources.getString(
                R.string.profile_actor_origin_text, actor.origin
            )
            context?.let {
                Glide.with(it)
                    .load("$SERVICE_IMAGE_URL${actor.profilePicture}")
                    .placeholder(R.drawable.poster_placeholder)
                    .circleCrop()
                    .into(profileFragmentActorPhotoImageView)
            }
        }
    }
}
