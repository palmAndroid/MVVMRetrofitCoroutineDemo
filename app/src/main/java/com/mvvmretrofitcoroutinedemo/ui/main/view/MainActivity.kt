package com.mvvmretrofitcoroutinedemo.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvmretrofitcoroutinedemo.R
import com.mvvmretrofitcoroutinedemo.data.api.ApiHelper
import com.mvvmretrofitcoroutinedemo.data.api.ApiService
import com.mvvmretrofitcoroutinedemo.data.api.RetrofitBuilder
import com.mvvmretrofitcoroutinedemo.data.model.User
import com.mvvmretrofitcoroutinedemo.ui.base.ViewModelFactory
import com.mvvmretrofitcoroutinedemo.ui.main.adapter.MainAdapter
import com.mvvmretrofitcoroutinedemo.ui.main.viewmodel.MainViewModel
import com.mvvmretrofitcoroutinedemo.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
    }


    private fun setupViewModel() {

        var apiHelper = ApiHelper(RetrofitBuilder.apiService)
        viewModel = ViewModelProvider(this,ViewModelFactory(apiHelper))
            .get(MainViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}