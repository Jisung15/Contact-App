package com.example.contactapp.contact

data class ArticleModel (
    val name: String,
    val phoneNumber : String,
    val mail : String,
    val imageUrl: Int,
    var dHeartCheck: Boolean = false
)