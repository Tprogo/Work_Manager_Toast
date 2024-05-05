package com.example.workmanagerapplication

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {



    private lateinit var darkModeBtn: Switch

    private lateinit var setThemeText: TextView

    private var isInitialLoad = true

//    init {
//        checkSavedThemeMode()
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         darkModeBtn = findViewById(R.id.switchBtn)
         setThemeText = findViewById<TextView>(R.id.setTheme)

        setThemeText.text = "Enable Dark Mode"

    checkSavedThemeMode()






        // check the theme mode


        val pDatastore = PDatastore(this@MainActivity)







        //set condition for work manager to run

        // this is network requirement constraint

        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        // set worker request with interval of 15 minutes

        val workerRequest = PeriodicWorkRequest.Builder(WorkerClass::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        WorkManager.getInstance(this).enqueue(workerRequest)



//         check the theme(dark or light)
//    fun isDarkThemeEnabled(context: Context): Boolean {
//        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
//    }
//
//    if (isDarkThemeEnabled(this)){
//        darkModeBtn.isChecked = true
//    } else {
//        darkModeBtn.isChecked = false
//    }


        // set dark mode with the help of switch button

        darkModeBtn.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked){

            setThemeMode(true)
            CoroutineScope(Dispatchers.IO).launch {
                pDatastore.saveThemeState(true)
            }
            } else {
                setThemeMode(false)
            CoroutineScope(Dispatchers.IO).launch {
                pDatastore.saveThemeState(false)
            }
        }

            Log.d("Saved TAG","State in DataStore $isChecked")
        }


    }

    private fun setThemeMode(flag: Boolean) {
        if (flag){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setThemeText.text = "Disable Dark Mode"




        } else{

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setThemeText.text = "Enable Dark Mode"


        }
    }

    private fun checkSavedThemeMode(){
        CoroutineScope(Dispatchers.Main).launch{
            val pDatastore = PDatastore(this@MainActivity)

            val dataState = pDatastore.getThemeState().first()


                Log.d("Retrieve TAG","State in DataStore $dataState")


            if (dataState == true){
                setThemeMode(true)
                darkModeBtn.isChecked = true
            } else {
                setThemeMode(false)
                darkModeBtn.isChecked = false
            }





            }






    }
}