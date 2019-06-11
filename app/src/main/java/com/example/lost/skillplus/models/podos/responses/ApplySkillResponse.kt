package com.example.lost.skillplus.models.podos.responses

import java.io.Serializable


data class ApplySkillResponse(
        var status : Boolean,
        var sqlresponse: ResponseApplyArray?,
        var message : String
): Serializable