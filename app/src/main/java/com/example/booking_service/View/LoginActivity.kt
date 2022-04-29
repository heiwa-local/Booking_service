package com.example.booking_service.View

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.booking_service.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextViewEmail: EditText
    private lateinit var editTextViewPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.buttonLogin.setOnClickListener { View ->
            if (binding.editTextViewEmail.text.toString().isNotEmpty() and binding.editTextViewPassword.text.toString().isNotEmpty()) {
                val email = binding.editTextViewEmail.text.toString()
                val password = binding.editTextViewPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            if (user != null) {
                                reload(user)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else{
                Toast.makeText(
                    baseContext, "Заполните все поля",
                    Toast.LENGTH_SHORT)
            }
        }
        binding.textViewRegister.setOnClickListener { View->
            val intent: Intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload(currentUser);
        }
    }

    private fun reload(currentUser: FirebaseUser) {
        val bundle: Bundle = Bundle()
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email",currentUser.email.toString())
        startActivity(intent)
    }
}