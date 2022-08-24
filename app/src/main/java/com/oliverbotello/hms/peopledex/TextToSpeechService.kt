package com.oliverbotello.hms.peopledex

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.mlsdk.model.download.MLLocalModelManager
import com.huawei.hms.mlsdk.model.download.MLModelDownloadListener
import com.huawei.hms.mlsdk.model.download.MLModelDownloadStrategy
import com.huawei.hms.mlsdk.tts.*

class TextToSpeechService() {
    companion object {
        private var ENGINE: MLTtsEngine? = null

        fun validateModel(
            model: String,
            successListener: OnSuccessListener<Boolean>,
            failureListener: OnFailureListener
        ) {
            val localModelManager = MLLocalModelManager.getInstance()
            val model = MLTtsLocalModel.Factory(model).create()

            localModelManager.isModelExist(model)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)
        }

        fun downloadModel(
            person: String,
            downloadListener: MLModelDownloadListener,
            successListener: OnSuccessListener<Void>,
            failureListener: OnFailureListener
        ) {
            // Create an on-device TTS model manager.
            val localModelManager = MLLocalModelManager.getInstance()
            // Create an MLTtsLocalModel instance and pass the speaker (indicating by person) to download the language model corresponding to the speaker.
            val model = MLTtsLocalModel.Factory(person).create()
            // Create a download policy configurator. You can set that when any of the following conditions is met, the model can be downloaded: 1. The device is charging; 2. Wi-Fi is connected; 3. The device is idle.
            val request = MLModelDownloadStrategy.Factory().needWifi()
                .create()
            // Create a download progress listener for the on-device model.
            val modelDownloadListener = downloadListener
            // Call the download API of the on-device model manager.
            localModelManager.downloadModel(model, request, modelDownloadListener)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)
        }
    }

    init {
        if (ENGINE == null) {
            val mlTtsConfig = MLTtsConfig()
                .setLanguage(MLTtsConstants.TTS_LAN_ES_ES) // Set the speaker
                // Set the TTS mode to on-device mode
                .setPerson(MLTtsConstants.TTS_SPEAKER_OFFLINE_ES_ES_FEMALE_BEE)
                .setSynthesizeMode(MLTtsConstants.TTS_OFFLINE_MODE)
            ENGINE = MLTtsEngine(mlTtsConfig)
        }
//        ENGINE!!.setTtsCallback(listener)
    }

    fun talk(message: String) {
        if (ENGINE == null) throw Exception("Engine is not init")

        ENGINE!!.speak(message, MLTtsEngine.QUEUE_APPEND or MLTtsEngine.OPEN_STREAM)
    }
}