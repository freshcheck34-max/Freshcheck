package uk.ac.tees.mad.freshcheck

import android.app.Application
import androidx.work.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FreshCheckApp : Application()
//    , Configuration.Provider {
//
//    @Inject
//    lateinit var workerFactory: WorkerFactory
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun getWorkManagerConfiguration(): Configuration {
//        return Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//    }
//}