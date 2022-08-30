package com.oliverbotello.hms.peopledex.ui.list

import android.app.Person
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.oliverbotello.hms.peopledex.model.PeopleDexConnection
import com.oliverbotello.hms.peopledex.model.PersonEnt
import com.oliverbotello.hms.peopledex.utils.TextToSpeechService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ListViewModel : ViewModel(), OnItemSelect<PersonEnt> {
    val selectedPerson: MutableLiveData<PersonEnt> = MutableLiveData(null)
    val lstPeople: MutableLiveData<List<PersonEnt>> = MutableLiveData(listOf())
    val isBussy: MutableLiveData<Boolean> = MutableLiveData(false)
    val talker = TextToSpeechService()

    init {
        talker.talk("Bienvenido Oliver")
        readData()
    }

    fun readData() {
        setBussyValue(true)
        GlobalScope.launch {
            val lst = PeopleDexConnection().getAll()
            GlobalScope.launch(Dispatchers.Main) {
                lstPeople.value = lst

                if (lst.size > 0)
                    selectedPerson.value = lst[0]

                setBussyValue(false)
            }
        }

    }

    private fun setBussyValue(value: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            isBussy.value = value
        }
    }

    fun setBussyObsever(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        isBussy.observe(lifecycleOwner, observer)
    }

    fun setListPeople(lifecycleOwner: LifecycleOwner, observer: Observer<List<PersonEnt>>) {
        lstPeople.observe(lifecycleOwner, observer)
    }

    fun setPersonObserver(lifecycleOwner: LifecycleOwner, observer: Observer<PersonEnt>) {
        selectedPerson.observe(lifecycleOwner, observer)
    }

    override fun onSelect(item: PersonEnt) {
        selectedPerson.value = item
    }
}