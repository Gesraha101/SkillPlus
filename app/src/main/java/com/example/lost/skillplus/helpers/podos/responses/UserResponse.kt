package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.User

data class UserResponse(
        var status : Boolean,
        var userlogined : User,
        var message : String
)