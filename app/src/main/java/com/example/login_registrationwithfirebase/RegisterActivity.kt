package com.example.login_registrationwithfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        val loginBtn = findViewById<TextView>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener {
            val email = findViewById<TextView>(R.id.email).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()

            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {

                            Log.d("looog", "createUserWithEmail:success")
                            val user = mAuth!!.currentUser

                            val db = FirebaseFirestore.getInstance()
                            val userData: HashMap<String, Any> = HashMap<String, Any>()

                            val username = findViewById<TextView>(R.id.username).text.toString()
                            val phone = findViewById<TextView>(R.id.phone).text.toString()
                            userData["username"] = username
                            userData["phone"] = phone

                            val userClass = User(username, email, phone)

                            db.collection("users").document(user!!.uid)
                                .set(userClass)
                                .addOnSuccessListener {


                                    user!!.sendEmailVerification()
                                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this, "Please check your email for verification",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                var intent = Intent(this, LoginActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }else{
                                                Toast.makeText(
                                                    this, "Registration failed.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this, "Registration failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }



                        } else {
                            Toast.makeText(
                                this, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    })
        }
    }
}