package com.example.mislugares.casos_uso

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import com.example.mislugares.R
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.AdaptadorLugaresBD
import com.example.mislugares.presentacion.DialogoSelectorHora
import java.util.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mislugares.presentacion.DialogoSelectorFecha

class CasosUsoLugarFecha(
   override val actividad: FragmentActivity,
   override val fragment: Fragment,
   override val lugares: LugaresBD,
   override val adaptador: AdaptadorLugaresBD
) : CasosUsoLugar(actividad, fragment, lugares, adaptador),
   TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {

   lateinit var lugar: Lugar
   //   lateinit var tHora: TextView
   var pos: Int = -1

   fun cambiarHora(pos: Int /*, tHora: TextView*/) {
      lugar = adaptador.lugarPosicion(pos)
//      this.tHora = hora
      this.pos = pos
      val dialogo = DialogoSelectorHora()
      dialogo.setOnTimeSetListener(this)
      val args = Bundle()
      args.putLong("fecha", lugar.fecha)
      dialogo.setArguments(args)
      dialogo.show(
         (actividad /*as AppCompatActivity*/).supportFragmentManager
         , "selectorHora"
      )
   }

   override fun onTimeSet(vista: TimePicker?, horas: Int, minuto: Int) {
      val calendario = Calendar.getInstance()
      calendario.setTimeInMillis(lugar.fecha)
      calendario.set(Calendar.HOUR_OF_DAY, horas)
      calendario.set(Calendar.MINUTE, minuto)
      lugar.fecha = calendario.getTimeInMillis()
      actualizaPosLugar(pos, lugar)   //** public

      val textView = actividad.findViewById<TextView>(R.id.hora)
      //val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
      //textView.text = formato.format(Date(lugar.fecha))

      textView.text= java.text.DateFormat.getTimeInstance().format(Date(lugar.fecha))
   }

   fun cambiarFecha(pos: Int) {
      lugar = adaptador.lugarPosicion(pos)
      this.pos = pos
      val dialogo = DialogoSelectorFecha()
      dialogo.setOnDateSetListener(this)
      val args = Bundle()
      args.putLong("fecha", lugar.fecha)
      dialogo.setArguments(args)
      dialogo.show(actividad.supportFragmentManager, "selectorFecha")
   }

   override fun onDateSet(view: DatePicker, anyo: Int, mes: Int, dia: Int) {
      val calendario = Calendar.getInstance()
      calendario.timeInMillis = lugar.fecha
      calendario.set(Calendar.YEAR, anyo)
      calendario.set(Calendar.MONTH, mes)
      calendario.set(Calendar.DAY_OF_MONTH, dia)
      lugar.fecha = calendario.timeInMillis
      actualizaPosLugar(pos, lugar)
      val textView = actividad.findViewById<TextView>(R.id.fecha)
      textView.text = java.text.DateFormat.getDateInstance().format(Date(lugar.fecha))
   }

 //  val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
//   textView.text = formato.format(Date(lugar.fecha))


}