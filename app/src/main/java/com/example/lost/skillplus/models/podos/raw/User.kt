package com.example.lost.skillplus.models.podos.raw

data class User(var id: Int = 0,
                var name: String? = null,
                var email: String? = null,
                var password: String? = null,
                var pic: String? = null,
                var rate: Float? = 0F)