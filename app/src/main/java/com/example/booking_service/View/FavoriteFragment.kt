package com.example.booking_service.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.R
import com.example.booking_service.View.Adapters.CustomAdapter
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class FavoriteFragment : Fragment() {
    private lateinit var recyclerViewFavorite: RecyclerView
    private var viewModel: MainActivityViewModel = MainActivityViewModel()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFavorite = view.findViewById(R.id.recyclerViewFavorite)
        recyclerViewFavorite.layoutManager = LinearLayoutManager(activity)

        val mutableLiveDataUser: MutableLiveData<Int> = viewModel.findUser(Firebase.auth.currentUser?.email.toString())
        mutableLiveDataUser.observe(this, Observer {
            val mutableLiveData: MutableLiveData<MutableList<String>> = viewModel.favorite(it)
            mutableLiveData.observe(this, Observer {
                recyclerViewFavorite.adapter = CustomAdapter(it)
            })
        })
//        val mutableLiveDataListOfHotels: MutableLiveData<MutableList<String>> = viewModel.favorite(userId)
//        mutableLiveDataListOfHotels.observe(this, Observer{
//            recyclerViewFavorite.adapter = FavoriteAdapter(it)
//        })

    }

}
