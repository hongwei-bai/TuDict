package com.mike.tudict.model.dictionary.txtparser

import android.util.Log
import com.mike.tudict.model.DictConstants

import java.util.ArrayList
import java.util.Stack
import java.util.regex.Matcher
import java.util.regex.Pattern

class BracketUtils(private val mString: String) {
    private val mBracketPositionList = ArrayList<Int>()
    private val mBracketTypeList = ArrayList<String>()
    private var mBracketPairCount = 0

    private val mLeftBracketOrginazied = ArrayList<Int>()
    private val mRightBracketOrginazied = ArrayList<Int>()

    init {
        parse()
    }

    fun inBracket(position: Int): Boolean {
        if (0 == mBracketPairCount) {
            return false
        }

        // Log.d("mString=" + mString);
        // Log.d("mBracketPairCount=" + mBracketPairCount + ", size=" +
        // mLeftBracketOrginazied.size());
        for (i in 0 until mBracketPairCount) {
            if (position > mLeftBracketOrginazied[i] && position < mRightBracketOrginazied[i]) {
                // Log.d("[在括号里] mString=" + mString);
                return true
            }
        }

        return false
    }

    private fun parse() {
        goThroughAllBrackets()

        val stack = Stack<Int>()
        for (i in mBracketPositionList.indices) {
            val position = mBracketPositionList[i]
            val type = mBracketTypeList[i]
            if (type == "(") {
                stack.push(position)
            } else {
                if (!stack.isEmpty()) {
                    val pairPos = stack.pop()
                    mLeftBracketOrginazied.add(pairPos)
                    mRightBracketOrginazied.add(position)
                    // Log.d("pair:" + pairPos + " ~ " + position);
                } else {
                    if (matchNumberTilePattern(mString, position)) {
                        Log.i(
                            TAG, "legal unmatched bracket found, pos=" + position + ", mString="
                                    + mString
                        )
                    } else {
                        Log.e(TAG, "bracket mismatch! mString=$mString")
                    }
                }
            }
        }
    }

    private fun goThroughAllBrackets() {
        var from = 0
        while (true) {
            val posL = mString.indexOf("(", from)
            val posR = mString.indexOf(")", from)
            var posMin = -1
            var type: String? = null
            if (-1 == posL) {
                posMin = posR
                type = ")"
            } else if (-1 == posR) {
                posMin = posL
                type = "("
            } else {
                if (posL < posR) {
                    posMin = posL
                    type = "("
                } else {
                    posMin = posR
                    type = ")"
                }
            }
            if (posMin > -1) {
                mBracketPositionList.add(posMin)
                mBracketTypeList.add(type)
                from = posMin + 1
            } else {
                break
            }
        }
    }

    private fun matchNumberTilePattern(fullstring: String, curPos: Int): Boolean {
        var targetString: String? = null
        if (curPos >= 2) {
            targetString = fullstring.substring(curPos - 2, curPos + 1)
        } else if (curPos >= 1) {
            targetString = fullstring.substring(curPos - 1, curPos + 1)
        } else {
            return false
        }
        val pattern = Pattern.compile(DictConstants.RegexConstants.NUMBLE_TILE_APATTERN)
        val matcher = pattern.matcher(targetString)
        return if (matcher.find()) {
            true
        } else false
    }

    private fun checkBracketPair(): Boolean {
        if (mBracketPositionList.size % 2 == 1) {
            Log.d(TAG, "mBracketPositionList.size = " + mBracketPositionList.size)
            return false
        }

        mBracketPairCount = mBracketPositionList.size / 2
        var i = 0
        for (s in mBracketTypeList) {
            if (s == "(") {
                i++
            }
        }
        if (i != mBracketPairCount) {
            Log.d(TAG, "mBracketPairCount = $mBracketPairCount, i = $i")
        }
        return i == mBracketPairCount
    }

    companion object {
        private val TAG = "BracketUtils"
    }
}
