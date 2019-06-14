package com.example.lost.skillplus.views.activities


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.views.fragments.CategoriesFragment
import com.example.lost.skillplus.views.fragments.FavoritesFragment
import com.example.lost.skillplus.views.fragments.NotificationsFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlin.system.exitProcess

class HomeActivity : NavigationDrawerActivity(), MySkillsFragment.OnFragmentInteractionListener, SkillLearnersFragments.OnFragmentInteractionListener, MyNeedsFragment.OnFragmentInteractionListener,SkillDetailsFragment.OnFragmentInteractionListener {
    override fun onBackPressed() {


class HomeActivity : NavigationDrawerActivity() {

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 0) {//Check if there are no fragments at backstack
            val builder = AlertDialog.Builder(this@HomeActivity)

            builder.setTitle("Quit")
            builder.setMessage("Are you sure you want to quit?")

            builder.setPositiveButton("YES") { dialog, which ->
                exitProcess(0)
            }
            builder.setNegativeButton("NO") { dialog, which ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        else {
            if(supportFragmentManager.findFragmentByTag("details_frag_from_favorites")!!.isVisible)
                rv_favorites.visibility=View.VISIBLE

            supportFragmentManager.popBackStack()
        }
    }
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