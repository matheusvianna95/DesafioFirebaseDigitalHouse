package com.example.desafiofirebasedigitalhouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.desafiofirebasedigitalhouse.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        navController = findNavController()

        binding.backButton.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailTextInput.editText?.text.toString()
            if (matchingPassword()) {
                val password = binding.passwordTextInput.editText?.text.toString()
                createUser(email, password)
            } else {
                Toast.makeText(
                    requireContext(), "Passwords did not match.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    private fun createUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Firebase", "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    navController.navigate(R.id.action_registerFragment_to_homeFragment)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Firebase", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun matchingPassword(): Boolean =
        binding.passwordTextInput.editText?.text.toString() == binding.repeatTextInput.editText?.text.toString()

}