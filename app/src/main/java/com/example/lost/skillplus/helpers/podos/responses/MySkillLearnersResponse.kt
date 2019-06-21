package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Learner

data class MySkillLearnersResponse(
        var status: Boolean,
        var skills: List<Learner>,
        var message: String)