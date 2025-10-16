package com.example.whatnownews.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatnownews.R
import com.example.whatnownews.domain.models.articles.FavoriteArticlesModel
import com.example.whatnownews.databinding.ArticleListItemBinding

class FavoriteArticlesAdapter(
    private var items: List<FavoriteArticlesModel> = emptyList(),
    private val onFavoriteClick: (FavoriteArticlesModel) -> Unit = {},
    private val onShareClick: (FavoriteArticlesModel) -> Unit = {},
    private val onArticleClick: (FavoriteArticlesModel) -> Unit = {}
) : RecyclerView.Adapter<FavoriteArticlesAdapter.VH>() {

    inner class VH(val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val article = items[position]

        holder.binding.articleText.text = article.title

        Glide.with(holder.binding.articleImage.context)
            .load(article.urlToImage.ifEmpty { null })
            .into(holder.binding.articleImage)

        holder.binding.favFab.setImageResource(
            if (article.isFavorite) R.drawable.ic_favorite_filled
            else R.drawable.ic_favorite_empty
        )

        holder.binding.favFab.setOnClickListener { onFavoriteClick(article) }

        holder.binding.shareFab.setOnClickListener {
            onShareClick(article)
        }

        holder.binding.articleContainer.setOnClickListener {
            onArticleClick(article)
        }

        holder.itemView.setOnClickListener { onArticleClick(article) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<FavoriteArticlesModel>) {
        items = newList
        notifyDataSetChanged()
    }
}
