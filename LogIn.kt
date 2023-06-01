package com.example.activityrecommendationdeclanhopkinsmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class LogIn : AppCompatActivity() {

    //open database
    private val config = RealmConfiguration.Builder(
        schema = setOf(User::class)
    )
        .schemaVersion(3)
        .build()
    private val realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //initialisation of widget elements
        val btnLogin = findViewById<Button>(R.id.buttonLogIn)
        val btnRegister = findViewById<Button>(R.id.buttonRegister)
        val edtEmail = findViewById<EditText>(R.id.editTextEmail)
        val edtPassword = findViewById<EditText>(R.id.editTextPassword)


        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                val matchUser : User? =
                    realm.query<User>("userEmail = '$email' AND userPass = '$pass'").first().find()
                if (matchUser != null) {
                    matchUser.temp_id = matchUser._id.toString()
                    if (matchUser.userEmail == email && matchUser.userPass == pass){
                        if(email.equals("admin")){
                            val intent = Intent(this@LogIn, Admin::class.java)
                            realm.close()
                            startActivity(intent)
                            finish()
                        }else if(matchUser.preferencesCompleted == false){
                            val intent = Intent(this@LogIn, UserPreferences::class.java)
                            intent.putExtra("userId", matchUser.temp_id)
                            realm.close()
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(this@LogIn, HomePage::class.java)
                            intent.putExtra("userId", matchUser.temp_id)
                            realm.close()
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        Toast.makeText(this,"Account Password is wrong please try again", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"Account Not found. Email could be entered incorrectly. Please try again", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Please enter an Email and Password to login.", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            realm.close()
            val intent = Intent(this@LogIn, Register::class.java)
            startActivity(intent)
            finish()
        }
    }
}