package com.task.wikicats.ui

import android.content.Context
import android.widget.Toast
import com.task.wikicats.R
import java.io.IOException

object ErrorManager {
    fun Context.showErrorMessage(error: Throwable) {
        val message = when (error) {
            is IOException -> getString(R.string.error_no_internet_connection)
            else -> error.message ?: getString(R.string.error_unknown)
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}