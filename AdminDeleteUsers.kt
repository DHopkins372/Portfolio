package com.example.activityrecommendationdeclanhopkinsmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.AdminItemAdapter
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.AdminUserItemAdapter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class AdminDeleteUsers : AppCompatActivity() {

    //open database
    val config = RealmConfiguration.Builder(
        schema = setOf(User::class)
    )
        .schemaVersion(3)
        .build()
    val realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete_users)

        populateRecycle()
    }

    private fun populateRecycle(){
        val users : RealmResults<User> = realm.query<User>().find()
        val adminItemAdapter =  AdminUserItemAdapter(this, users, realm)
        val recyclerViewAdmin : RecyclerView = findViewById(R.id.recyclerViewDeleteUsers)
        recyclerViewAdmin.layoutManager = LinearLayoutManager(this)
        recyclerViewAdmin.setHasFixedSize(true)
        recyclerViewAdmin.adapter = adminItemAdapter
    }
}