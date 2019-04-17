package com.mike.tudict.view.animation

import android.animation.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Property
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mike.tudict.R


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/15
 * Description:
 */
object CircularReveal {
    fun transact(
        activity: AppCompatActivity,
        startViews: Array<View>,
        targetView: FrameLayout,
        targetFragment: Fragment
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.supportFragmentManager.beginTransaction().replace(R.id.container, targetFragment)
                .commitAllowingStateLoss()
            return
        }

        // Deal with nested startViews
        var parentX = 0
        var parentY = 0
        for (i in startViews.size - 1 downTo 1) {
            parentX += startViews[i].left
            parentY += startViews[i].top
        }
        val startView = startViews.first()
        var startViewL = parentX + startView.left
        var startViewR = parentX + startView.right
        var startViewTop = parentY + startView.top
        var startViewBottom = parentY + startView.bottom

        var centerX = (startViewL + startViewR) / 2
        var centerY = (startViewTop + startViewBottom) / 2 //- startView.height
        val endRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()

        val accentColor = ContextCompat.getColor(activity.applicationContext, R.color.color_jd_red)
        var colorChange = ObjectAnimator.ofInt(
            targetView,
            FOREGROUND_COLOR, accentColor, Color.TRANSPARENT
        )
            .apply {
                setEvaluator(ArgbEvaluator())
//                    interpolator = this@QuizActivity.interpolator
            }

        var circularReveal: Animator = ViewAnimationUtils.createCircularReveal(
            targetView, centerX, centerY, startView.width.toFloat() / 2, endRadius
        )
            .apply {
                interpolator = FastOutLinearInInterpolator()

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        //                            icon?.visibility = View.GONE
                        removeListener(this)
                    }
                })
            }

        activity.supportFragmentManager.beginTransaction().replace(R.id.container, targetFragment)
            .commitAllowingStateLoss()
        ViewCompat.animate(startView)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .alpha(1.2f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                override fun onAnimationEnd(view: View?) {
                    targetView.visibility = View.VISIBLE
                }
            })
            .start()
        with(AnimatorSet()) {
            play(circularReveal).with(colorChange)
            start()
        }
    }

    val FOREGROUND_COLOR: Property<FrameLayout, Int>
        get() = object :
            IntProperty<FrameLayout>("foregroundColor") {

            override fun setValue(layout: FrameLayout, value: Int) {
                if (layout.foreground is ColorDrawable) {
                    (layout.foreground.mutate() as ColorDrawable).color = value
                } else {
                    layout.foreground = ColorDrawable(value)
                }
            }

            override fun get(layout: FrameLayout): Int? {
                return if (layout.foreground is ColorDrawable) {
                    (layout.foreground as ColorDrawable).color
                } else {
                    Color.TRANSPARENT
                }
            }
        }

    abstract class IntProperty<T>(name: String) : Property<T, Int>(Int::class.java, name) {

        /**
         * A type-specific override of the [.set] that is faster when
         * dealing
         * with fields of type `int`.
         */
        abstract fun setValue(type: T, value: Int)

        override fun set(type: T, value: Int?) = setValue(type, value!!.toInt())
    }
}