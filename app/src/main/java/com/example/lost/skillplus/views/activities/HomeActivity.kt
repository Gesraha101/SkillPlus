package com.example.lost.skillplus.views.activities


import android.app.job.JobScheduler
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.models.services.NotificationScheduler
import com.example.lost.skillplus.views.fragments.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_my_needs.*
import kotlinx.android.synthetic.main.fragment_my_skills.*
import org.aviran.cookiebar2.CookieBar


class HomeActivity : NavigationDrawerActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var notifications: ArrayList<Notification>

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 0) {//Check if there are no fragments at backstack
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            CookieBar.build(this@HomeActivity)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setMessage("Press back again to exit")
                    .setBackgroundColor(R.color.alert)
                    .show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else if (supportFragmentManager.findFragmentByTag("skill_learner_fragment")!!.isVisible) {
            main_my_skill.visibility = View.VISIBLE
            sec_my_skill.visibility = View.GONE

        } else if (supportFragmentManager.findFragmentByTag("need_form_fragment")!!.isVisible) {
            main_my_need.visibility = View.VISIBLE
            sec_my_need.visibility = View.GONE

        } else {
            if(supportFragmentManager.findFragmentByTag("details_frag_from_favorites")!!.isVisible)
                rv_favorites.visibility=View.VISIBLE

            supportFragmentManager.popBackStack()
        }
    }
    private fun loadFragment(item: MenuItem) {
        val tag = item.itemId.toString()
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (item.itemId) {
            R.id.navigation_categories -> {
                CategoriesFragment.newInstance()
            }
            R.id.navigation_favorites-> {
                FavoritesFragment.newInstance()
            }
            R.id.navigation_notifications -> {
                NotificationsFragment.newInstance(notifications)
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
            notifications = intent.getSerializableExtra(Keys.NOTIFICATIONS.key) as ArrayList<Notification>
            PreferencesManager(this@HomeActivity).setLastUpdated(System.currentTimeMillis())
            bottom_nav.selectedItemId = R.id.navigation_notifications
        } else if (intent.getSerializableExtra(Keys.NOTIFICATION.key) != null) {
            val notification = intent.getSerializableExtra(Keys.NOTIFICATION.key) as Notification
            when {
                notification.skill_name != null -> {                //Skill applied for
                    FragmentsManager.replaceFragment(supportFragmentManager, SkillLearnersFragment.newInstance(notification.skill_id!!), R.id.fragment_container, null, true)
                }
                notification.need_id != null -> {                   //Form proposed
                    FragmentsManager.replaceFragment(supportFragmentManager, NeedFormFragment.newInstance(notification.need_id, notification.form_id!!), R.id.fragment_container, null, true)
                }
                else -> {                                           //Form approved
                    FragmentsManager.replaceFragment(supportFragmentManager, MentoredNeedsFragment.newInstance(notification.form_id!!), R.id.fragment_container, null, true)
                }
            }
        } else {
            bottom_nav.selectedItemId = R.id.navigation_categories
        }
        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if(intent.getBooleanExtra("isComingAfterSubmition",false))
        {
            CookieBar.build(this@HomeActivity)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setMessage("Submitted successfully !")
                    .show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (getSystemService(JobScheduler::class.java).getPendingJob(Ids.JOB.ordinal) == null)
                NotificationScheduler.scheduleJob(this@HomeActivity, null)
        }
    }
}