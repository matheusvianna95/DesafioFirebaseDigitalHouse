package com.example.desafiofirebasedigitalhouse

import java.io.Serializable

data class Game (
    var name: String = "",
    var release: String = "",
    var description: String = "",
    var imgUrl: String = "",
    var id: String = ""
) : Serializable