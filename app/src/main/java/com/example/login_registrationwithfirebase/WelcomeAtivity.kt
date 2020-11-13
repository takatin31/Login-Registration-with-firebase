package com.example.login_registrationwithfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class WelcomeAtivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_ativity)

        mAuth = FirebaseAuth.getInstance()
        var logout = findViewById<FloatingActionButton>(R.id.logout)

        logout.setOnClickListener {
            mAuth!!.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser != null){
            val db = FirebaseFirestore.getInstance()
            Log.e("looog", currentUser.uid)
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        var welcomeMsg = findViewById<TextView>(R.id.welcomeMsg)
                        var phoneMsg = findViewById<TextView>(R.id.phonemsg)

                        val username = task.getResult()!!.getString("username")
                        val phone = task.getResult()!!.getString("phone")
                        welcomeMsg.text = "Welcome Back "+ username
                        phoneMsg.text = "Your Phone is "+phone

                    } else {
                        Log.e(
                            "looog",
                            "Error getting documents.")
                    }
                })

        }
    }
}



