package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.SqlResponseFromMyNeedForms

data class MyNeedFormsResponse(
        var status: Boolean,
        var sqlresponse: List<SqlResponseFromMyNeedForms>,
        var message: String)