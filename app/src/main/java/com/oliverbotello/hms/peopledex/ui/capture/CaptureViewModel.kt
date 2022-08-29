package com.oliverbotello.hms.peopledex.ui.capture

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.model.PeopleDexConnection
import com.oliverbotello.hms.peopledex.model.PersonEnt
import com.oliverbotello.hms.peopledex.utils.AUX_BPICTURE
import com.oliverbotello.hms.peopledex.utils.AUX_NPICTURE
import com.oliverbotello.hms.peopledex.utils.AUX_RPICTURE
import java.util.*

class CaptureViewModel : ViewModel() {
    private val errorForm: MutableLiveData<Pair<String?, String?>> = MutableLiveData(Pair(null, null))
    private val isBussy: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setBussyObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        isBussy.observe(lifecycleOwner, observer)
    }

    fun setOnErrorObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Pair<String?, String?>>) {
        errorForm.observe(lifecycleOwner, observer)
    }

    fun onSave(name: String, description: String) {
        isBussy.value = true

        if (validate(name, description)) {
            val id = Date().time
            val person = PersonEnt(id, name, true, "Beautiful", "")
            val picHelper = PicturesHelper()

            PeopleDexConnection().insertPerson(person)
            picHelper.renameImage(AUX_NPICTURE, "${id}n.png")
            picHelper.renameImage(AUX_RPICTURE, "${id}r.png")
            picHelper.renameImage(AUX_BPICTURE, "${id}b.png")
        }

        isBussy.value = false
    }

    fun validate(name: String, description: String): Boolean {
        var valid = true
        var nameError: String? = null
        var descriptionError: String? = null

        if (name.isBlank()) {
            valid = false

            if (name.isNotEmpty())
                nameError = "Escribe un nombre"
        }

        if (description.isBlank()) {
            valid = false

            if (description.isNotEmpty())
                descriptionError = "Escribe una descripcion"
        }

        errorForm.value = Pair(nameError, descriptionError)

        return valid
    }
}