package com.oliverbotello.hms.peopledex.ui.splash

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.mlsdk.model.download.MLModelDownloadListener
import com.huawei.hms.mlsdk.tts.MLTtsConstants
import com.oliverbotello.hms.peopledex.TextToSpeechService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel : ViewModel(), OnSuccessListener<Any?>,
    OnFailureListener, MLModelDownloadListener {
    private val progress: MutableLiveData<Int> = MutableLiveData(0)
    private val model: String = MLTtsConstants.TTS_SPEAKER_OFFLINE_ES_ES_FEMALE_BEE

    init {
        setProgress(10)
        TextToSpeechService.validateModel(model, this as OnSuccessListener<Boolean>, this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setProgress(value: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            progress.value = value
        }
    }

    fun setProgressObserver(owner: LifecycleOwner, observer: Observer<Int>) {
        progress.observe(owner, observer)
    }

    private fun onSuccessValidateModel(isDownloaded: Boolean) {
        if (isDownloaded)  // if Voice Model is downloaded
            setProgress(100)
        else {
            setProgress(20)

            TextToSpeechService.downloadModel(model, this, this as OnSuccessListener<Void>, this)
        }
    }

    private fun onSuccessDownloadModel() {
        setProgress(100)
    }

    /* OnSuccessListener<Boolean>*/
    override fun onSuccess(result: Any?) {
        if (result is Boolean) onSuccessValidateModel(result)
        else onSuccessDownloadModel()
    }

    /* OnFailureListener*/
    override fun onFailure(exception: Exception?) {
        Log.e("Oliver404", "Error")
    }

    /*
    * MLModelDownloadListener
    * */
    override fun onProcess(alreadyDownLength: Long, totalLength: Long) {
        setProgress((60.0 / totalLength * alreadyDownLength + 20).toInt())
    }
}