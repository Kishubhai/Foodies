package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodies.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email=binding.emaill.text.toString()
            val pass=binding.passwordd.text.toString()

            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Enter all credentials !", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else
                        Toast.makeText(this,"Login Failed: ${it.exception}", Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.SigninTxt.setOnClickListener {
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()

        }

    }
}