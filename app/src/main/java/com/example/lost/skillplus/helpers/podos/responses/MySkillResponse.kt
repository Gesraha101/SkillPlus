package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Skill


data class MySkillResponse(
        var status: Boolean,
        var skills: List<Skill>,
        var message: String
)