package com.star.project.hiltpractice.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.star.project.hiltpractice.model.Blog
import com.star.project.hiltpractice.repository.MainRepository
import com.star.project.hiltpractice.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
):ViewModel(){

    private val _dataState: MutableLiveData<DataState<List<Blog>>> = MutableLiveData()

    val dataState : LiveData<DataState<List<Blog>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetBlogEvents ->{
                    mainRepository.getBlogs()
                        .onEach {
                            _dataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None ->{
                    // do nothing
                }
            }
        }
    }
}

sealed class MainStateEvent{

    object GetBlogEvents: MainStateEvent()

    object None: MainStateEvent()
}