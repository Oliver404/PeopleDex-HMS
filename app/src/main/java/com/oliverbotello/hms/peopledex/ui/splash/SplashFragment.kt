package com.oliverbotello.hms.peopledex.ui.splash

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.oliverbotello.hms.peopledex.R

class SplashFragment : Fragment(), Observer<Int> {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.setProgressObserver(viewLifecycleOwner, this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root: View = inflater.inflate(R.layout.fragment_splash, container, false)
        progressBar = root.findViewById(R.id.prgssbr_splash)

        return root
    }

    /*
    * Observer<Double>
    * */
    override fun onChanged(progress: Int) {
        progressBar.progress = progress

        if (progress >= 100)
            Navigation.findNavController(this.requireView())
                .navigate(R.id.action_splashFragment_to_listFragment)
    }
}