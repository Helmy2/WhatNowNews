package com.example.whatnownews.presentation.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatnownews.R
import com.example.whatnownews.domain.model.FavoriteArticlesModel

class FavoriteArticlesAdapter(
    private var items: List<FavoriteArticlesModel> = emptyList(),
    private val onFavoriteClick: (FavoriteArticlesModel) -> Unit = {},
    private val onArticleClick: (FavoriteArticlesModel) -> Unit = {}
) : RecyclerView.Adapter<FavoriteArticlesAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.article_text)
        val hero: ImageView = view.findViewById(R.id.article_image)
        val fav: ImageView = view.findViewById(R.id.fav_fab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_list_item, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val article = items[position]
        holder.title.text = article.title

        Glide.with(holder.hero.context)
            .load(article.imageUrl.ifEmpty { null })
            .into(holder.hero)

        holder.fav.setImageResource(
            if (article.isFavorite) R.drawable.ic_favorite_filled
            else R.drawable.ic_favorite_empty
        )

        holder.fav.setOnClickListener {
            onFavoriteClick(article)
        }

        //TODO: Handle article click for navigation
        holder.itemView.setOnClickListener {
            onArticleClick(article)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<FavoriteArticlesModel>) {
        items = newList
        notifyDataSetChanged()
    }
}
