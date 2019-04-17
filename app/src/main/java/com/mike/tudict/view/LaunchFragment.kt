package com.mike.tudict.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mike.tudict.R
import com.mike.tudict.viewmodel.AboutInfo
import kotlinx.android.synthetic.main.view_memory.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */

class LaunchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_memory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            textViewMemoryWords.text = textViewMemoryWords.text.toString() + AboutInfo.appendAppVersionInfo(this)
        }
    }
}