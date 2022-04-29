package com.example.booking_service.View.Adapters

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.View.DataToBookingFragment
import com.example.booking_service.View.HotelInfoFragment
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class BookingAdapter(private val listOfRooms: MutableList<String>):
    RecyclerView.Adapter<BookingAdapter.MyViewHolder>() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewOrderId: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewOrderId)
        val textViewHotelNameInBooking: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewHotelNameInBooking)
        val textViewDateArrivalInBooking: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewDateArrivalInBooking)
        val textViewDateDepartureInBooking: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewDateDepartureInBooking)
        val textViewClassInBooking: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewClassInBooking)
        val textViewStatusInBooking: TextView = itemView.findViewById(com.example.booking_service.R.id.textViewStatusInBooking)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(com.example.booking_service.R.layout.recyclerview_item_my_bookings, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewOrderId.text = "Идентификатор брони: "+ listOfRooms.get(position)
        val pos21 = listOfRooms.get(position)

        val ref: DatabaseReference = database.getReference()

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.textViewDateArrivalInBooking.text = "Дата заселения: "+snapshot.child("order").child(pos21).child("dateArrival").getValue().toString()
                holder.textViewDateDepartureInBooking.text = "Дата выселения: "+ snapshot.child("order").child(pos21).child("dateDeparture").getValue().toString()
                holder.textViewStatusInBooking.text = "Статус бронирования: " + snapshot.child("order").child(pos21).child("status").getValue().toString()
                if(snapshot.child("order").child(pos21).child("status").getValue().toString() == "Завершено"){
                    holder.textViewStatusInBooking.setTextColor(Color.parseColor("#FF2DAC1F"))
                }
                else if (snapshot.child("order").child(pos21).child("status").getValue().toString() == "Подтверждено"){
                    holder.textViewStatusInBooking.setTextColor(Color.parseColor("#FF6E11CC"))
                }
                else{
                    holder.textViewStatusInBooking.setTextColor(Color.parseColor("#AC1F1F"))
                }

                holder.textViewHotelNameInBooking.text = "Название отеля: "+ snapshot.child("hotel").child(snapshot.child("order").child(pos21).child("hotel").getValue().toString()).child("name").getValue().toString()
                holder.textViewClassInBooking.text = "Тип комнаты: "+ snapshot.child("hotel").child(snapshot.child("order").child(pos21).child("hotel").getValue().toString()).child("room").child(
                    snapshot.child("order").child(pos21).child("room").getValue().toString()).child("class").getValue().toString()

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getItemCount() = listOfRooms.size
}




