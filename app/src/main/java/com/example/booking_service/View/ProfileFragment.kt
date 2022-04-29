package com.example.booking_service.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.booking_service.R
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class ProfileFragment : Fragment() {
    private lateinit var buttonSignOut: Button
    private lateinit var textViewName: TextView
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var viewModel: MainActivityViewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSignOut = view.findViewById(R.id.buttonSignOut)
        textViewName = view.findViewById(R.id.textViewName)
//        textViewName.text =
        viewModel.findUser(Firebase.auth.currentUser?.email.toString()).observe(this, Observer {
            val ref2: DatabaseReference = database.getReference()
            ref2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    textViewName.text = snapshot.child("user").child(it.toString()).child("name").getValue().toString()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        })

        buttonSignOut.setOnClickListener { View->
            Firebase.auth.signOut()
            val intent: Intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}