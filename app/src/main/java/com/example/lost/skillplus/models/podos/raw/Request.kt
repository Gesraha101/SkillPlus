package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Request(
                val need_id: Int,
                val need_name: String,
                val need_desc: String,
                val need_photo: String,
                val cat_id: Int,
                val user_id: Int,
                val adding_date: String,
                val user_name: String): Serializable