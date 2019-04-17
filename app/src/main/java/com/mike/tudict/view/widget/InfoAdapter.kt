package com.mike.tudict.view.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mike.tudict.R
import com.mike.tudict.viewmodel.AboutInfo
import kotlinx.android.synthetic.main.view_memory.view.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */

class InfoAdapter(var context: Context) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    val TYPE_MEMORY = 0
    val TYPE_ABOUT_NAME = 1
    val TYPE_ABOUT_EMAIL = 2
    val TYPE_ABOUT_LINKEDIN = 3
    val TYPE_ABOUT_TEL = 4
    val TYPE_ABOUT_BLOG = 5
    // bottom is for better user exp, can drag down a little bit more.
    val TYPE_ABOUT_BOTTOM = 6

    var list = mutableListOf<Int>(
        TYPE_MEMORY,
        TYPE_ABOUT_NAME,
        TYPE_ABOUT_LINKEDIN,
        TYPE_ABOUT_BLOG,
        TYPE_ABOUT_BOTTOM
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var rootView: View
        when (viewType) {
            TYPE_MEMORY -> {
                rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_memory, parent, false)
                rootView.imageViewDown.visibility = View.VISIBLE
                rootView.textViewMemoryWords.text =
                    rootView.textViewMemoryWords.text.toString() + AboutInfo.appendAppVersionInfo(context)
            }

            TYPE_ABOUT_NAME -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_name, parent, false)
            TYPE_ABOUT_EMAIL -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_email, parent, false)
            TYPE_ABOUT_LINKEDIN -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_linkedin, parent, false)
            TYPE_ABOUT_TEL -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_tel, parent, false)
            TYPE_ABOUT_BLOG -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_blog, parent, false)
            TYPE_ABOUT_BOTTOM -> rootView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_about_bottom, parent, false)
            else -> rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_about_bottom, parent, false)
        }
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemViewType(position: Int): Int {
        return list[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}