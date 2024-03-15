package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.foodies.databinding.ActivityCategoryBinding
import com.example.foodies.databinding.ActivityHomeBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvAdapter:CategoryAdapter
    private lateinit var dataList:ArrayList<Recipe>

    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tittle.text=intent.getStringExtra("TITLE")!!

        binding.goBackHome.setOnClickListener {
            finish()
        }
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        dataList = ArrayList()

        binding.rvCategory.layoutManager = LinearLayoutManager(this)

        // Move the database initialization outside the loop
        var db = Room.databaseBuilder(this@CategoryActivity, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()

        var daoObject = db.getDao()
        var recipes = daoObject.all

        // Move the adapter initialization outside the loop
        rvAdapter = CategoryAdapter(dataList, this)

        for (i in recipes!!.indices) {
            if (recipes[i]!!.category.contains(intent.getStringExtra("CATEGORY")!!)) {
                dataList.add(recipes[i]!!)
            }
        }

        // Set the adapter outside the loop after dataList is populated
        binding.rvCategory.adapter = rvAdapter
    }
}