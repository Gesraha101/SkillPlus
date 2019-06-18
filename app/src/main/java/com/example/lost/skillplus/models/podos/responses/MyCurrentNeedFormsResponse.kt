package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.SqlResponseFromMyNeedForms

data class MyCurrentNeedFormsResponse(
        var status: Boolean,
        var data: SqlResponseFromMyNeedForms,
        var message: String)