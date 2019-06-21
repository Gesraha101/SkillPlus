package com.example.lost.skillplus.helpers.podos.raw

import java.io.Serializable

data class ApplySkill (
        var learner : Int ,
        var skill : Int ,
        var schedule: List<Long>?): Serializable