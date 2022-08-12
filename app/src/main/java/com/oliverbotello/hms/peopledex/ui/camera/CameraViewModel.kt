package com.oliverbotello.hms.peopledex.ui.camera

import android.util.Log
import android.util.SparseArray
import androidx.lifecycle.ViewModel
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import com.oliverbotello.hms.peopledex.PicturesHelper

class CameraViewModel : ViewModel(), MLAnalyzer.MLTransactor<MLFace?>,
    LensEngine.ShutterListener, LensEngine.PhotographListener{
    private var setting = MLFaceAnalyzerSetting.Factory() // Set whether to detect key face points.
        .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS) // Set whether to detect facial features and expressions.
        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES) // Enable only facial expression detection and gender detection.
        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURE_EMOTION or MLFaceAnalyzerSetting.TYPE_FEATURE_GENDAR) // Set whether to detect face contour points.
        .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES) // Set whether to enable face tracking and specify the fast tracking mode.
        .setTracingAllowed(true, MLFaceAnalyzerSetting.MODE_TRACING_FAST) // Set the speed and precision of the detector.
        .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED) // Set whether to enable pose detection (enabled by default).
        .setPoseDisabled(true)
        .create()
    var analyzer: MLFaceAnalyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting)
        private set
//    private var analyzerDefault: MLFaceAnalyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer()
//    private val facesData: SparseArray<MLFace?> = SparseArray<MLFace?>()
    val automatic: Boolean = false
    var drawListener: OnDrawChange? = null
    var faceDetect: OnFaceDetect? = null

    init {
        analyzer.setTransactor(this)
    }

    /*
    * MLAnalyzer.MLTransactor<MLFace?>
    * */
    override fun transactResult(result: MLAnalyzer.Result<MLFace?>) {
        drawListener?.onDrawChange(result.analyseList)

        if (automatic && result.analyseList.size() > 0)
            faceDetect?.onFaceDetect()


        Log.e("Oliver404", "faces")
    }

    override fun destroy() {

    }

    interface OnDrawChange {
        fun onDrawChange(data: SparseArray<MLFace?>)
    }

    interface OnFaceDetect {
        fun onFaceDetect()
    }

    override fun clickShutter() {
        // Show loading
    }

    override fun takenPhotograph(biteArray: ByteArray?) {
        biteArray?.let {
            PicturesHelper().saveImage(it)
        }
    }
}