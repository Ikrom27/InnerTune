package com.zionhuang.music.repos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zionhuang.innertube.YouTube
import com.zionhuang.innertube.models.BaseItem
import com.zionhuang.innertube.models.BrowseEndpoint
import com.zionhuang.innertube.models.Section
import com.zionhuang.music.extensions.toPage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

object YouTubeRepository {
    fun searchAll(query: String) = object : PagingSource<String, Section>() {
        override suspend fun load(params: LoadParams<String>): LoadResult<String, Section> = withContext(IO) {
            try {
                YouTube.searchAllType(query).toPage()
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<String, Section>): String? = null
    }

    fun search(query: String, filter: YouTube.SearchFilter): PagingSource<String, Section> = object : PagingSource<String, Section>() {
        override suspend fun load(params: LoadParams<String>): LoadResult<String, Section> = withContext(IO) {
            try {
                if (params.key == null) YouTube.search(query, filter).toPage()
                else YouTube.search(YouTube.Continuation(params.key!!)).toPage()
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<String, Section>): String? = null
    }

    fun browse(endpoint: BrowseEndpoint): PagingSource<String, Section> = object : PagingSource<String, Section>() {
        override suspend fun load(params: LoadParams<String>): LoadResult<String, Section> = withContext(IO) {
            try {
                if (params.key == null) YouTube.browse(endpoint).toBrowseResult().toPage()
                else YouTube.browse(YouTube.Continuation(params.key!!)).toBrowseResult().toPage()
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<String, Section>): String? = null
    }

    suspend fun getSuggestions(query: String): List<BaseItem> = withContext(IO) {
        YouTube.getSearchSuggestions(query)
    }
}