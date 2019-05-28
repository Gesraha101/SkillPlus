package com.example.lost.skillplus.models.podos

data class UserResponse(
        var status : Boolean,
        var data : User,
        var message : String
)