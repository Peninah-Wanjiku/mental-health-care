package com.pesh.mentalcare.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pesh.mentalcare.model.IllnessTypes

@Dao
interface DAO {

    @Insert
    fun insert(illness : EntityIllness)

    @Update
    fun update(illness : EntityIllness)

    @Delete
    fun delete(illness : EntityIllness)

    @Query("DELETE FROM `illness_table`")
    fun deleteAllIllnesses()

    @Query("SELECT * FROM `illness_table`")
    fun getAllIllnesses(): LiveData<List<EntityIllness>>
}