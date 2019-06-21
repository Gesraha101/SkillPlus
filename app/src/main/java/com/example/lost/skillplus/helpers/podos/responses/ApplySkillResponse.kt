package com.example.lost.skillplus.helpers.podos.responses

import java.io.Serializable


data class ApplySkillResponse(
        var status : Boolean,
        var sqlresponse: SQLResponse?,
        var message : String
): Serializable