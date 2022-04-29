package com.example.booking_service.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_service.View.Adapters.CustomAdapter
import com.example.booking_service.R
import com.example.booking_service.ViewModel.MainActivityViewModel
import com.google.firebase.database.*

class MainMenuFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = MainActivityViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview_mainMenu)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val country = arguments?.getString("country").toString()
        val city = arguments?.getString("city").toString()
        val mutableLiveDataListOfHotels: MutableLiveData<MutableList<String>> = mainActivityViewModel.search(country,city)

        mutableLiveDataListOfHotels.observe(this, Observer{
            recyclerView.adapter = CustomAdapter(it)
        }

        )


    }
}