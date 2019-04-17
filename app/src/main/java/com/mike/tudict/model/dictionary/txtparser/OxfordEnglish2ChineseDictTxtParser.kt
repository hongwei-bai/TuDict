package com.mike.tudict.model.dictionary.txtparser

import android.util.Log
import com.mike.tudict.model.Constants
import com.mike.tudict.model.DictConstants
import com.mike.tudict.model.DictConstants.Companion.NOTE_ON_USAGE
import com.mike.tudict.model.DictConstants.Companion.abbrevation
import com.mike.tudict.model.DictConstants.Companion.prefix
import com.mike.tudict.model.DictConstants.Companion.symbol
import com.mike.tudict.model.DictConstants.RegexConstants.Companion.PRONUNCIATION_REGEX
import com.mike.tudict.model.DictConstants.RegexConstants.Companion.PRONUNCIATION_REGEX_TWINS

import java.util.*
import kotlin.collections.Map.Entry
import java.util.regex.Pattern

class OxfordEnglish2ChineseDictTxtParser : DictTxtParser(), DictConstants.RegexConstants, DictConstants {

    private var mSupportProperties: ArrayList<String>? = null
    private var mSupportPropertiesBegin: ArrayList<String>? = null
    private var mSupportPropertiesExt: ArrayList<String>? = null
    private var mSupportPropertiesRepeat: ArrayList<String>? = null
    private var mSupportNumbers: ArrayList<String>? = null
    private var mSupportAlphabetas: ArrayList<String>? = null

    private val integerComparator = Comparator<Int> { o1, o2 ->
        if (o1 > o2) {
            1
        } else if (o1 < o2) {
            -1
        } else {
            0
        }
    }

    init {
        initProperty()
        initNumbers()
        initAlphabetas()
    }

    private fun initProperty() {
        var basiclist = ArrayList<String>()
        basiclist = ArrayList()
        basiclist.add(WordProperty.noun)
        basiclist.add(WordProperty.pronoun)
        basiclist.add(WordProperty.adjective)
        basiclist.add(WordProperty.adverb)
        basiclist.add(WordProperty.verb)
        basiclist.add(WordProperty.numeral)
        basiclist.add(WordProperty.article)
        basiclist.add(WordProperty.indefinite_article)
        basiclist.add(WordProperty.preposition)
        basiclist.add(WordProperty.conjunction)
        basiclist.add(WordProperty.interjection)

        val useagelist = ArrayList<String>()
        useagelist.add(NOTE_ON_USAGE)
        useagelist.add(prefix)
        useagelist.add(abbrevation)
        useagelist.add(symbol)

        mSupportPropertiesBegin = ArrayList()
        mSupportProperties = ArrayList()
        mSupportPropertiesRepeat = ArrayList()
        for (b in basiclist) {
            mSupportPropertiesBegin!!.add(b)
            mSupportProperties!!.add(" $b")
            mSupportPropertiesRepeat!!.add("$b.")
        }
        for (u in useagelist) {
            mSupportPropertiesBegin!!.add(u)
            mSupportProperties!!.add(" $u")
        }

        mSupportPropertiesExt = ArrayList()
        mSupportPropertiesExt!!.add(" ")
        mSupportPropertiesExt!!.add("[")
        mSupportPropertiesExt!!.add(":")
    }

    private fun initNumbers() {
        mSupportNumbers = ArrayList()
        for (i in 0..39) {
            mSupportNumbers!!.add(" $i ")
        }
    }

    private fun initAlphabetas() {
        mSupportAlphabetas = ArrayList()
        var c = 'a'
        while (c <= 'z') {
            val cArray = CharArray(1)
            cArray[0] = c
            val cString = String(cArray)
            mSupportAlphabetas!!.add("($cString)")
            c++
        }
    }

