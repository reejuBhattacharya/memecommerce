package com.example.ecommerce

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ecommerce.databinding.ActivityAddProductBinding
import com.example.ecommerce.models.Product
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import java.util.jar.Manifest

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private var imgUri: Uri? = null

    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var quantity: String
    private lateinit var price: String

    companion object {
        val IMG_PICK_CODE = 1000
        val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddProductBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Product"
        }

        pickImage()
        binding.addproductButton.setOnClickListener {
            addProductToDatabase()
        }
    }

    private fun pickImage() {
        binding.addproductAddimageLayout.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    // ask for permission
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else pickImageFromGallery()
            } else pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMG_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode== PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
            else {
                Toast.makeText(this,
                    "Please give permission to pick image from gallery",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMG_PICK_CODE) {
            binding.apply {
                addproductAddimageIcon.visibility = View.GONE
                addproductAddimageImageview.setImageURI(data?.data)
                imgUri = data?.data
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun validateInput() : Boolean {

        title = binding.addproductTitleTextinputedittext.text.toString().trim()
        desc = binding.addproductDescriptionTextinputedittext.text.toString().trim()
        quantity = binding.addproductQuantityTextinputedittext.text.toString().trim()
        price = binding.addproductPriceTextinputedittext.text.toString().trim()

        return when {
            title.isEmpty() -> {
                showSnackBar("Title")
                false
            }
            desc.isEmpty() -> {
                showSnackBar("Description")
                false
            }
            quantity.toInt()<=0 -> {
                showSnackBar("Quantity")
                false
            }
            price.toInt()<=0 -> {
                showSnackBar("Price")
                false
            }
            else -> true
        }
    }

    fun addProductToDatabase() {
        if(!validateInput())    return

        if(imgUri===null) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show()
            return
        }
        val uid = UUID.randomUUID().toString()
        val product = Product(
            uid = uid,
            creatorUid = FirebaseAuth.getInstance().uid!!,
            name = title,
            price = price,
            desc = desc,
            quantity = quantity,
            )
        FirestoreClass().uploadProduct(imgUri, product)
    }

    private fun showSnackBar(field: String) {
        val text = "Input in $field is not valid"
        Snackbar.make(binding.addproductLayout, text, Snackbar.LENGTH_SHORT).show()
    }


}