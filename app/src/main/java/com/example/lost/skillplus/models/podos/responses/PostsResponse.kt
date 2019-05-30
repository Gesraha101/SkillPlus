package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.lists.SkillsAndNeeds

data class PostsResponse(
        var status: Boolean,
        var skillsAndNeeds: SkillsAndNeeds,
        var message: String
)