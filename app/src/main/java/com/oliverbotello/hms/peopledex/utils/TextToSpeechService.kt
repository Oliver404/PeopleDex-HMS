package com.oliverbotello.hms.peopledex.utils

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
            modelName: String,
            successListener: OnSuccessListener<Boolean>,
            failureListener: OnFailureListener
        ) {
            val localModelManager = MLLocalModelManager.getInstance()
            val model = MLTtsLocalModel.Factory(modelName).create()

            localModelManager.isModelExist(model)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)
        }

        fun downloadModel(
            modelName: String,
            downloadListener: MLModelDownloadListener,
            successListener: OnSuccessListener<Void>,
            failureListener: OnFailureListener
        ) {
            val localModelManager = MLLocalModelManager.getInstance()
            val model = MLTtsLocalModel.Factory(modelName).create()
            val request = MLModelDownloadStrategy.Factory().needWifi()
                .create()

            localModelManager.downloadModel(model, request, downloadListener)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)
        }
    }

    init {
        if (ENGINE == null) {
            val mlTtsConfig = MLTtsConfig()
                .setLanguage(MLTtsConstants.TTS_LAN_ES_ES)
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