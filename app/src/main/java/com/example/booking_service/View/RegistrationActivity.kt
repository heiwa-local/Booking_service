package com.example.booking_service.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.booking_service.R
import com.example.booking_service.databinding.ActivityLoginBinding
import com.example.booking_service.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegistration.setOnClickListener { View->
            val email = binding.editTextTextEmailAddress.text.toString()
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordConfirm = binding.editTextPasswordConfirm.text.toString()

            if (email.isNotEmpty() and name.isNotEmpty() and password.isNotEmpty() and passwordConfirm.isNotEmpty()){
                if (password == passwordConfirm){
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val ref: DatabaseReference = database.getReference("user")

                                ref.addValueEventListener(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        var position = 0
                                        snapshot.children.forEach {
                                            position += 1
                                        }
                                        database.getReference("user").child(position.toString()).child("email").setValue(email)
                                        database.getReference("user").child(position.toString()).child("name").setValue(name)
                                        ref.removeEventListener(this)
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })

                                Toast.makeText(baseContext, "Пользователь успешно создан",
                                    Toast.LENGTH_SHORT).show()
                                val intent: Intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(baseContext, "Ошибка при регистрации",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}