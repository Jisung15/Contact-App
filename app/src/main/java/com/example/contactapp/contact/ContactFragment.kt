package com.example.contactapp.contact

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.ItemTouchHelperCallBack
import com.example.contactapp.ItemTouchHelperListener
import com.example.contactapp.R
import com.example.contactapp.databinding.FragmentContactBinding

class ContactFragment : Fragment(R.layout.fragment_contact), ItemTouchHelperListener {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    private val CALL = 1
    private lateinit var adapter: ArticleAdapter
    private var itemList = mutableListOf<ArticleModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        itemList = listOf(
            ArticleModel("지민", "010-1234-5678", "spara@gmail.com", R.drawable.jimin),
            ArticleModel("장원영","010-1234-1234", "sparta@gmail.com", R.drawable.wonyoung),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("사쿠라","010-1234-1234", "sparta@gmail.com", R.drawable.sakura),
            ArticleModel("수빈","010-1234-5678", "sparta@gmail.com", R.drawable.soobin),
            ArticleModel("카리나","010-1234-1234", "sparta@gmail.com", R.drawable.karina),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("민지","010-1234-1234", "sparta@gmail.com", R.drawable.minji),
            ArticleModel("지민","010-1234-5678", "sparta@gmail.com", R.drawable.jimin),
            ArticleModel("장원영","010-1234-1234", "sparta@gmail.com", R.drawable.wonyoung),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("사쿠라","010-1234-1234", "sparta@gmail.com", R.drawable.sakura),
            ArticleModel("수빈","010-1234-5678", "sparta@gmail.com", R.drawable.soobin),
            ArticleModel("카리나","010-1234-1234", "sparta@gmail.com", R.drawable.karina),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("민지","010-1234-1234", "sparta@gmail.com", R.drawable.minji),
            ArticleModel("지민","010-1234-5678", "sparta@gmail.com", R.drawable.jimin),
            ArticleModel("장원영","010-1234-1234", "sparta@gmail.com", R.drawable.wonyoung),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("사쿠라","010-1234-1234", "sparta@gmail.com", R.drawable.sakura),
            ArticleModel("수빈","010-1234-5678", "sparta@gmail.com", R.drawable.soobin),
            ArticleModel("카리나","010-1234-1234", "sparta@gmail.com", R.drawable.karina),
            ArticleModel("성한빈","010-1234-5678", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("민지","010-1234-1234", "sparta@gmail.com", R.drawable.minji)
        ) as MutableList<ArticleModel>

        adapter = ArticleAdapter(this)
        adapter.submitList(itemList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articleRecyclerView.adapter = adapter

        binding.articleRecyclerView.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL))

        val itemTouchHelperCallBack = ItemTouchHelperCallBack(this)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.articleRecyclerView)

        return binding.root
    }

    override fun onItemSwipe(position: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: ${itemList[position].phoneNumber}"))
            startActivity(intent)
            adapter.notifyItemChanged(position)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE), CALL)
            adapter.notifyItemChanged(position)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "권한이 허용되지 않았습니다. 권한을 허용해야 전화를 걸 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //    private lateinit var articleAdapter: ArticleAdapter
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val fragmentContactBinding = FragmentContactBinding.bind(view)
//        binding = fragmentContactBinding
//
//        articleAdapter = ArticleAdapter()
//        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
//            add(ArticleModel("최봉준","01058442237","dacafo77@nate.com",R.drawable.profile1))
//        })
//
//        fragmentContactBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
//        fragmentContactBinding.articleRecyclerView.adapter = articleAdapter
//        fragmentContactBinding.addFloatingButton.setOnClickListener{
//            val intent= Intent(requireContext(),AddContactActivity::class.java )
//            startActivity(intent)
//        }
//        fragmentContactBinding.addFloatingButton.setOnClickListener {
//            showDialog()
//        }
//    }
//
//    private fun showDialog() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.addcontact, null)
//        val editText = dialogView.findViewById<EditText>(R.id.addNameEditText)
//
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setView(dialogView)
//        builder.create().show()
//    }
//



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}