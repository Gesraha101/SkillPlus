package com.example.lost.skillplus.helpers.podos.raw

class CurrentSkill(
        val skill_id: Int?,
        val skill_name: String?,
        val skill_desc: String?,
        val session_no: Int,
        val skill_price: Float,
        val duration: Float,
        val extra_fees: Float,
        val photo_path: String?,
        val user_id: Int,
        val cat_id: Int,
        val adding_date: String?,
        val my_sessions: Int?,
        var schedule: List<Long>?
)