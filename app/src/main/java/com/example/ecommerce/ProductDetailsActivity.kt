package com.example.ecommerce

import android.icu.text.NumberFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecommerce.databinding.ActivityProductDetailsBinding
import com.example.ecommerce.models.Product
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        val product: Product = intent.getSerializableExtra("Product") as Product

        binding.apply {
            Picasso.get().load(product.photoUri).into(productDetailsImageview)
            productDetailsName.text = product.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productDetailsPrice.text = NumberFormat.getInstance().format(product.price)
            } else {
                productDetailsPrice.text = product.price.toString().plus(".00")
            }
            productDetailsDescription.text = product.desc
            productDetailsQuantity.text = product.quantity.toString()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
    }
}