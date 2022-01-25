package com.example.neostore.model

data class LoginErrorResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)