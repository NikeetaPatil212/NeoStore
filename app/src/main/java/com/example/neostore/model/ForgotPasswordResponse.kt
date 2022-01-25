package com.example.neostore.model

data class ForgotPasswordResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)