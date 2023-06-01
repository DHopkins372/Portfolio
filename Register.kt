package com.example.activityrecommendationdeclanhopkinsmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import java.util.*


class Register : AppCompatActivity() {

    //open database
    private val config = RealmConfiguration.Builder(
        schema = setOf(User::class)
    )
        .schemaVersion(3)
        .build()
    private val realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //initialisation of widgets
        val btnRegister = findViewById<Button>(R.id.buttonRegisterReg)
        val edtEmail = findViewById<EditText>(R.id.editTextEmailReg)
        val edtPassword = findViewById<EditText>(R.id.editTextPasswordReg)
        val edtConfirmPass = findViewById<EditText>(R.id.editTextPasswordConfirm)

        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPassword.text.toString()
            val confirmPass = edtConfirmPass.text.toString()
            if ((email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) && (pass == confirmPass)) {
                val matchUser : User? =
                    realm.query<User>("userEmail = '$email'").first().find()
                if (matchUser == null){
                    realm.writeBlocking{
                        this.copyToRealm(User().apply {
                            _id
                            userEmail = email
                            userPass = pass
                            preferences
                            preferencesCompleted
                        })
                    }
                    realm.close()
                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Register, LogIn::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Account with this email already exists!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Error! Need both an Email and Password to register.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
