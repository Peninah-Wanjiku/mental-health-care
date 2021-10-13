package com.pesh.mentalcare.data.roomdb

import android.app.Application
import androidx.lifecycle.LiveData
import com.pesh.mentalcare.data.roomdb.utils.subscribeOnBackground

class Repository(application: Application) {

    private var dao: DAO
    private var allIllness: LiveData<List<EntityIllness>>

    private val database = DatabaseIllness.getInstance(application)

    init {
        dao = database.dao()
        allIllness = dao.getAllIllnesses()
    }

    fun insert(illness: EntityIllness) {
        subscribeOnBackground {
            dao.insert(illness)
        }
    }

    fun update(illness: EntityIllness) {
        subscribeOnBackground {
            dao.update(illness)
        }
    }

    fun delete(illness: EntityIllness) {
        subscribeOnBackground {
            dao.delete(illness)
        }
    }

    fun deleteAllNotes() {
        subscribeOnBackground {
            dao.deleteAllIllnesses()
        }
    }

    fun getAllNotes(): LiveData<List<EntityIllness>> {
        return allIllness
    }



}