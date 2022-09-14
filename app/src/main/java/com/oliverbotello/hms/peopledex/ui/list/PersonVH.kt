package com.oliverbotello.hms.peopledex.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.imageview.ShapeableImageView
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.model.PersonEnt
import com.oliverbotello.hms.peopledex.utils.getResourceCodeForType

class PersonVH(itemView: View, val onSelectListener: OnItemSelect<PersonEnt>?) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    companion object {
        fun newInstance(parent: ViewGroup, onSelectListener: OnItemSelect<PersonEnt>?): PersonVH =
            PersonVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.person_vh_layout, parent, false),
                onSelectListener
            )
    }
    private val image: ShapeableImageView
    private val txtvwName: AppCompatTextView
    private val chpType: Chip
    private val txtvwNumber: AppCompatTextView
    private var person: PersonEnt? = null

    init {
        image = itemView.findViewById(R.id.imgvw_person)
        txtvwName = itemView.findViewById(R.id.txtvw_person_name)
        chpType = itemView.findViewById(R.id.chp_person_type)
        txtvwNumber = itemView.findViewById(R.id.txtvw_person_number)

        this.itemView.setOnClickListener(this)
    }

    fun bind(bindPerson: PersonEnt) {
        person = bindPerson
        txtvwName.text = bindPerson.name
        txtvwNumber.text = "${bindPerson.id}"
        chpType.text = bindPerson.type

        chpType.setBackgroundColor(
            itemView.resources.getColor(getResourceCodeForType(bindPerson.type))
        )
        image.setImageBitmap(PicturesHelper().getImage("${bindPerson.id}n.png"))
    }

    override fun onClick(v: View?) {
        person?.let {
            onSelectListener?.onSelect(it)
        }
    }
}