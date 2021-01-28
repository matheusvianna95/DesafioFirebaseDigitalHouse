package com.example.desafiofirebasedigitalhouse

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.desafiofirebasedigitalhouse.databinding.FragmentAddGameBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

private const val IMG_CODE = 1001

class AddGameFragment : Fragment() {
    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var firestoreDatabase: FirebaseFirestore

    private var currentGame: Game? = Game()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)

        firestoreDatabase = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        binding.uploadPhotoCard.setOnClickListener {
            getImage()
        }

        binding.saveButton.setOnClickListener {
            // update currentGame
            currentGame?.apply {
                name = binding.nameTextInput.editText?.text.toString()
                release = binding.releaseTextInput.editText?.text.toString()
                description = binding.descriptionTextInput.editText?.text.toString()
            }

            Log.d("game.id", currentGame!!.id)

            firestoreDatabase.collection("Games")
                .document(currentGame!!.id)
                .set(currentGame!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Firestore", "DocumentSnapshot added with ID: ${currentGame!!.id}")
                        findNavController().navigateUp()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMG_CODE) {

            try {
                val ref = data?.data.hashCode().toString()
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

    override fun onDestroy() {
        super.onDestroy()

        currentGame = null
        _binding = null
    }

}