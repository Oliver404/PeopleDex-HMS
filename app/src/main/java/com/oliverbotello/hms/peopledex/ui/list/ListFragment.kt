package com.oliverbotello.hms.peopledex.ui.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.huawei.hms.mlsdk.tts.*
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.TextToSpeechService
import com.oliverbotello.hms.peopledex.ui.camera.CameraFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class ListFragment : Fragment(), View.OnClickListener, MLTtsCallback {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var txt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().findViewById<AppCompatImageView>(R.id.imgvw_preview).setImageBitmap(PicturesHelper().getImage("oli.jpg"))
        requireView().findViewById<AppCompatImageView>(R.id.imgvw_preview_person).setImageBitmap(PicturesHelper().getImage("oli.jpg"))
    }

    override fun onResume() {
        super.onResume()
        Log.e("Oliver404", "OnResume")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root: View = inflater.inflate(R.layout.fragment_list, container, false)

        root.findViewById<ExtendedFloatingActionButton>(R.id.btn_new).setOnClickListener(this)
        root.findViewById<ExtendedFloatingActionButton>(R.id.btn_talk).setOnClickListener(this)
        txt = root.findViewById<TextView>(R.id.txtlog)

        return root
    }

    override fun onClick(v: View?) {
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_listFragment_to_cameraFragment)
//        Runtime.getRuntime().exec("logcat -c").inputStream
//        val itxt = Runtime.getRuntime().exec("logcat").inputStream.bufferedReader()
//        itxt.useLines {
//                lines ->
//            for (i in 0 until 10) {
//                TextToSpeechService().talk(lines.elementAt(i))
//            }
//
//
//
//        }

    }

    /*
   * MLTtsCallback
   * */
    override fun onError(taskId: String, err: MLTtsError) {
//        TODO("Not yet implemented")
    }

    override fun onWarn(taskId: String, warn: MLTtsWarn) {
//        TODO("Not yet implemented")
    }

    override fun onRangeStart(taskId: String, start: Int, end: Int) {
//        TODO("Not yet implemented")
    }

    override fun onAudioAvailable(
        taskId: String,
        audioFragment: MLTtsAudioFragment,
        offset: Int,
        range: Pair<Int, Int>,
        bundle: Bundle
    ) {
//        TODO("Not yet implemented")
    }

    override fun onEvent(taskId: String, eventId: Int, bundle: Bundle?) {
        val isInterrupted: Boolean
        when (eventId) {
            MLTtsConstants.EVENT_PLAY_START -> {
            }
            MLTtsConstants.EVENT_PLAY_STOP -> // Called when playback stops.
                isInterrupted = bundle?.getBoolean(MLTtsConstants.EVENT_PLAY_STOP_INTERRUPTED)?:false
            MLTtsConstants.EVENT_PLAY_RESUME -> {
            }
            MLTtsConstants.EVENT_PLAY_PAUSE -> {
            }
            MLTtsConstants.EVENT_SYNTHESIS_START -> {
            }
            MLTtsConstants.EVENT_SYNTHESIS_END -> {
            }
            MLTtsConstants.EVENT_SYNTHESIS_COMPLETE -> // TTS is complete. All synthesized audio streams are passed to the app.
                isInterrupted = bundle?.getBoolean(MLTtsConstants.EVENT_SYNTHESIS_INTERRUPTED)?:false
            else -> {
            }
        }
    }
}