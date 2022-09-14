package com.oliverbotello.hms.peopledex.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.model.PersonEnt
import com.oliverbotello.hms.peopledex.utils.getResourceBackgroundForType
import com.oliverbotello.hms.peopledex.utils.getResourceCodeForType


class ListFragment : Fragment(), View.OnClickListener, Observer<Any> {
    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var imgvwDetailPerson: AppCompatImageView
    private lateinit var txtVwDetailName: AppCompatTextView
    private lateinit var chpDetailGender: Chip
    private lateinit var chpDetailType: Chip
    private lateinit var txtVwDetailDescription: AppCompatTextView
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

        viewModel.setBussyObsever(viewLifecycleOwner, this as Observer<Boolean>)
        viewModel.setListPeople(viewLifecycleOwner, this as Observer<List<PersonEnt>>)
        viewModel.setPersonObserver(viewLifecycleOwner, this as Observer<PersonEnt>)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root: View = inflater.inflate(R.layout.fragment_list, container, false)
        val mySnapHelper = LinearSnapHelper()
        val recycler: RecyclerView = root.findViewById(R.id.rcclrvw_list)
        recycler.adapter = PersonAdapter(listOf(), null)
        imgvwDetailPerson = root.findViewById(R.id.imgvw_detail_person)
        txtVwDetailName = root.findViewById(R.id.txtvw_detail_name)
        chpDetailGender = root.findViewById(R.id.chp_detail_gender)
        chpDetailType = root.findViewById(R.id.chp_detail_type)
        txtVwDetailDescription = root.findViewById(R.id.txtvw_detail_description)

        mySnapHelper.attachToRecyclerView(recycler)
        root.findViewById<View>(R.id.btn_new).setOnClickListener(this)

        return root
    }

    private fun showDetail(person: PersonEnt) {
        imgvwDetailPerson.setImageBitmap(PicturesHelper().getImage("${person.id}r.png"))
        txtVwDetailName.text = person.name

        if (person.gender) {
            chpDetailGender.text = "Mujer"
            chpDetailGender.setChipStrokeColorResource(R.color.magenta)
            chpDetailGender.setTextColor(resources.getColor(R.color.magenta))
            chpDetailGender.chipIcon = resources.getDrawable(R.drawable.woman)
        }
        else {
            chpDetailGender.text = "Hombre"
            chpDetailGender.setChipStrokeColorResource(R.color.teal)
            chpDetailGender.setTextColor(resources.getColor(R.color.teal))
            chpDetailGender.chipIcon = resources.getDrawable(R.drawable.man)
        }

        chpDetailType.text = person.type
        txtVwDetailDescription.text = person.description

        imgvwDetailPerson.background = resources.getDrawable(getResourceBackgroundForType(person.type))

        chpDetailType.setChipStrokeColorResource(getResourceCodeForType(person.type))
        chpDetailType.setTextColor(resources.getColor(getResourceCodeForType(person.type)))
    }

    override fun onClick(v: View?) {
        Log.e("Oliver404", "Go to Camera")
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_listFragment_to_cameraFragment)
    }

    override fun onChanged(t: Any?) {
        t?.let {
            when (it) {
                is Boolean -> requireView().findViewById<View>(R.id.loading_dialog).isVisible = it
                is List<*> -> {
                    val recycler: RecyclerView = requireView().findViewById(R.id.rcclrvw_list)
                    recycler.adapter = PersonAdapter(it as List<PersonEnt>, viewModel)
                }
                is PersonEnt -> showDetail(it)
            }
        }
    }
}