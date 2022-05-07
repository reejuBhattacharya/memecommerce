package com.example.ecommerce

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.adapters.ProductsAdapter
import com.example.ecommerce.databinding.FragmentProductsBinding
import com.example.ecommerce.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject

class ProductsFragment : Fragment() {

    companion object {
        private val TAG  = "ProductsFragment"
    }

    private lateinit var binding : FragmentProductsBinding
    private lateinit var list : MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    private fun getProducts(){
        FirestoreClass().db.collection("products")
            .whereNotEqualTo("creatorUid", FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener { result ->
                if(!result.isEmpty) {
                    binding.productsTextView.visibility = View.GONE
                    for (document in result) {
                        val product = document.toObject<Product>()
                        list.add(product)
                    }
                    binding.productsRecyclerview.apply {
                        adapter!!.notifyDataSetChanged()
                        visibility = View.VISIBLE
                    }
                    Log.d(TAG, "Successfully fetched product from database: $list")
                }
            }.addOnFailureListener {
                Log.d(TAG, "Failed to get list of products")
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = mutableListOf()
        binding.productsRecyclerview.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = ProductsAdapter(list) { position -> onListItemClick(position) }
        }
        getProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_addproduct, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onListItemClick(position: Int) {
        val product = list[position]
        val intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra("Product", product)
    }
}