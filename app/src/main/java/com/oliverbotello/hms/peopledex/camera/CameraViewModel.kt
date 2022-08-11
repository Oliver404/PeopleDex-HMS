package com.oliverbotello.hms.peopledex.camera

import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import kotlin.coroutines.coroutineContext

class CameraViewModel : ViewModel(), SurfaceHolder.Callback, MLAnalyzer.MLTransactor<MLFace?> {
    private var setting = MLFaceAnalyzerSetting.Factory() // Set whether to detect key face points.
        .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS) // Set whether to detect facial features and expressions.
        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES) // Enable only facial expression detection and gender detection.
        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURE_EMOTION or MLFaceAnalyzerSetting.TYPE_FEATURE_GENDAR) // Set whether to detect face contour points.
        .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES) // Set whether to enable face tracking and specify the fast tracking mode.
        .setTracingAllowed(true, MLFaceAnalyzerSetting.MODE_TRACING_FAST) // Set the speed and precision of the detector.
        .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED) // Set whether to enable pose detection (enabled by default).
        .setPoseDisabled(true)
        .create()
    private var analyzer: MLFaceAnalyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting)
            get() = analyzer
    private var analyzerDefault: MLFaceAnalyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer()

    init {
        analyzer!!.setTransactor(FaceAnalyzerTransactor())
    }

    inner class FaceAnalyzerTransactor : MLAnalyzer.MLTransactor<MLFace?> {
        override fun transactResult(results: MLAnalyzer.Result<MLFace?>) {
            val items = results.analyseList
            Log.e("Oliver404", items[0].toString())
        }

        override fun destroy() {
            // Callback method used to release resources when the detection ends.
        }
    }

    /*
    * SurfaceHolder.Callback
    * */
    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    /*
    * MLAnalyzer.MLTransactor<MLFace?>
    * */
    override fun transactResult(p0: MLAnalyzer.Result<MLFace?>?) {
        TODO("Not yet implemented")
    }

    override fun destroy() {

    }
}