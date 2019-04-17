package com.mike.tudict.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mike.tudict.R
import com.mike.tudict.R.id.recyclerViewInfo
import com.mike.tudict.view.widget.InfoAdapter
import kotlinx.android.synthetic.main.fragment_info.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class InfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity ?: apply {
            return
        }
        val infoAdapter = InfoAdapter(activity!!.applicationContext)
        val layoutManager = LinearLayoutManager(activity)
        recyclerViewInfo.layoutManager = layoutManager
        recyclerViewInfo.adapter = infoAdapter
    }
}