package com.arif.guvercintakip.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateText(): String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(this))
