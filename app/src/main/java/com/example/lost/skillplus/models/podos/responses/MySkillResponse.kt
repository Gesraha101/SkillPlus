package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Skill


data class MySkillResponse(
        var status: Boolean,
        var skills: List<Skill>,
        var message: String
)