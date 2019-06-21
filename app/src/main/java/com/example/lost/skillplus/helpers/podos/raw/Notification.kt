package com.example.lost.skillplus.helpers.podos.raw

import java.io.Serializable

data class Notification(val user_id: Int?,
                        val user_name: String?,
                        val need_name: String?,
                        val skill_name: String?,
                        val user_pic: String?,
                        val skill_id: Int?,
                        val form_id: Int?,
                        val need_id: Int?,
                        val schedule: List<Long>?): Serializable