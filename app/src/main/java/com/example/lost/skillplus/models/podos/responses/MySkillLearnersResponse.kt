package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.User

data class MySkillLearnersResponse(
        var status: Boolean,
        var skills: List<User>,
        var message: String)