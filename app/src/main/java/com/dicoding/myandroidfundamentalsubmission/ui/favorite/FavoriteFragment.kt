package com.dicoding.myandroidfundamentalsubmission.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myandroidfundamentalsubmission.data.Result
import com.dicoding.myandroidfundamentalsubmission.R
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.ListEventsItem
import com.dicoding.myandroidfundamentalsubmission.databinding.FragmentFavoriteBinding
import com.dicoding.myandroidfundamentalsubmission.ui.ViewModelFactory
import com.dicoding.myandroidfundamentalsubmission.ui.adapter.EventAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter{ selectedEvent ->
            val bundle = Bundle().apply {
                putInt("EVENT_ID", selectedEvent.id)
                putString("EVENT_NAME", selectedEvent.name)
                putString("EVENT_OWNER", selectedEvent.ownerName)
                putString("EVENT_PHOTO", selectedEvent.mediaCover)
                putString("EVENT_BEGIN_TIME", selectedEvent.beginTime)
                putInt("EVENT_QUOTA", selectedEvent.quota ?: 0)
                putInt("EVENT_REGISTRANT", selectedEvent.registrants ?: 0)
                putString("EVENT_DESCRIPTION", selectedEvent.description)
                putString("EVENT_LINK", selectedEvent.link)
            }
            findNavController().navigate(R.id.action_navigation_favorite_to_navigation_detail, bundle)
        }
        binding.rvEvents.layoutManager = LinearLayoutManager(context)
        binding.rvEvents.adapter = eventAdapter

        favoriteViewModel.getAllEvent().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val items = result.data.map { user ->
                        ListEventsItem(
                            id = user.id ?: 0,
                            name = user.name ?: "",
                            ownerName = user.ownerName,
                            mediaCover = user.mediaCover,
                            beginTime = user.beginTime,
                            quota = user.quota,
                            registrants = user.registrants,
                            description = user.description,
                            link = user.link
                        )
                    }
                    eventAdapter.submitList(items)
                    binding.tvError.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}