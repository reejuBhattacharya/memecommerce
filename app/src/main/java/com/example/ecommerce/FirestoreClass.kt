package com.example.ecommerce

import android.net.Uri
import android.util.Log
import com.example.ecommerce.models.Product
import com.example.ecommerce.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirestoreClass {

    companion object {
        val TAG = "FireStoreClass"
    }

    val db = Firebase.firestore

    fun registerUser(user: User) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        db.collection("users")
            .document(uid)
            .set(user, SetOptions.merge())
            .addOnCompleteListener {
                if(it.isSuccessful) Log.d("RegisterActivity", "User Data stored")
                else Log.d("RegisterActivity", "Failed to store user data")
            }
    }

    fun getUser() : User? {
        val uid = FirebaseAuth.getInstance().uid ?: return User()
        var user: User? = null
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it != null) {
                    user = it.toObject<User>()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to get user details")
            }
        return user
    }

    fun uploadProduct(imageUri: Uri?, product: Product) {
        if(imageUri==null)   return

        Log.d(TAG, "inside uploadImage function")

        val uid = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("product_images/$uid")
        ref.putFile(imageUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "successfully received download uri: $it")
                product.photoUri = it.toString()
                FirestoreClass().addProduct(product)
            }.addOnFailureListener {
                Log.d(TAG, "Could not get download link to profile image: ${it.message}")
            }
        }.addOnFailureListener {
            Log.d(TAG, "Could not upload image to storage: ${it.message}")
        }
    }

    private fun addProduct(product: Product) {
        db.collection("products")
            .document(product.uid)
            .set(product)
            .addOnCompleteListener {
                if(it.isSuccessful) Log.d(TAG, "Product uploaded successfully")
                else Log.d(TAG, "Product upload unsuccessful: ${it.exception}")
            }
    }

    fun getProducts() : MutableList<Product>{
        val list = mutableListOf<Product>()
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    val product = document.toObject<Product>()
                    list.add(product)
                }
            }.addOnFailureListener {
                Log.d(TAG, "Failed to get list of products")
            }
        return list
    }

    fun getUserProducts() : MutableList<Product> {
        val list = mutableListOf<Product>()
        db.collection("products")
            .whereEqualTo("creatorUid", FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    val product = document.toObject<Product>()
                    list.add(product)
                }
            }.addOnFailureListener {
                Log.d(TAG, "Failed to get list of products")
            }
        return list
    }

    fun deleteProduct(uid: String) {
        db.collection("products")
            .document(uid)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "Document successfully deleted")}
            .addOnFailureListener { Log.d(TAG, "Document deletion failed: ${it.message}") }
    }

    fun uploadCartItem(product: Product) {}
}