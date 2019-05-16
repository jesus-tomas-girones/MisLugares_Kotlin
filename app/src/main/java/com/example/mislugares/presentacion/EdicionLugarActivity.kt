package com.example.mislugares.presentacion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.edicion_lugar.*
import android.widget.ArrayAdapter
import com.example.mislugares.Aplicacion
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.R
import com.example.mislugares.modelo.TipoLugar

class EdicionLugarActivity: AppCompatActivity() {

   val lugares by lazy { (application as Aplicacion).lugares }
   val adaptador by lazy { (application as Aplicacion).adaptador }
   val usoLugar by lazy { CasosUsoLugar(this, null, lugares, adaptador ) } //**
   var pos = 0
   var _id = -1
   lateinit var lugar: Lugar

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.edicion_lugar)
      pos = intent.extras?.getInt("pos", -1) ?: 0
      _id = intent.extras?.getInt("_id", -1) ?: 0
      lugar = if (_id != -1) lugares.elemento(_id)
              else           adaptador.lugarPosicion(pos)
      actualizaVistas()
   }

   fun actualizaVistas() {
//      lugar = (application as Aplicacion).lugares.elemento(pos/*.toInt()*/)
      nombre.setText(lugar.nombre)
      direccion.setText(lugar.direccion)
      telefono.setText(Integer.toString(lugar.telefono))
      url.setText(lugar.url)
      comentario.setText(lugar.comentarios)
      val adaptador = ArrayAdapter<String>(this,
         android.R.layout.simple_spinner_item,
         lugar.tipoLugar.getNombres()
      )
      adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      tipo.adapter = adaptador
      tipo.setSelection(lugar.tipoLugar.ordinal)
   }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
      menuInflater.inflate(R.menu.edicion_lugar, menu)
      return true
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when (item.getItemId()) {
         R.id.accion_cancelar -> {
            if (_id!=-1) lugares.borrar(_id)
            finish()
            return true
         }
         R.id.accion_guardar -> {
            val nuevoLugar = Lugar(
               nombre.text.toString(),
               direccion.text.toString(),
               lugar.posicion,
               TipoLugar.values()[tipo.selectedItemPosition],
               lugar.foto,
               Integer.parseInt(telefono.text.toString()),
               url.text.toString(),
               comentario.text.toString(),
               lugar.fecha,
               lugar.valoracion
            )
//            nuevoLugar.posicion = (application as Aplicacion).posicionActual;
            if (_id==-1) _id = adaptador.idPosicion(pos)
            usoLugar.guardar(_id, nuevoLugar)
            /*with (application as Aplicacion) {
               if (_id==-1) _id = adaptador.idPosicion(pos)
               lugares.actualiza(_id, nuevoLugar)
               adaptador.cursor = lugares.extraeCursor()
               if (pos !== -1)
                  adaptador.notifyItemChanged(pos)
               else
                  adaptador.notifyDataSetChanged()
            }    */
            finish()
            return true
         }
         else -> return super.onOptionsItemSelected(item)
      }
   }

}