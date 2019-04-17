package com.mike.tudict.viewmodel

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast
import java.util.*

class VoiceTTSHelper private constructor() {
    private var mTTSEnglish: TextToSpeech? = null
    private var mTTSChinese: TextToSpeech? = null
    private var mContext: Context? = null
    private var mWord: String? = null
    private var mOn = false
    private var mHasChineseEngine = false

    private var speakOnceList: ArrayList<String>? = ArrayList()
    private var speakOnceListB: ArrayList<String>? = null
    private var speakRepeatTimes = 1
    private var speakOnceThread: Thread? = null
    private var listener: SpeakAllCompleteListener? = null
    var speakAllCurrentIdx = -1
        private set
    private var savedWordlist: ArrayList<String>? = null
    private var savedExampleList: ArrayList<String>? = null

    private var ttsFailureListener: (FAILURE_CAUSE) -> Unit = {}

    @JvmOverloads
    fun initialize(contextIn: Context, onlyStartEnglishEngine: Boolean = false) {
        mContext = contextIn
        if (mTTSEnglish != null) {
            return
        }
        mTTSEnglish = TextToSpeech(mContext, OnInitListener { status ->
            if (TextToSpeech.SUCCESS != status) {
//                Toast.makeText(mContext, "TTS English init failed!", Toast.LENGTH_SHORT).show()
                ttsFailureListener.invoke(FAILURE_CAUSE.NO_GOOGLE_TTS)
                return@OnInitListener
            }
            var result = mTTSEnglish!!.setLanguage(Locale.UK)
            if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
//                Toast.makeText(mContext, "Language:UK NOT support!", Toast.LENGTH_SHORT).show()

                result = mTTSEnglish!!.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                    ttsFailureListener.invoke(FAILURE_CAUSE.NO_LANGUAGE)
                    return@OnInitListener
                }
            }
            mOn = true
        })// , DEFAULT_ENGLISH_ENGINE);
        if (onlyStartEnglishEngine) {
            return
        }
        mTTSChinese = TextToSpeech(mContext, OnInitListener { status ->
            if (TextToSpeech.SUCCESS != status) {
//                Toast.makeText(mContext, "TTS Chinese init failed!", Toast.LENGTH_SHORT).show()
                return@OnInitListener
            }
            val result = mTTSEnglish!!.setLanguage(Locale.UK)
            if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
//                Toast.makeText(mContext, "Language:UK NOT support!", Toast.LENGTH_SHORT).show()
                return@OnInitListener
            }
            mOn = true
            mHasChineseEngine = true
        }, DEFAULT_CHINESE_ENGINE)
        mTTSChinese!!.setSpeechRate(CHINESE_SPEECH_RATE)
    }

    fun setTTSInitializeFailureListener(listener: (FAILURE_CAUSE) -> Unit) {
        ttsFailureListener = listener
    }

    fun speakEnglishNow(englishOnly: String) {
        if (mOn) {
            mWord = englishOnly
            mTTSEnglish!!.speak(mWord, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    fun speakEnglish(englishOnly: String) {
        if (mOn) {
            mWord = englishOnly
            mTTSEnglish!!.speak(mWord, TextToSpeech.QUEUE_ADD, null)
        }
    }

    fun speakChineseNow(string: String) {
        if (mOn && mHasChineseEngine) {
            mWord = string
            mTTSChinese!!.speak(mWord, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    fun speakChinese(string: String) {
        if (mOn && mHasChineseEngine) {
            mWord = string
            mTTSChinese!!.speak(mWord, TextToSpeech.QUEUE_ADD, null)
        }
    }

    fun stopSpeaking(): Int {
        return mTTSEnglish!!.stop()
    }

    fun shutdown() {
        mTTSEnglish!!.shutdown()
        if (mHasChineseEngine) {
            mTTSChinese!!.shutdown()
        }
        if (speakOnceThread != null) {
            speakOnceThread!!.interrupt()
        }
        mOn = false
    }

    private fun resetCurrentWordIdx() {
        speakAllCurrentIdx = -1
    }

    private fun resetSpeakAllSession() {
        savedWordlist = null
        savedExampleList = null
        resetCurrentWordIdx()
    }

    private fun setCurrentWordIdx(idx: Int) {
        speakAllCurrentIdx = idx
    }

    interface SpeakAllCompleteListener {
        fun onComplete()
    }

    fun speakAll(
        wordlist: ArrayList<String>?, exampleList: ArrayList<String>?,
        repeatTimes: Int, l: SpeakAllCompleteListener?
    ) {
        speakRepeatTimes = repeatTimes
        savedWordlist = wordlist
        savedExampleList = exampleList
        listener = l
        synchronized(this) {
            speakOnceList = wordlist
            speakOnceListB = exampleList
            if (mOn) {
                speakOnceThread = Thread(Runnable {
                    try {
                        if (speakAllCurrentIdx < 0) {
                            speakAllCurrentIdx = 0
                            val introduction = ("There are " + speakOnceList!!.size
                                    + " words for this time's study, now start")
                            mTTSEnglish!!.speak(introduction, TextToSpeech.QUEUE_ADD, null)
                            Thread.sleep(_2_SECOND.toLong())
                        }

                        // main body
                        for (i in Math.max(0, speakAllCurrentIdx) until speakOnceList!!.size) {
                            setCurrentWordIdx(i)
                            val mWord = speakOnceList!![i]
                            val exampleString = speakOnceListB!![i]
                            var examples: Array<String>? = null
                            if (exampleString != null && !exampleString.isEmpty()) {
                                examples =
                                    exampleString.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            }

                            // speak 1st time
                            Thread.sleep(_4_SECOND.toLong())
                            mTTSEnglish!!.speak(mWord, TextToSpeech.QUEUE_ADD, null)
                            Thread.sleep(estimateSpeakingDelay(mWord).toLong())

                            if (examples != null) {
                                for (k in examples.indices) {
                                    Thread.sleep(_2_SECOND.toLong())
                                    mTTSEnglish!!
                                        .speak(examples[k], TextToSpeech.QUEUE_ADD, null)
                                    Thread.sleep(estimateSpeakingDelay(examples[k]).toLong())
                                }
                            }

                            // speak 2-n times
                            for (j in 1 until speakRepeatTimes) {
                                Thread.sleep(_2_SECOND.toLong())
                                mTTSEnglish!!.speak(mWord, TextToSpeech.QUEUE_ADD, null)
                                Thread.sleep(estimateSpeakingDelay(mWord).toLong())

                                if (examples != null) {
                                    for (k in examples.indices) {
                                        Thread.sleep(_2_SECOND.toLong())
                                        mTTSEnglish!!.speak(examples[k], TextToSpeech.QUEUE_ADD, null)
                                        Thread.sleep(estimateSpeakingDelay(examples[k]).toLong())
                                    }
                                }
                            }
                        }

                        // end
                        Thread.sleep(_2_SECOND.toLong())
                        val ending = "Study complete!"
                        mTTSEnglish!!.speak(ending, TextToSpeech.QUEUE_ADD, null)
                        Thread.sleep(_2_SECOND.toLong())
                        resetSpeakAllSession()
                        savedWordlist = null
                        savedExampleList = null
                        if (listener != null) {
                            listener!!.onComplete()
                        }
                    } catch (e: InterruptedException) {
                    }
                })
                speakOnceThread!!.start()
            }
        }
    }

    private fun estimateSpeakingDelay(content: String): Int {
        val length = content.length
        val unit = length / 40
        val halfUnit = length % 40 >= 20
        return _2_SECOND * unit + if (halfUnit) _1_SECOND else 0
    }

    fun suspendSpeakOnce() {
        stopSpeaking()
        if (speakOnceThread != null) {
            speakOnceThread!!.interrupt()
        }
    }

    fun resumeSpeakOnce(): Boolean {
        if (speakAllCurrentIdx < 0) {
            return false
        }
        speakAll(savedWordlist, savedExampleList, speakRepeatTimes, listener)
        return true
    }

    fun stopSpeakAllOnce() {
        suspendSpeakOnce()
        resetSpeakAllSession()
        if (listener != null) {
            listener!!.onComplete()
        }
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        shutdown()
    }

    companion object {

        @Volatile
        private var mInstance: VoiceTTSHelper? = null

        val instance: VoiceTTSHelper?
            get() {
                if (null == mInstance) {
                    synchronized(VoiceTTSHelper::class.java) {
                        if (null == mInstance) {
                            mInstance = VoiceTTSHelper()
                        }
                    }
                }
                return mInstance
            }

        private val ENGINE_GOOGLE = "com.google.android.tts"
        private val ENGINE_IFLYTEK = "com.iflytek.tts"

        val DEFAULT_ENGLISH_ENGINE = ENGINE_GOOGLE
        val DEFAULT_CHINESE_ENGINE = ENGINE_IFLYTEK

        val CHINESE_SPEECH_RATE = 1.2f
        private val _1_SECOND = 1000
        private val _2_SECOND = 2000
        private val _4_SECOND = 4000

        enum class FAILURE_CAUSE {
            NO_GOOGLE_TTS,
            NO_LANGUAGE,
        }
    }
}
