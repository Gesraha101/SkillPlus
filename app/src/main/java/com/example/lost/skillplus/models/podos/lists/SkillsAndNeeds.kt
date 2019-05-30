package com.example.lost.skillplus.models.podos.lists

import com.example.lost.skillplus.models.podos.raw.Request
import com.example.lost.skillplus.models.podos.raw.Skill
import java.io.Serializable

data class SkillsAndNeeds(val skills: List<Skill>,
                          val needs: List<Request>): Serializable
