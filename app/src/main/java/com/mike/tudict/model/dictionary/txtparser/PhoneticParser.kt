package com.mike.tudict.model.dictionary.txtparser

import java.util.HashMap
import kotlin.collections.Map.Entry

class PhoneticParser {
    companion object {
        private var pronunciationMap: HashMap<String, String>? = null

        init {
            pronunciationMap = HashMap()
            pronunciationMap!!["U"] = "ʊ"
            pronunciationMap!!["V"] = "ʒ"
            pronunciationMap!!["A"] = "æ"
            pronunciationMap!!["E"] = "ə"
            pronunciationMap!!["I"] = "ɪ"
            pronunciationMap!!["R"] = "ɔ"
            pronunciationMap!!["F"] = "ʃ"
            pronunciationMap!!["N"] = "ŋ"
            pronunciationMap!!["B"] = "ɑ"
            pronunciationMap!!["Q"] = "ʌ"
            pronunciationMap!!["C"] = "ɔ"// ɒ
            pronunciationMap!!["W"] = "θ"
            pronunciationMap!!["\\"] = "ɜ"
            pronunciationMap!!["5"] = "ˈ"
            pronunciationMap!!["9"] = "ˌ"
            pronunciationMap!!["T"] = "ð"

            // American
            pronunciationMap!!["o"] = "oʊ"
            pronunciationMap!!["J"] = "ʊ"
            pronunciationMap!!["L"] = "ər"
            pronunciationMap!!["^"] = "ɡ"
            pronunciationMap!!["Z"] = "ɛ"
            pronunciationMap!!["["] = "ɜr"

        }

        fun parse(txtCode: String?): String? {
            if (null == txtCode) {
                return null
            }
            var result = txtCode
            val iterator = pronunciationMap!!.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Entry<String, String>
                val key = entry.key
                val value = entry.value
                result = result!!.replace(key, value)
            }
            return result
        }
    }
}// TODO Auto-generated constructor stub
