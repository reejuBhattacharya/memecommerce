package com.example.ecommerce.models

import android.net.Uri
import java.io.Serializable

data class Product(
    var uid : String = "",
    var creatorUid: String = "",
    var name: String = "",
    var price: String = "",
    var desc: String = "",
    var quantity: String = "",
    var photoUri: String = ""
) : Serializable


