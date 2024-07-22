package com.example.contactapp.contact

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter: ListAdapter<ArticleAdapter,ArticleAdapter.ViewHolder> (diffUtil){
    inner class ViewHoldr(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root){

    }
}