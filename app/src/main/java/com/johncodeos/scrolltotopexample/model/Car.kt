package com.johncodeos.scrolltotopexample.model

typealias Car = ArrayList<CarElement>

data class CarElement(
    val carMake: String,
    val carModel: String,
    val carModelYear: Int
)
