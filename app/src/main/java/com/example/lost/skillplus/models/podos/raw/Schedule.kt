package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Schedule(val date: Long,
                    val otherId: Int?,
                    val isMentor: Boolean) : Serializable