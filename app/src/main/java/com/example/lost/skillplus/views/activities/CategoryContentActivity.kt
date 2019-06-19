package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.RequestsAdapter
import com.example.lost.skillplus.models.adapters.SkillsAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.*
import com.example.lost.skillplus.models.podos.responses.FavouriteResponse
import com.example.lost.skillplus.models.podos.responses.PostsResponse
import com.example.lost.skillplus.views.fragments.RequestDetailsFragment
import com.example.lost.skillplus.views.fragments.SkillDetailsFragment
import kotlinx.android.synthetic.main.activity_category_content.*
import kotlinx.android.synthetic.main.fragment_category_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable


class CategoryContentActivity : AppCompatActivity() {
    companion object {
        lateinit var skillPlaceholder : LinearLayout //Memory leak , But i can't find another way
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var frag: Fragment? = null
    private var activatedCategory: Category? = null
    fun loadFragment(isSkill: Boolean?, paramPassed: Serializable) {
        val fragment : Fragment = if (isSkill!!) {
            SkillDetailsFragment.newInstance(paramPassed as Skill)
        } else {
            RequestDetailsFragment.newInstance(paramPassed as Request)
        }
        frag = fragment
        FragmentsManager.replaceFragment(supportFragmentManager, fragment, R.id.post_fragment_container, "details_fragment_from_categories", true)
        tabbed_view.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_category_content)
        super.onCreate(savedInstanceState)

        //initializing references of  static skills and needs
        PostsListFragment.skills = arrayListOf()
        PostsListFragment.needs = arrayListOf()

        //initializing references of  static skill placeholder
        skillPlaceholder = findViewById(R.id.skill_placeholder)

        //Handling selecting one of the tabs of viewpager to handle its placeholder
        var viewPager = findViewById<ViewPager>(R.id.container)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                if(position==0) {
                    if (PostsListFragment.skills.isEmpty())
                        skill_placeholder.visibility = View.VISIBLE

                    need_placeholder.visibility = View.GONE

                }
                else{
                    if(PostsListFragment.needs.isEmpty())
                        need_placeholder.visibility=View.VISIBLE

                    skill_placeholder.visibility=View.GONE
                    }

            }

        })

        activatedCategory = intent.getSerializableExtra(Keys.CATEGORY.key) as Category

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        btn_add.setOnClickListener {
            if (tabs.selectedTabPosition == 0) {
                startActivity(Intent(this, AddTeacherSkillActivity::class.java).putExtra(Keys.CATEGORY.key, activatedCategory))
            } else {
                startActivity(Intent(this, AddStudentNeedActivity::class.java).putExtra(Keys.CATEGORY.key, activatedCategory!!.cat_id))
            }
        }

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
            return PostsListFragment.newInstance(position, activatedCategory)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

    class PostsListFragment : Fragment() {


        var isSkill: Boolean? = false

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
            val call: Call<PostsResponse>? = service?.getCategoryPosts(ActivatedCategory(activatedCategory!!.cat_id, PreferencesManager(context!!).getId()))
            call?.enqueue(object : Callback<PostsResponse> {

                override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            rv_posts_list.apply {
                                // set a LinearLayoutManager to handle Android
                                // RecyclerView behavior
                                layoutManager = LinearLayoutManager(activity)

                                CategoryContentActivity.skillPlaceholder.visibility=View.VISIBLE  //make skill placeholder appear by default because skills are automatically selected
                                if (isSkill!! && response.body()?.skillsAndNeeds?.skills!!.isNotEmpty()) {
                                    CategoryContentActivity.skillPlaceholder.visibility=View.GONE  //disable its placeholder if there are actual data to display

                                    skills= (response.body()?.skillsAndNeeds?.skills as ArrayList<Skill>?)!!
                                    adapter = SkillsAdapter(response.body()!!.skillsAndNeeds.skills)
                                    (adapter as SkillsAdapter).onItemClick = { post ->
                                        (activity as CategoryContentActivity).loadFragment(isSkill, post)
                                    }
                                    (adapter as SkillsAdapter).onFavouriteClick= {post ->
                                        var favouriteUpdate= FavouriteUpdate(
                                                PreferencesManager(this@PostsListFragment.context!!).getId(),
                                                post.skill_id!!
                                        )

                                        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                                        val call: Call<FavouriteResponse>? = service?.updateFavourite(favouriteUpdate)
                                        call?.enqueue(object : Callback<FavouriteResponse> {
                                            override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                                                if (response.isSuccessful) {
                                                    if (response.body()?.status == true) {
                                                        if(response.body()?.message==" package added to favorite") {
                                                            Snackbar.make(view, "Added to your favourites !", Snackbar.LENGTH_SHORT).show()
                                                            post.is_favorite=true
                                                        }
                                                        else {
                                                            Snackbar.make(view, "Removed from your favourites !", Snackbar.LENGTH_SHORT).show()
                                                            post.is_favorite=false
                                                        }
                                                    } else {
                                                        Toast.makeText(this@PostsListFragment.context!!, "Failed1", Toast.LENGTH_LONG).show()

                                                    }
                                                } else {
                                                    Toast.makeText(this@PostsListFragment.context!!, "Failed2", Toast.LENGTH_LONG).show()

                                                    //Received response but not "OK" response i.e error in the request sent (Server can't handle this request)
                                                }
                                            }

                                            override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                                                Toast.makeText(this@PostsListFragment.context!!, t.message, Toast.LENGTH_LONG).show()
                                                //Error receiving response from server i.e error in podo received (Retrofit can't handle this response)
                                            }

                                        })
                                    }

                                } else if (!isSkill!! && response.body()?.skillsAndNeeds?.needs!!.isNotEmpty()){
                                    needs= (response.body()?.skillsAndNeeds?.needs as ArrayList<Request>?)!!
                                    adapter = RequestsAdapter(response.body()!!.skillsAndNeeds.needs)
                                    (adapter as RequestsAdapter).onItemClick = { post ->
                                        (activity as CategoryContentActivity).loadFragment(isSkill, post)
                                    }
                                } else
                                    isSkill=null
                            }
                        } else {
                            Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                    Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()
                }
            })
        }
        companion object {
            lateinit var skills: ArrayList<Skill>
            lateinit var needs: ArrayList<Request>

            private const val ARG_SECTION_NUMBER = "section_number"
            private const val ARG_ACTIVATED_CAT = "activated_cat"

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


