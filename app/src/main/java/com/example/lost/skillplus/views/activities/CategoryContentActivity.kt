package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.RequestsAdapter
import com.example.lost.skillplus.models.adapters.SkillsAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.podos.raw.ActivatedCategory
import com.example.lost.skillplus.models.podos.raw.Category
import com.example.lost.skillplus.models.podos.raw.Request
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.responses.PostsResponse
import com.example.lost.skillplus.views.fragments.RequestDetailsFragment
import com.example.lost.skillplus.views.fragments.SkillDetailsFragment
import kotlinx.android.synthetic.main.activity_category_content.*
import kotlinx.android.synthetic.main.fragment_category_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class CategoryContentActivity : AppCompatActivity(), SkillDetailsFragment.OnFragmentInteractionListener, RequestDetailsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var frag: Fragment? = null
    private var activatedCategory: Category? = null

    fun loadFragment(paramPassed: Serializable) {
        val fragment : Fragment = if (PostsListFragment.isSkill!!) {
            SkillDetailsFragment.newInstance(paramPassed as Skill)
        } else {
            RequestDetailsFragment.newInstance(paramPassed as Request)
        }

        btn_add.setOnClickListener {
            if (PostsListFragment.isSkill == true) {
                startActivity(Intent(this@CategoryContentActivity, AddTeacherSkillActivity::class.java))
                //TODO
            } else {
                startActivity(Intent(this@CategoryContentActivity, AddNeedActivity::class.java))
                //TODO
            }
        }
        frag = fragment
        FragmentsManager.replaceFragment(supportFragmentManager, fragment, R.id.post_fragment_container, null, true)
        tabbed_view.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_category_content)
        super.onCreate(savedInstanceState)
        activatedCategory = intent.getSerializableExtra("CATEGORY") as Category

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
        // automatically handle clicks on the Home/Up button_layout, so long
        // as you specify a parent activity in AndroidManifest.xml.

        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (frag != null) {
            if (frag!!.isVisible)
                tabbed_view.visibility = View.VISIBLE
        }
        super.onBackPressed()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PostsListFragment.newInstance(position, activatedCategory)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

    class PostsListFragment : Fragment() {

        private var activatedCategory: Category? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_category_content, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            activatedCategory = arguments?.getSerializable(ARG_ACTIVATED_CAT) as Category
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 0) {
                isSkill = true
            }
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<PostsResponse>? = service?.getCategoryPosts(ActivatedCategory(activatedCategory!!.cat_id))
            call?.enqueue(object : Callback<PostsResponse> {

                override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            rv_posts_list.apply {
                                // set a LinearLayoutManager to handle Android
                                // RecyclerView behavior
                                layoutManager = LinearLayoutManager(activity)
                                if (isSkill!! && response.body()?.skillsAndNeeds?.skills!!.isNotEmpty()) {
                                    adapter = SkillsAdapter(response.body()!!.skillsAndNeeds.skills)
                                    (adapter as SkillsAdapter).onItemClick = { post ->
                                        (activity as CategoryContentActivity).loadFragment(post)
                                    }
                                } else if (!isSkill!! && response.body()?.skillsAndNeeds?.needs!!.isNotEmpty()){
                                    adapter = RequestsAdapter(response.body()!!.skillsAndNeeds.needs)
                                    (adapter as RequestsAdapter).onItemClick = { post ->
                                        (activity as CategoryContentActivity).loadFragment(post)
                                    }
                                } else
                                    isSkill = null
                            }
                        } else {
                            Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
                }
            })
        }

        companion object {

            private const val ARG_SECTION_NUMBER = "section_number"
            private const val ARG_ACTIVATED_CAT = "activated_cat"
            var isSkill: Boolean? = false

            fun newInstance(sectionNumber: Int, activatedCategory: Category?): PostsListFragment {
                val fragment = PostsListFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                args.putSerializable(ARG_ACTIVATED_CAT, activatedCategory)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
