package com.example.activityrecommendationdeclanhopkinsmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.AdminItemAdapter
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.ItemAdapter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class AdminDelete : AppCompatActivity() {

    //open database
    val config = RealmConfiguration.Builder(
        schema = setOf(Activity::class)
    )
        .schemaVersion(3)
        .build()
    val realm = Realm.open(config)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete)

        populateRecycle()
    }

    private fun populateRecycle(){
        val activities : RealmResults<Activity> = realm.query<Activity>().find()
        val adminItemAdapter =  AdminItemAdapter(this, activities, realm)
        val recyclerViewAdmin : RecyclerView = findViewById(R.id.recyclerViewDeleteActivity)
        recyclerViewAdmin.layoutManager = LinearLayoutManager(this)
        recyclerViewAdmin.setHasFixedSize(true)
        recyclerViewAdmin.adapter = adminItemAdapter
    }

}