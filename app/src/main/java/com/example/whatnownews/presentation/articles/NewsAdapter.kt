package com.example.whatnownews.presentation.articles

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.whatnownews.R
import com.example.whatnownews.databinding.ArticleListItemBinding
import com.example.whatnownews.domain.models.articles.ArticleModel
import com.example.whatnownews.domain.models.articles.FavoriteArticlesModel
import com.example.whatnownews.presentation.favorites.FavoritesViewModel

class NewsAdapter(
    private val activity: Activity,
    private val articles: ArrayList<ArticleModel>,
    private val favoritesViewModel: FavoritesViewModel
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val favoriteIds = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val b = ArticleListItemBinding.inflate(activity.layoutInflater, parent, false)
        return NewsViewHolder(b)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        val url = article.url

        holder.binding.articleText.text = article.title
        Glide.with(holder.binding.articleImage.context)
            .load(article.urlToImage)
            .error(R.drawable.ic_broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.articleImage)

        setFavoriteIcon(holder, favoriteIds.contains(url))

        holder.binding.articleContainer.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, url.toUri())
            activity.startActivity(i)
        }

        holder.binding.shareFab.setOnClickListener {
            ShareCompat.IntentBuilder(activity)
                .setType("text/plain")
                .setChooserTitle("Share this article")
                .setText(url)
                .startChooser()
        }

        holder.binding.favFab.setOnClickListener {
            val isNowFavorite = if (favoriteIds.contains(url)) {
                favoriteIds.remove(url)
                false
            } else {
                favoriteIds.add(url)
                true
            }

            setFavoriteIcon(holder, isNowFavorite)

            val favModel = FavoriteArticlesModel(
                id = article.url,
                title = article.title,
                url = article.url,
                urlToImage = article.urlToImage,
                isFavorite = isNowFavorite
            )

            favoritesViewModel.addFavorite(favModel)

            val message = if (isNowFavorite) "Added to Favorites!" else "Removed from Favorites!"
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = articles.size

    private fun setFavoriteIcon(holder: NewsViewHolder, isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_empty
        holder.binding.favFab.setImageResource(iconRes)
    }

    class NewsViewHolder(val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

