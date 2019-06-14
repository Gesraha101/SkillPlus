package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Learner

data class MySkillLearnersResponse(
        var status: Boolean,
        var skills: List<Learner>,
        var message: String)