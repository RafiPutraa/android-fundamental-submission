package com.dicoding.myandroidfundamentalsubmission.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myandroidfundamentalsubmission.R
import com.dicoding.myandroidfundamentalsubmission.databinding.FragmentHomeBinding
import com.dicoding.myandroidfundamentalsubmission.ui.adapter.CarouselAdapter
import com.dicoding.myandroidfundamentalsubmission.ui.adapter.EventAdapter
import androidx.appcompat.widget.SearchView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var eventAdapter: EventAdapter
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var searchAdapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter{ selectedEvent ->
            val bundle = Bundle().apply {
                putInt("EVENT_ID", selectedEvent.id)
                putString("EVENT_NAME", selectedEvent.name)
                putString("EVENT_PHOTO", selectedEvent.mediaCover)
                putString("EVENT_OWNER", selectedEvent.ownerName)
                putString("EVENT_BEGIN_TIME", selectedEvent.beginTime)
                putInt("EVENT_QUOTA", selectedEvent.quota ?: 0)
                putInt("EVENT_REGISTRANT", selectedEvent.registrants ?: 0)
                putString("EVENT_DESCRIPTION", selectedEvent.description)
                putString("EVENT_LINK", selectedEvent.link)
            }
            findNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
        }
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        carouselAdapter = CarouselAdapter { selectedEvent ->
            val bundle = Bundle().apply {
                putInt("EVENT_ID", selectedEvent.id)
                putString("EVENT_NAME", selectedEvent.name)
                putString("EVENT_PHOTO", selectedEvent.mediaCover)
                putString("EVENT_OWNER", selectedEvent.ownerName)
                putString("EVENT_BEGIN_TIME", selectedEvent.beginTime)
                putInt("EVENT_QUOTA", selectedEvent.quota ?: 0)
                putInt("EVENT_REGISTRANT", selectedEvent.registrants ?: 0)
                putString("EVENT_DESCRIPTION", selectedEvent.description)
                putString("EVENT_LINK", selectedEvent.link)
            }
            findNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
        }
        binding.rvCarousel.adapter = carouselAdapter

        searchAdapter = CarouselAdapter { selectedEvent ->
            val bundle = Bundle().apply {
                putInt("EVENT_ID", selectedEvent.id)
                putString("EVENT_NAME", selectedEvent.name)
                putString("EVENT_PHOTO", selectedEvent.mediaCover)
                putString("EVENT_OWNER", selectedEvent.ownerName)
                putString("EVENT_BEGIN_TIME", selectedEvent.beginTime)
                putInt("EVENT_QUOTA", selectedEvent.quota ?: 0)
                putInt("EVENT_REGISTRANT", selectedEvent.registrants ?: 0)
                putString("EVENT_DESCRIPTION", selectedEvent.description)
                putString("EVENT_LINK", selectedEvent.link)
            }
            findNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
        }

        binding.rvSearchResultsCarousel.adapter = searchAdapter

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            carouselAdapter.submitList(events.take(5))
            eventAdapter.submitList(events)
            binding.tvNoResult.visibility = View.GONE
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events.take(5))
            if (events.isNullOrEmpty()) {
                binding.tvNoEvent.visibility = View.VISIBLE
            } else {
                binding.tvNoEvent.visibility = View.GONE
                binding.tvNoResult.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.findEvent()

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterEvents(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText)
                return true
            }
        })

    }

    private fun filterEvents(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            binding.rvSearchResultsCarousel.visibility = View.GONE
            binding.tvNoResult.visibility = View.GONE
            return
        } else {
            val allEvents = (viewModel.finishedEvents.value ?: emptyList()) + (viewModel.upcomingEvents.value ?: emptyList())
            allEvents.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        if (filteredList.isEmpty()) {
            binding.rvSearchResultsCarousel.visibility = View.GONE
            binding.tvNoResult.visibility = View.VISIBLE
        } else {
            binding.rvSearchResultsCarousel.visibility = View.VISIBLE
            binding.tvNoResult.visibility = View.GONE
            searchAdapter.submitList(filteredList)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}