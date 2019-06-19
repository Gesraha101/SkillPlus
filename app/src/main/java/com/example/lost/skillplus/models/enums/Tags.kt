package com.example.lost.skillplus.models.enums

import com.example.lost.skillplus.R

enum class Tags(val tag: String) {
    APPLICANT_NOTIFICATION("applicant_notification"),
    FORM_RECEIVED("form_received"),
    FORM_APPROVED("form_approved"),
    LEARNER_FRAGMENT("skill_learner_fragment"),
    FORM_FRAGMENT("need_form_fragment"),
    DETAILS_FROM_FAVORITE("details_frag_from_favorites"),
    MY_NEEDS("my_needs"),
    MY_SKILLS("my_skills"),
    CURRENT_NEEDS("current_needs"),
    CURRENT_SKILLS("current_skills"),
    CATEGORIES_CONTAINER(R.id.navigation_categories.toString()),
    CATEGORIES_FROM_SIDE("categories_container"),
    FAVOURITES_CONTAINER("favourites_container"),
    NOTIFICATIONS_CONTAINER("notifications_container")

}