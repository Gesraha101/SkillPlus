package com.example.lost.skillplus.views.activities
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.views.fragments.MySkillsFragment
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*

open class NavigationDrawerActivity : AppCompatActivity(),MySkillsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

//        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.nav_skills-> {
                loadFiragment( MySkillsFragment())
                Toast.makeText(this@NavigationDrawerActivity, "nav_skills", Toast.LENGTH_LONG).show()


            }
            R.id.nav_needs-> {
                Toast.makeText(this@NavigationDrawerActivity, "nav_needs", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_active_jobs -> {
                Toast.makeText(this@NavigationDrawerActivity, "nav_active_jobs", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {

            }
            R.id.nav_about_us -> {

            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFiragment(fragment: Fragment){
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frameLayout , fragment)
        fm.commit()
    }

}
