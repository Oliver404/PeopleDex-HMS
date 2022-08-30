package com.oliverbotello.hms.peopledex.ui.camera

import android.os.Bundle
import android.util.SparseArray
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import com.huawei.hms.mlsdk.face.MLFaceEmotion
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.utils.AUX_NPICTURE
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel(), MLAnalyzer.MLTransactor<MLFace?>,
    LensEngine.ShutterListener, LensEngine.PhotographListener {
    val analyzer: MLFaceAnalyzer
//    private var analyzerDefault: MLFaceAnalyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer()
    var drawListener: OnDrawChange? = null
    var faceDetect: OnFaceDetect? = null
    private val params: MutableLiveData<Bundle> = MutableLiveData(null)
    private val available: MutableLiveData<Boolean> = MutableLiveData(false)
    private var type: String = "NORMAL"
    private var gender: String = "MAN"

    init {
        val setting = MLFaceAnalyzerSetting.Factory() // Set whether to detect key face points.
            .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS) // Set whether to detect facial features and expressions.
            .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES) // Enable only facial expression detection and gender detection.
            .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURE_EMOTION or MLFaceAnalyzerSetting.TYPE_FEATURE_GENDAR) // Set whether to detect face contour points.
            .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES) // Set whether to enable face tracking and specify the fast tracking mode.
            .setTracingAllowed(true, MLFaceAnalyzerSetting.MODE_TRACING_FAST) // Set the speed and precision of the detector.
            .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED) // Set whether to enable pose detection (enabled by default).
            .setPoseDisabled(true)
            .create()
        analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting)

        analyzer.setTransactor(this)
    }

    fun setNavitionObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Bundle>) {
        params.observe(lifecycleOwner, observer)
    }

    fun setAvailableObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        available.observe(lifecycleOwner, observer)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setAvailableValue(isAvailable: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            available.value = isAvailable
        }
    }

    private fun setGender(sexProbability: Float) {
        gender = if (sexProbability > .5f) "WOMAN" else "MAN"
    }

    private fun setType(emotions: MLFaceEmotion) {
        var probability = emotions.smilingProbability
        type = "SMILING"

        if (emotions.neutralProbability > probability) {
            probability = emotions.neutralProbability
            type = "NEUTRAL"
        }

        if (emotions.angryProbability > probability) {
            probability = emotions.angryProbability
            type = "ANGRY"
        }

        if (emotions.disgustProbability > probability) {
            probability = emotions.disgustProbability
            type = "DISGUST"
        }

        if (emotions.fearProbability > probability) {
            probability = emotions.fearProbability
            type = "FEAR"
        }

        if (emotions.sadProbability > probability) {
            probability = emotions.sadProbability
            type = "SAD"
        }

        if (emotions.surpriseProbability > probability) {
            probability = emotions.surpriseProbability
            type = "SURPRISE"
        }
    }
    /*
    * MLAnalyzer.MLTransactor<MLFace?>
    * */
    override fun transactResult(result: MLAnalyzer.Result<MLFace?>) {
        val faces = result.analyseList

        faces[0]?.let {
            setGender(it.features.sexProbability)
            setType(it.emotions)
        }

        setAvailableValue(faces.size() > 0)
        drawListener?.onDrawChange(faces)

    }

    override fun destroy() {

    }

    /*
    * LensEngine.PhotographListener
    * */
    override fun clickShutter() {
        // Show loading
    }

    override fun takenPhotograph(bteArray: ByteArray?) {
        bteArray?.let {
            PicturesHelper().saveImage(AUX_NPICTURE, bteArray)

            params.value = bundleOf("gender" to gender, "type" to type)
        }
    }

    interface OnDrawChange {
        fun onDrawChange(data: SparseArray<MLFace?>)
    }

    interface OnFaceDetect {
        fun onFaceDetect()
    }
}