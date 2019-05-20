package com.example.lost.skillx.views.activities

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.example.lost.skillx.R
import com.example.lost.skillx.views.fragments.CategoriesFragment
import com.example.lost.skillx.views.fragments.InboxFragment
import com.example.lost.skillx.views.fragments.NotificationsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), CategoriesFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {

    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String?) {
        if (tag.isNullOrBlank())
            supportFragmentManager.inTransaction{ replace(frameId, fragment, tag) }
        else
            supportFragmentManager.inTransaction{ replace(frameId, fragment) }
    }

    fun loadFragment(item: MenuItem) {
        val tag = item.itemId.toString()
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (item.itemId) {
            R.id.navigation_home -> {
                CategoriesFragment.newInstance()
            }
            R.id.navigation_inbox -> {
                InboxFragment.newInstance()
            }
            R.id.navigation_notifications -> {
                NotificationsFragment.newInstance()
            }
            else -> {
                null
            }
        }

        // replace fragment
        if (fragment != null) {
            replaceFragment(fragment, R.id.fragment_container, tag)
        }
    }

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragment(CategoriesFragment.newInstance(), R.id.fragment_container, null)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
