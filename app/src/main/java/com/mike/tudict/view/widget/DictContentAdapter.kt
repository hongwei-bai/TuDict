package com.mike.tudict.view.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mike.tudict.R
import com.mike.tudict.Constants
import com.mike.tudict.model.DisplayItem
import kotlinx.android.synthetic.main.dictionary_item_explanation.view.*
import kotlinx.android.synthetic.main.dictionary_item_phontic.view.*
import kotlinx.android.synthetic.main.dictionary_item_rating.view.*
import kotlinx.android.synthetic.main.dictionary_lookup_item.view.*


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */

class DictContentAdapter() : RecyclerView.Adapter<DictContentAdapter.ViewHolder>() {
    private val emptyList = mutableListOf<DisplayItem>()
    private var list = emptyList
    private var clickListener: (String) -> Unit = {}
    private var voiceSpeakListener: () -> Unit = {}

    val TYPE_EXPLANATION = Constants.VIEW_TYPE_EXPLAIN
    val TYPE_PHONTIC = Constants.VIEW_TYPE_PHONTIC
    val TYPE_RATING = Constants.VIEW_TYPE_RATING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var rootView: View
        when (viewType) {
            TYPE_EXPLANATION -> {
                rootView =
                    LayoutInflater.from(parent.context).inflate(R.layout.dictionary_item_explanation, parent, false)
                rootView.setOnClickListener {
                    clickListener.invoke(it.textView.text.toString())
                }
                return ViewHolderExplanation(rootView)
            }
            TYPE_PHONTIC -> return ViewHolderPhontic(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.dictionary_item_phontic,
                    parent,
                    false
                ).also {
                    it.speakImageView.setOnClickListener {
                        voiceSpeakListener.invoke()
                    }
                }
            )
            TYPE_RATING -> return ViewHolderRating(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.dictionary_item_rating,
                    parent,
                    false
                )
            )
            else ->
                return ViewHolderExplanation(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.dictionary_item_explanation,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(data: MutableList<DisplayItem>) {
        list = data
        notifyDataSetChanged()
    }

    fun emptyData() {
        list = emptyList
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: DisplayItem)
    }

    class ViewHolderExplanation(itemView: View) : ViewHolder(itemView) {
        override fun bind(data: DisplayItem) {
            itemView.checkBox.text = data.string
        }
    }

    class ViewHolderPhontic(itemView: View) : ViewHolder(itemView) {
        override fun bind(data: DisplayItem) {
            itemView.phonticTextView.text = data.string
        }
    }

    class ViewHolderRating(itemView: View) : ViewHolder(itemView) {
        override fun bind(data: DisplayItem) {
            itemView.ratingBar.rating = 2.0f
        }
    }

    fun setOnItemWordClickListener(listener: (String) -> Unit) {
        clickListener = listener
    }

    fun setOnSpeakVoiceListener(listener: () -> Unit) {
        voiceSpeakListener = listener
    }
}