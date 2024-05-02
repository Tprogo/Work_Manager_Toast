package com.example.workmanagerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set condition for work manager to run

        // this is network requirement constraint

        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        // set worker request with interval of 15 minutes

        val workerRequest = PeriodicWorkRequest.Builder(WorkerClass::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        WorkManager.getInstance(this).enqueue(workerRequest)
    }
}