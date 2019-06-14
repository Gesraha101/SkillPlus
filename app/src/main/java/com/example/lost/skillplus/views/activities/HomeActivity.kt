package com.example.lost.skillplus.views.activities


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.views.fragments.CategoriesFragment
import com.example.lost.skillplus.views.fragments.FavoritesFragment
import com.example.lost.skillplus.views.fragments.NotificationsFragment
import kotlinx.android.synthetic.main.activity_home.*


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

        if (fragment != null) {
            FragmentsManager.replaceFragment(supportFragmentManager, fragment, R.id.fragment_container, tag, false)
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)

        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (intent.getSerializableExtra(Keys.NOTIFICATIONS.key) != null) {
            FragmentsManager.replaceFragment(supportFragmentManager, NotificationsFragment.newInstance(intent.getSerializableExtra(Keys.NOTIFICATIONS.key) as ArrayList<Notification>?), R.id.fragment_container, null, false)
            bottom_nav.selectedItemId = R.id.navigation_notifications
        } else {
            FragmentsManager.replaceFragment(supportFragmentManager, CategoriesFragment.newInstance(), R.id.fragment_container, null, false)
        }

    }
}