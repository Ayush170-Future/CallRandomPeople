package com.example.callrandompeople

import android.content.Intent
import android.hardware.usb.UsbRequest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var UsersList: ArrayList<User>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        UsersList = arrayListOf<User>()
        getUserData()

        btn_call_main.setOnClickListener{
            val user = UsersList.random()
            val user_number = user.number
            val number: Long? = user_number?.toLong()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
        }
    }

    private fun getUserData() {

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapShot in snapshot.children) {
                        val user = userSnapShot.getValue(User::class.java)
                        UsersList.add(user!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}