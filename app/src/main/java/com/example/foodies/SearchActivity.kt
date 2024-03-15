package com.example.foodies

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.foodies.databinding.ActivitySearchBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var rvAdapter: SearchAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private lateinit var recipes:List<Recipe?>

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.requestFocus()
        binding.goBackHome.setOnClickListener {
            finish()
        }
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()

        // Retrieve the recipes once from the database
        val daoObject = db.getDao()
        recipes = daoObject.all!!

        binding.search.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString()!=""){
                    filterr(s.toString())
                }
                else{
                    setUpRecyclerView()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                }

        })
//        var imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodService
        binding.rvSearch.setOnTouchListener { v, event ->
            hideKeyboard()
            false
        }
        setUpRecyclerView()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.search.windowToken, 0)
    }

    private fun filterr(filterText: String) {

        var filterData=ArrayList<Recipe>()

        for (i in recipes.indices){
            if (recipes[i]!!.tittle.lowercase().contains(filterText.lowercase())){
                filterData.add(recipes[i]!!)
            }
            rvAdapter.filterLint(filterData)
        }
    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()

        binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)

        // Initialize the database outside the loop


        // Populate dataList
        for (i in recipes!!.indices) {
            if (recipes[i]!!.category.contains("Popular")) {
                dataList.add(recipes[i]!!)
            }
        }

        // Initialize the adapter and set it to the RecyclerView
        rvAdapter = SearchAdapter(dataList, this)
        binding.rvSearch.adapter = rvAdapter
    }
}
