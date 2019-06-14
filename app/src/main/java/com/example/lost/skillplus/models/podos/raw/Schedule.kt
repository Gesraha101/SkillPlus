package com.example.lost.skillplus.models.podos.raw

class Schedule {

    var isSelected: Boolean = false
    var schedulee: String? = null
    fun getSchedule(): String {
        return this.schedulee.toString()
    }

    fun setSchedule(animal: String) {
        this.schedulee = animal
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}