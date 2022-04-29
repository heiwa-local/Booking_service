package com.example.booking_service.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.R
import com.example.booking_service.View.Adapters.FavoriteAdapter
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class HotelInfoFragment : Fragment() {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var viewModel: MainActivityViewModel = MainActivityViewModel()
    private lateinit var imageViewOpenInfo: ImageView
    private lateinit var imageButtonNext: ImageButton
    private lateinit var imageButtonBack: ImageButton
    private lateinit var imageButtonToFavorite: ImageButton
    private lateinit var buttonAddToBooking: Button
    private lateinit var textViewDescription: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewNameHot: TextView
    private lateinit var recyclerViewRooms: RecyclerView
    private lateinit var textViewContextToolbar: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_hotel_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getString("name")
        imageViewOpenInfo = view.findViewById(R.id.imageViewOpenInfo)
        imageButtonNext = view.findViewById(R.id.imageButtonNext)
        imageButtonBack = view.findViewById(R.id.imageButtonBack)
        textViewDescription = view.findViewById(R.id.textViewDescription)
        textViewAddress = view.findViewById(R.id.textViewAddress)
        textViewNameHot = view.findViewById(R.id.textViewNameHot)
        imageButtonToFavorite = view.findViewById(R.id.imageButtonToFavorite)
        recyclerViewRooms = view.findViewById(R.id.recyclerViewRooms)

        val ref: DatabaseReference = database.getReference() // Key

        var imagePosition = 0
        var size = 0
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                textViewDescription.text = snapshot.child("hotel").child(position.toString()).child("description").getValue().toString()
                textViewNameHot.text = snapshot.child("hotel").child(position.toString()).child("name").getValue().toString() + " " + snapshot.child("hotel").child(position.toString()).child("stars").getValue().toString()+"*"
                textViewAddress.text = snapshot.child("hotel").child(position.toString()).child("address").child("country").getValue().toString()+
                        ", "+snapshot.child("hotel").child(position.toString()).child("address").child("city").getValue().toString()+
                        ", "+snapshot.child("hotel").child(position.toString()).child("address").child("street").getValue().toString()+
                        ", "+snapshot.child("hotel").child(position.toString()).child("address").child("building").getValue().toString()

                //Кол-во картинок выбранного жилья
                snapshot.child("hotel").child(position.toString()).child("image").children.forEach {
                    //"it" is the snapshot
//                    val key: String = it.key.toString()
                    size+=1

                }

                //Стартовая картинка
                var url: String = snapshot.child("hotel").child(position.toString()).child("image").child(imagePosition.toString()).getValue().toString()
                Picasso.with(imageViewOpenInfo.context).load(url).resize(700, 500).centerCrop().into(imageViewOpenInfo)

                //Кнопка - следующая картинка
                imageButtonNext.setOnClickListener { View->
                    if (imagePosition+1 < size) {
                        imagePosition+=1
                        var url: String = snapshot.child("hotel").child(position.toString()).child("image").child(imagePosition.toString()).getValue().toString()
                        Picasso.with(imageViewOpenInfo.context)
                            .load(url) //optional
                            .resize(700, 500)         //optional
                            .centerCrop()                        //optional
                            .into(imageViewOpenInfo);
                    }
                }

                //Кнопка - предидущая картинка
                imageButtonBack.setOnClickListener { View->
                    if (imagePosition-1 >= 0) {
                        imagePosition-=1
                        var url: String = snapshot.child("hotel").child(position.toString()).child("image").child(imagePosition.toString()).getValue().toString()
                        Picasso.with(imageViewOpenInfo.context)
                            .load(url) //optional
                            .resize(700, 500)         //optional
                            .centerCrop()                        //optional
                            .into(imageViewOpenInfo);
                    }
                }

                //Список номеров
                var mutableList: MutableList<String> = mutableListOf()
                var roomPosition1 = 0
                snapshot.child("hotel").child(position.toString()).child("room").children.forEach{
                    mutableList.add(snapshot.child("hotel").child(position.toString()).child("room").child(roomPosition1.toString()).key.toString())
                    roomPosition1+=1
                }

                recyclerViewRooms.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewRooms.adapter = FavoriteAdapter(position.toString(),mutableList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        viewModel.findUser(Firebase.auth.currentUser?.email.toString()).observe(this, Observer {
            imageButtonToFavorite.setOnClickListener { View ->
                val ref2: DatabaseReference = database.getReference("user").child(it.toString()).child("favorite")
                ref2.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var position1 = 0
                        var inFavorite = false
                        snapshot.children.forEach {
                            if (snapshot.child(position1.toString()).getValue() == position.toString()){
                                inFavorite = true

                                ref2.child(position1.toString()).removeValue()
                                System.out.println("Я Есть")
                                imageButtonToFavorite.setImageResource(R.drawable.from_favorite_icon)
                                ref2.removeEventListener(this)
                            }
                            position1 += 1
                        }
                        if (inFavorite == false) {
                            database.getReference("user").child(it.toString()).child("favorite")
                                .child(position1.toString()).setValue(position.toString())
                            imageButtonToFavorite.setImageResource(R.drawable.from_favorite_icon)
                            ref2.removeEventListener(this)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        })
    }
}