package com.mike.tudict.util

import android.content.Context
import android.util.Log
import com.mike.tudict.component.CrashHandler
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.Exception as Exception1

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
object AssetsCopy {
    fun copyFromAssetsToData(context: Context, assetsSrcFilePath: String, dataDestFilePath: String): Boolean {
        var in1: InputStream? = null
        var out: FileOutputStream? = null
        //"/mydb.db3"
//            val path = context!!.filesDir.absolutePath + dataDestFilePath // data/data目录
        val file = File(dataDestFilePath)
        file.parentFile.mkdirs()
        if (!file.exists()) {
            try {
                //"db/mydb.db3"
                in1 = context.assets.open(assetsSrcFilePath) // 从assets目录下复制
                out = FileOutputStream(file)
                var length = -1
                val buf = ByteArray(1024)
                while (in1.read(buf).also { length = it } != -1) {
                    out.write(buf, 0, length)
                }
                out.flush()
                return true
            } catch (e: Exception) {
                return false
            } finally {
                if (in1 != null) {
                    try {
                        in1.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
                if (out != null) {
                    try {
                        out.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
            }
        }
        return false
    }
}