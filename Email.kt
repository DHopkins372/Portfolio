package com.example.activityrecommendationdeclanhopkinsmaster

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class Email : AppCompatActivity() {

    private var to : String = ""
    private var userId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val txtViewTo : TextView = findViewById(R.id.textViewTo)
        val edtTxtSubject : EditText = findViewById(R.id.editTextSubject)
        val edtTxtMessage : EditText = findViewById(R.id.editTextMessage)
        val btnSend : Button = findViewById(R.id.buttonSend)
        val imgViewHome : ImageView =findViewById(R.id.imgViewHomeEmail)
        val imgViewProf : ImageView =findViewById(R.id.imgViewProfileEmail)

        //Collect parsed Intent
        val bundle : Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val id : String = getString("email").toString()
                to = id

                val user : String = getString("userId").toString()
                userId = user
            }
        }

        txtViewTo.text = to

        //initialise on clicks for buttons
        imgViewHome.setOnClickListener {
            val intent = Intent(this@Email, ProfileManagement::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        imgViewProf.setOnClickListener {
            val intent = Intent(this@Email, ProfileManagement::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }


        btnSend.setOnClickListener {
            val subject: String = edtTxtSubject.text.toString()
            val message : String = edtTxtMessage.text.toString()

            if (to.isNotEmpty() && subject.isNotEmpty() && message.isNotEmpty()){
                sendEmail(subject, message)
            }else{
                Toast.makeText(this,"Please Fill All Fields!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendEmail(subject: String, message : String){
        val email = Intent(Intent.ACTION_SEND)
        email.data = Uri.parse("mailto:")
        email.type = "text/plain"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
        email.putExtra(Intent.EXTRA_TEXT, message)


        startActivity(Intent.createChooser(email, "Sign Up"))
    }
}