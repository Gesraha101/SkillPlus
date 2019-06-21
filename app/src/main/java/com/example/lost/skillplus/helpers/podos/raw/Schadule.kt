package com.example.lost.skillplus.helpers.podos.raw

class Schadule {

    var isSelected: Boolean = false
    var schedulee: String? = null

    fun getSchedule(): String {
//        val date = schedulee?.toLong()?.let { Date(it) }
//        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        return format.format(date)

//
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