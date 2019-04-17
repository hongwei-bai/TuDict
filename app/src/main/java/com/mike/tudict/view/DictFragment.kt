package com.mike.tudict.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mike.tudict.R
import com.mike.tudict.model.DisplayItem
import com.mike.tudict.view.widget.DictContentAdapter
import com.mike.tudict.view.widget.DictLookupAutoCompleteAdapter
import com.mike.tudict.viewmodel.DictionaryViewModel
import com.mike.tudict.viewmodel.VoiceTTSHelper
import kotlinx.android.synthetic.main.fragment_dict.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DictFragment : Fragment() {
    private lateinit var viewModel: DictionaryViewModel

    private lateinit var dictLookupAutoCompleteAdapter: DictLookupAutoCompleteAdapter

    private lateinit var dictContentAdapter: DictContentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dict, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lookup dropdown list
        val layoutManager = LinearLayoutManager(activity)
        dictLookupAutoCompleteAdapter = DictLookupAutoCompleteAdapter()

        // [ENTRY]One of suggested words in list is clicked ->
        dictLookupAutoCompleteAdapter.setOnItemWordClickListener {
            lookuptil.setText(it)
            dictLookupAutoCompleteAdapter.emptyData()
            viewModel.loadWordDetail(it)
        }
        recyclerViewLookup.layoutManager = layoutManager
        recyclerViewLookup.adapter = dictLookupAutoCompleteAdapter

        // Dictionary word content(explanation) block
        val layoutManager2 = LinearLayoutManager(activity)
        dictContentAdapter = DictContentAdapter()
        recyclerViewWordContent.layoutManager = layoutManager2
        recyclerViewWordContent.adapter = dictContentAdapter

        // Deal with situation that when lookup textbox has content, and just navigate back from other fragment,
        // when clicking it, show auto-complete dropdown list immediately rather than waiting for another text change.
        lookuptil.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewModel.lookupKeywords(lookuptil.text.toString())
            }
        }

        lookuptil.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dictContentAdapter.emptyData()
                viewModel.lookupKeywords(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        dictContentAdapter.setOnSpeakVoiceListener {
            VoiceTTSHelper.instance?.speakEnglishNow(lookuptil.text.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DictionaryViewModel::class.java)
        viewModel.initializeDictDbConnection(activity!!.applicationContext)
        viewModel.lookupResult.observe(this, Observer<MutableList<String>> { newList ->
            dictLookupAutoCompleteAdapter.setData(newList)
        })
        viewModel.explanationResult.observe(this, Observer<MutableList<DisplayItem>> { newList ->
            dictContentAdapter.setData(newList)
        })
    }

    override fun onResume() {
        super.onResume()
        dictLookupAutoCompleteAdapter.emptyData()
    }
}