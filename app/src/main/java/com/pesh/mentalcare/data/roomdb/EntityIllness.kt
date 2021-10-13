package com.pesh.mentalcare.data.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "illness_table")
data class EntityIllness(val mentalIllness: String,
                         val explanation: String,
                         @PrimaryKey(autoGenerate = true) val id: Int? = null)