package com.example.contactapp.contact

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("최봉준","01058442237","dacafo77@nate.com",R.drawable.profile1))
        })

        fragmentContactBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentContactBinding.articleRecyclerView.adapter = articleAdapter
        fragmentContactBinding.addFloatingButton.setOnClickListener{
            val intent= Intent(requireContext(),AddContactActivity::class.java )
            startActivity(intent)
        }
        fragmentContactBinding.addFloatingButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.addcontact, null)


        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}