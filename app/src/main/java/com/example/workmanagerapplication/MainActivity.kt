package com.example.workmanagerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {


    private var savedState: Boolean? = null
    private lateinit var darkModeBtn: Switch

    private lateinit var setThemeText: TextView
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


        // set dark theme with the help of switch button

        darkModeBtn.setOnCheckedChangeListener { _, isChecked ->

            setThemeMode(isChecked)
            CoroutineScope(Dispatchers.Main).launch {
                pDatastore.saveThemeState(isChecked)
            }
        }
    }

    private fun setThemeMode(flag: Boolean){
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

            pDatastore.getThemeState().collect{it ->

                savedState = it

                if (it != null) {
                    darkModeBtn.isChecked = it
                }





            }





        }
    }
}