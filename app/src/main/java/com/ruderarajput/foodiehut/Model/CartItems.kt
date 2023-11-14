package com.ruderarajput.foodiehut.Model

data class CartItems
    (
    var foodName: String? = null,
    var foodPrice: String? = null,
    var foodDescription: String? = null,
    var foodIngredients: String? = null,
    var foodQuantity: Int? = null,
    var foodImage: String? = null
)