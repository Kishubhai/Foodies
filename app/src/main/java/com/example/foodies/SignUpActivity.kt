package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodies.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var Sigin: Button
    private lateinit var loginTxt: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivitySignUpBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Sigin=binding.button
        loginTxt=binding.LoginTxt
        firebaseAuth = FirebaseAuth.getInstance()
        auth= Firebase.auth

        if (firebaseAuth.currentUser!=null){
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginTxt.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        Sigin.setOnClickListener {
            val name = binding.namee.text.toString()
            val email = binding.emaill.text.toString()
            val pass = binding.passwordd.text.toString()

            if (name=="" || email=="" || pass==""){
                Toast.makeText(this, "Enter all credentials !", Toast.LENGTH_SHORT).show()
            }
            else{
                sigin(name,email,pass)

            }
        }


    }

    private fun sigin(name: String, email: String, pass: String) {

        // Check if the email is already in use
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { checkTask ->
                if (checkTask.isSuccessful) {
                    val result = checkTask.result
                    if (result?.signInMethods?.isEmpty() == true) {
                        // No user with this email address exists, you can proceed with user registration
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { registrationTask ->
                                if (registrationTask.isSuccessful) {
                                    // Registration was successful

                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {

                                    Toast.makeText(this, "Registration failed: " + registrationTask.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
//                            progressDialog.dismiss()
//                            // A user with this email address already exists, display an error message to the user
                        Toast.makeText(this, "A user with this email already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
//                        progressDialog.dismiss()
                    // An error occurred while checking the email
                    val exception = checkTask.exception
                    Toast.makeText(this, "Error: " + exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}