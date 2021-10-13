package com.pesh.mentalcare.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pesh.mentalcare.data.roomdb.utils.subscribeOnBackground

@Database(entities = [EntityIllness::class], version = 1)
abstract class DatabaseIllness : RoomDatabase() {

    abstract fun dao(): DAO

    companion object {
        private var instance: DatabaseIllness? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseIllness {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, DatabaseIllness::class.java,
                    "illness_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: DatabaseIllness) {
            val dao = db.dao()
            subscribeOnBackground {
                dao.insert(EntityIllness(
                    "Anxiety disorders",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, ph  obias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Impilse controll disorder",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, phobias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Anxiety disorders",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, ph  obias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Impilse controll disorder",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, phobias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Anxiety disorders",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, ph  obias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Impilse controll disorder",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, phobias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Anxiety disorders",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, ph  obias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))
                dao.insert(EntityIllness(
                    "Impilse controll disorder",
                    "These mental disorders come with excessive worry, fear and nervousness. Some of them include:" +
                            "agoraphobia, phobias, panic disorder, obsessive-compulsive disorder and social anxiety"
                ))

            }
        }
    }



}