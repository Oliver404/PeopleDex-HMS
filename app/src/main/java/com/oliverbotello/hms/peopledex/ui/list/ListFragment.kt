package com.oliverbotello.hms.peopledex.ui.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.ui.camera.CameraFragment

class ListFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().findViewById<AppCompatImageView>(R.id.imgvw_preview).setImageBitmap(PicturesHelper().getImage("oli.jpg"))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root: View = inflater.inflate(R.layout.fragment_list, container, false)

        root.findViewById<ExtendedFloatingActionButton>(R.id.btn_new).setOnClickListener(this)

        return root
    }

    override fun onClick(v: View?) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frmlyt_frame, CameraFragment.newInstance()).commit()
    }
}