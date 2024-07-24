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
            binding.like.setImageResource(R.drawable.heart)

            // profileImage 라운딩 처리
            binding.profileImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, 35f)
                }
            }
            binding.profileImage.clipToOutline = true

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

            // like 이미지 변경
            if (articleModel.dHeartCheck) {
                binding.like.setImageResource(R.drawable.fulllike_icon)
            } else {
                binding.like.setImageResource(R.drawable.heart)
            }
            binding.like.setOnClickListener {
                articleModel.dHeartCheck = !articleModel.dHeartCheck
                if (articleModel.dHeartCheck) {
                    binding.like.setImageResource(R.drawable.fulllike_icon)
                } else {
                    binding.like.setImageResource(R.drawable.heart)
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
