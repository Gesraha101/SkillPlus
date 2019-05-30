package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.User

data class UserResponse(
        var status : Boolean,
        var user : User,
        var message : String
)