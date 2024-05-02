package com.example.workmanagerapplication

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerClass (private val context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {

        // do whatever you want to perform here

        // without handler work manager for the toast won't work

        Handler(Looper.getMainLooper()).post {
            Log.d("TAG", "Work Manager Successfully Ran")
            Toast.makeText(context, "Work Manager Worked", Toast.LENGTH_LONG).show()
        }

        return Result.success()

    }
}