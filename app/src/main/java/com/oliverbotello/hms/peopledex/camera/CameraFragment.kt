package com.oliverbotello.hms.peopledex.camera

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.view.PixelCopy.OnPixelCopyFinishedListener
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.scale
import androidx.core.util.size
import androidx.core.util.valueIterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.BitmapUtils
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import com.oliverbotello.hms.peopledex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CameraFragment : Fragment() {

    companion object {
        fun newInstance() = CameraFragment()
    }

    private lateinit var surfaceView: SurfaceView
    private lateinit var viewModel: CameraViewModel
    private lateinit var lensEngine: LensEngine

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_camera, container, false)

        surfaceView = root.findViewById<SurfaceView>(R.id.srfcvw_camera)
        val surfacePaint = root.findViewById<SurfaceView>(R.id.srfcvw_paint)
        surfacePaint.holder.setFormat(PixelFormat.TRANSPARENT)
        surfaceView.holder.addCallback(
            object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {

                }


                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                    Log.e("Oliver404", "Cambio la imagen")
                    val analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(
                        MLFaceAnalyzerSetting.Factory() // Set whether to detect key face points.
                        .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS) // Set whether to detect facial features and expressions.
                        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES) // Enable only facial expression detection and gender detection.
                        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURE_EMOTION or MLFaceAnalyzerSetting.TYPE_FEATURE_GENDAR) // Set whether to detect face contour points.
                        .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES) // Set whether to enable face tracking and specify the fast tracking mode.
                        .setTracingAllowed(true, MLFaceAnalyzerSetting.MODE_TRACING_FAST) // Set the speed and precision of the detector.
                        .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED) // Set whether to enable pose detection (enabled by default).
                        .create()
                    )
                    analyzer.setTransactor(
                        object : MLAnalyzer.MLTransactor<MLFace?> {
                            override fun transactResult(results: MLAnalyzer.Result<MLFace?>) {
                                val items = results.analyseList
                                surfaceView
                                if (items.size > 0) {
                                    Log.e("Oliver404", "faces: ${items[0].toString()}")
                                    draw(items)

//                                    lensEngine.photograph()
//                                    lensEngine.e.lens.takePicture()
                                    /// esto medio funicona
//                                    lensEngine.photograph(
//                                        object : LensEngine.ShutterListener {
//                                            override fun clickShutter() {
//
//                                            }
//                                        },
//                                        object : LensEngine.PhotographListener {
//                                            override fun takenPhotograph(p0: ByteArray?) {
//                                                GlobalScope.launch(Dispatchers.Main) {
//                                                    this@CameraFragment.requireView()
//                                                        .findViewById<AppCompatImageView>(R.id.preview)
////                                                        .setImageBitmap(BitmapUtils.getBitmap(p0, results.frameProperty))
//                                                        .setImageBitmap(BitmapFactory.decodeByteArray(p0,0,p0!!.size))
//
//                                                }
//
//
//                                            }
//
//                                        }
//                                    )


                                }
                            }

                            override fun destroy() {
                                Log.e("Oliver404", "destroy")
                            }

                            private fun draw(faces: SparseArray<MLFace?>) {
                                val canvas = surfacePaint.holder.lockCanvas()

                                if (canvas != null && faces != null) {

                                    //Clear the canvas
                                    canvas.drawColor(0, PorterDuff.Mode.CLEAR)

                                    for (face in faces.valueIterator()) {
                                        val icon = BitmapFactory.decodeResource(this@CameraFragment.resources, if(face!!.features.sexProbability > 0.5f)R.drawable.woman else R.drawable.man).scale(64, 64, true)



                                        Paint().also {
                                            it.color = Color.GREEN
                                            it.textSize = 48F
                                            it.textAlign = Paint.Align.LEFT
                                            canvas.drawText("Smiling: ${face!!.emotions.smilingProbability * 100}%", face.border.left.toFloat(), 64f, it)
                                            canvas.drawText("Angry: " + face!!.emotions.angryProbability, face.border.left.toFloat(), 64f + 50, it)
                                            canvas.drawText("Disgust: " + face!!.emotions.disgustProbability, face.border.left.toFloat(), 64f + 100, it)
                                            canvas.drawText("Fear: " + face!!.emotions.fearProbability, face.border.left.toFloat(), 64f+ 150, it)
                                            canvas.drawText("Neutral: " + face!!.emotions.neutralProbability, face.border.left.toFloat(), 64f + 200, it)
                                            canvas.drawText("Sad: " + face!!.emotions.sadProbability, face.border.left.toFloat(), 64f + 250, it)
//                                            canvas.drawText("Surprise: " + face!!.emotions.surpriseProbability, face.border.left.toFloat(), 64f + 300, it)
                                            canvas.drawText("Test: " + face!!.faceKeyPoints.size, face.border.left.toFloat(), 64f + 300, it)
                                            canvas.drawBitmap(icon,64f, 64f, it)

                                        }
//                                        BitmapUtils.cut(face.getFaceShape().coordinatePoints)

                                    }

                                    surfacePaint.holder.unlockCanvasAndPost(canvas)
                                }
                            }
                        }
                    )
                    lensEngine = LensEngine.Creator(context, analyzer)
                        .setLensType(LensEngine.FRONT_LENS)
                        .applyDisplayDimension(height - 64, width - 64)
                        .applyFps(30.0f)
                        .enableAutomaticFocus(true)
                        .create()



                    Log.e("Oliver404", "" + this@CameraFragment.requireView().findViewById<SurfaceView>(R.id.srfcvw_camera).holder)
                    lensEngine!!.run(this@CameraFragment.requireView().findViewById<SurfaceView>(R.id.srfcvw_camera).holder)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {

                }
            }
        )

        return root
    }

}