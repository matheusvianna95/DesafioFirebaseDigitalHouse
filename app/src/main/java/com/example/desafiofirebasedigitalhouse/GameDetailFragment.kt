package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.desafiofirebasedigitalhouse.databinding.FragmentGameDetailBinding
import com.squareup.picasso.Picasso
import kotlin.random.Random

class GameDetailFragment : Fragment() {
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private val args: GameDetailFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)

        navController = findNavController()
        setContents()

        binding.backButton.setOnClickListener {
            navController.navigate(R.id.action_gameDetailFragment_to_homeFragment)
            navController.popBackStack(R.id.homeFragment, false)
        }

        binding.editButton.setOnClickListener {
            navController.navigate(
                GameDetailFragmentDirections.actionGameDetailFragmentToAddGameFragment(
                    args.game
                )
            )
        }

        return binding.root
    }

    private fun setContents() {
        binding.gameTitleImage.text = args.game.name
        binding.gameTitleText.text = args.game.name
        binding.gameReleaseText.text = "Released: ${args.game.release}"
        binding.gameDescriptionText.text = args.game.description
        Picasso.get().load(args.game.imgUrl  + "&" + Random.nextInt()).fit().centerCrop().into(binding.gameCover)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}