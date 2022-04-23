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

const val PLAYLIST_EXTRA = "PLAYLIST_EXTRA"
class PlaylistAdapter(val context: Context, private val playlists: List<Playlist>) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_playlist,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        private val ivPlaylistThumbnail: ImageView = itemView.findViewById(R.id.ivPlaylistThumbnail)
        private val tvPlaylistAuthor: TextView = itemView.findViewById(R.id.tvPlaylistAuthor)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(playlist: Playlist) {
            tvPlaylistTitle.text = playlist.getName()
            tvPlaylistAuthor.text = playlist.getUser()?.username
            Glide.with(itemView.context).load(playlist.getImage()).into(ivPlaylistThumbnail)
        }

        override fun onClick(p0: View?) {
            val playlist = playlists[adapterPosition].getId()
            val intent = Intent(context,SongActivity::class.java)
            intent.putExtra(PLAYLIST_EXTRA,playlist)
            context.startActivity(intent)
        }
    }
}
