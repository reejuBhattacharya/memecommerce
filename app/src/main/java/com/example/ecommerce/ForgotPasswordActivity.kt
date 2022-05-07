package com.example.ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ecommerce.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    companion object {
        val TAG = "ForgotPasswordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.forgotpassButton.setOnClickListener {
            val email = binding.forgotpassEmailTextinputedittext.text.toString().trim()
            if(email.isEmpty()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Log.d(TAG, "Password Reset unsuccessful: ${it.exception}")
                        Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}