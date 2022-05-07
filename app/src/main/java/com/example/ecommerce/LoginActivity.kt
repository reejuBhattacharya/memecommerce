package com.example.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.example.ecommerce.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide();

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            loginButton.setOnClickListener(this@LoginActivity)
            loginForgotpasswordTextview.setOnClickListener(this@LoginActivity)
            loginSignupTextview.setOnClickListener(this@LoginActivity)
        }
//
//        binding.apply {
//            loginSignupTextview.setOnClickListener(this@LoginActivity)
//            loginForgotpasswordTextview.setOnClickListener(this@LoginActivity)
//            loginButton.setOnClickListener(this@LoginActivity)
//        }
    }

    override fun onClick(view: View?) {
        if(view==null)  return

        when(view.id) {
            R.id.login_forgotpassword_textview -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }

            R.id.login_signup_textview -> {
                Toast.makeText(this, "CLicked here", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.login_button -> {
                Log.d(TAG, "Login button pressed")
                if(validateInput()) {
                    login()
                }
                else return
            }
        }
    }

    private fun login() {
        val email = binding.loginEmailTextinputedittext.text.toString().trim()
        val password = binding.loginPasswordTextinputedittext.text.toString().trim()

        Log.d(TAG, "In login function")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    Log.d(TAG, "Signed in successfully")
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Log.d(TAG, "Failed to log in: ${it.exception}")
                    return@addOnCompleteListener
                }
            }
    }

    private fun validateInput() : Boolean{
        val email = binding.loginEmailTextinputedittext.text.toString().trim()
        val password = binding.loginPasswordTextinputedittext.text.toString().trim()
        return if(email.isEmpty()) {
            showSnackBar("Email")
            false
        } else if(password.isEmpty()) {
            showSnackBar("Password")
            false
        } else {
            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Password: $password")
            true
        }
    }

    private fun showSnackBar(field: String) {
        val text = "Input in $field is not valid"
        Snackbar.make(binding.loginLayout, text, Snackbar.LENGTH_SHORT).show()
    }
}