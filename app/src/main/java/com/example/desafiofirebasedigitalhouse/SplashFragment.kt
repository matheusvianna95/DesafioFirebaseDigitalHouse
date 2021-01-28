package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.desafiofirebasedigitalhouse.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        navController = findNavController()
        CoroutineScope(Main).launch {
            delayLoginFragment()
        }

        return binding.root
    }

    private suspend fun delayLoginFragment() {
        delay(3000)
        navController.navigate(R.id.action_splashFragment_to_loginFragment)
    }

}