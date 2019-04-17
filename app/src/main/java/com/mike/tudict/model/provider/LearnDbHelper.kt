package com.mike.tudict.model.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class LearnDbHelper(context: Context) : SQLiteOpenHelper(context,
    DBNAME, null,
    VERSION
) {

    interface TABLE_WORD {
        companion object {
            val TABLE = "table_word"
            val _ID = "_id"
            val ENGLISH = "english"
            val CHINESE = "chinese"
            val PHONETIC = "phonetic"
            val DATE_ADDED = "date_added"
            val DATE_UPDATED = "date_updated"
            val IMPORTANCE = "importance"

            // has mod function for these 3 columns
            val EN_PASS = "count_pass"
            val CN_PASS = "count_fail"
            val REVIEW_FLAG = "count_sum"

            val DIRECTION = "direction"
            val DELETE = "delete"
            val ARCHIVE = "archive"
            val EXT1 = "ext1"
            val EXT2 = "ext2"
            val SOURCE = "ext3"
            val EXT4 = "ext4"
        }
    }

    interface MIME_TYPE {
        companion object {
            val PROPERTY = "property"
            val EXAMPLE = "example"
            val TOPIC = "topic"
            val COHERENCE = "coherence"
        }
    }

    interface TABLE_MIME_TYPE {
        companion object {
            val TABLE = "table_mime_type"
            val MIME_ID = "mime_id"
            val MIME_TYPE = "mime_type"
        }
    }

    interface TABLE_DATA {
        companion object {
            val TABLE = "table_data"
            val _ID = "_id"
            val WORD_ID = "word_id"
            val MIME_ID = "mime_id"
            val MIME_TYPE = "mime_type"
            val DATA1 = "data1"
            val DATA2 = "data2"
            val DATA3 = "data3"
            val DATA4 = "data4"
            val DATA5 = "data5"
            val DATA6 = "data6"
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        createWordTable(db)
        createDataTable(db)
    }

    private fun createWordTable(db: SQLiteDatabase) {
        var sql = "CREATE TABLE " + TABLE_WORD.TABLE + "("
        sql += "'" + TABLE_WORD._ID + "' integer primary key autoincrement, "
        sql += "'" + TABLE_WORD.ENGLISH + "' varchar(500), "
        sql += "'" + TABLE_WORD.CHINESE + "' varchar(3000), "
        sql += "'" + TABLE_WORD.PHONETIC + "' varchar(100), "
        sql += "'" + TABLE_WORD.DATE_ADDED + "' integer, "
        sql += "'" + TABLE_WORD.DATE_UPDATED + "' integer, "
        sql += "'" + TABLE_WORD.IMPORTANCE + "' integer, "
        sql += "'" + TABLE_WORD.EN_PASS + "' integer, "
        sql += "'" + TABLE_WORD.CN_PASS + "' integer, "
        sql += "'" + TABLE_WORD.REVIEW_FLAG + "' integer, "
        sql += "'" + TABLE_WORD.DIRECTION + "' integer, "
        sql += "'" + TABLE_WORD.DELETE + "' integer, "
        sql += "'" + TABLE_WORD.ARCHIVE + "' integer, "
        sql += "'" + TABLE_WORD.EXT1 + "' integer, "
        sql += "'" + TABLE_WORD.EXT2 + "' integer, "
        sql += "'" + TABLE_WORD.SOURCE + "' varchar(50), "
        sql += "'" + TABLE_WORD.EXT4 + "' varchar(50))"
        db.execSQL(sql)
    }

    private fun createDataTable(db: SQLiteDatabase) {
        var sql = "CREATE TABLE " + TABLE_DATA.TABLE + "("
        sql += "'" + TABLE_DATA._ID + "' integer primary key autoincrement, "
        sql += "'" + TABLE_DATA.WORD_ID + "' integer, "
        sql += "'" + TABLE_DATA.MIME_ID + "' integer, "
        sql += "'" + TABLE_DATA.MIME_TYPE + "' varchar(20), "
        sql += "'" + TABLE_DATA.DATA1 + "' integer, "
        sql += "'" + TABLE_DATA.DATA2 + "' integer, "
        sql += "'" + TABLE_DATA.DATA3 + "' varchar(50), "
        sql += "'" + TABLE_DATA.DATA4 + "' varchar(50), "
        sql += "'" + TABLE_DATA.DATA5 + "' varchar(500), "
        sql += "'" + TABLE_DATA.DATA6 + "' varchar(500))"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private val VERSION = 1
        private val DBNAME = "word_db"
    }

}
