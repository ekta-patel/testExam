package com.exam.engineer.ai.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.exam.engineer.ai.util.Listing
import com.exam.engineer.ai.api.UserApi
import java.util.concurrent.Executor

class UserRepository(
    private val redditApi: UserApi,
    private val networkExecutor: Executor
) {
    @MainThread
    fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<UserApi.ListingResponse.Data.Users> {
        val sourceFactory = UserDataSourceFactory(
                redditApi,
                subReddit,
                networkExecutor
        )

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
                pageSize = pageSize,
                // provide custom executor for network requests, otherwise it will default to
                // Arch Components' IO pool which is also used for disk access
                fetchExecutor = networkExecutor
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}

