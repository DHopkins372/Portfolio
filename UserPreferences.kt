package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class UserPreferences : AppCompatActivity() {
    var userId : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_preferences)

        val bundle : Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val id : String? = getString("userId")
                userId = id
            }
        }

        //initialise variables
        val rgQ1 : RadioGroup = findViewById(R.id.rgQ1)
        val rgQ2 : RadioGroup = findViewById(R.id.rgQ2)
        val rgQ3 : RadioGroup = findViewById(R.id.rgQ3)
        val rgQ4 : RadioGroup = findViewById(R.id.rgQ4)
        val btnQuestionnaireSubmit : Button = findViewById(R.id.buttonQuestionnaireSubmit)

        btnQuestionnaireSubmit.setOnClickListener {

            //open database
            val config = RealmConfiguration.Builder(
                schema = setOf(User::class)
            )
                .schemaVersion(3)
                .build()
            val realm = Realm.open(config)


            val answer1: RadioButton = findViewById(rgQ1.checkedRadioButtonId)
            val answer2: RadioButton = findViewById(rgQ2.checkedRadioButtonId)
            val answer3: RadioButton = findViewById(rgQ3.checkedRadioButtonId)
            val answer4: RadioButton = findViewById(rgQ4.checkedRadioButtonId)

            var preferences: Array<String> = Array(4){"";"";"";""}
            if (answer1.tag.toString().toInt() < 3) {
                val preference = "Low Mobility"
                preferences[0] = preference
            } else {
                val preference = "Good Mobility"
                preferences[0] = preference
            }

            if (answer2.tag.toString().toInt() < 3) {
                val preference = "null"
                preferences[1] = preference
            } else {
                val preference = "Physical Activities"
                preferences[1] = preference
            }

            if (answer3.tag.toString().toInt() < 3) {
                val preference = "null"
                preferences[2] = preference
            } else {
                val preference = "Social Activities"
                preferences[2] = preference
            }

            if (answer4.tag.toString().toInt() < 3) {
                val preference = "null"
                preferences[3] = preference
            } else {
                val preference = "Learning Activities"
                preferences[3] = preference
            }
            val user: User? =
                realm.query<User>("_id == oid($userId)").first().find()
            realm.writeBlocking {
                if(user != null){
                    findLatest(user)
                        ?.also {
                            it.preferencesCompleted = true
                            it.preferences[0] = preferences[0]
                            it.preferences[1] = preferences[1]
                            it.preferences[2] = preferences[2]
                            it.preferences[3] = preferences[3]
                        }
                }
            }
            realm.close()
            val intent = Intent(this@UserPreferences, HomePage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
    }
}