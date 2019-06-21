package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.lists.SkillsAndNeeds

data class PostsResponse(
        var status: Boolean,
        var skillsAndNeeds: SkillsAndNeeds,
        var message: String
)