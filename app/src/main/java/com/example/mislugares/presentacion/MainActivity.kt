package com.example.mislugares.presentacion

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import java.lang.Integer.parseInt
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mislugares.Aplicacion
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsoActividades
import com.example.mislugares.casos_uso.CasosUsoLocalizacion
import com.example.mislugares.datos.LugaresBD
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
   val RESULTADO_PREFERENCIAS = 0
   val SOLICITUD_PERMISO_LOCALIZACION = 1

   val lugares:LugaresBD by lazy { (application as Aplicacion).lugares }
   val adaptador by lazy { (application as Aplicacion).adaptador }
   val usoLugar by lazy { CasosUsoLugar(this, null,  lugares, adaptador) }
   val usoActividades by lazy { CasosUsoActividades(this) }
   val usoLocalizacion by lazy { CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION) }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      setSupportActionBar(toolbar) //as Toolbar?
      fab.setOnClickListener { view ->
         usoLugar.nuevo()
      }
   }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
      // Inflate the menu; this adds items to the action bar if it is present.
      menuInflater.inflate(R.menu.menu_main, menu)
      return true
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.

      return when (item.itemId) {
         R.id.action_settings -> {
            usoActividades.lanzarPreferencias(RESULTADO_PREFERENCIAS)
            //lanzarPreferencias()
            true
         }
         R.id.acercaDe -> {
            usoActividades.lanzarAcerdaDe()
            //lanzarAcercaDe()
            true
         }
         R.id.menu_buscar -> {
            lanzarVistaLugar()
            true
         }
         R.id.menu_mapa -> {
            usoActividades.lanzarMapa()
            true
         }
         else -> super.onOptionsItemSelected(item)
      }
   }

/*   fun lanzarAcercaDe(view: View? = null) {
      val i = Intent(this, AcercaDeActivity::class.java)
      startActivity(i)
   }

   fun lanzarPreferencias(view: View? = null) =
      startActivityForResult(
         Intent(this, PreferenciasActivity::class.java), RESULTADO_PREFERENCIAS)
*/
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?  ) {
      super.onActivityResult(requestCode, resultCode, data)  //<<<<<<<<<<<<<<<<<<<<<<<<<<
      if (requestCode == RESULTADO_PREFERENCIAS) {
         //usoLugar.refrescarAcaptador()
         adaptador.cursor = lugares.extraeCursor()
         adaptador.notifyDataSetChanged()
         //Si tenemos fragment a la derecha lo refrescamos
         if (usoLugar.obtenerFragmentVista() != null)
            usoLugar.mostrar(0)    /////////////////



         /*with(adaptador) {
            cursor = usoLugar.lugares.extraeCursor()
            notifyDataSetChanged()
          }*/
      }
   }

   //El siguiente método es un ejercicio, y se podría eliminar
   fun lanzarVistaLugar(view: View? = null) {
      val entrada = EditText(this)
      entrada.setText("0")
      AlertDialog.Builder(this)
         .setTitle("Selección de lugar")
         .setMessage("indica su pos:")
         .setView(entrada)
         .setPositiveButton("Ok") { dialog, whichButton ->
            val id = parseInt(entrada.text.toString())
            //val pos = java.lang.Long.parseLong(entrada.text.toString())
            usoLugar.mostrar(id) // , vista_lugar_fragment as VistaLugarFragment)  //**
            /*val i = Intent(
               this@MainActivity,
               VistaLugarActivity::class.java
            )
            i.putExtra("pos", pos)
            startActivity(i)*/
         }
         .setNegativeButton("Cancelar", null)
         .show()
   }

   // LOCALIZACION

   override fun onRequestPermissionsResult(requestCode: Int,
                                           permissions: Array<String>, grantResults: IntArray ) {
      if (requestCode == SOLICITUD_PERMISO_LOCALIZACION) {
         if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            usoLocalizacion.permisoConcedido()
            /*ultimaLocalizazion()
            activarProveedores()
            usoLugar.adaptador.notifyDataSetChanged()*/
         }
      }
   }
   override fun onResume() {
      super.onResume()
      usoLocalizacion.activar()
      //if (hayPermisoLocalizacion()) activarProveedores()
   }

   override fun onPause() {
      super.onPause()
      usoLocalizacion.desactivar()
      //if (hayPermisoLocalizacion()){ manejadorLoc.removeUpdates(this) }
   }

}
