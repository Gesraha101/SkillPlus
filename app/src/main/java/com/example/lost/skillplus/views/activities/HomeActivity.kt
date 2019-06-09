package com.example.lost.skillplus.views.activities

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.views.fragments.*
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.views.fragments.CategoriesFragment
import com.example.lost.skillplus.views.fragments.FavoritesFragment
import com.example.lost.skillplus.views.fragments.NotificationsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : NavigationDrawerActivity(), CategoriesFragment.OnFragmentInteractionListener, MySkillsFragment.OnFragmentInteractionListener, MyNeedsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
class HomeActivity : NavigationDrawerActivity() {

    private fun loadFragment(item: MenuItem) {
        val tag = item.itemId.toString()
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (item.itemId) {
            R.id.navigation_home -> {
                CategoriesFragment.newInstance()
            }
            R.id.navigation_favorites-> {
                FavoritesFragment.newInstance()
            }
            R.id.navigation_notifications -> {
                NotificationsFragment.newInstance(null)
            }
            else -> {
                null
            }
        }

        // replace fragment
        if (fragment != null) {
            FragmentsManager.replaceFragment(supportFragmentManager, fragment, R.id.fragment_container, tag, false)
        }
    }

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)
        FragmentsManager.replaceFragment(supportFragmentManager, CategoriesFragment.newInstance(), R.id.fragment_container, null, false)

        nav_view2.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
