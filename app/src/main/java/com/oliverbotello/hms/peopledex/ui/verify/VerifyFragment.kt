package com.oliverbotello.hms.peopledex.ui.verify

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.oliverbotello.hms.peopledex.R

class VerifyFragment : Fragment(), VerifyViewModel.OnImageMade {

    companion object {
        fun newInstance() = VerifyFragment()
    }

    private lateinit var imgvwPerson: AppCompatImageView
    private lateinit var viewModel: VerifyViewModel

    /*
    * Fragment
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VerifyViewModel::class.java]
        viewModel.imageListener = this

        this.context?.let { viewModel.detectFace(it) }
    }

    /*
    * Class
    * */
    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_verify, container, false)
        imgvwPerson = root.findViewById(R.id.imgvw_person)

        return root
    }

    override fun onImageMade(image: Bitmap) {
        imgvwPerson.setImageBitmap(image)
    }
}