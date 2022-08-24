package com.oliverbotello.hms.peopledex.ui.list

import androidx.lifecycle.ViewModel
import com.oliverbotello.hms.peopledex.TextToSpeechService

class ListViewModel : ViewModel() {
    val talker = TextToSpeechService()

    init {
        talker.talk("Bienvenido Oliver")
    }
}