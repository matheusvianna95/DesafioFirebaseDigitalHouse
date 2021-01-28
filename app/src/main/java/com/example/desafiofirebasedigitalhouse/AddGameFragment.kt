package com.example.desafiofirebasedigitalhouse

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.desafiofirebasedigitalhouse.databinding.FragmentAddGameBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

private const val IMG_CODE = 1001

class AddGameFragment : Fragment() {
    private lateinit var binding: FragmentAddGameBinding
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGameBinding.inflate(inflater, container, false)

        firebaseStorage = FirebaseStorage.getInstance()
        storageRef = firebaseStorage.getReference("gameCover")

        binding.uploadPhotoCard.setOnClickListener {
            getImage()
        }

        binding.saveButton.setOnClickListener {

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMG_CODE) {

            try {
                val uploadFile = storageRef.putFile(data?.data!!)
                uploadFile.continueWithTask { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Imagem carregada com sucesso", Toast.LENGTH_SHORT).show()
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val imgUrl = task.result.toString().substring(0, task.result.toString().indexOf("&token"))
                        Log.i("URL", imgUrl)
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

}