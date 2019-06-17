package com.example.lost.skillplus.models.podos.raw

class Schadule {

    var isSelected: Boolean = false
    var schedulee: String? = null
    fun getSchedule(): String {
        return this.schedulee.toString()
    }

    fun setSchedule(x: String) {
        this.schedulee = x
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}