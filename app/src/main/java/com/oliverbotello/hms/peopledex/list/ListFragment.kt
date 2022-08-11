package com.oliverbotello.hms.peopledex.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.camera.CameraFragment

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