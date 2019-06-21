package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.SqlResponseFromMyNeedForms

data class MyCurrentNeedFormsResponse(
        var status: Boolean,
        var data: SqlResponseFromMyNeedForms,
        var message: String)