package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Form(

                val sessions_no: Int,
                val duration: Float,
                val price: Float,
                val extra: Float,
                var schedule: List<Long>?,
                val user_id:Int): Serializable