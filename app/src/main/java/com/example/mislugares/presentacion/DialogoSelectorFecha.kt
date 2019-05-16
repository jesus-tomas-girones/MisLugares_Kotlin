package com.example.mislugares.presentacion

import android.app.DatePickerDialog
import android.os.Bundle
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.support.v4.app.DialogFragment
import java.util.*


class DialogoSelectorFecha : DialogFragment() {

   private var escuchador: OnDateSetListener? = null

   fun setOnDateSetListener(escuchador: OnDateSetListener) {
      this.escuchador = escuchador
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val calendario = Calendar.getInstance()
      val fecha = getArguments()?.getLong("fecha")?:0L
      calendario.setTimeInMillis(fecha)
      val anyo = calendario.get(Calendar.YEAR)
      val mes = calendario.get(Calendar.MONTH)
      val dia = calendario.get(Calendar.DAY_OF_MONTH)
      return DatePickerDialog(getActivity(), escuchador, anyo, mes, dia)
   }
}
