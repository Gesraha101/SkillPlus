package com.example.lost.skillplus.models.podos.responses


data class ResponseApplyArray(
val fieldCount: Int  ,
val affectedRows : Int  ,
val insertId : Int ,
val serverStatus : Int  ,
val warningCount : Int  ,
val message  : String  ,
val protocol41 :Boolean  ,
val changedRows : Int
)