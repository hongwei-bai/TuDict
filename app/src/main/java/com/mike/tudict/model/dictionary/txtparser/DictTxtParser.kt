package com.mike.tudict.model.dictionary.txtparser

import android.util.Log
import com.mike.tudict.model.Constants

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.ArrayList

abstract class DictTxtParser {
    var data: ArrayList<WordData>? = null
        private set
    private val mLines = ArrayList<String>()
    private var mFile: File? = null

    fun parseTxtFile(mFile: File): Boolean {
        this.mFile = mFile
        if (parseTxtFile()) {
            data = parseFile(mFile.name, this.mLines)
            if (data != null && !data!!.isEmpty()) {
                return true
            }
        }
        return false
    }

    protected abstract fun parseFile(filename: String, mLines: ArrayList<String>): ArrayList<WordData>

    private fun parseTxtFile(): Boolean {
        mLines.clear()
        try {
            val encoding = Constants.DB.DICT_TXT_ENCODE
            if (mFile!!.isFile && mFile!!.exists()) {
                val read = InputStreamReader(FileInputStream(mFile), encoding)
                val bufferedReader = BufferedReader(read)
                var lineTxt: String? = null
                while ((bufferedReader.readLine().apply { lineTxt = this }) != null) {
                    mLines.add(lineTxt!!)
                }
                read.close()
                return true
            } else {
                Log.d(TAG, "DictTxtParser File not found!")
                return false
            }
        } catch (e: Exception) {
            Log.d(TAG, "DictTxtParser Exception reading mFile!")
            e.printStackTrace()
            return false
        }

    }

    fun clearData() {
        data!!.clear()
    }

    companion object {
        private val TAG = "DictTxtParser"
    }
}
