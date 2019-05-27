package com.example.lost.skillx.views.models

 data class User(var id: Int = 0,
                var name: String? = null,
                var email: String? = null,
                var password: String? = null,
                var pic : String? = null)

data class logUser( var email: String? ,
                    var password: String? )