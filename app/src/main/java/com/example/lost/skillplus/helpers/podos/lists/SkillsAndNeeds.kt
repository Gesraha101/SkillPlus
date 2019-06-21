package com.example.lost.skillplus.helpers.podos.lists

import com.example.lost.skillplus.helpers.podos.raw.Request
import com.example.lost.skillplus.helpers.podos.raw.Skill
import java.io.Serializable

data class SkillsAndNeeds(val skills: List<Skill>,
                          val needs: List<Request>): Serializable
