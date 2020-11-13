package com.example.login_registrationwithfirebase

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        var registerBtn = findViewById<TextView>(R.id.registerBtn)
        registerBtn.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        var loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val email = findViewById<TextView>(R.id.email).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()

            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            if (mAuth!!.currentUser!!.isEmailVerified){
                                Log.d("looog", "signInWithEmail:success")
                                val user = mAuth!!.currentUser
                                Toast.makeText(
                                    this, "Welcome",
                                    Toast.LENGTH_SHORT
                                ).show()

                                var intent = Intent(this, WelcomeAtivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(
                                    this, "Please verify your email first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(this, "signInWithEmail failed.",
                                    Toast.LENGTH_SHORT).show()
                        }

                    }
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser != null){
            if (currentUser.isEmailVerified){
                var intent = Intent(this, WelcomeAtivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}