//package com.example.booking_service.View.Adapters
//
//import android.R
//import android.content.Context
//
//import android.widget.TextView
//
//import android.view.LayoutInflater
//import android.view.View
//
//import android.view.ViewGroup
//
//import android.widget.ArrayAdapter
//
//
//class SpinnerAdapter(context: Context?, textViewResourceId: Int, objects: Array<String?>?) :
//    ArrayAdapter<String?>(context!!, textViewResourceId, objects!!) {
//    override fun getDropDownView(
//        position: Int, convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        return getCustomView(position, convertView, parent)
//    }
//
//    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        return getCustomView(position, convertView, parent)
//    }
//
//    fun getCustomView(
//        position: Int, convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        val inflater: LayoutInflater = getLayoutInflater()
//        val row: View = inflater.inflate(R.layout.row, parent, false)
//        val label = row.findViewById(R.id.weekofday) as TextView
//        label.setText(dayOfWeek.get(position))
//        val icon: ImageView = row.findViewById(R.id.icon) as ImageView
//        if (dayOfWeek.get(position) === "Котопятница"
//            || dayOfWeek.get(position) === "Субкота"
//        ) {
//            icon.setImageResource(R.drawable.paw_on)
//        } else {
//            icon.setImageResource(R.drawable.ic_launcher)
//        }
//        return row
//    }
//}