package com.example.neostore.model

data class FetchAccountDetail(
    val `data`: AccountDetail,
    val status: Int
)

data class AccountDetail(
    val product_categories: List<ProductCategory>,
    val total_carts: Int,
    val total_orders: Int,
    val user_data: Data
)

data class ProductCategory(
    val created: String,
    val icon_image: String,
    val id: Int,
    val modified: String,
    val name: String
)
