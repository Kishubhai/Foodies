package com.example.foodies

import androidx.room.Dao
import androidx.room.Query

@Dao
interface Dao {
    @get:Query("Select * FROM recipe")
    val all:List<Recipe?>?
}