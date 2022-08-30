package com.oliverbotello.hms.peopledex.ui.capture

import android.os.Bundle
import androidx.core.os.bundleOf
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CaptureViewModel : ViewModel() {
    private val errorForm: MutableLiveData<Pair<String?, String?>> = MutableLiveData(Pair(null, null))
    private val isBussy: MutableLiveData<Boolean> = MutableLiveData(false)
    private val params: MutableLiveData<Bundle> = MutableLiveData(null)
    private var savedInstance: Bundle? = null

    fun setSavedInstance(savedInstance: Bundle?) {
        this.savedInstance = savedInstance
    }

    private fun setParamsValue(id: Long, name: String) {
        GlobalScope.launch(Dispatchers.Main) {
            params.value = bundleOf("id" to id, "name" to name)
        }
    }

    private fun setBussyValue(value: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            isBussy.value = value
        }
    }

    private fun setErrorFormValue(value: Pair<String?, String?>) {
        GlobalScope.launch(Dispatchers.Main) {
            errorForm.value = value
        }
    }

    fun setParamsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Bundle>) {
        params.observe(lifecycleOwner, observer)
    }

    fun setBussyObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        isBussy.observe(lifecycleOwner, observer)
    }

    fun setOnErrorObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Pair<String?, String?>>) {
        errorForm.observe(lifecycleOwner, observer)
    }

    fun onSave(name: String, description: String) {
        GlobalScope.launch {
            setBussyValue(true)

            val gender = savedInstance?.getString("gender")?:"MAN"
            val type = savedInstance?.getString("type")?:"NEUTRAL"

            if (validate(name, description)) {
                val id = Date().time
                val person = PersonEnt(id, name, gender.compareTo("MAN", true) != 0, type, "")
                val picHelper = PicturesHelper()

                PeopleDexConnection().insertPerson(person)
                picHelper.renameImage(AUX_NPICTURE, "${id}n.png")
                picHelper.renameImage(AUX_RPICTURE, "${id}r.png")
                picHelper.renameImage(AUX_BPICTURE, "${id}b.png")
                setParamsValue(id, name)
            }

            setBussyValue(false)
        }
    }

    private fun validate(name: String, description: String): Boolean {
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

        setErrorFormValue(Pair(nameError, descriptionError))

        return valid
    }
}