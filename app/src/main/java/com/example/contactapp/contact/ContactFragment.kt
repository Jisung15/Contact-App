package com.example.contactapp.contact

import android.Manifest
import android.app.ActionBar.LayoutParams
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.MainActivity
import com.example.contactapp.R
import com.example.contactapp.databinding.AddContactDialogBinding
import com.example.contactapp.databinding.FragmentContactBinding

class ContactFragment : Fragment(R.layout.fragment_contact), ItemTouchHelperListener {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArticleAdapter
    private var itemList = mutableListOf<ArticleModel>()
    private val CALL = 1
    private val REQUEST_CODE_NOTIFICATION = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        dummyData()
        recyclerViewAdapter()
        viewTypeChange()
        itemTouchHelperCallBack()
        addContacts()

        return binding.root
    }

    private fun dummyData() {
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
    }

    private fun recyclerViewAdapter() {
        // RecyclerView 어댑터 설정
        adapter = ArticleAdapter(R.layout.list_item_article, this::updateItems)
        adapter.submitList(itemList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articleRecyclerView.adapter = adapter
    }

    private fun itemTouchHelperCallBack() {
        // 연락처 드래그 동작 정의
        val itemTouchHelperCallBack = ItemTouchHelperCallBack(this)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.articleRecyclerView)
    }

    private fun addContacts() {
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
                val event = binding.addEventEditText

                // 유효성 검사 구문
                if (name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty()) {
                    val newItem = ArticleModel(name, phoneNumber, email, image)
                    val updatedList = mutableListOf(newItem)
                    updatedList.addAll(adapter.currentList)
                    adapter.submitList(updatedList)
                    dialog.dismiss()

                    // 알림을 울리게 하는 시간을 이벤트 EditText에 입력한 숫자에 따라 결정
                    val delayMillis: Long
                    when (event.text.toString().toInt()) {
                        5 -> {
                            delayMillis = 5000L
                            Toast.makeText(requireContext(), "5초 뒤에 알림이 울립니다.", Toast.LENGTH_SHORT).show()
                        }
                        10 -> {
                            delayMillis = 10000L
                            Toast.makeText(requireContext(), "10초 뒤에 알림이 울립니다.", Toast.LENGTH_SHORT).show()
                        }
                        30 -> {
                            delayMillis = 30000L
                            Toast.makeText(requireContext(), "30초 뒤에 알림이 울립니다.", Toast.LENGTH_SHORT).show()
                        }
                        0 -> {
                            delayMillis = 0L
                            Toast.makeText(requireContext(), "알림이 바로 울립니다.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), "잘못된 입력입니다.", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    // 그리고 그 시간에 맞춰서 알림 설정
                    Handler(Looper.getMainLooper()).postDelayed({
                        showNotification()
                    }, delayMillis)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "모든 내용을 입력해야 연락처 추가가 가능합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // ViewType을 list나 Grid로 자유롭게 바꿀 수 있도록 설정
    private fun viewTypeChange() {
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
    }

    // 알림 채널 생성하고 알림 실제로 울리게 하는 부분
    // 권한을 허용해야만 울리게 설정
    private fun showNotification() {
        val channelId = "Alarm_Channel"
        val channelName = "Contact Notifications"

        // 일정 버전 이상에서 권한이 허용되지 않으면 요청 창을 띄워 권한 허용을 할 것인지 선택하도록 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        // 알림 채널 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for contact notifications"
            }

            notificationManager.createNotificationChannel(channel)

            // Pending Intent 설정 파트
            val intent = Intent(requireContext(), MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("연락처 알림")
                .setContentText("연락을 할 시간입니다!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(1, builder.build())
        }
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

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "권한이 허용되지 않았습니다. 권한을 허용해야 전화를 걸 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "알림 권한이 허용되지 않았습니다. 권한을 허용해야 알림을 받을 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //정렬 메서드
    fun updateItems(updatedItem: ArticleModel) {
        val updatedList = adapter.currentList.toMutableList().apply {
            remove(updatedItem)
            if (updatedItem.dHeartCheck) {
                add(0, updatedItem)
            } else {
                add(updatedItem)
            }
        }
        adapter.submitList(updatedList)
    }

    // Fragment에는 이거 추가 필수..
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
