package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desafiofirebasedigitalhouse.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), HomeAdapter.OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: HomeViewModel by viewModels()
    private var homeAdapter: HomeAdapter = HomeAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        navController = findNavController()

        val recyclerView = binding.gameListRecycler
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = homeAdapter

        viewModel.gameList.observe(viewLifecycleOwner) {
            homeAdapter.gameList = it
            homeAdapter.notifyDataSetChanged()
        }

        binding.addGameButton.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddGameFragment(null))
        }

        viewModel.getGameList()

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val clickedItem: Game = homeAdapter.gameList[position]
        Log.d("item clicked", clickedItem.toString())
        navController.navigate(
            HomeFragmentDirections.actionHomeFragmentToGameDetailFragment(
                clickedItem
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy", "ed")
        _binding = null
    }

}