package com.example.covid_19india

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.viewpager2.adapter.StatefulAdapter
import com.example.covid_19india.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header,binding!!.list,false))

        fetchResults()
    }

    private fun fetchResults() {
        GlobalScope.launch {
           val response= withContext(Dispatchers.IO){ Client.api.execute()}
            if(response.isSuccessful){
                val data=Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(1, data.statewise.size))
                }
            }
        }
    }
    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateAdapter=StateAdapter(subList)
        binding!!.list.adapter=stateAdapter
    }
    @SuppressLint("SetTextI18n")
    private fun bindCombinedData(data: StatewiseItem) {
        val lastUpdatedTime=data.lastupdatedtime
        val simpleDateFormat=SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        binding!!.tvlastupdate.text="Last Updated \n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"
      binding!!.tvconfirmed.text=data.confirmed
      binding!!.tvactive.text=data.active
      binding!!.tvRecovered.text=data.recovered
      binding!!.tvDeceased.text=data.deaths
    }
    fun getTimeAgo(past:Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes ago"
            }
            hours < 24 -> {
                "$hours hours ${minutes % 60} minutes ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(past.toString())
            }

        }
    }
}

