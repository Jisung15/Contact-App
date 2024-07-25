package com.example.contactapp.contact

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.R
import com.example.contactapp.databinding.AddContactDialogBinding
import com.example.contactapp.databinding.FragmentContactBinding

class ContactFragment : Fragment(R.layout.fragment_contact), ItemTouchHelperListener {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArticleAdapter
    private var itemList = mutableListOf<ArticleModel>()
    private val CALL = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        // 연락처 더미 데이터
        itemList = listOf(
            ArticleModel("지민", "010-1234-1234", "spara@gmail.com", R.drawable.jimin),
            ArticleModel("장원영", "010-1234-1243", "sparta@gmail.com", R.drawable.wonyoung),
            ArticleModel("성한빈", "010-1234-2134", "sparta@gmail.com", R.drawable.hanbin),
            ArticleModel("사쿠라", "010-1234-2143", "sparta@gmail.com", R.drawable.sakura),
            ArticleModel("수빈", "010-1234-5678", "sparta@gmail.com", R.drawable.soobin),
            ArticleModel("카리나", "010-1234-5687", "sparta@gmail.com", R.drawable.karina),
            ArticleModel("민지", "010-1234-6587", "sparta@gmail.com", R.drawable.minji)
        ) as MutableList<ArticleModel>

        // RecyclerView 어댑터 설정
        adapter = ArticleAdapter(R.layout.list_item_article)
        adapter.submitList(itemList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articleRecyclerView.adapter = adapter

        binding.ivMenu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.dropdown_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_item1 -> {
                        adapter.updateLayout(R.layout.grid_item_article)
                        binding.articleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
                        true
                    }
                    R.id.action_item2 -> {
                        adapter.updateLayout(R.layout.list_item_article)
                        binding.articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }


        // 연락처 드래그 동작 정의
        val itemTouchHelperCallBack = ItemTouchHelperCallBack(this)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.articleRecyclerView)

        // 연락처 추가 버튼 눌렀을 때
        binding.addFloatingButton.setOnClickListener {

            // 연락처 추가 다이얼로그 생성
            val builder = AlertDialog.Builder(requireContext())
            val binding = AddContactDialogBinding.inflate(LayoutInflater.from(requireContext()))
            builder.setView(binding.root)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            dialog.window?.setLayout(
                (requireContext().resources.displayMetrics.widthPixels * 0.9).toInt(),
                LayoutParams.WRAP_CONTENT
            )

            // 취소 누르면 다이얼로그 종료
            binding.finishButton.setOnClickListener {
                dialog.dismiss()
            }

            // 등록 버튼 누르면 연락처 추가
            val addButton = binding.submitButton
            addButton.setOnClickListener {
                val name = binding.addNameEditText.text.toString()
                val phoneNumber = binding.addPhoneNumberEditText.text.toString()
                val email = binding.addMailEditText.text.toString()
                val image = R.drawable.primary_profile

                // 유효성 검사 구문
                if (name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty()) {
                    val newItem = ArticleModel(name, phoneNumber, email, image)
                    val updatedList = mutableListOf(newItem)
                    updatedList.addAll(adapter.currentList)
                    adapter.submitList(updatedList)
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "모든 내용을 입력해야 연락처 추가가 가능합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    // 연락처 드래그 시 권한 허용 여부에 따른 동작 정의
    override fun onItemSwipe(position: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: ${itemList[position].phoneNumber}"))
            startActivity(intent)
            adapter.notifyDataSetChanged()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE), CALL)
            adapter.notifyDataSetChanged()
        }
    }

    // 권한 요청
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "권한이 허용되지 않았습니다. 권한을 허용해야 전화를 걸 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
