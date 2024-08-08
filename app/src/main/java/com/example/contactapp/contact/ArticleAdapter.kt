package com.example.contactapp.contact

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.ItemProfileActivity
import com.example.contactapp.R
import com.example.contactapp.databinding.GridItemArticleBinding
import com.example.contactapp.databinding.ListItemArticleBinding

class ArticleAdapter() :
    ListAdapter<ArticleModel, RecyclerView.ViewHolder>(diffUtil) {

    inner class GridViewHolder(private val binding: GridItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {
            binding.apply {
                binding.name.text = articleModel.name
                binding.profileImage.setImageResource(articleModel.imageUrl)
                binding.profileImage.setOnClickListener {
                    startProfileActivity(itemView, articleModel)
                }
            }
        }
    }

    inner class ListViewHolder(private val binding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {
            binding.apply {
                binding.name.text = articleModel.name
                binding.profileImage.setImageResource(articleModel.imageUrl)
                binding.like.setImageResource(R.drawable.heart_outlined)
                binding.profileImage.setOnClickListener {
                    startProfileActivity(itemView, articleModel)
                }
                binding.like.setOnClickListener {
                    articleModel.dHeartCheck = !articleModel.dHeartCheck
                    binding.like.setImageResource(
                        if (articleModel.dHeartCheck) R.drawable.heart_filled
                        else R.drawable.heart_outlined
                    )
                }
            }
        }
    }

    private fun startProfileActivity(itemView: View, articleModel: ArticleModel) {
        val context = itemView.context
        val intent = Intent(context, ItemProfileActivity::class.java).apply {
            putExtra("name", articleModel.name)
            putExtra("phoneNumber", articleModel.phoneNumber)
            putExtra("email", articleModel.mail)
            putExtra("profileImage", articleModel.imageUrl)
        }
        context.startActivity(intent)
    }

    private var layoutId = R.layout.list_item_article

    fun setLayoutId(newLayoutId: Int) {
        layoutId = newLayoutId
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutId == R.layout.list_item_article) LIST_VIEW else GRID_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LIST_VIEW->{
                val binding = ListItemArticleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ListViewHolder(binding)
            }
            GRID_VIEW->{
                val binding = GridItemArticleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                GridViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (holder) {
            is ListViewHolder -> holder.bind(item)
            is GridViewHolder -> holder.bind(item)
        }
    }

    companion object {
        const val LIST_VIEW = 1
        const val GRID_VIEW = 2

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
