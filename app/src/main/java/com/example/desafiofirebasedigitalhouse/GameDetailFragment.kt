package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desafiofirebasedigitalhouse.databinding.FragmentGameDetailBinding

class GameDetailFragment : Fragment() {
    private lateinit var binding: FragmentGameDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

}