package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Skill(val skill_id: Int?,
                 val name: String,
                 val desc: String,
                 val session_no: Int,
                 val price: Float,
                 val duration: Float,
                 val extra: Float,
                 val pic:String,
                 val user_id: Int,
                 val cat_id: Int,
                 val adding_date: String?,
                 val rate: Float?,
                 val user_name: String?,
                 var schedule: List<Long>?): Serializable