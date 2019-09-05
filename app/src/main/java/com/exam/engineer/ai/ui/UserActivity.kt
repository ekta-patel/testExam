package com.exam.engineer.ai.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.exam.engineer.ai.R
import com.exam.engineer.ai.api.UserApi
import kotlinx.android.synthetic.main.activity_main.*

class UserActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdapter()
        userViewModel.showUser("story")
    }

    private fun initAdapter() {
        adapter = UserAdapter { userViewModel.retry() }
        list.adapter = adapter
        userViewModel.posts.observe(this, Observer<PagedList<UserApi.ListingResponse.Data.Users>> {
            adapter.submitList(it)
        })
        userViewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }
}
