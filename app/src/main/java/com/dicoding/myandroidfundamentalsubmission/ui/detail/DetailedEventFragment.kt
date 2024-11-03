package com.dicoding.myandroidfundamentalsubmission.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.myandroidfundamentalsubmission.R
import com.dicoding.myandroidfundamentalsubmission.data.local.entity.FavoriteEventEntity
import com.dicoding.myandroidfundamentalsubmission.ui.MainActivity
import com.dicoding.myandroidfundamentalsubmission.databinding.FragmentDetailedEventBinding
import com.dicoding.myandroidfundamentalsubmission.ui.ViewModelFactory
import com.dicoding.myandroidfundamentalsubmission.ui.favorite.FavoriteViewModel

class DetailedEventFragment : Fragment() {
    private var _binding: FragmentDetailedEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).hideBottomNavigation()
        _binding = FragmentDetailedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        favoriteViewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        val eventId = arguments?.getInt("EVENT_ID")
        val eventName = arguments?.getString("EVENT_NAME")
        val eventPhoto = arguments?.getString("EVENT_PHOTO")
        val eventOwner = arguments?.getString("EVENT_OWNER")
        val eventBeginTime = arguments?.getString("EVENT_BEGIN_TIME")
        val eventQuota = arguments?.getInt("EVENT_QUOTA")
        val eventRegistrant = arguments?.getInt("EVENT_REGISTRANT")
        val eventDescription = arguments?.getString("EVENT_DESCRIPTION")
        val eventLink = arguments?.getString("EVENT_LINK")

        binding.tvEventName.text = eventName
        binding.tvOwner.text = "Organized by : $eventOwner"
        binding.tvStart.text = "Date: $eventBeginTime"
        binding.tvQuota.text = "Quota : ${eventQuota?.minus(eventRegistrant ?: 0)}"
        binding.tvDesc.text =
            eventDescription?.let { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY) }

        Glide.with(binding.imgMediaCover.context)
            .load(eventPhoto)
            .into(binding.imgMediaCover)

        binding.btnRegister.setOnClickListener {
            eventLink?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }

        setHasOptionsMenu(true)

        eventId?.let { id ->
            favoriteViewModel.getFavoriteEventById(id)
                .observe(viewLifecycleOwner) { favoriteEvent ->
                    if (favoriteEvent != null) {
                        binding.btnFavorite.setImageResource(R.drawable.ic_fav_white)
                    } else {
                        binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    }

                    binding.btnFavorite.setOnClickListener {
                        if (favoriteEvent != null) {
                            favoriteViewModel.deleteEvent(favoriteEvent, id)
                            binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                        } else {
                            val newFavoriteEvent = FavoriteEventEntity(
                                id = id,
                                name = eventName,
                                ownerName = eventOwner,
                                mediaCover = eventPhoto,
                                beginTime = eventBeginTime,
                                quota = eventQuota ?: 0,
                                registrants = eventRegistrant ?: 0,
                                description = eventDescription,
                                link = eventLink,
                                isBookmarked = true
                            )
                            favoriteViewModel.saveEvent(newFavoriteEvent, id)
                            binding.btnFavorite.setImageResource(R.drawable.ic_fav_white)
                        }
                    }
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as MainActivity).showBottomNavigation()
    }
}