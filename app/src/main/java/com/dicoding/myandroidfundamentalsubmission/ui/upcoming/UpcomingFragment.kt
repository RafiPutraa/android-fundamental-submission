package com.dicoding.myandroidfundamentalsubmission.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.myandroidfundamentalsubmission.databinding.FragmentUpcomingBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myandroidfundamentalsubmission.R
import com.dicoding.myandroidfundamentalsubmission.ui.adapter.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UpcomingViewModel>()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_navigation_upcoming_to_navigation_detail, bundle)
        }
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        viewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
            if (events.isNullOrEmpty()) {
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = errorMessage
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.findEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}