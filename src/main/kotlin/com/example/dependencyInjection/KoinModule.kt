package com.example.dependencyInjection

import com.example.data.repository.UserDataSourceImpl
import com.example.domain.repository.UserDataSource
import com.example.utils.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo.createClient(/*System.getenv(Constants.MONGODB_URI)*/)
            .coroutine
            .getDatabase(Constants.DATABASE_NAME)
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
}