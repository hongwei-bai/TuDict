package com.mike.tudict.model.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class LearnProvider : ContentProvider() {
    private var dbHelper: LearnDbHelper? = null

    override fun onCreate(): Boolean {
        dbHelper = LearnDbHelper(context!!)
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val cursor: Cursor
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString =
                LearnDbHelper.TABLE_WORD.TABLE
            URI_WORD_ID -> {
                tableNameString = LearnDbHelper.TABLE_WORD.TABLE
                val id = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_WORD._ID + " = " + id
            }
            URI_DATA -> tableNameString =
                LearnDbHelper.TABLE_DATA.TABLE
            URI_DATA_ID -> {
                tableNameString = LearnDbHelper.TABLE_DATA.TABLE
                val wordId = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_DATA.WORD_ID + " = " + wordId
            }
            else -> {
                throw IllegalArgumentException("Unknown URI$uri")
            }
        }
        cursor = db.query(tableNameString, projection, selection, selectionArgs, null, null, null)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString =
                LearnDbHelper.TABLE_WORD.TABLE
            URI_DATA -> tableNameString =
                LearnDbHelper.TABLE_DATA.TABLE
            else -> throw IllegalArgumentException("Unknown URI$uri")
        }
        rowId = db.insert(tableNameString, null, values)
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
            return noteUri
        }
        throw IllegalArgumentException("Unknown URI$uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString =
                LearnDbHelper.TABLE_WORD.TABLE
            URI_WORD_ID -> {
                tableNameString = LearnDbHelper.TABLE_WORD.TABLE
                val id = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_WORD._ID + " = " + id
            }
            URI_DATA -> tableNameString =
                LearnDbHelper.TABLE_DATA.TABLE
            URI_DATA_ID -> {
                tableNameString = LearnDbHelper.TABLE_DATA.TABLE
                val wordId = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_DATA.WORD_ID + " = " + wordId
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        rowId = db.delete(tableNameString, selection, selectionArgs).toLong()
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
        }
        return rowId.toInt()
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString =
                LearnDbHelper.TABLE_WORD.TABLE
            URI_WORD_ID -> {
                tableNameString = LearnDbHelper.TABLE_WORD.TABLE
                val id = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_WORD._ID + " = " + id
            }
            URI_DATA -> tableNameString =
                LearnDbHelper.TABLE_DATA.TABLE
            URI_DATA_ID -> {
                tableNameString = LearnDbHelper.TABLE_DATA.TABLE
                val wordId = ContentUris.parseId(uri).toInt()
                selection = LearnDbHelper.TABLE_DATA.WORD_ID + " = " + wordId
            }
            else -> throw IllegalArgumentException("Unknown URI$uri")
        }
        rowId = db.update(tableNameString, values, selection, selectionArgs).toLong()
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
            return rowId.toInt()
        }
        throw IllegalArgumentException("Unknown URI$uri")
    }

    companion object {
        private var uriMatcher: UriMatcher? = null
        private val URI_WORD = 1
        private val URI_WORD_ID = 2
        private val URI_DATA = 3
        private val URI_DATA_ID = 4
        val AUTHORITIES = "tudict.learn.provider"
        val AUTHORITIES_URI = Uri.parse("content://$AUTHORITIES")
        val CONTENT_URI = Uri.withAppendedPath(AUTHORITIES_URI, "word")
        val DATA_URI = Uri.withAppendedPath(AUTHORITIES_URI, "data")

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(
                AUTHORITIES, "word",
                URI_WORD
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "word/#",
                URI_WORD_ID
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "data",
                URI_DATA
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "data/#",
                URI_DATA_ID
            )
        }
    }

}
