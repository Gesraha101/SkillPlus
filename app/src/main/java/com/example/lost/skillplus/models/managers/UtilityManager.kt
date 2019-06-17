package com.example.lost.skillplus.models.managers

import android.view.inputmethod.EditorInfo
import android.widget.EditText


class UtilityManager {

    companion object {

        fun hideKeyboard(editText: EditText) {
            editText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }
}