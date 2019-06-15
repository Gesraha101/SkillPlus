package com.example.lost.skillplus.views.activities


import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.views.fragments.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlin.system.exitProcess

class HomeActivity : NavigationDrawerActivity(), MySkillsFragment.OnFragmentInteractionListener, SkillLearnersFragments.OnFragmentInteractionListener, MyNeedsFragment.OnFragmentInteractionListener,SkillDetailsFragment.OnFragmentInteractionListener {
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 0) {//Check if there are no fragments at backstack
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Snackbar.make(container, "Press back again to exit", Snackbar.LENGTH_SHORT).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }
        else {
            if(supportFragmentManager.findFragmentByTag("details_frag_from_favorites")!!.isVisible)
                    rv_favorites.visibility=View.VISIBLE

            supportFragmentManager.popBackStack()
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadFragment(item: MenuItem) { //tag name of these fragments are created by its menu items id
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

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)
        if (intent.getSerializableExtra(Keys.NOTIFICATIONS.key) != null) {
            FragmentsManager.replaceFragment(supportFragmentManager, NotificationsFragment.newInstance(intent.getSerializableExtra(Keys.NOTIFICATIONS.key) as ArrayList<Notification>?), R.id.fragment_container, null, false)
        } else {
            FragmentsManager.replaceFragment(supportFragmentManager, CategoriesFragment.newInstance(), R.id.fragment_container, null, false)
        }
        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        /*val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val notifyAt = System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15
            am.setExact(AlarmManager.RTC_WAKEUP, notifyAt, PendingIntent.getBroadcast(this, Keys.REQUEST_CODE.ordinal, Intent(this, AlarmReceiver::class.java).setAction(Actions.NOTIFY.action).putExtra(Keys.FIRE_DATE.key, notifyAt).addCategory("" + notifyAt), PendingIntent.FLAG_UPDATE_CURRENT))
            am.setExact(AlarmManager.RTC_WAKEUP, notifyAt + AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15, PendingIntent.getBroadcast(this, Keys.REQUEST_CODE.ordinal, Intent(this, AlarmReceiver::class.java).setAction(Actions.ALERT.action).putExtra(Keys.FIRE_DATE.key, notifyAt).addCategory("" + notifyAt), PendingIntent.FLAG_UPDATE_CURRENT))
        }*/
    }
}