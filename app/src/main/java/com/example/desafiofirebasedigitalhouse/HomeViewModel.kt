package com.example.desafiofirebasedigitalhouse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.launch

private const val COLLECTION = "Game"

class HomeViewModel : ViewModel() {
    private val firestoreDatabase = FirebaseFirestore.getInstance()
    val gameList = MutableLiveData<MutableList<Game>>()

    fun getGameList() {
        viewModelScope.launch {
            val incomingGameList = mutableListOf<Game>()

            firestoreDatabase.collection(COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("Firestore", "${document.id} => ${document.data}")
                        incomingGameList.add(document.toObject(Game::class.java))
                    }

                    gameList.value = incomingGameList
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents.", exception)
                }

        }
    }

}