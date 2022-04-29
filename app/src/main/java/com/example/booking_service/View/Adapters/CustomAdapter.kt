package com.example.booking_service.View.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.View.HotelInfoFragment
import com.example.booking_service.View.MainMenuFragment
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.database.*
import com.google.firebase.inject.Deferred
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.net.URL
import android.R
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatViewInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import java.security.AccessController.getContext


class CustomAdapter(private val listOfHotels: MutableList<String>):
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hotelName: TextView = itemView.findViewById(com.example.booking_service.R.id.hotelName)
        val imageView: ImageView = itemView.findViewById(com.example.booking_service.R.id.imageView)
//        val hotelDescription: TextView = itemView.findViewById(R.id.hotelDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(com.example.booking_service.R.layout.recyclerview_item_hotels, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val ref: DatabaseReference = database.getReference("hotel").child(listOfHotels.get(position)) // Key

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.hotelName.text = snapshot.child("name").getValue().toString() +" "+ snapshot.child("stars").getValue().toString() +"*"
                var url: String = snapshot.child("image").child("0").getValue().toString()
                Picasso.with(holder.imageView.context)
                    .load(url) //optional
                    .resize(500, 200)         //optional
                    .centerCrop()                        //optional
                    .into(holder.imageView);
                holder.imageView.setOnClickListener{View->
                    val activity1 = View.context as AppCompatActivity
                    val hotelInfoFragment: HotelInfoFragment = HotelInfoFragment()

                    val bundle = Bundle()
                    bundle.putString("name", snapshot.key.toString())
                    hotelInfoFragment.arguments = bundle

                    activity1.supportFragmentManager.commit {
                        setReorderingAllowed(true).
                        replace(com.example.booking_service.R.id.frameLayoutFullScreen,hotelInfoFragment)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun getItemCount() = listOfHotels.size
}
