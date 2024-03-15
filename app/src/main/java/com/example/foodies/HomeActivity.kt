package com.example.foodies

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.foodies.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding
    private lateinit var rvAdapter:popularAdapter
    private lateinit var dataList:ArrayList<Recipe>

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth =FirebaseAuth.getInstance()
        binding.more.setOnClickListener {
            var dialog=Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet)
            dialog.show()

            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)

            val privacy = dialog.findViewById<View>(R.id.privacy_policy)
            val about = dialog.findViewById<View>(R.id.about_dev)
            val logout=dialog.findViewById<View>(R.id.logout)

            // Set OnClickListener for the element inside the dialog
            privacy?.setOnClickListener {
                // Handle the click event for the element inside the dialog
                // For example, you can perform some action or dismiss the dialog
                val intent=Intent(this,PrivacyActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            about.setOnClickListener {
                val intent=Intent(this,AboutActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            logout.setOnClickListener {
                auth.signOut()
                val intent=Intent(this,SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        binding.search.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        binding.salad.setOnClickListener {
            var myIntent=Intent(this@HomeActivity,CategoryActivity::class.java)
            myIntent.putExtra("TITLE","Salad")
            myIntent.putExtra("CATEGORY","Salad")
            startActivity(myIntent)
        }
        binding.MainDish.setOnClickListener {
            var myIntent=Intent(this@HomeActivity,CategoryActivity::class.java)
            myIntent.putExtra("TITLE","Main Dish")
            myIntent.putExtra("CATEGORY","Dish")
            startActivity(myIntent)
        }
        binding.drinks.setOnClickListener {
            var myIntent=Intent(this@HomeActivity,CategoryActivity::class.java)
            myIntent.putExtra("TITLE","Drinks")
            myIntent.putExtra("CATEGORY","Drinks")
            startActivity(myIntent)
        }
        binding.deserts.setOnClickListener {
            var myIntent=Intent(this@HomeActivity,CategoryActivity::class.java)
            myIntent.putExtra("TITLE","Desserts")
            myIntent.putExtra("CATEGORY","Desserts")
            startActivity(myIntent)
        }
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        dataList = ArrayList()

        binding.rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Move the database initialization outside the loop
        var db = Room.databaseBuilder(this@HomeActivity, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()

        var daoObject = db.getDao()
        var recipes = daoObject.all

        // Move the adapter initialization outside the loop
        rvAdapter = popularAdapter(dataList, this)

        for (i in recipes!!.indices) {
            if (recipes[i]!!.category.contains("Popular")) {
                dataList.add(recipes[i]!!)
            }
        }

        // Set the adapter outside the loop after dataList is populated
        binding.rvPopular.adapter = rvAdapter
    }


}