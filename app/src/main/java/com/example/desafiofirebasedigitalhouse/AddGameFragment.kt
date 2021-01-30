package com.example.desafiofirebasedigitalhouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.desafiofirebasedigitalhouse.databinding.FragmentAddGameBinding
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlin.random.Random

private const val IMG_CODE = 1001
private const val COLLECTION = "Game"

class AddGameFragment : Fragment() {
    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    private val args: AddGameFragmentArgs by navArgs()

    private lateinit var navController: NavController

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var firestoreDatabase: FirebaseFirestore

    private var pictureLoaded: Boolean = false

    private var currentGame: Game? = Game()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)

        navController = findNavController()

        firestoreDatabase = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        if (args.game != null) {
            setGameEdit()
        }

        binding.uploadPhotoCard.setOnClickListener {
            getImage()
        }

        setSaveButtonClick()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMG_CODE) {

            try {
                val ref: String = if (args.game != null) currentGame!!.id else data?.data.hashCode().toString()
                storageRef = firebaseStorage.getReference(ref)

                val uploadFile = storageRef.putFile(data?.data!!)
                uploadFile.continueWithTask { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Imagem carregada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val imgUrl = task.result.toString()
                            .substring(0, task.result.toString().indexOf("&token"))
                        Log.i("URL", imgUrl)

                        // update currentGame
                        currentGame?.apply {
                            this.imgUrl = imgUrl
                            id = ref
                        }
                        Picasso.get().load(imgUrl).into(binding.gameCoverImage)
                        pictureLoaded = true
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Upload falhou", Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                return
            }

        }

    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Capture image"), IMG_CODE)
    }

    private fun setSaveButtonClick() {
        binding.saveButton.setOnClickListener {
            // update currentGame
            currentGame?.apply {
                name = binding.nameTextInput.editText?.text.toString()
                release = binding.releaseTextInput.editText?.text.toString()
                description = binding.descriptionTextInput.editText?.text.toString()
            }

            Log.d("game.id", currentGame!!.id)

            if (pictureLoaded) {
                firestoreDatabase.collection(COLLECTION)
                    .document(currentGame!!.id)
                    .set(currentGame!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("Firestore", "DocumentSnapshot added with ID: ${currentGame!!.id}")
                            navController.navigate(R.id.action_addGameFragment_to_homeFragment)
                            navController.popBackStack(R.id.homeFragment, false)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding document", e)
                    }
            } else {
                Toast.makeText(requireContext(), "Adicione uma imagem.", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setGameEdit() {
        currentGame = args.game
        Picasso.get().load(currentGame?.imgUrl + "&" + Random.nextInt()).into(binding.gameCoverImage)
        pictureLoaded = true
        binding.apply {
            nameTextInput.editText?.setText(currentGame?.name)
            releaseTextInput.editText?.setText(currentGame?.release)
            descriptionTextInput.editText?.setText(currentGame?.description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        currentGame = null
        _binding = null
    }

}