package com.example.ecommerce

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.ecommerce.databinding.ActivityRegisterBinding
import com.example.ecommerce.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide();

        binding.registerGotologinTextview.setOnClickListener {
            // go to the login screen which is present in backstack
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            if(validateInput()) {
                binding.progressContainer.visibility = View.VISIBLE
                binding.registerLayout.setBackgroundColor(Color.parseColor("#C0C0C0"))
                Log.d(TAG, "Validation successful")
                registerUser()
            }
            else {
                Log.d(TAG, "User not valid")
                return@setOnClickListener
            }
        }
    }

    private fun validateInput(): Boolean {
        val firstName = binding.registerFirstnameTextinputedittext.text.toString().trim()
        val lastName = binding.registerLastnameTextinputedittext.text.toString().trim()
        val email = binding.registerEmailTextinputedittext.text.toString().trim()
        val password = binding.registerPasswordTextinputedittext.text.toString().trim()
        val confirmPassword = binding.registerConfirmpasswordTextinputedittext.text.toString().trim()
        val tnc = binding.registerTandcCheckbox.isChecked
        if (firstName.isEmpty()){
            showSnackBar("First Name")
            return false
        } else if (lastName.isEmpty()) {
            showSnackBar("First Name")
            return false
        } else if (email.isEmpty()) {
            showSnackBar("Email ID")
            return false
        } else if (password.isEmpty()) {
            showSnackBar("Password")
            return false
        } else if (confirmPassword.isEmpty()) {
            showSnackBar("Confirm Password")
            return false
        } else if(password != confirmPassword) {
            val text = "Password does not match with Confirm Password"
            Snackbar.make(binding.registerLayout, text, Snackbar.LENGTH_SHORT).show()
            return false
        } else if(!tnc) {
            val text = "Please agree to the Terms and Conditions to proceed"
            Snackbar.make(binding.registerLayout, text, Snackbar.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun registerUser() {
        val email = binding.registerEmailTextinputedittext.text.toString().trim()
        val password = binding.registerPasswordTextinputedittext.text.toString().trim()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    val uid = FirebaseAuth.getInstance().uid ?: return@addOnCompleteListener
                    val user = User(
                        uid,
                        binding.registerFirstnameTextinputedittext.text.toString().trim(),
                        binding.registerLastnameTextinputedittext.text.toString().trim(),
                        binding.registerEmailTextinputedittext.text.toString().trim()
                    )
                    FirestoreClass().registerUser(user)
                    Log.d(TAG, "User Registered Successfully")
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.d(TAG, "Registration failed: ${it.exception}")
                    Toast.makeText(this, "Unsuccessful. Please try again", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.progressContainer.visibility = View.GONE
                binding.registerLayout.setBackgroundColor(Color.WHITE)
            }
    }

    private fun showSnackBar(field: String) {
        val text = "Please enter a valid $field"
        Snackbar.make(binding.registerLayout, text, Snackbar.LENGTH_SHORT).show()
    }
}