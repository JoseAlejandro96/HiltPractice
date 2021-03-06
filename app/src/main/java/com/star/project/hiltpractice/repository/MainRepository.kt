package com.star.project.hiltpractice.repository

import com.star.project.hiltpractice.model.Blog
import com.star.project.hiltpractice.retrofit.BlogRetrofit
import com.star.project.hiltpractice.retrofit.NetworkMapper
import com.star.project.hiltpractice.room.BlogDao
import com.star.project.hiltpractice.room.CacheMapper
import com.star.project.hiltpractice.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MainRepository
constructor(
    private val blogDao: BlogDao,
    private val blogRetrofit: BlogRetrofit,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
){
    suspend fun getBlogs(): Flow<DataState<List<Blog>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        try {
            val networkBlogs = blogRetrofit.get()
            val blogs = networkMapper.mapFromEntityList(networkBlogs)
            for(blog in blogs){
                blogDao.insert(cacheMapper.mapToEntity(blog))
            }
            val cachedBlogs = blogDao.get()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBlogs)))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}