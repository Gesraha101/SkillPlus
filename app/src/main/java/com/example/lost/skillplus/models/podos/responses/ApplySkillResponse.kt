package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.ResponseApplyArray


data class ApplySkillResponse(
        var status : Boolean,
        var data :Array<ResponseApplyArray>,
        var message : String
)