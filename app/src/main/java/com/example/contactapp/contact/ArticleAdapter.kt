package com.example.contactapp.contact

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.contactapp.ItemProfileActivity
import com.example.contactapp.R
import com.example.contactapp.databinding.GridItemArticleBinding
import com.example.contactapp.databinding.ListItemArticleBinding

class ArticleAdapter(
    private var layoutId: Int,
    private val callBack: (ArticleModel) -> Unit
) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleModel: ArticleModel) {
            when (binding) {
                is GridItemArticleBinding -> {
                    binding.name.text = articleModel.name
                    binding.profileImage.setImageResource(articleModel.imageUrl)

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
                }
                is ListItemArticleBinding -> {
                    binding.name.text = articleModel.name
                    binding.profileImage.setImageResource(articleModel.imageUrl)
                    binding.like.setImageResource(
                        if (articleModel.dHeartCheck) R.drawable.heart_filled
                        else R.drawable.heart_outlined
                    )

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
                        binding.like.setImageResource(
                            if (articleModel.dHeartCheck) R.drawable.heart_filled
                            else R.drawable.heart_outlined
                        )
                        callBack(articleModel)
                    }
                }
                updateSortedList()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            R.layout.grid_item_article -> GridItemArticleBinding.inflate(inflater, parent, false)
            R.layout.list_item_article -> ListItemArticleBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid layout resource ID")
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun updateSortedList() {
        // 좋아요 상태에 따라 목록을 정렬
        val sortedList = currentList.sortedByDescending { it.dHeartCheck }
        submitList(sortedList)
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

    fun updateLayout(newLayoutId: Int) {
        this.layoutId = newLayoutId
        notifyDataSetChanged()
    }
}
