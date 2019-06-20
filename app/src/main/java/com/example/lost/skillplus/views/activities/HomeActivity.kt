package com.example.lost.skillplus.views.activities


import android.app.job.JobScheduler
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.enums.Tags
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
    private var notifications: ArrayList<Notification>? = null
    private lateinit var navigation_view : NavigationView
    private lateinit var navigation_container : FrameLayout
    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount==0) {//Check if there are no fragments at backstack
            for (tag in Tags.values()) {
                val frag = supportFragmentManager.findFragmentByTag(tag.tag)
                if (frag != null) {
                    if (frag.isVisible) {
                        when (frag.tag) {
                            Tags.CATEGORIES_CONTAINER.tag ->{
                                if (doubleBackToExitPressedOnce)
                                    super.onBackPressed()

                                promptDoubleTabToGoBack()
                            }

                            Tags.CATEGORIES_CONTAINER_NEW.tag ->{
                                if (doubleBackToExitPressedOnce)
                                    super.onBackPressed()

                                promptDoubleTabToGoBack()
                            }

                            else ->{
                                bottom_nav.selectedItemId=R.id.navigation_categories
                            }

                        }

                    }
                }

            }

        }
        else {
            for (tag in Tags.values()) {
                val frag = supportFragmentManager.findFragmentByTag(tag.tag)
                if (frag != null) {
                    if (frag.isVisible) {
                        when (frag.tag) {
                            Tags.CATEGORIES_CONTAINER_NEW.tag ->{
                                if (doubleBackToExitPressedOnce) {
                                    supportFragmentManager.popBackStack()
                                    super.onBackPressed()
                                }

                                promptDoubleTabToGoBack()
                            }
                            Tags.FAVOURITES_CONTAINER.tag ->{
                                bottom_nav.selectedItemId=R.id.navigation_categories
                            }
                            Tags.NOTIFICATIONS_CONTAINER.tag ->{
                                bottom_nav.selectedItemId=R.id.navigation_categories
                            }
                            Tags.LEARNER_FRAGMENT.tag -> {
                                main_my_skill.visibility = View.VISIBLE
                                sec_my_skill.visibility = View.GONE
                                supportFragmentManager.popBackStack()

                            }
                            Tags.FORM_FRAGMENT.tag -> {
                                main_my_need.visibility = View.VISIBLE
                                sec_my_need.visibility = View.GONE
                                supportFragmentManager.popBackStack()
                            }
                            Tags.DETAILS_FROM_FAVORITE.tag -> {
                                rv_favorites.visibility = View.VISIBLE
                                supportFragmentManager.popBackStack()
                            }
                            Tags.FORM_RECEIVED.tag, Tags.FORM_APPROVED.tag, Tags.APPLICANT_NOTIFICATION.tag -> {
                                supportFragmentManager.popBackStack()
                                bottom_nav.selectedItemId = R.id.navigation_categories
                            }
                        }
                    }
                }
            }
        }

    }

    private fun promptDoubleTabToGoBack() {
        this.doubleBackToExitPressedOnce = true
        CookieBar.build(this@HomeActivity)
                .setCookiePosition(CookieBar.BOTTOM)
                .setMessage("Press back again to exit")
                .setBackgroundColor(R.color.alert)
                .show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun loadFragment(item: MenuItem) {
        val tag = item.itemId.toString()
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (item.itemId) {
            R.id.navigation_categories -> {
                supportActionBar!!.title="Categories"
                CategoriesFragment.newInstance()
            }
            R.id.navigation_favorites -> {
                supportActionBar!!.title="My Favourites"
                FavoritesFragment.newInstance()
            }
            R.id.navigation_notifications -> {
                supportActionBar!!.title="My Notifications"
                NotificationsFragment.newInstance(notifications)
            }
            else -> {
                null
            }
        }

        if (fragment != null) {
            FragmentsManager.replaceFragment(supportFragmentManager, fragment, R.id.fragment_container, tag, false)
        } else {
            navigation_view.menu.setGroupCheckable(0, false, true)
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)

         navigation_container = findViewById<FrameLayout>(R.id.fragment_container)
         navigation_view = findViewById<NavigationView>(R.id.nav_view)
        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        when {
            intent.getSerializableExtra(Keys.NOTIFICATIONS.key) != null -> {
                notifications = intent.getSerializableExtra(Keys.NOTIFICATIONS.key) as ArrayList<Notification>
                bottom_nav.selectedItemId = R.id.navigation_notifications
                PreferencesManager(this@HomeActivity).setIsNotified(false)
            }
            intent.getSerializableExtra(Keys.NOTIFICATION.key) != null -> {
                val notification = intent.getSerializableExtra(Keys.NOTIFICATION.key) as Notification
                when {
                    notification.skill_name != null -> {                //Skill applied for
                        FragmentsManager.replaceFragment(supportFragmentManager, SkillLearnersFragment.newInstance(notification.skill_id!!), R.id.fragment_container, Tags.APPLICANT_NOTIFICATION.tag, true)
                    }
                    notification.need_id != null -> {                   //Form proposed
                        FragmentsManager.replaceFragment(supportFragmentManager, NeedFormFragment.newInstance(notification.need_id, notification.form_id!!), R.id.fragment_container, Tags.FORM_RECEIVED.tag, true)
                    }
                    else -> {                                           //Form approved
                        FragmentsManager.replaceFragment(supportFragmentManager, MentoredNeedsFragment.newInstance(notification.form_id!!), R.id.fragment_container, Tags.FORM_APPROVED.tag, true)
                    }
                }
                navigation_view.menu.setGroupCheckable(0, false, true)
            }
            else -> bottom_nav.selectedItemId = R.id.navigation_categories
        }
        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (intent.getBooleanExtra("isComingAfterSubmition", false)) {
            CookieBar.build(this@HomeActivity)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setMessageColor(R.color.colorTabBarBackground)
                    .setMessage("Submitted successfully!")
                    .show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (getSystemService(JobScheduler::class.java).getPendingJob(Ids.JOB.ordinal) == null)
                NotificationScheduler.scheduleJob(this@HomeActivity, null)
        }
    }
}