package com.example.bopshareapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SongAdapter(val context: Context, private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_song,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvSongTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        private val ivAlbumCover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
        private val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(song: Song) {
            tvSongTitle.text = song.getTitle()
            tvArtist.text = song.getArtist()
            Glide.with(itemView.context).load(song.getImage()).into(ivAlbumCover)
        }

        override fun onClick(p0: View?) {
            val song = songs[adapterPosition].getId()
            // change the color of the song to show that it is playing

            // start playing the song

        }
    }
}