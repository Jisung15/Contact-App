package com.example.contactapp.contact

data class ArticleModel(
    val name: String,
    val phoneNumber: String,
    val mail: String,
    val imageUrl: Int,
    val dHeartCheck: Boolean = false      // 이거 val로 해 주어야 한다고 하십니다.
)