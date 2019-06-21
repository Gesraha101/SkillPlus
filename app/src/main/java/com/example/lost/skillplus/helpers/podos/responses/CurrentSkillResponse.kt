package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.CurrentSkill


data class CurrentSkillResponse(
        var status: Boolean,
        var skills: List<CurrentSkill>,
        var message: String
)