package com.example.mislugares.presentacion

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.app.TimePickerDialog.OnTimeSetListener
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import java.util.*

class DialogoSelectorHora : DialogFragment() {

   private var escuchador: OnTimeSetListener? = null

   fun setOnTimeSetListener(escuchador: OnTimeSetListener) {
      this.escuchador = escuchador
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val calendario = Calendar.getInstance()
      val fecha = arguments?.getLong("fecha")?:System.currentTimeMillis()
      calendario.setTimeInMillis(fecha)
      val hora = calendario.get(Calendar.HOUR_OF_DAY)
      val minuto = calendario.get(Calendar.MINUTE)
      return TimePickerDialog(
         getActivity(), escuchador, hora,
         minuto, DateFormat.is24HourFormat(getActivity())
      )
   }
}