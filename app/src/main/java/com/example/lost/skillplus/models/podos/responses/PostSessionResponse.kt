package com.example.lost.skillplus.models.podos.responses

import java.io.Serializable

data class PostSessionResponse(val isFinished: Boolean,
                               val skill_id: Int,
                               val need_id: Int) : Serializable