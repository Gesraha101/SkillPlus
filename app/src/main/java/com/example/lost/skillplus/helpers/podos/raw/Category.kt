package com.example.lost.skillplus.helpers.podos.raw

import java.io.Serializable

data class Category(val cat_id: Int,
                    val cat_name: String,
                    val cat_description: String,
                    val cat_photo: String) : Serializable