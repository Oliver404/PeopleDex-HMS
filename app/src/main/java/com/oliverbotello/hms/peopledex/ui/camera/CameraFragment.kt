package com.oliverbotello.hms.peopledex.ui.camera

import android.graphics.*
import android.os.Bundle
import android.util.SparseArray
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.scale
import androidx.core.util.valueIterator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.face.MLFace
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.utils.OFF_SET

class CameraFragment : Fragment(), SurfaceHolder.Callback,
    CameraViewModel.OnDrawChange, CameraViewModel.OnFaceDetect,
    Observer<Any>, View.OnClickListener {

    companion object {
        fun newInstance() = CameraFragment()
    }

    private lateinit var surfaceCamera: SurfaceView
    private lateinit var surfacePaint: SurfaceView
    private lateinit var btnTakePicture: AppCompatButton
    private lateinit var viewModel: CameraViewModel
    private lateinit var lensEngine: LensEngine

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        viewModel.drawListener = this
        viewModel.faceDetect = this

        viewModel.setNavitionObserver(viewLifecycleOwner, this as Observer<Bundle>)
        viewModel.setAvailableObserver(viewLifecycleOwner, this as Observer<Boolean>)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        surfaceCamera = root.findViewById(R.id.srfcvw_camera)
        surfacePaint = root.findViewById(R.id.srfcvw_paint)
        btnTakePicture = root.findViewById(R.id.btn_take_picture)

        surfacePaint.holder.setFormat(PixelFormat.TRANSPARENT)
        surfaceCamera.holder.addCallback(this)
        btnTakePicture.setOnClickListener(this)

        return root
    }

    private fun draw(faces: SparseArray<MLFace?>) {
        val canvas = surfacePaint.holder.lockCanvas()

        if (canvas != null) {
            val label: String = resources.getString(R.string.person_label).uppercase()
            val iconSize = resources.getDimension(R.dimen.camera_gener_icon).toInt()
            val fontSize = resources.getDimension(R.dimen.camera_text_size)
            val margin = resources.getDimension(R.dimen.margin)
            val widthBanner = iconSize + fontSize * label.length * .65f + margin * 3
            canvas.drawColor(0, PorterDuff.Mode.CLEAR) // Clear canvas

            for (face in faces.valueIterator()) {
                val (icon, color) =
                    if(face!!.features.sexProbability > 0.5f)
                        Pair(
                            BitmapFactory
                                .decodeResource(this@CameraFragment.resources, R.drawable.woman)
                                .scale(iconSize, iconSize, true),
                            Color.MAGENTA
                        )
                    else
                        Pair(
                            BitmapFactory
                                .decodeResource(this@CameraFragment.resources, R.drawable.man)
                                .scale(iconSize, iconSize, true),
                            Color.CYAN
                        )

                Paint().also {
                    it.textSize = fontSize
                    it.textAlign = Paint.Align.LEFT
                    it.color = Color.BLACK
                    it.alpha = 75
                    canvas.drawRoundRect(
                        canvas.width - OFF_SET - face.border.exactCenterX() - widthBanner/2,
                        face.border.top.toFloat() - margin * 5 - fontSize,
                        canvas.width - OFF_SET - face.border.exactCenterX() + widthBanner/2,
                        face.border.top.toFloat() - margin * 3,
                        50f,
                        50f,
                        it
                    )
                    it.alpha = 100
                    it.color = color
                    canvas.drawBitmap(
                        icon,
                        canvas.width - OFF_SET - face.border.exactCenterX() - widthBanner/2 + margin,
                        face.border.top.toFloat() - margin * 4 - fontSize,
                        it
                    )
                    canvas.drawText(
                        label,
                        canvas.width - OFF_SET - face.border.exactCenterX() - widthBanner/2 + margin * 2 + fontSize,
                        face.border.top.toFloat() - margin * 3 - fontSize/2,
                        it
                    )

                }
            }

            surfacePaint.holder.unlockCanvasAndPost(canvas)
        }
    }

    /*
    * SurfaceHolder.Callback
    * */
    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        lensEngine = LensEngine.Creator(context, viewModel.analyzer)
            .setLensType(LensEngine.BACK_LENS)
            .applyDisplayDimension(height - OFF_SET.toInt(), width - OFF_SET.toInt())
            .applyFps(30.0f)
            .enableAutomaticFocus(true)
            .create()

        lensEngine.run(surfaceCamera.holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    override fun onDrawChange(data: SparseArray<MLFace?>) {
        draw(data)
    }

    override fun onFaceDetect() {
        lensEngine.photograph(viewModel, viewModel)
        lensEngine.release()
    }

    override fun onChanged(t: Any?) {
        t?.let {
            if (t is Bundle)
                Navigation.findNavController(this.requireView())
                    .navigate(R.id.action_cameraFragment_to_verifyFragment, it as Bundle)
            else if (t is Boolean)
                btnTakePicture.isEnabled = t
        }
    }

    /*
    * View.OnClickListener
    * */
    override fun onClick(v: View?) {
        lensEngine.photograph(viewModel, viewModel)
    }
}