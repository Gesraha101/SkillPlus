package com.example.lost.skillplus.views.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.views.fragments.CategoriesFragment
import com.example.lost.skillplus.views.fragments.MyNeedsFragment
import com.example.lost.skillplus.views.fragments.MySkillsFragment
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import kotlinx.android.synthetic.main.nav_header_navigation_drawer.view.*


open class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var activeUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        activeUser = PreferencesManager(this@NavigationDrawerActivity).getUser()
        nav_view.getHeaderView(0).user_name.text = activeUser.name
        nav_view.setNavigationItemSelectedListener(this)
        Glide.with(this)
                .load(activeUser.pic)
                .into(nav_view.getHeaderView(0).user_image)
        nav_view.getHeaderView(0).user_email.text = activeUser.email
        nav_view.getHeaderView(0).user_rate.rating = if(activeUser.rate!! == -1F) 0F else activeUser.rate!!
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

                findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE

                supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        MySkillsFragment()).commit()
                nav_view.setCheckedItem(R.id.nav_skills)
                Log.d("test", "nav_skills")
            }
            R.id.nav_needs-> {
                findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        MyNeedsFragment()).commit()
                nav_view.setCheckedItem(R.id.nav_needs)
                Log.d("test", "nav_needs")
            }
            R.id.nav_active_jobs -> {
//                findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE
//                supportFragmentManager.beginTransaction().replace(
//                        R.id.fragment_container,My
//                        ).commit()
//                nav_view.setCheckedItem(R.id.nav_needs)
//                Log.d("test", "nav_needs")

            }
            R.id.nav_settings -> {
                findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        CategoriesFragment()).commit()
                nav_view.setCheckedItem(R.id.nav_needs)
                Log.d("test", "nav_needs")



            }
            R.id.nav_about_us -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
