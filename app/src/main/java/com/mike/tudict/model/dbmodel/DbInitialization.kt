package com.mike.tudict.model.dbmodel

import android.content.Context
import android.util.Log
import com.mike.tudict.util.AssetsCopy
import java.io.File

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */

object DbInitialization {
    val TAG = "DbInit"

    /*
     * Simple check condition:
     *  a. dir data/data/<package>/databases/ contains more than 4 files (incl.4).
     *  b. one of those files exceeds 16,000 bytes.
     */
    fun check(context: Context): Boolean {
        val rootdir = context.getDatabasePath("stub").parentFile
        val filelist = rootdir.listFiles()
        Log.i(TAG, "Number of files in $rootdir is ${filelist?.size}).")

        filelist?.takeIf { it.size >= 4 }?.forEach {
            it.takeIf { it.length() > 16000 }?.apply { return true }
        }
        Log.i(TAG, "Check failure. No dictionary db found.")
        return false;
    }

    fun initization(context: Context): Boolean {
        Log.i(TAG, "Dictionary db initization called.")
        return initDictionaryByCopyDbFromAssets(context);
    }

    private fun initDictionaryByCopyDbFromAssets(context: Context): Boolean {
        val databaseRoot = context.getDatabasePath("stub").parent
        return copyImpl(context, databaseRoot, "dict_db")
                && copyImpl(context, databaseRoot, "dict_db-journal")
                && copyImpl(context, databaseRoot, "word_db")
                && copyImpl(context, databaseRoot, "word_db-journal")
    }

    private fun copyImpl(context: Context, databaseRoot: String, dbfilename: String): Boolean {
        return AssetsCopy.copyFromAssetsToData(
            context, dbfilename, databaseRoot + File.separator + dbfilename
        );
    }
}