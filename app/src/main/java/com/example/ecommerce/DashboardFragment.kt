package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.adapters.DashboardAdapter
import com.example.ecommerce.adapters.ProductsAdapter
import com.example.ecommerce.databinding.FragmentDashboardBinding
import com.example.ecommerce.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject

class DashboardFragment : Fragment() {

    lateinit var binding : FragmentDashboardBinding
    lateinit var list : MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserProducts(){
        FirestoreClass().db.collection("products")
            .whereEqualTo("creatorUid", FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener { result ->
                if(!result.isEmpty) {
                    binding.dashboardTextView.visibility = View.GONE
                    for (document in result) {
                        val product = document.toObject<Product>()
                        list.add(product)
                    }
                    binding.dashboardRecyclerview.apply {
                        adapter!!.notifyDataSetChanged()
                        visibility = View.VISIBLE
                    }
                    Log.d(
                        "DashboardFragment",
                        "Successfully fetched product from database: ${list}"
                    )
                }
            }.addOnFailureListener {
                Log.d(FirestoreClass.TAG, "Failed to get list of products")
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = mutableListOf()
        binding.dashboardRecyclerview.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter =DashboardAdapter(list) { position -> onListItemClick(position) }
        }

        getUserProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.dashboard_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
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