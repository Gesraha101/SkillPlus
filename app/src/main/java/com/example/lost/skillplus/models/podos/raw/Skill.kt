package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Skill(val skill_id: Int?,
                 val skill_name: String,
                 val skill_desc: String,
                 val session_no: Int,
                 val skill_price: Float,
                 val duration: Float,
                 val extra_fees: Float,
                 val user_id: Int,
                 val cat_id: Int,
                 val adding_date: String?,
                 val rate: Float?,
                 val user_name: String?,
                 val schedule: List<Long>?): Serializable