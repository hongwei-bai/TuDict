package com.mike.tudict.view.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mike.tudict.R
import kotlinx.android.synthetic.main.dictionary_lookup_item.view.*


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */

class DictLookupAutoCompleteAdapter() : RecyclerView.Adapter<DictLookupAutoCompleteAdapter.ViewHolder>() {
    private val emptyList = mutableListOf<String>()
    private var list = emptyList
    private var clickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.dictionary_lookup_item, parent, false)
        rootView.setOnClickListener {
            clickListener.invoke(it.textView.text.toString())
        }
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(data: MutableList<String>) {
        list = data
        notifyDataSetChanged()
    }

    fun emptyData() {
        list = emptyList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: String) {
            itemView.textView.text = data
        }
    }

    fun setOnItemWordClickListener(listener: (String) -> Unit) {
        clickListener = listener
    }
}