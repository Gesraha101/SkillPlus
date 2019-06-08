package com.example.lost.skillplus.views.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.example.lost.skillplus.R
import com.example.lost.skillplus.views.fragments.MySkillsFragment
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*

open class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_skills-> {
//                    findViewById<FrameLayout>(R.id.framecategory).visibility= View.VISIBLE
//                    findViewById<FrameLayout>(R.id.frameID).visibility= View.GONE
            }
            R.id.nav_needs-> {
//                findViewById<FrameLayout>(R.id.framecategory).visibility= View.GONE
//                findViewById<FrameLayout>(R.id.frameID).visibility= View.VISIBLE
//                supportFragmentManager.beginTransaction().replace(
//                        R.id.frameID , MySkillsFragment()
//                ).commit()
            }
            R.id.nav_active_jobs -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_about_us -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
