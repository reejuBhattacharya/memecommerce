package com.example.ecommerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.models.Product
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val dataset: MutableList<Product>,
    private val onItemClicked: (position: Int) -> Unit
    )
    : RecyclerView.Adapter<ProductsAdapter.ViewHolder>()
{
    class ViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val imageView = view.findViewById<ImageView>(R.id.product_imageview)
        val nameTextView = view.findViewById<TextView>(R.id.product_name)
        val priceTextView = view.findViewById<TextView>(R.id.product_price)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataset[position]
        holder.apply {
            nameTextView.text = product.name
            priceTextView.text = product.price
        }
        Picasso.get().load(product.photoUri).into(holder.imageView)
    }

    override fun getItemCount() = dataset.size
}