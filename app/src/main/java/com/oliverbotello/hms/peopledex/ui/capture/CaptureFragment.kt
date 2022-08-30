package com.oliverbotello.hms.peopledex.ui.capture

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.model.PersonEnt

class CaptureFragment : Fragment(), Observer<Any>, View.OnClickListener {

    companion object {
        fun newInstance() = CaptureFragment()
    }

    private lateinit var nameLayout: TextInputLayout
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var viewModel: CaptureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CaptureViewModel::class.java)

        viewModel.setSavedInstance(arguments)
        viewModel.setBussyObserver(viewLifecycleOwner, this as Observer<Boolean>)
        viewModel.setParamsObserver(viewLifecycleOwner, this as Observer<Bundle>)
        viewModel.setOnErrorObserver(viewLifecycleOwner, this as Observer<Pair<String?, String?>>)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_capture, container, false)

        nameLayout = root.findViewById(R.id.txtinplyt_name)
        nameInput = root.findViewById(R.id.medttxt_name)
        descriptionLayout = root.findViewById(R.id.txtinplyt_description)
        descriptionInput = root.findViewById(R.id.medttxt_description)

        root.findViewById<View>(R.id.btn_save_capture).setOnClickListener(this)

        return root
    }

    override fun onChanged(t: Any?) {
        if (t is Boolean)
            this.requireView().findViewById<View>(R.id.loading_capture).isVisible = t
        else if (t is Bundle?) {
            t?.let {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_captureFragment_to_savedFragment, it)
            }
        }
        else if (t is Pair<*, *>?){

            nameLayout.error = t?.first as String?
            descriptionLayout.error = t?.second as String?
        }

    }

    override fun onClick(v: View?) {
        viewModel.onSave(nameInput.text.toString(), descriptionInput.text.toString())
    }

}