package com.example.contactapp.contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.R
import com.example.contactapp.databinding.FragmentContactBinding

class ContactFragment : Fragment(R.layout.fragment_contact) {

    private var binding: FragmentContactBinding? = null
    private lateinit var articleAdapter: ArticleAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentContactBinding = FragmentContactBinding.bind(view)
        binding = fragmentContactBinding

        articleAdapter = ArticleAdapter()

        fragmentContactBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentContactBinding.articleRecyclerView.adapter = articleAdapter
    }


}



