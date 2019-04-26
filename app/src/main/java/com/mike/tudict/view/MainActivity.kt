package com.mike.tudict.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mike.tudict.R
import com.mike.tudict.view.animation.CircularReveal
import com.mike.tudict.viewmodel.VoiceTTSInitializer
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_memory.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class MainActivity : DaggerAppCompatActivity() {

    private var startupFragment = LaunchFragment()
    var dictFragment = DictFragment()
    private var learnFragment = LearnFragment()
    private var infoFragment = InfoFragment()
    private var configFragment = ConfigFragment()

    private val voiceTTSInitializer = VoiceTTSInitializer()

    companion object {
        const val REQUEST_TTS_DATA_CHECK_CODE = 1
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dict -> {
                message.setText(R.string.title_dict)
                supportFragmentManager.beginTransaction().replace(R.id.container, dictFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_learn -> {
                message.setText(R.string.title_learn)
                supportFragmentManager.beginTransaction().replace(R.id.container, learnFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info -> {
                message.setText(R.string.title_info)
                supportFragmentManager.beginTransaction().replace(R.id.container, infoFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_config -> {
                message.setText(R.string.title_config)
                supportFragmentManager.beginTransaction().replace(R.id.container, configFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, startupFragment)
            .commitAllowingStateLoss()

        Handler().postDelayed({
            circleView?.let {
                CircularReveal.transact(this, arrayOf(circleView, circleLayout), container, dictFragment)
            }
            voiceTTSInitializer.initialize(this)
        }, 2000)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        voiceTTSInitializer.forwardOnActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()

        voiceTTSInitializer.releaseActivityInstance()
    }
}
