package com.example.lost.skillplus.models.podos.raw

import java.io.Serializable

data class Notification(val user_name: String?,
                        val need_name: String?,
                        val skill_name: String?,
                        val user_pic: String?,
                        val form_id: Int?,
                        val need_id: Int?,
                        val session_no: Int?,
                        val duration: Float?,
                        val need_price: Float?,
                        val extra_fees: Float?,
                        val schedule: List<Long>?): Serializable