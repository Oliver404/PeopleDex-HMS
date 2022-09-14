package com.oliverbotello.hms.peopledex.utils

import com.oliverbotello.hms.peopledex.R

fun getResourceCodeForType(type: String): Int = when(type) {
    "SMILING" -> R.color.happy
    "NEUTRAL" -> R.color.neutral
    "ANGRY" -> R.color.angry
    "DISGUST" -> R.color.disgust
    "FEAR" -> R.color.fear
    "SAD" -> R.color.sad
    "SURPRISE" -> R.color.surprice
    else -> R.color.neutral
}

fun getResourceBackgroundForType(type: String): Int = when(type) {
    "SMILING" -> R.drawable.ring_gradient_happy
    "NEUTRAL" -> R.drawable.ring_gradient_neutral
    "ANGRY" -> R.drawable.ring_gradient_angry
    "DISGUST" -> R.drawable.ring_gradient_disgust
    "FEAR" -> R.drawable.ring_gradient_fear
    "SAD" -> R.drawable.ring_gradient_sad
    "SURPRISE" -> R.drawable.ring_gradient_surprice
    else -> R.drawable.ring_gradient_neutral
}