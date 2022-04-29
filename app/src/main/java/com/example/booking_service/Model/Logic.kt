package com.example.booking_service.Model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class Logic {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mutableLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var mutableLiveData1: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var mutableLiveData2: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var mutableLiveDataUser: MutableLiveData<Int> = MutableLiveData()
    private var mutableLiveDataCountOfFavorite: MutableLiveData<Int> = MutableLiveData()

    fun search(country: String, city: String) : MutableLiveData<MutableList<String>> {
        val ref: DatabaseReference = database.getReference("hotel")


        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val mutableList: MutableList<String> = mutableListOf()
                var size: Int = 0

                if (city.isEmpty()) {
                    snapshot.children.forEach {
                        if (snapshot.child(size.toString()).child("address").child("country")
                                .getValue().toString() == country
                        ) {
                            mutableList.add(snapshot.child(size.toString()).key.toString())
                        }
                        size += 1
                    }
                    mutableLiveData.postValue(mutableList)
                }
                else{
                    snapshot.children.forEach {
                        if (snapshot.child(size.toString()).child("address").child("country").getValue().toString() == country && snapshot.child(size.toString()).child("address").child("city").getValue().toString() == city
                        ) {
                            mutableList.add(snapshot.child(size.toString()).key.toString())
                        }
                        size += 1
                    }
                    mutableLiveData.postValue(mutableList)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableLiveData
    }

    fun favorite(userId: Int):MutableLiveData<MutableList<String>>{
        val ref: DatabaseReference = database.getReference("user").child(userId.toString()).child("favorite")


        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mutableList: MutableList<String> = mutableListOf()

                var position = 0
                snapshot.children.forEach{
                    mutableList.add(snapshot.child(position.toString()).getValue().toString())
                    position+=1
                }
                System.out.println("Фэворит: " + mutableList)

                mutableLiveData1.postValue(mutableList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableLiveData1
    }

    fun findUser(email: String): MutableLiveData<Int>{
        val ref: DatabaseReference = database.getReference("user")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var position = 0
                var userId = -1
                val mutableList: MutableList<String> = mutableListOf()

                snapshot.children.forEach {
                    if (snapshot.child(position.toString()).child("email").getValue().toString() == email) {
                        userId = position
                        System.out.println("позиция: " + userId)
                    }
                    position += 1
                }

                mutableLiveDataUser.postValue(userId)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableLiveDataUser
    }

    fun countOfFavorite(userId: Int): MutableLiveData<Int>{
        val ref2: DatabaseReference = database.getReference("user").child(userId.toString()).child("favorite")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var position1 = 0
                snapshot.children.forEach {
                    position1 += 1
                }
                mutableLiveDataCountOfFavorite.postValue(position1)
                ref2.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveDataCountOfFavorite
    }

    fun getOrders(userId: Int): MutableLiveData<MutableList<String>>{
        database.getReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var mutableList: MutableList<String> = mutableListOf()
                var pos10 = 0
                snapshot.child("user").child(userId.toString()).child("order").children.forEach{
                    mutableList.add(snapshot.child("user").child(userId.toString()).child("order").child(pos10.toString()).getValue().toString())

                    pos10+=1
                }
                mutableLiveData2.postValue(mutableList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData2
    }
}


//fun findUser(email: String): MutableLiveData<MutableList<String>>{
//    val ref: DatabaseReference = database.getReference("user")
//
//    ref.addValueEventListener(object: ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            var position = 0
//            var userId = -1
//            val mutableList: MutableList<String> = mutableListOf()
//
//            snapshot.children.forEach {
//                if (snapshot.child(position.toString()).child("email").getValue().toString() == email) {
//                    userId = position
//                    System.out.println("позиция: " + userId)
//                }
//                position += 1
//            }
//            position = 0
//            snapshot.children.forEach{
//                mutableList.add(snapshot.child(userId.toString()).child("favorite").child(position.toString()).getValue().toString())
//                position+=1
//            }
//            System.out.println("Фэворит: " + mutableList)
//
//            mutableLiveData1.postValue(mutableList)
//        }
//        override fun onCancelled(error: DatabaseError) {
//        }
//    })
//    return mutableLiveData1
//}
