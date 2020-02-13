package com.johncodeos.scrolltotopexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johncodeos.scrolltotopexample.model.Car
import kotlinx.android.synthetic.main.car_row.view.*

class Cars_RVAdapter(private var carsCells: Car) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.car_row, parent, false)
        return CarsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return carsCells.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val carsViewHolder = holder as CarsViewHolder
        carsViewHolder.itemView.carMake.text = carsCells[position].carMake
        carsViewHolder.itemView.carModel.text = carsCells[position].carModel
        carsViewHolder.itemView.carModelYear.text = carsCells[position].carModelYear.toString()

    }

}