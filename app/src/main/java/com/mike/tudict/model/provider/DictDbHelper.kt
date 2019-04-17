package com.mike.tudict.model.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mike.tudict.model.ada.DbAdaTable

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DictDbHelper(context: Context) : SQLiteOpenHelper(context,
    DBNAME, null,
    VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db, DbAdaTable.TABLE_WORD.TABLE, DbAdaTable.TABLE_WORD.CREATER)
        createTable(db, DbAdaTable.TABLE_PROPERTY.TABLE, DbAdaTable.TABLE_PROPERTY.CREATER)
        createTable(db, DbAdaTable.TABLE_ITEM.TABLE, DbAdaTable.TABLE_ITEM.CREATER)
        createTable(db, DbAdaTable.TABLE_EXAMPLE.TABLE, DbAdaTable.TABLE_EXAMPLE.CREATER)
        createIndex(db)
    }

    private fun createTable(db: SQLiteDatabase, table: String, creator: Array<String>) {
        var sql = "CREATE TABLE $table("
        sql += "'" + DbAdaTable.TABLE_WORD._ID + "' integer primary key, "
        for (i in 0 until creator.size - 1) {
            sql += creator[i] + ","
        }
        sql += creator[creator.size - 1] + ")"
        db.execSQL(sql)
    }

    private fun createIndex(db: SQLiteDatabase) {
        for (indexStru in DbAdaTable.INDEX.ARRAY) {
            val name = indexStru.table + "_" + indexStru.field
            val sql = ("CREATE " + indexStru.type + " INDEX " + name + " ON " + indexStru.table
                    + "(" + indexStru.field + ")")
            db.execSQL(sql)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private val VERSION = 1
        private val DBNAME = "dict_db"
    }

}
