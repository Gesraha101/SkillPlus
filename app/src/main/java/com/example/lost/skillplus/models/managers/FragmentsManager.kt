package com.example.lost.skillplus.models.managers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

class FragmentsManager {

    companion object {

        inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit, addToBackStack: Boolean) {
            val fragmentTransaction = beginTransaction()
            fragmentTransaction.func()
            if (addToBackStack)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        fun replaceFragment(manager: FragmentManager, fragment: Fragment, frameId: Int, tag: String?, addToBackStack: Boolean) {
            if (tag.isNullOrBlank())
                manager.inTransaction({ replace(frameId, fragment)}, addToBackStack)
            else
                manager.inTransaction({ replace(frameId, fragment, tag) }, addToBackStack)
        }

        fun removeFragment(manager: FragmentManager, fragment: Fragment, addToBackStack: Boolean) {
            manager.inTransaction({ remove(fragment)}, addToBackStack)
        }

    }
}