package com.star.project.hiltpractice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.star.project.hiltpractice.databinding.ActivityMainBinding
import com.star.project.hiltpractice.model.Blog
import com.star.project.hiltpractice.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding

    private val TAG : String = "AppDebug"

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer {dataState->
            when(dataState){
                is DataState.Success<List<Blog>> ->{
                    displayProgressBar(false)
                    appendBlogTitles(dataState.data)
                }
                is DataState.Error ->{
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading ->{
                    displayProgressBar(true)

                }
            }
        })
    }

    private fun displayError(message: String?){
        if (message!=null){
            binding.textView.text = message
        }else{
            binding.textView.text = "Unknown error"
        }
    }
    private fun displayProgressBar(isDisplayed: Boolean){
        binding.progressBar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }
    private fun appendBlogTitles(blogs: List<Blog>){
        val sb = StringBuilder()
        for(blog in blogs){
            sb.append(blog.title + "\n")
        }
        binding.textView.text = sb.toString()
    }
}