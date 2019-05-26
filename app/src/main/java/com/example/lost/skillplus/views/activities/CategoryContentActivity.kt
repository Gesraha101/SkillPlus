package com.example.lost.skillplus.views.activities

import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.RequestsAdapter
import com.example.lost.skillplus.models.adapters.SkillsAdapter
import com.example.lost.skillplus.models.podos.Request
import com.example.lost.skillplus.models.podos.Skill
import com.example.lost.skillplus.views.fragments.SkillDetailsFragment
import com.example.lost.skillplus.views.fragments.RequestDetailsFragment
import kotlinx.android.synthetic.main.activity_category_content.*
import kotlinx.android.synthetic.main.fragment_category_content.*
import java.io.Serializable

class CategoryContentActivity : AppCompatActivity(), SkillDetailsFragment.OnFragmentInteractionListener, RequestDetailsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var frag : Fragment? = null
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ replace(frameId, fragment) }
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ add(frameId, fragment) }
    }

    fun loadFragment(isSkill: Boolean, paramPassed: Serializable) {
        val fragment : Fragment = if (isSkill) {
            SkillDetailsFragment.newInstance(paramPassed as Skill)
        } else {
            RequestDetailsFragment.newInstance(paramPassed as Request)
        }
        addFragment(fragment, R.id.post_fragment_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_content)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_category_content, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PostsListFragment.newInstance(position)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

    class PostsListFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_category_content, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val list : List<Any>
            var isSkill = false
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 0) {
                list = listOf(
                        Skill("Guitar", "I am a top-notch musician with 5 years experience", null, 555.5F,"4.8", "Abd Elsame3")
                        ,Skill("Piano", "I am a top-notch pianist with 5 years experience", null, 555.5F, "5", "Toka")
                )
                isSkill = true
            } else {
                list = listOf(
                        Request("Carpentering", "Need a carpenter to fix my drawer", "2.5", "Gesraha")
                        ,Request("Plumbing", "Need a plumber to fix my sink", "2", "Khaled")

                )
            }

            rv_posts_list.apply {
                // set a LinearLayoutManager to handle Android
                // RecyclerView behavior
                layoutManager = LinearLayoutManager(activity)

                adapter = if (isSkill)
                    SkillsAdapter(list as List<Skill>)
                else
                    RequestsAdapter(list as List<Request>)
                if (isSkill) {
                    (adapter as SkillsAdapter).onItemClick = { post ->
                        (activity as CategoryContentActivity).loadFragment(isSkill, post)
                    }
                } else {
                    (adapter as RequestsAdapter).onItemClick = { post ->
                        (activity as CategoryContentActivity).loadFragment(isSkill, post)
                    }
                }

            }
        }

        companion object {

            private const val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PostsListFragment {
                val fragment = PostsListFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
