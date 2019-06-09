package com.example.lost.skillplus.models.podos.responses

import java.io.Serializable


data class ApplySkillResponse(
        var status : Boolean,
        var data :Array<ResponseApplyArray>,
        var message : String
): Serializable