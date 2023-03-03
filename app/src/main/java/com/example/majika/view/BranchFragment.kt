package com.example.majika.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.majika.adapter.BranchAdapter
import com.example.majika.databinding.FragmentBranchBinding
import com.example.majika.viewmodel.BranchViewModel

class BranchFragment : Fragment() {
    private var _binding: FragmentBranchBinding? = null
    private val binding get() = _binding!!

    private lateinit var branchViewModel: BranchViewModel
    private var branchAdapter: BranchAdapter = BranchAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBranchBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            branchFragment = this@BranchFragment
        }

        // fill here
        branchViewModel = ViewModelProvider(this).get(BranchViewModel::class.java)
        branchViewModel.branchItem.observe(viewLifecycleOwner, { branches ->
            branches?.let {
                branchAdapter.setBranches(branches)
                branchAdapter.notifyDataSetChanged()
            }
        })
        val recyclerView = binding.branchRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = branchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
