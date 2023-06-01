package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class ProfileManagement : AppCompatActivity() {

    private var userId : String? = ""

    //open database
    private val config = RealmConfiguration.Builder(
        schema = setOf(User::class)
    )
        .schemaVersion(3)
        .build()
    private val realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_management)

        val edtViewUpEmail : EditText = findViewById(R.id.editTextUpEmail)
        val edtViewUpPass: EditText = findViewById(R.id.editTextPassword)
        val btnUpdateEmail: Button = findViewById(R.id.buttonUpdateEmail)
        val btnUpdatePass: Button = findViewById(R.id.buttonUpdatePass)
        val imgViewHome : ImageView = findViewById(R.id.imgViewHomeProf)
        val imgViewProfile : ImageView = findViewById(R.id.imgViewProfileProf)

        //Collect parsed Intent
        val bundle : Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val id : String? = getString("userId")
                userId = id
            }
        }

        //initialise on clicks for buttons
        imgViewHome.setOnClickListener {
            val intent = Intent(this@ProfileManagement, HomePage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        imgViewProfile.setOnClickListener {
            val intent = Intent(this@ProfileManagement, ProfileManagement::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }


        btnUpdateEmail.setOnClickListener {
            val email = edtViewUpEmail.text.toString()
            if (email.isNotEmpty()){
                val user:User? =
                    realm.query<User>("_id == oid($userId)").first().find()
                realm.writeBlocking {
                    if (user != null) {
                        findLatest(user)
                            ?.also { it.userEmail = email }
                    }
                }
                edtViewUpEmail.setText("")
                Toast.makeText(this,"Email Updated!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Nothing was inputted to Update", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdatePass.setOnClickListener {
            val pass = edtViewUpPass.text.toString()
            if (pass.isNotEmpty()){
                val user:User? =
                    realm.query<User>("_id == oid($userId)").first().find()
                realm.writeBlocking {
                    if (user != null) {
                        findLatest(user)
                            ?.also { it.userPass = pass }
                    }
                }
                edtViewUpPass.setText("")
                Toast.makeText(this, "Password Updated!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Nothing was inputted to Update", Toast.LENGTH_SHORT).show()
            }
        }

    }
}