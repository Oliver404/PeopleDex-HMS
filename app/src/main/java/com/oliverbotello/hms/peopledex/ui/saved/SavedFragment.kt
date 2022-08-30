package com.oliverbotello.hms.peopledex.ui.saved

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.Navigation
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.utils.TextToSpeechService

class SavedFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SavedFragment()
    }

    private lateinit var imgPerson: AppCompatImageView
    private lateinit var txtName: AppCompatTextView
    private lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val root = inflater.inflate(R.layout.fragment_saved, container, false)

        imgPerson = root.findViewById(R.id.imgvw_person)
        txtName = root.findViewById(R.id.txtvw_is)

        arguments?.let {
            txtName.text = it.getCharSequence("name")

            imgPerson.setImageBitmap(PicturesHelper().getImage("${it.getLong("id")}r.png"))
            TextToSpeechService().talk("Es ${it.getCharSequence("name")}")
        }

        root.findViewById<View>(R.id.btn_accept).setOnClickListener(this)

        return root
    }

    override fun onClick(v: View?) {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_savedFragment_to_listFragment)
    }

}