package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.query.RealmResults
import java.util.*

class Admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //initialise buttons and textViews
        val btnDeleteActivities : Button = findViewById(R.id.buttonDeleteActivities)
        val btnCreateActivity : Button = findViewById(R.id.buttonCreateActivity)
        val btnDeleteUsers : Button = findViewById(R.id.buttonDeleteUsers)
        val edtViewName : EditText = findViewById(R.id.editTextActivityName)
        val edtViewDesc : EditText = findViewById(R.id.editTextTextMultiLine)
        val edtTxtAdd : EditText = findViewById(R.id.editTextTextPostalAddress)
        val edtTxtEmail : EditText = findViewById(R.id.editTextEmailAddress)
        val edtTxtUrl : EditText = findViewById(R.id.editTextImageUrl)
        val tagsSpinner1 : Spinner = findViewById(R.id.tags_spinner1)

        val config = RealmConfiguration.Builder(
            schema = setOf(Activity::class)
        )
            .schemaVersion(3)
            .build()
        val realm = Realm.open(config)

        populateSpinner(tagsSpinner1)

        btnCreateActivity.setOnClickListener {

            val tag = tagsSpinner1.selectedItem.toString()
            val activityName = edtViewName.text.toString()
            val activityDescription = edtViewDesc.text.toString()
            val activityAddress = edtTxtAdd.text.toString()
            val activityEmail = edtTxtEmail.text.toString()
            var url = edtTxtUrl.text.toString()
            if (url == ""){
                url = "null"
            }
            if (activityName.isNotEmpty() && activityDescription.isNotEmpty() && activityAddress.isNotEmpty()) {
                val match : Activity? =
                    realm.query<Activity>("name ='$activityName'").first().find()
                if(match == null){
                    realm.writeBlocking {
                        this.copyToRealm(Activity().apply {
                            _id
                            name = activityName
                            desc = activityDescription
                            address = activityAddress
                            email = activityEmail
                            tags = tag
                            imageUrl = url
                        })
                    }
                    edtViewName.setText("")
                    edtViewDesc.setText("")
                    edtTxtAdd.setText("")
                    edtTxtEmail.setText("")
                    edtTxtUrl.setText("")
                    Toast.makeText(this, "Activity Created and put into Database", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Actvity already exists!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "ERROR! Not all required fields have been filled", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeleteActivities.setOnClickListener {
            realm.close()
            startActivity(Intent(this@Admin, AdminDelete::class.java))
        }

        btnDeleteUsers.setOnClickListener {
            realm.close()
            startActivity(Intent(this@Admin, AdminDeleteUsers::class.java))
        }
    }

    private fun populateSpinner(spinner: Spinner){
        ArrayAdapter.createFromResource(
            this,
            R.array.tags_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = adapter
        }
    }
}