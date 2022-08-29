package com.oliverbotello.hms.peopledex.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.oliverbotello.hms.peopledex.R

class ListFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initView(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root: View = inflater.inflate(R.layout.fragment_list, container, false)
        val mySnapHelper = LinearSnapHelper()
        val recycler: RecyclerView = root.findViewById(R.id.rcclrvw_list)
        recycler.adapter = PersonAdapter(listOf("","","","","","","","","","","","",""))

        mySnapHelper.attachToRecyclerView(recycler)
        root.findViewById<View>(R.id.btn_new).setOnClickListener(this)

        return root
    }

    override fun onClick(v: View?) {
        Log.e("Oliver404", "Go to Camera")
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_listFragment_to_cameraFragment)
    }
}