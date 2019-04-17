package com.mike.tudict.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.view.Window
import com.mike.tudict.R
import com.mike.tudict.view.MainActivity.Companion.REQUEST_TTS_DATA_CHECK_CODE
import kotlinx.android.synthetic.main.dialog_tts_install.*


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/16
 * Description:
 */

class VoiceTTSInitializer {
    private var activity: Activity? = null

    fun initialize(activity: Activity) {
        this.activity = activity

        promoteDialogForAddTTSLanguage()
        VoiceTTSHelper.instance?.initialize(activity.applicationContext)
        VoiceTTSHelper.instance?.setTTSInitializeFailureListener {
            when (it) {
                VoiceTTSHelper.Companion.FAILURE_CAUSE.NO_GOOGLE_TTS -> {
                    promoteDialogForInstallTTS()
                }
                VoiceTTSHelper.Companion.FAILURE_CAUSE.NO_LANGUAGE -> {
                    promoteDialogForAddTTSLanguage()
                }
            }
        }
    }

    fun forwardOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TTS_DATA_CHECK_CODE -> if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            }
        }
    }

    fun releaseActivityInstance() {
        activity = null
    }

    private fun promoteDialogForInstallTTS() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_tts_install)
        dialog.ok.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts&hl=en")
            activity?.startActivity(intent)
            dialog.dismiss()
        }
        dialog.cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun promoteDialogForAddTTSLanguage() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_tts_add_lang)
        dialog.ok.setOnClickListener {
            val intent = Intent()
            intent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            activity?.startActivity(intent)
            dialog.dismiss()
        }
        dialog.cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}