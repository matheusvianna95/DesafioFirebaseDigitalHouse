package com.example.desafiofirebasedigitalhouse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
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
                    for (game in result) {
                        Log.d("Firestore", "${game.id} => ${game.data}")
                        val incomingGame = Game(
                            game.get("name").toString(),
                            game.get("release").toString(),
                            game.get("description").toString(),
                            game.get("imgUrl").toString(),
                            game.get("id").toString()
                        )
                        incomingGameList.add(incomingGame)
                    }

                    gameList.postValue(incomingGameList)
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents.", exception)
                }

        }

    }
}