package com.example.booking_service.View

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.R
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.time.temporal.ChronoUnit
import java.util.*

class DataToBookingFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_data_to_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textViewHotelName: TextView = view.findViewById(R.id.textViewHotelName)
        val textViewRoomClass: TextView = view.findViewById(R.id.textViewRoomClass)
        val textViewCountNights: TextView = view.findViewById(R.id.textViewCountNights)
        val textViewRoomPriceSum: TextView = view.findViewById(R.id.textViewRoomPriceSum)
        val editTextDateArrival: EditText = view.findViewById(R.id.editTextDateArrival)
        val editTextDateDeparture: EditText = view.findViewById(R.id.editTextDateDeparture)
        val imageViewRoomImage: ImageView = view.findViewById(R.id.imageViewRoomImage)
        val buttonAccept: Button = view.findViewById(R.id.buttonAccept)
        val buttonCheck: Button = view.findViewById(R.id.buttonCheck)

        val mutableLiveDataUser: MutableLiveData<Int> = viewModel.findUser(Firebase.auth.currentUser?.email.toString())
        mutableLiveDataUser.observe(this, androidx.lifecycle.Observer {
            val ref2: DatabaseReference = database.getReference()
            ref2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val price = snapshot.child("hotel").child(
                        arguments?.getString("hotelName").toString()
                    ).child("room").child(
                        arguments?.getString("roomName").toString()
                    ).child("price").getValue().toString()
                    val roomClass =
                        snapshot.child("hotel").child(arguments?.getString("hotelName").toString())
                            .child("room").child(arguments?.getString("roomName").toString())
                            .child("class").getValue().toString()
                    textViewRoomClass.text = "Тип номера: " + roomClass

                    textViewHotelName.text =
                        snapshot.child("hotel").child(arguments?.getString("hotelName").toString())
                            .child("name").getValue().toString() + " " + snapshot.child("hotel")
                            .child(arguments?.getString("hotelName").toString()).child("stars")
                            .getValue().toString() + "*"

                    var url: String =
                        snapshot.child("hotel").child(arguments?.getString("hotelName").toString())
                            .child("room").child(arguments?.getString("roomName").toString())
                            .child("roomImage").getValue().toString()
                    Picasso.with(imageViewRoomImage.context)
                        .load(url) //optional
                        .resize(500, 200)         //optional
                        .centerCrop()                        //optional
                        .into(imageViewRoomImage)

                    buttonCheck.setOnClickListener { View ->
                        val dateArrival = editTextDateArrival.text.toString()
                        val dateDeparture = editTextDateDeparture.text.toString()
                        val dateFormatInput = SimpleDateFormat("dd.MM.yyyy")
                        val d3parse = dateFormatInput.parse(dateArrival)
                        val d4parse = dateFormatInput.parse(dateDeparture)
                        var countNightsInMili: Long = d4parse.time - d3parse.time
                        var nights = countNightsInMili / (1000 * 60 * 60 * 24)
                        textViewCountNights.text = "Количество ночей: " + nights.toString()
                        textViewRoomPriceSum.text =
                            "Итого: " + (price.toInt() * nights).toString() + " Рублей"
                    }
                    buttonAccept.setOnClickListener { View ->
                        var pos = 0
                        snapshot.child("order").children.forEach {
                            pos += 1
                        }
                        ref2.child("order").child(pos.toString()).child("dateArrival")
                            .setValue(editTextDateArrival.text.toString())
                        ref2.child("order").child(pos.toString()).child("dateDeparture")
                            .setValue(editTextDateDeparture.text.toString())
                        ref2.child("order").child(pos.toString()).child("status")
                            .setValue("На рассмотрении")
                        ref2.child("order").child(pos.toString()).child("hotel")
                            .setValue(arguments?.getString("hotelName").toString())
                        ref2.child("order").child(pos.toString()).child("room")
                            .setValue(arguments?.getString("roomName").toString())
                        ref2.child("order").child(pos.toString()).child("email")
                            .setValue(Firebase.auth.currentUser?.email.toString())
                        var pos2 = 0
                        snapshot.child("user").child(it.toString()).child("order").children.forEach{
                            pos2+=1
                        }
                        ref2.child("user").child(it.toString()).child("order").child(pos2.toString()).setValue(pos.toString())
                        Toast.makeText(
                            context, "Запрос на бронирование отправлен",
                            Toast.LENGTH_SHORT
                        ).show()
                        val activity1 = View.context as AppCompatActivity
                        activity1.supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<MyBookingsFragment>(R.id.frameLayoutFullScreen)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        })
    }
}