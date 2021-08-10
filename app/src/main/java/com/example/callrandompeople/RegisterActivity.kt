package com.example.callrandompeople

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color.red
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        progress_register.visibility = View.GONE
        login_button_register_screen.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupActionBar()

        
        register_button_register_screen.setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_registration_activity as Toolbar?)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        (toolbar_registration_activity as Toolbar?)?.setNavigationOnClickListener { onBackPressed() }
    }

    fun showErrorSnackBar(message: String, error: Boolean)
    {
        val snackBar =  Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if(error) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.red)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.green)
            )
        }
        snackBar.show()
    }

    private fun validateRightDetails(): Boolean {
        val str = mobile_number_edittext_register_screen.text.toString()
        return when {
            TextUtils.isEmpty(mobile_number_edittext_register_screen.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Enter your Mobile Number", true)
                false
            }
            TextUtils.isEmpty(password_edittext_register_screen.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Enter your Password", true)
                false
            }
            !isNumber(str) -> {
                showErrorSnackBar("Enter a correct Mobile Number", true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    private fun registerUser() {

        progress_register.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance().getReference("Users")
        if(validateRightDetails()) {

            val number: String = mobile_number_edittext_register_screen.text.toString().trim{it <= ' '}
            val password: String = password_edittext_register_screen.text.toString().trim{it <= ' '}

            val email: String = "$number@gmail.com"

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> {task ->

                        if(task.isSuccessful) {
                            progress_register.visibility = View.GONE
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar(
                                "You are Successfully Signed Up",
                                false
                            )
                            val user = User(email, number)
                            database.child(number).setValue(user)

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            
                        } else {
                            progress_register.visibility = View.GONE
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        } else {
            progress_register.visibility = View.GONE
        }
    }
}