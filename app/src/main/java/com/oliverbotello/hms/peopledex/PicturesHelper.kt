package com.oliverbotello.hms.peopledex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.lang.Exception

class PicturesHelper {
    companion object {
        private var FILES_DIR: String? = null

        fun initialize(context: Context) {
            FILES_DIR = context.filesDir.absolutePath
        }
    }

    fun renameImage(oldName: String, newName: String) {
        if (FILES_DIR == null) throw Exception("PicturesHelper is not init")

        val imgFile: File = File("${FILES_DIR}/${oldName}")
        val newFile: File = File("${FILES_DIR}/${newName}")
        imgFile.renameTo(newFile)
    }

    fun saveImage(imageName: String = "oli.jpg", byteArray: ByteArray): String {
        if (FILES_DIR == null) throw Exception("PicturesHelper is not init")

        val imgFile: File = File("${FILES_DIR}/${imageName}")

        imgFile.writeBytes(byteArray)

        return imageName
    }

    fun getImage(name: String): Bitmap? {
        if (FILES_DIR == null) throw Exception("PicturesHelper is not init")

        return BitmapFactory.decodeFile("${FILES_DIR}/${name}")
    }
}