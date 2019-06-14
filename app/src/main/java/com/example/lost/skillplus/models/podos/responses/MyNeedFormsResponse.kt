package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.SqlResponseFromMyNeedForms

data class MyNeedFormsResponse(
        var status: Boolean,
        var sqlresponse: List<SqlResponseFromMyNeedForms>,
        var message: String)