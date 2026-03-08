package uz.gita.contactapp.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView


fun Activity.showNotification(message: String, type: NotificationType) {
    val rootView = findViewById<ViewGroup>(android.R.id.content)

    val bannerView = TextView(this).apply {
        text = message
        setTextColor(Color.WHITE)
        textSize = 14f
        gravity = Gravity.CENTER
        setPadding(40, 30, 40, 30)

        val (startColor, endColor) = when (type) {
            NotificationType.SUCCESS -> "#00A63E" to "#009966"
            NotificationType.ERROR -> "#F54900" to "#E7000B"
            NotificationType.WARNING -> "#F9A825" to "#FBC02D"
            NotificationType.INFO -> "#0277BD" to "#01579B"
        }

        background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
        ).apply {
            cornerRadius = 24f
        }
    }

    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.TOP
        topMargin = 150
        leftMargin = 60
        rightMargin = 60
    }

    rootView.addView(bannerView, params)

    bannerView.alpha = 0f
    bannerView.translationY = -100f
    bannerView.animate()
        .alpha(1f)
        .translationY(0f)
        .setDuration(600)
        .withEndAction {
            bannerView.animate()
                .alpha(0f)
                .translationY(-100f)
                .setStartDelay(2500)
                .setDuration(700)
                .withEndAction { rootView.removeView(bannerView) }
                .start()
        }
        .start()
}