    override fun parseFile(filename: String, list: ArrayList<String>): ArrayList<WordData> {
        val hashMap = HashMap<String, WordData>()
        val datalist = ArrayList<WordData>()

        if (list.size <= 0 || list.size / 2 == 1) {
            Log.e(TAG, "OxfordEnglish2ChineseDictTxtParser parseFile Invalid list size! size = " + list.size)
            return datalist
        }

        var i = 0
        while (i < list.size - 1) {
            val key = list[i].trim { it <= ' ' }
            val info = list[i + 1]

            var previousInfo: String? = null
            if (i >= 2) {
                previousInfo = list[i - 1]
            }

            var tmpDate: WordData? = null
            if (hashMap.containsKey(key)) {
                tmpDate = parseWordOriginalInfo(hashMap[key]!!, key, info, previousInfo)
                hashMap.remove(key)
                hashMap[key] = tmpDate
            } else {
                tmpDate = parseWordOriginalInfo(key, info, previousInfo)
                tmpDate.file = filename
                hashMap[key] = tmpDate
            }
            i += 2
        }

        val iterator = hashMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next() as Entry<String, WordData>
            datalist.add(entry.value)
        }

        for (data in datalist) {
            data.properties = ArrayList()
            if (data.reference != null) {
                if (1 == data.originallist!!.size) {
                    data.properties!!
                        .add(parseWordProperty(data.reference, data.originallist!![0]))
                } else {
                    if (!isLegalCompoundWord(data)) {
                        Log.e(
                            TAG,
                            "OxfordEnglish2ChineseDictTxtParser unexception structure, word<"
                                    + data.english + ">, ref=" + data.reference
                        )
                    }
                }
            } else {
                for (original in data.originallist!!) {
                    data.properties!!.add(parseWordProperty(null, original))
                }
            }
        }

        // for (WordData d : datalist) {
        // Log.d("key = " + d.english);
        // for (WordProperty p : d.properties) {
        // Log.d("<<" + p.property + ">>");
        // Log.d(" PP" + p.pronunciationRaw);
        // }
        // Log.d("-------");
        // }

