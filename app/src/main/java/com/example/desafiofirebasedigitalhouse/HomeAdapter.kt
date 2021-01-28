package com.example.desafiofirebasedigitalhouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HomeAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<HomeAdapter.GameViewHolder>() {

    var gameList = mutableListOf<Game>()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class GameViewHolder(
        itemView: View, listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val gameCover: ImageView = itemView.findViewById(R.id.game_item_cover_image)
        val gameName: TextView = itemView.findViewById(R.id.game_item_name_text)
        val gameRelease: TextView = itemView.findViewById(R.id.game_item_release_text)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item, parent, false)

        return GameViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val currentItem = gameList[position]

        holder.gameName.text = currentItem.name
        holder.gameRelease.text = currentItem.release

        val imgUrl = currentItem.imgUrl
        Picasso.get().load(imgUrl).fit().centerCrop().into(holder.gameCover)
    }

    override fun getItemCount() = gameList.size
}