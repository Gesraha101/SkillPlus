package com.example.lost.skillplus.models.podos.raw

class Schedule {

    var isSelected: Boolean = false
    var animal: String? = null
    fun getSchedule(): String {
        return this.animal.toString()
    }

    fun setSchedule(animal: String) {
        this.animal = animal
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}