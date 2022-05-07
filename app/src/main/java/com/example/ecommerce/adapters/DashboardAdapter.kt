package com.example.ecommerce.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.FirestoreClass
import com.example.ecommerce.R
import com.example.ecommerce.models.Product
import com.squareup.picasso.Picasso

class DashboardAdapter(
    private val dataset: MutableList<Product>,
    private val onItemClicked: (position: Int) -> Unit
    )
    : RecyclerView.Adapter<DashboardAdapter.ViewHolder>()
{
    class ViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        val imageView = view.findViewById<ImageView>(R.id.user_product_imageview)
        val nameTextView = view.findViewById<TextView>(R.id.user_product_name)
        val priceTextView = view.findViewById<TextView>(R.id.user_product_price)
        val deleteImageView = view.findViewById<ImageView>(R.id.user_product_delete_imageview)

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
            .inflate(R.layout.item_user_product, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataset[position]
        holder.apply {
            nameTextView.text = product.name
            priceTextView.text = product.price
            deleteImageView.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        FirestoreClass().deleteProduct(product.uid)
                        dataset.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(it.context, "Item successfully deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { _, _ ->
                        Toast.makeText(it.context, "Deletion abandoned", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        Picasso.get().load(product.photoUri).into(holder.imageView)
    }

    override fun getItemCount() = dataset.size
}