        return datalist
    }

    private fun isLegalCompoundWord(data: WordData): Boolean {
        if (data.reference != WordProperty.adverb_dot) {
            return false
        }
        for (orginal in data.originallist!!) {
            val wordPropertyTmp = parseWordProperty(null, orginal)
            if (null == wordPropertyTmp.property) {
                continue
            }
            if (wordPropertyTmp.property == WordProperty.adjective) {
                if (data.english!!.endsWith("ly")) {
                    return true
                }
            } else if (wordPropertyTmp.property == WordProperty.verb) {
                if (data.english!!.endsWith("ingly")) {
                    return true
                }
            }
        }
        return false
    }

    private fun parseWordOriginalInfo(
        cur: WordData, key: String, info: String,
        previousInfo: String?
    ): WordData {
        cur.originallist!!.add(info)
        return cur
    }

    private fun parseWordOriginalInfo(key: String, info: String, previousInfo: String?): WordData {
        val originalInfo = WordData()
        originalInfo.english = key.trim { it <= ' ' }
        originalInfo.originallist = ArrayList()

        for (support in mSupportPropertiesRepeat!!) {
            if (info.trim { it <= ' ' } == support) {
                originalInfo.reference = support
                originalInfo.originallist!!.add(previousInfo!!)
            }
        }

        if (null == originalInfo.reference) {
            originalInfo.originallist!!.add(info)
        }

        return originalInfo
    }

    private fun parseWordProperty(property: String?, orininal: String): WordProperty {
        val wordProperty = WordProperty()
        wordProperty.pronunciationRaw = trimPronunciation(orininal)
        wordProperty.pronunciation = PhoneticParser.parse(wordProperty.pronunciationRaw)

        var pending: String? = null
        if (wordProperty.pronunciationRaw != null) {
            pending = orininal.replace(wordProperty.pronunciationRaw!!, "")
            wordProperty.pronunciationRaw = wordProperty.pronunciationRaw!!.trim { it <= ' ' }
        } else {
            pending = orininal
        }

        if (property != null) {
            wordProperty.property = property
            wordProperty.property = wordProperty.property!!.trim { it <= ' ' }
        } else {
            wordProperty.property = trimWordProperty(pending)
            if (wordProperty.property != null) {
                pending = pending.replaceFirst(wordProperty.property!!.toRegex(), "")
                wordProperty.property = wordProperty.property!!.trim { it <= ' ' }
            }
        }

        val items = parseWordItems(pending)
        wordProperty.items = items.items

        return wordProperty
    }

    private fun parseWordItems(info: String): WordProperty {
        var parent = WordProperty()
        // Log.d(">>info=" + info);
        var positionlist = ArrayList<Int>()
        val hashMap = HashMap<Int, String>()

        val bracketUtils = BracketUtils(info)
        for (number in mSupportNumbers!!) {
            var position = -1
            var from = 0
            while (true) {
                position = info.indexOf(number, from)
                if (-1 == position) {
                    break
                }
                if (!bracketUtils.inBracket(position)) {
                    break
                }
                from = position + 1
            }
            if (position > -1) {
                positionlist.add(position)
                hashMap[position] = number
            }
        }
        for (alpha in mSupportAlphabetas!!) {
            val position = info.indexOf(alpha)
            if (position > -1) {
                positionlist.add(position)
                hashMap[position] = alpha
            }
        }
        validateNumbers(positionlist, hashMap, info)

        if (positionlist.isEmpty()) {
            val item = Hierarchy()
            item.isleaf = true
            item.index = null
            item.list = null
            item.item = WordItem(info)
            parent.items = item
            parent = parseWordItemSegments(parent, null, info)
        } else {
            // positionlist.sort(integerComparator);
            positionlist = sort(positionlist)
            // Log.d("info = " + info);
            // for (Integer s : positionlist) {
            // Log.d("idx=" + s + ", symbol=" + hashMap.get(s));
            // }
            // Log.d("----------");
            if (positionlist[0] > 1) {
                parent.content = info.substring(0, positionlist[0] - 1)
            } else {
                parent.content = null
            }
            parent.items = organiseHierarchy(positionlist, hashMap)
            val DLRlist = generateDLRWithNextStartIdxInfo(parent.items!!)
            parent = parseWordItemSegments(parent, DLRlist, info)
        }

        return parent
    }

    private fun validateNumbers(
        positionlist: ArrayList<Int>, hashMap: HashMap<Int, String>,
        info: String
    ) {
        val illigelPosList = ArrayList<Int>()
        var lastNumberIdx = -1
        for (pos in positionlist) {
            val itemheader = hashMap[pos]
            if (mSupportNumbers!!.contains(itemheader)) {
                val curNumberIdx = mSupportNumbers!!.indexOf(itemheader)
                if (lastNumberIdx > -1) {
                    if (curNumberIdx != lastNumberIdx + 1 || lastNumberIdx == 0) {
                        illigelPosList.add(pos)
                    }
                }
                lastNumberIdx = curNumberIdx
            }
        }
        if (!illigelPosList.isEmpty()) {
            var msg = "1st parsed items:"
            for (pos in positionlist) {
                val itemheader = hashMap[pos]
                msg += itemheader!! + ", "
            }
            msg += "illigel:"
            for (pos in illigelPosList) {
                val itemheader = hashMap[pos]
                msg += itemheader!! + ", "
                positionlist.remove(pos)
                hashMap.remove(pos)
            }
            // Log.i("info=" + info);
            // Log.i(msg);
        }
    }

    private fun generateDLRWithNextStartIdxInfo(top: Hierarchy): ArrayList<Hierarchy> {
        var DLRlist = ArrayList<Hierarchy>()
        DLRlist = recursionConvertToDLR(DLRlist, top)
        for (i in 0 until DLRlist.size - 1) {
            DLRlist[i].nextIndexStart = DLRlist[i + 1].indexStart
        }
        DLRlist[DLRlist.size - 1].nextIndexStart = -1

        // for (Hierarchy h : DLRlist) {
        // Log.d("idx=" + h.index + ", st= " + (h.indexEnd+1) + ", end=" +
        // h.nextIndexStart);
        // }

        return DLRlist
    }

    private fun recursionConvertToDLR(result: ArrayList<Hierarchy>, node: Hierarchy): ArrayList<Hierarchy> {
        var result = result
        result.add(node)

        if (node.isleaf) {
            // Log.d("recursionConvertToDLR leaf=" + node.index);
            return result
        } else {
            for (child in node.list!!) {
                result = recursionConvertToDLR(result, child)
            }
        }
        return result
    }

    private fun parseWordItemSegments(
        wordProperty: WordProperty,
        DLRlist: ArrayList<Hierarchy>?, info: String
    ): WordProperty {
        val indexSegmentMap = HashMap<String, String>()

        if (null == DLRlist) {
            wordProperty.items!!.item = parseWordItemInternal(wordProperty.items!!.item!!)
            return wordProperty
        }

        try {
            for (h in DLRlist) {
                val endIndex = if (h.nextIndexStart != -1) h.nextIndexStart else info.length
                val segment = info.substring(h.indexEnd + 1, endIndex)
                // Log.d(">>put idx=" + h.index + ": segment=" + segment);
                indexSegmentMap[h.index!!] = segment
            }
        } catch (e: StringIndexOutOfBoundsException) {
            Log.d(TAG, "OxfordEnglish2ChineseDictTxtParser info=$info")
            e.printStackTrace()
        }

        val top = wordProperty.items
        for (i in 0 until top!!.list!!.size) {
            val level1Node = top!!.list!![i]
            val original = indexSegmentMap[level1Node.index]
            if (!original!!.isEmpty() && original.trim { it <= ' ' } != "") {
                level1Node.item = WordItem()
                level1Node.item!!.original = original
                top.list!![i] = level1Node
            }
            if (!level1Node.isleaf) {
                for (j in 0 until level1Node.list!!.size) {
                    val level2Node = level1Node.list!![j]
                    if (level2Node.isleaf) {
                        val original2 = indexSegmentMap[level2Node.index]
                        if (!original2!!.isEmpty() && original2.trim { it <= ' ' } != "") {
                            level2Node.item = WordItem()
                            level2Node.item!!.original = original2
                            level1Node.list!![j] = level2Node
                        }
                    }
                }
            }
        }

        for (i in 0 until top.list!!.size) {
            val level1Node = top.list!![i]
            if (level1Node.item != null) {
                // Log.d("idx=" + level1Node.index + ": segment=" +
                // level1Node.item.original);
                level1Node.item = parseWordItemInternal(level1Node.item!!)
            }
            level1Node.index = level1Node.index!!.trim { it <= ' ' }
            top.list!![i] = level1Node

            if (!level1Node.isleaf) {
                for (j in 0 until level1Node.list!!.size) {
                    val level2Node = level1Node.list!![j]
                    // Log.d(" idx=" + level2Node.index + ": segment=" +
                    // level2Node.item.original);
                    level2Node.index = level2Node.index!!.trim { it <= ' ' }
                    level2Node.item = parseWordItemInternal(level2Node.item!!)
                    level1Node.list!![j] = level2Node
                }
            }
        }

        return wordProperty
    }

    private fun parseWordItemInternal(item: WordItem): WordItem {
        val original = item.original
        val position = original!!.indexOf(DictConstants.SEPARATOR_EXPLAINATION)
        if (-1 == position) {
            item.explaination = original
            val explainationSplit = separateEnglishAndChinese(item.explaination!!)
            item.explainationEng = explainationSplit[0]
            item.explainationChn = explainationSplit[1]
            item.examples = null
            return item
        }

        item.explaination = original.substring(0, position)
        val explainationSplit = separateEnglishAndChinese(item.explaination!!)
        item.explainationEng = explainationSplit[0]
        item.explainationChn = explainationSplit[1]

        val pending = original.substring(position + DictConstants.SEPARATOR_EXPLAINATION.length)
        // Log.d("explaination=" + item.explaination);
        // Log.d("pending=" + pending);

        item.examples = ArrayList()
        val exampleArray =
            pending.split(DictConstants.SEPARATOR_EXAMPLE.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (example in exampleArray) {
            /*
             * (idm 习语) + idom xxx. ,idom2... to be parsed in the future. just
             * truncate at the moment.
             */
            if (example.length >= 600) {
                // Log.e(TAG, "example too long, len=" + example.length() +
                // ", example=" + example);
                val newExample = example.substring(0, 550) + "...(idom cut by bhw1899)"
                item.examples!!.add(example)
            } else {
                item.examples!!.add(example)
            }
            // Log.d("example=" + example);
        }

        return item
    }

    private fun separateEnglishAndChinese(original: String): Array<String?> {
        val charArray = original.toCharArray()
        var firstChineseCharacterPosition = -1
        var i = 0
        while (i < charArray.size) {
            if (PinyinUtil.isChinese(charArray[i])) {
                var skip = false
                var skipLength = -1

                val position = original.indexOf(DictConstants.EXPLAINATION_SKIP_PATTERN, i)
                if (position > -1) {
                    skip = true
                    skipLength = position - i
                }

                if (skip) {
                    i += skipLength
                } else {
                    firstChineseCharacterPosition = i
                    break
                }
            }
            i++
        }

        // Log.d("info=" + original);
        val result = arrayOfNulls<String?>(2)
        if (-1 == firstChineseCharacterPosition || 0 == firstChineseCharacterPosition) {
            result[0] = original
            result[1] = null
            return result
        }

        result[0] = original.substring(0, firstChineseCharacterPosition - 1)
        result[1] = original.substring(firstChineseCharacterPosition)
        // Log.d("eng=" + result[0]);
        // Log.d("chn=" + result[1]);
        // Log.d("--------");
        return result
    }

    private fun sort(list: ArrayList<Int>): ArrayList<Int> {
        for (i in list.indices) {
            for (j in 0 until i) {
                if (list[i] < list[j]) {
                    val tmp = list[j]
                    list[j] = list[i]
                    list[i] = tmp
                }
            }
        }
        return list
    }

    private fun organiseHierarchy(
        positionlist: ArrayList<Int>,
        hashMap: HashMap<Int, String>
    ): Hierarchy {
        val hierarchy = Hierarchy()
        hierarchy.isleaf = false
        hierarchy.index = null
        hierarchy.indexStart = -1
        hierarchy.indexEnd = -1
        hierarchy.item = null
        hierarchy.list = ArrayList()

        var currentIdx = -1
        for (position in positionlist) {
            val flag = hashMap[position]
            if (mSupportNumbers!!.contains(flag)) {
                val numberHierarchy = Hierarchy()
                numberHierarchy.isleaf = true
                numberHierarchy.index = flag
                numberHierarchy.indexStart = position
                numberHierarchy.indexEnd = position + flag!!.length - 1
                numberHierarchy.item = null
                numberHierarchy.list = null
                hierarchy.list!!.add(numberHierarchy)
                currentIdx = hierarchy.list!!.size - 1
            } else if (mSupportAlphabetas!!.contains(flag)) {
                val alphaHierarchy = Hierarchy()
                alphaHierarchy.isleaf = true
                alphaHierarchy.index = flag
                alphaHierarchy.indexStart = position
                alphaHierarchy.indexEnd = position + flag!!.length - 1
                alphaHierarchy.item = WordItem()
                alphaHierarchy.list = null

                if (-1 == currentIdx) {
                    hierarchy.list!!.add(alphaHierarchy)
                } else {
                    val numberHierarchy = hierarchy.list!![currentIdx]
                    numberHierarchy.isleaf = false
                    numberHierarchy.item = null
                    if (null == numberHierarchy.list) {
                        numberHierarchy.list = ArrayList()
                    }
                    numberHierarchy.list!!.add(alphaHierarchy)
                }
            }
        }
        return hierarchy
    }

    private fun trimPronunciation(info: String): String? {
        var p = Pattern.compile(PRONUNCIATION_REGEX_TWINS)
        // Log.d("info=" + info);
        var m = p.matcher(info)
        if (m.find()) {
            // Log.d("findex=" + m.group(0));
            return m.group(0)
        }
        p = Pattern.compile(PRONUNCIATION_REGEX)
        m = p.matcher(info)
        return if (m.find()) {
            // Log.d("find=" + m.group(0));
            m.group(0)
        } else null
    }

    private fun trimWordProperty(info: String): String? {
        val MAX_PARSE_PROPERTY_LEN = 15
        val bUseSubString = info.length > MAX_PARSE_PROPERTY_LEN
        for (ext in mSupportPropertiesExt!!) {
            for (support in mSupportPropertiesBegin!!) {
                if (info.startsWith(support + ext)) {
                    return if (ext == " ") {
                        support
                    } else support.replace(ext, "")
                }
            }
            for (support in mSupportProperties!!) {
                if (info.startsWith(support)) {
                    return if (ext == " ") {
                        support
                    } else support.replace(ext, "")
                }
            }
            for (support in mSupportProperties!!) {
                var position = -1
                if (bUseSubString) {
                    position = info.substring(0, MAX_PARSE_PROPERTY_LEN).indexOf(support)
                } else {
                    position = info.indexOf(support)
                }
                if (position > -1) {
                    return if (ext == " ") {
                        support
                    } else support.replace(ext, "")
                }
            }
        }
        return null
    }

    companion object {
        private val TAG = Constants.TAG
    }
}
