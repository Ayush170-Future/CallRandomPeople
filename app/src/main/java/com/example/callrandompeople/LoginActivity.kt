package com.example.callrandompeople

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progress_login.visibility = View.GONE
        register_button_login_screen.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        login_button.setOnClickListener{
            loginUser()
        }
    }

    private fun showErrorSnackBar(message: String, error: Boolean)
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
        val str = mobile_number_edittext.text.toString()
        return when {
            TextUtils.isEmpty(mobile_number_edittext.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Enter your Mobile Number", true)
                false
            }
            TextUtils.isEmpty(password_edittext.text.toString().trim{it <= ' '}) -> {
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

    private fun loginUser() {

        progress_login.visibility = View.VISIBLE
        val number: String = mobile_number_edittext.text.toString().trim{it <= ' '}
        val password: String = password_edittext.text.toString().trim{it <= ' '}

        val email: String = "$number@gmail.com"
        if(validateRightDetails()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progress_login.visibility = View.GONE
                    showErrorSnackBar("Signing In", false)
                } else {
                    progress_login.visibility = View.GONE
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            }
        } else {
            progress_login.visibility = View.GONE
        }
    }
}