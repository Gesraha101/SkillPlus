package com.example.lost.skillplus.models.podos.responses


data class ApplySkillResponse(
        var status : Boolean,
        var data :Array<ResponseApplyArray>,
        var message : String
)