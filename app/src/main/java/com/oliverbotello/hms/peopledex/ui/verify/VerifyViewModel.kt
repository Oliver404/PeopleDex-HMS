package com.oliverbotello.hms.peopledex.ui.verify

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.lifecycle.ViewModel
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerFactory
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerSetting
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentation
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationScene
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import java.io.ByteArrayOutputStream


class VerifyViewModel : ViewModel(), OnSuccessListener<MLImageSegmentation>, OnFailureListener {
//    private val setting: MLImageSegmentationSetting = MLImageSegmentationSetting.Factory()
//        .setExact(false)
//        .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
//        .setScene(MLImageSegmentationScene.FOREGROUND_ONLY)
//        .create()
//    private val analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting)
    private var faceAnalyzer = MLFaceVerificationAnalyzerFactory.getInstance().getFaceVerificationAnalyzer()
    private val picturesHelper: PicturesHelper = PicturesHelper()
    var imageListener: OnImageMade? = null

    init {
//        picturesHelper.getImage("oli.jpg")?.let {
//            val frame = MLFrame.fromBitmap(it)
//            analyzer.asyncAnalyseFrame(frame)
//                .addOnSuccessListener(this)
//                .addOnFailureListener(this)
//        }
//        detectFace()
    }

    private fun makeBlackImage(picture: Bitmap) {
        val canvas = Canvas(picture)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(picture, 0f, 0f, paint)
        imageListener?.onImageMade(picture)
        // Mostrar imagen
        Log.e("Oliver404", "Result OK")
    }
//picture: Bitmap
    fun detectFace(context: Context) {
        val oli = picturesHelper.getImage("oli.jpg")

        if (oli != null) Log.e("Oliver404", "Oli is not null")

        var templateFrame = MLFrame.fromBitmap(oli)
        val results = faceAnalyzer.setTemplateFace(templateFrame)

        Log.e("Oliver404", "detectFace OK: ${results.size}")
//        faceAnalyzer.asyncAnalyseFrame(MLFrame.fromBitmap(picturesHelper.getImage("person.jpg"))).addOnSuccessListener {
//            Log.e("Oliver404", "detectFace OK: success")
//        }.addOnFailureListener {
//            Log.e("Oliver404", "detectFace OK: FAIL")
//        }
    }

    override fun onSuccess(result: MLImageSegmentation) {
        val picture = result.foreground
        val stream = ByteArrayOutputStream()

        picture.compress(Bitmap.CompressFormat.PNG, 100, stream)
        picturesHelper.saveImage("person2.jpg", stream.toByteArray())
//        detectFace(picture)
        makeBlackImage(picture)
    }

    override fun onFailure(p0: Exception?) {
        Log.e("Oliver404", "Error")
    }

    interface OnImageMade {
        fun onImageMade(image: Bitmap): Unit
    }
}