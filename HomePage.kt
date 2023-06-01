package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.ItemAdapter
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.ReviewAdapter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlin.math.log


class HomePage : AppCompatActivity() {

    lateinit var realmActivity : Realm
    var userId : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        //initialisation of widgets
        val imgViewHome = findViewById<ImageView>(R.id.imgViewHome)
        val imgViewProf = findViewById<ImageView>(R.id.imgViewProfile)
        val searchViewSearch = findViewById<SearchView>(R.id.searchViewSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActivities)
        //Collect parsed Intent
        val bundle : Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val id : String? = getString("userId")
                userId = id
            }
        }

        //open database
        val configActivity = RealmConfiguration.Builder(
            schema = setOf(Activity::class)
        )
            .schemaVersion(3)
            .build()
        realmActivity = Realm.open(configActivity)

        //fill recycler view with values from Database
        val bundleNew = Bundle()
        bundleNew.putString("userId" , userId)
        populateRecycle(recyclerView, bundleNew)

        //initialise on clicks for buttons
        imgViewHome.setOnClickListener {
            realmActivity.close()
            val intent = Intent(this@HomePage, HomePage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        imgViewProf.setOnClickListener {
            realmActivity.close()
            val intent = Intent(this@HomePage, ProfileManagement::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        searchViewSearch.setOnSearchClickListener {
            searchViewSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(search: String): Boolean {
                    searchViewSearch.clearFocus()
                    populateRecycleOnSearch(recyclerView, bundleNew, search)
                    return false
                }

                override fun onQueryTextChange(newSearch: String): Boolean {
                    populateRecycleOnSearch(recyclerView, bundleNew, newSearch)
                    return false
                }
            })
        }
    }

    private fun populateRecycle(recyclerView : RecyclerView, bundleNew : Bundle){
        val configUser = RealmConfiguration.Builder(
            schema = setOf(User::class)
        )
            .schemaVersion(3)
            .build()
        val realmUser = Realm.open(configUser)

        //val matchUser : User? = realmUser.query<User>("_id == oid($userId)").first().find()
        //val activities : RealmResults<Activity> = realmActivity.query<Activity>(
        //    "tags = '${matchUser?.preferences?.get(0)}' OR tags = '${matchUser?.preferences?.get(1)}' OR tags = '${matchUser?.preferences?.get(2)}' OR tags = '${matchUser?.preferences?.get(3)}'"
        //).find()

        val activities : RealmResults<Activity> = realmActivity.query<Activity>().find()

        Log.d("ERROR", activities.toString())

        var itemAdapter = ItemAdapter(this, activities, bundleNew)

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        realmUser.close()
    }

    private fun populateRecycleOnSearch(recyclerView : RecyclerView, bundleNew : Bundle, search : String){
        val activities : RealmResults<Activity> = realmActivity.query<Activity>(
            "name CONTAINS[c] '$search' OR desc CONTAINS[c] '$search'"
        ).find()
        val itemAdapter = ItemAdapter(this, activities, bundleNew)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = itemAdapter
    }
}