package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.CurrentSkill


data class CurrentSkillResponse(
        var status: Boolean,
        var skills: List<CurrentSkill>,
        var message: String
)