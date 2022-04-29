package com.example.booking_service.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.booking_service.R
import com.example.booking_service.databinding.FragmentHomeBinding
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var buttonSearchHotel: Button
    private lateinit var editTextCountry: EditText
    private lateinit var editTextCity: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSearchHotel = view.findViewById(R.id.buttonSearchHotel2)
        editTextCountry = view.findViewById(R.id.editTextCounty)
        editTextCity = view.findViewById(R.id.editTextCity)
        buttonSearchHotel.setOnClickListener { View ->
            if (editTextCountry.text.isNotEmpty()) {
                val activity1 = View.context as AppCompatActivity
                val mainMenuFragment: MainMenuFragment = MainMenuFragment()

                val bundle = Bundle()
                bundle.putString("country", editTextCountry.text.toString())
                bundle.putString("city", editTextCity.text.toString())
                mainMenuFragment.arguments = bundle

                activity1.supportFragmentManager.commit {
                    setReorderingAllowed(true).replace(R.id.frameLayoutFullScreen, mainMenuFragment)
                }
            }
            else{
                Toast.makeText(this.context, "Введите название страны", Toast.LENGTH_SHORT).show()
            }
        }
    }

}