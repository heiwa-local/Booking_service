package com.example.booking_service.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.booking_service.Model.Logic
import com.google.firebase.database.*
import kotlin.math.log


class MainActivityViewModel {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var logic: Logic = Logic()
    private lateinit var mutableLiveData: MutableLiveData<MutableList<String>>

    fun search(country: String, city: String) : MutableLiveData<MutableList<String>> {
        return logic.search(country, city)
    }
    fun favorite(userId: Int): MutableLiveData<MutableList<String>>{
        return logic.favorite(userId)
    }
    fun findUser(email: String): MutableLiveData<Int>{
        return logic.findUser(email)
    }
    fun countOfFavorite(userId: Int): MutableLiveData<Int>{
        return logic.countOfFavorite(userId)
    }
    fun getOrders(userId: Int): MutableLiveData<MutableList<String>>{
        return logic.getOrders(userId)
    }

}