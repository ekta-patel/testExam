/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exam.engineer.ai.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.exam.engineer.ai.api.UserApi
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class UserDataSourceFactory(
    private val redditApi: UserApi,
    private val subredditName: String,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, UserApi.ListingResponse.Data.Users>() {
    val sourceLiveData = MutableLiveData<UserDataSource>()
    override fun create(): DataSource<Int, UserApi.ListingResponse.Data.Users> {
        val source = UserDataSource(
            redditApi,
            retryExecutor
        )
        sourceLiveData.postValue(source)
        return source
    }
}
