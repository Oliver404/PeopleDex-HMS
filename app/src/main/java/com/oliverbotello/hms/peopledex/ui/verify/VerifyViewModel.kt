package com.oliverbotello.hms.peopledex.ui.verify

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.oliverbotello.hms.peopledex.utils.AUX_BPICTURE
import com.oliverbotello.hms.peopledex.utils.AUX_RPICTURE
import com.oliverbotello.hms.peopledex.utils.TextToSpeechService
import java.io.ByteArrayOutputStream


class VerifyViewModel : ViewModel(), OnSuccessListener<MLImageSegmentation>, OnFailureListener {
    private val setting: MLImageSegmentationSetting = MLImageSegmentationSetting.Factory()
        .setExact(true)
        .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
        .setScene(MLImageSegmentationScene.FOREGROUND_ONLY)
        .create()
    private val analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting)
    private val picturesHelper: PicturesHelper = PicturesHelper()
    var imageListener: OnImageMade? = null
    val isBussy: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        picturesHelper.getImage("oli.jpg")?.let {
            isBussy.value = true
            val frame = MLFrame.fromBitmap(it)

            analyzer.asyncAnalyseFrame(frame)
                .addOnSuccessListener(this)
                .addOnFailureListener(this)
        }
    }

    fun setBussyObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        isBussy.observe(lifecycleOwner, observer)
    }

    private fun makeBlackImage(picture: Bitmap) {
        val stream = ByteArrayOutputStream()
        val canvas = Canvas(picture)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(picture, 0f, 0f, paint)
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream)
        picturesHelper.saveImage(AUX_BPICTURE, stream.toByteArray())
        imageListener?.onImageMade(picture)
    }

    override fun onSuccess(result: MLImageSegmentation) {
        Log.e("Oliver404", "Success")
        val picture = result.foreground
        val stream = ByteArrayOutputStream()

        picture.compress(Bitmap.CompressFormat.PNG, 100, stream)
        picturesHelper.saveImage(AUX_RPICTURE, stream.toByteArray())
        makeBlackImage(picture)

        isBussy.value = false

        TextToSpeechService().talk("Quien es esa persona?!")
    }

    override fun onFailure(p0: Exception?) {
        Log.e("Oliver404", "Error")

        isBussy.value = false
    }

    interface OnImageMade {
        fun onImageMade(image: Bitmap): Unit
    }
}