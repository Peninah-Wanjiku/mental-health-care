package com.pesh.mentalcare.data.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository(app)
    private val allIllnesses = repository.getAllNotes()

    fun insert(illness: EntityIllness) {
        repository.insert(illness)
    }

    fun update(illness: EntityIllness) {
        repository.update(illness)
    }

    fun delete(illness: EntityIllness) {
        repository.delete(illness)
    }

    fun deleteAllIllnesses() {
        repository.deleteAllNotes()
    }

    fun getAllIllnesses(): LiveData<List<EntityIllness>> {
        return allIllnesses
    }


}