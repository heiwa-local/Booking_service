package com.example.booking_service.View.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.View.DataToBookingFragment
import com.example.booking_service.View.HotelInfoFragment
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val hotelPosition: String, private val listOfRooms: MutableList<String>):
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var viewModel: MainActivityViewModel = MainActivityViewModel()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageViewRoom: ImageView = itemView.findViewById(com.example.booking_service.R.id.imageViewRoom)
        val textViewClass: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewClass)
        val textViewRoomPrice: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewRoomPrice)
        val buttonBooking: Button = itemView.findViewById(com.example.booking_service.R.id.buttonBooking)
//        val hotelDescription: TextView = itemView.findViewById(R.id.hotelDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(com.example.booking_service.R.layout.recyclerview_item_rooms, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val ref: DatabaseReference = database.getReference("hotel").child(hotelPosition.toString()).child("room").child(listOfRooms.get(position)) // Key

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.textViewClass.text = "Тип номера: "+snapshot.child("class").getValue().toString()
                var url: String = snapshot.child("roomImage").getValue().toString()
                Picasso.with(holder.imageViewRoom.context)
                    .load(url) //optional
                    .resize(500, 200)         //optional
                    .centerCrop()                        //optional
                    .into(holder.imageViewRoom)

                holder.buttonBooking.setOnClickListener{View->
                    val activity1 = View.context as AppCompatActivity
                    val dataToBookingFragment: DataToBookingFragment = DataToBookingFragment()

                    val bundle = Bundle()
                    bundle.putString("hotelName", hotelPosition)
                    bundle.putString("roomName", snapshot.key.toString())
                    dataToBookingFragment.arguments = bundle

                    activity1.supportFragmentManager.commit {
                        setReorderingAllowed(true).
                        replace(com.example.booking_service.R.id.frameLayoutFullScreen,dataToBookingFragment)
                    }
                }
                holder.textViewRoomPrice.text = "Стоимость одной ночи: " + snapshot.child("price").getValue().toString() + " Рублей"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


    }

    override fun getItemCount() = listOfRooms.size
}