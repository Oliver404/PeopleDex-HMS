package com.oliverbotello.hms.peopledex.ui.list

interface OnItemSelect<T> {
    fun onSelect(item: T)
}