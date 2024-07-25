package com.example.contactapp.contact

import android.content.Intent
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.ItemProfileActivity
import com.example.contactapp.R
import com.example.contactapp.databinding.ItemArticleBinding

class ArticleAdapter(private val listener: ItemTouchHelperListener) :
    ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleModel: ArticleModel) {
            binding.name.text = articleModel.name
            binding.profileImage.setImageResource(articleModel.imageUrl)
            binding.like.setImageResource(R.drawable.heart_outlined)

            binding.profileImage.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ItemProfileActivity::class.java).apply {
                    putExtra("name", articleModel.name)
                    putExtra("phoneNumber", articleModel.phoneNumber)
                    putExtra("email", articleModel.mail)
                    putExtra("profileImage", articleModel.imageUrl)
                }
                context.startActivity(intent)
            }

            binding.like.setOnClickListener {
                articleModel.dHeartCheck = !articleModel.dHeartCheck
                if (articleModel.dHeartCheck) {
                    binding.like.setImageResource(R.drawable.heart_filled)
                } else {
                    binding.like.setImageResource(R.drawable.heart_outlined)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.phoneNumber == newItem.phoneNumber
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
