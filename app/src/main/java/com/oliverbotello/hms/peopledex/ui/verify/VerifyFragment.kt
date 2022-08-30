package com.oliverbotello.hms.peopledex.ui.verify

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.ui.list.ListFragment

class VerifyFragment : Fragment(), VerifyViewModel.OnImageMade,
    Observer<Boolean>, View.OnClickListener {

    companion object {
        fun newInstance() = VerifyFragment()
    }

    private lateinit var imgvwPerson: AppCompatImageView
    private lateinit var txtVwWhoIs: AppCompatTextView
    private lateinit var btnCapture: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progress: CircularProgressIndicator
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

        viewModel.setBussyObserver(viewLifecycleOwner, this)
    }

    /*
    * Class
    * */
    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_verify, container, false)
        imgvwPerson = root.findViewById(R.id.imgvw_person)
        txtVwWhoIs = root.findViewById(R.id.txtvw_who_is)
        btnCapture = root.findViewById(R.id.btn_capture)
        btnCancel = root.findViewById(R.id.btn_cancel)
        progress = root.findViewById(R.id.progress_circular)

        btnCapture.setOnClickListener(this)
        btnCancel.setOnClickListener(this)

        return root
    }

    private fun loading(show: Boolean) {
        if (show) {
            progress.isVisible = true
            imgvwPerson.isVisible = false
            txtVwWhoIs.isVisible = false
            btnCapture.isVisible = false
            btnCancel.isVisible = false
        }
        else {
            progress.isVisible = false
            imgvwPerson.isVisible = true
            txtVwWhoIs.isVisible = true
            btnCapture.isVisible = true
            btnCancel.isVisible = true
        }
    }

    override fun onImageMade(image: Bitmap) {
        imgvwPerson.setImageBitmap(image)
    }

    /*
    * Observer
    * */
    override fun onChanged(t: Boolean?) {
        t?.let {
            loading(it)
        }
    }

    /*
    * View.OnClickListener
    * */
    override fun onClick(v: View?) {
        v?.let {
            when(v.id) {
                R.id.btn_capture ->
                    Navigation.findNavController(requireView()).navigate(
                        R.id.action_verifyFragment_to_captureFragment,
                        this@VerifyFragment.arguments
                    )
                R.id.btn_cancel ->
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_verifyFragment_to_listFragment2)
                else -> return
            }
        }
    }
}