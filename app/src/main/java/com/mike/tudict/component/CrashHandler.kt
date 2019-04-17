package com.mike.tudict.component

import android.content.Context
import android.util.Log
import java.lang.StringBuilder

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    private var mApplicationContext: Context? = null
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init(context: Context) {
        mApplicationContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            Log.w(TAG, "handleException --- ex==null")
            return true
        }
        printExceptionLog(ex)
        return true
    }

    private fun printExceptionLog(e: Throwable) {
        printExceptionLog(TAG, e)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Log.i(TAG, "uncaughtException!!!>>>>>>>>>>>$throwable")
        if (!handleException(throwable) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, throwable)
        } else {
            //Sleep一会后结束程序
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "Error : ", e)
            }

            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        }
    }

    companion object {
        private val TAG = "CrashHandler"

        fun printExceptionLog(tag: String, e: Throwable) {
            var stringBuilder = StringBuilder()
            stringBuilder.append("localizedMessage: " + e.localizedMessage + "\n")
            stringBuilder.append("cause: " + e.cause + "\n")
            stringBuilder.append("stack: " + "\n")

            for (element in e.stackTrace) {
                stringBuilder.append(" " + element.toString() + "\n")
            }

            Log.e(tag, stringBuilder.toString())
        }
    }
}
