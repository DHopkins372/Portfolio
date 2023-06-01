package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activityrecommendationdeclanhopkinsmaster.adapter.ReviewAdapter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.dynamic.DynamicMutableRealmObject
import io.realm.kotlin.dynamic.DynamicRealmObject
import io.realm.kotlin.dynamic.getValue
import io.realm.kotlin.ext.query
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId

class ActivityPage : AppCompatActivity() {
    private var actId : String = ""
    private var userId : String = ""
    private var actEmail : String = ""
    private var actName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        //Initialise Widgets in Activity
        val txtViewTitle : TextView = findViewById(R.id.textViewTitle)
        val txtViewDescription : TextView = findViewById(R.id.textViewDescription)
        val imgViewHome : ImageView = findViewById(R.id.imgViewHomeAct)
        val imgViewProf : ImageView = findViewById(R.id.imgViewProfileAct)
        val editTextReview : EditText = findViewById(R.id.editTextReview)
        val ratingBarStar : RatingBar = findViewById(R.id.ratingBar)
        val btnSubmit : Button = findViewById(R.id.buttonSubmit)
        val btnEmail : Button = findViewById(R.id.buttonEmail)
        val btnFind : Button = findViewById(R.id.buttonFindActivity)
        val recyclerViewReviews : RecyclerView = findViewById(R.id.recyclerViewReviews)

        //Collect parsed Intent
        val bundle = intent.extras
        actId = bundle!!.getString("actId","default")
        userId = bundle!!.getString("userId", "default")

        //open database for Activity
        val config = RealmConfiguration.Builder(
            schema = setOf(Activity::class)
        )
            .schemaVersion(3)
            .build()
        val realmActivity = Realm.open(config)
        //query database for parsed activity
        val matchActivity : Activity? =
            realmActivity.query<Activity>("_id == oid($actId)").first().find()

        //fill Widgets and On click listener
        if (matchActivity != null) {
            txtViewTitle.text = matchActivity.name
            txtViewDescription.text = matchActivity.desc
            actEmail = matchActivity.email
            actName = matchActivity.name
        }else{
            Log.d("Error", "Returned null object from database")
        }

        //open database for review
        val configRev = RealmConfiguration.Builder(
            schema = setOf(Review::class)
        )
            .schemaVersion(3)
            .build()
        val realmReview = Realm.open(configRev)
        //populate recyclerView with reviews function
        populateRecycle(recyclerViewReviews, realmReview)

        //initialise on clicks for buttons
        imgViewHome.setOnClickListener {
            realmActivity.close()
            realmReview.close()
            val intent = Intent(this@ActivityPage, HomePage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        imgViewProf.setOnClickListener {
            realmActivity.close()
            realmReview.close()
            val intent = Intent(this@ActivityPage, ProfileManagement::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        btnEmail.setOnClickListener {
            realmActivity.close()
            realmReview.close()
            val bundle = bundle
            val intent = Intent(this@ActivityPage, Email::class.java)
            bundle?.putString("email", actEmail)
            bundle?.putString("userId", userId)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

        btnFind.setOnClickListener {
            val address : String = matchActivity?.address.toString()
            val gmmIntent = Uri.parse("google.navigation:q=$address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntent)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        btnSubmit.setOnClickListener {
            val reviewContent : String = editTextReview.text.toString()
            val rating : Float = ratingBarStar.rating

            if(reviewContent.isNotEmpty() && rating != 0.0F){
                realmReview.writeBlocking{
                    this.copyToRealm(Review().apply {
                        _id = ObjectId.create()
                        activityReviewed = matchActivity?.name.toString()
                        username = userId
                        review = reviewContent
                        starRating = rating
                    })
                }
                Toast.makeText(this,"Review Posted!",Toast.LENGTH_SHORT).show()
                editTextReview.setText("")
                ratingBarStar.numStars = 0
                populateRecycle(recyclerViewReviews, realmReview)
            }else{
                Toast.makeText(this,"Please Fill All Fields!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateRecycle(recyclerViewReviews : RecyclerView, realmReview : Realm){
        val reviews : RealmResults<Review> = realmReview.query<Review>("activityReviewed = '$actName'").find()
        val reviewItemAdapter =  ReviewAdapter(this, reviews)
        recyclerViewReviews.layoutManager = LinearLayoutManager(this)
        recyclerViewReviews.setHasFixedSize(true)
        recyclerViewReviews.adapter = reviewItemAdapter
    }
}