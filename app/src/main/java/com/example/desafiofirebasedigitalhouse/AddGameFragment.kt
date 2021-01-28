package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desafiofirebasedigitalhouse.databinding.FragmentAddGameBinding

class AddGameFragment : Fragment() {
    private lateinit var binding: FragmentAddGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGameBinding.inflate(inflater, container, false)

        return binding.root
    }

}