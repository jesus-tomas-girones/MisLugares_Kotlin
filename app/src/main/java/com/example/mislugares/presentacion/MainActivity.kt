package com.example.mislugares.presentacion

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText
import java.lang.Integer.parseInt
import android.content.pm.PackageManager
import com.example.mislugares.Aplicacion
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsoActividades
import com.example.mislugares.casos_uso.CasosUsoLocalizacion
import com.example.mislugares.datos.LugaresBD

class MainActivity : AppCompatActivity() {//, LocationListener {
   //   private lateinit var recyclerView: RecyclerView
   //val TAG = "MisLugares"
   val RESULTADO_PREFERENCIAS = 0
   val SOLICITUD_PERMISO_LOCALIZACION = 1

   val lugares:LugaresBD by lazy { (application as Aplicacion).lugares }
   val adaptador by lazy { (application as Aplicacion).adaptador }
   val usoLugar by lazy { CasosUsoLugar(this, null,  lugares, adaptador) }
   val usoActividades by lazy { CasosUsoActividades(this) }
   val usoLocalizacion by lazy { CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION) }

   //   private lateinit var adaptador: AdaptadorLugaresBD
   //lateinit var manejadorLoc: LocationManager
   //var mejorLoc: Location? = null

//   private lateinit var layoutManager: RecyclerView.LayoutManager

//   lateinit private var recyclerView: RecyclerView//? = null
//   var adaptador = lazy { AdaptadorLugares( (application as Aplicacion).lugares) }
//   private var layoutManager: RecyclerView.LayoutManager? = null
   /*  companion object {
      lateinit var lugares: RepositorioLugares //= LugaresLista()
   }
   init {
 //     if (::lugares.isInitialized) {
         lugares = LugaresLista()
 //     }
   }*/

   override fun onCreate(savedInstanceState: Bundle?) {
//      Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

//      manejadorLoc = getSystemService(LOCATION_SERVICE) as LocationManager
//      ultimaLocalizazion()

      setSupportActionBar(toolbar)
/*        button03.setOnClickListener{
            lanzarAcercaDe(null)    }*/

/*      adaptador.onClick = {
         val id = it.tag as Int
         usoLugar.mostrar(id)//, vista_lugar_fragment as? VistaLugarFragment)
      } */

      fab.setOnClickListener { view ->
         //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
         //   .setAction("Action", null).show()
         usoLugar.nuevo()
         /*val _id = lugares.nuevo()
         val i = Intent(this@MainActivity, EdicionLugarActivity::class.java)
         i.putExtra("_id", _id)
         startActivity(i)*/
      }

//      val lugares =(application as Aplicacion).lugares
/*      adaptador = AdaptadorLugaresBD(lugares, lugares.extraeCursor()) {
         val pos = recyclerView.getChildAdapterPosition(it)
         val i = Intent(this@MainActivity, VistaLugarActivity::class.java)
         i.putExtra("pos",  pos);
         startActivity(i);
         //Toast.makeText(this, "pulsado $pos", Toast.LENGTH_LONG).show()
      }*/


/*      val tracker = SelectionTracker.Builder(
         "my-selection-pos",
         recyclerView as androidx.recyclerview.widget.RecyclerView,
         StableIdKeyProvider(recyclerView as androidx.recyclerview.widget.RecyclerView),
         MyDetailsLookup(recyclerView),
         StorageStrategy.createLongStorage()
      )
         .withOnItemActivatedListener(myItemActivatedListener)
         .build()
*/
//      recyclerView = findViewById<RecyclerView>(R.pos.recycler_view).apply {
  /*    adaptador.onClick = {
         val pos = it.tag as Int
         usoLugar.mostrar(pos)
      }

      recyclerView.apply {
         setHasFixedSize(true)
         layoutManager = LinearLayoutManager(this@MainActivity)
         adapter = adaptador
      }*/
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
            lanzarVistaLugar();
            true;
         }
         R.id.menu_mapa -> {
            usoActividades.lanzarMapa()
            true;
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

/*
   @SuppressLint("MissingPermission")
   fun ultimaLocalizazion() {
      if (hayPermisoLocalizacion()) {
         if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            actualizaMejorLocaliz(
               manejadorLoc.getLastKnownLocation(
                  LocationManager.GPS_PROVIDER
               )
            )
         }
         if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            actualizaMejorLocaliz(
               manejadorLoc.getLastKnownLocation(
                  LocationManager.NETWORK_PROVIDER
               )
            )
         } else {
            solicitarPermiso(
               Manifest.permission.ACCESS_FINE_LOCATION,
               "Sin el permiso localización no puedo mostrar la distancia" + " a los lugares.",
               SOLICITUD_PERMISO_LOCALIZACION,
               this
            )
         }
      }
   }

   fun solicitarPermiso(permiso: String, justificacion: String,
                        requestCode: Int, actividad: Activity
   ) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
            permiso)) {
         AlertDialog.Builder(actividad)
            .setTitle("Solicitud de permiso")
            .setMessage(justificacion)
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                  dialog, whichButton -> ActivityCompat.requestPermissions(
               actividad, arrayOf(permiso), requestCode )
            }).show()
      } else {
         ActivityCompat.requestPermissions(
            actividad,arrayOf(permiso), requestCode)
      }
   }


   @SuppressLint("MissingPermission")
   private fun activarProveedores() {
      if (hayPermisoLocalizacion()) {
         if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manejadorLoc.requestLocationUpdates(
               LocationManager.GPS_PROVIDER,
               20 * 1000, 5F, this
            )
         }
         if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manejadorLoc.requestLocationUpdates(
               LocationManager.NETWORK_PROVIDER,
               10 * 1000, 10F, this
            )
         }
      } else {
         solicitarPermiso(
            Manifest.permission.ACCESS_FINE_LOCATION,
            "Sin el permiso localización no puedo mostrar la distancia" + " a los lugares.",
            SOLICITUD_PERMISO_LOCALIZACION,
            this
         )
      }
   }

   override fun onLocationChanged(location: Location) {
      Log.d(TAG, "Nueva localización: $location")
      actualizaMejorLocaliz(location)
      usoLugar.adaptador.notifyDataSetChanged()
   }

   override fun onProviderDisabled(proveedor: String) {
      Log.d(TAG, "Se deshabilita: $proveedor")
      activarProveedores()
   }

   override fun onProviderEnabled(proveedor: String) {
      Log.d(TAG, "Se habilita: $proveedor")
      activarProveedores()
   }

   override fun onStatusChanged(proveedor: String, estado: Int, extras: Bundle) {
      Log.d(TAG, "Cambia estado: $proveedor")
      activarProveedores()
   }

   val DOS_MINUTOS:Long = (2 * 60 * 1000)

   private fun actualizaMejorLocaliz(localiz: Location?) {
      if (localiz != null && (mejorLoc == null
                 || localiz.accuracy < 2 * mejorLoc!!.getAccuracy()
                 || localiz.time - mejorLoc!!.getTime() > DOS_MINUTOS)
      ) {
         Log.d(TAG, "Nueva mejor localización")
         mejorLoc = localiz
         (application as Aplicacion).posicionActual.latitud = localiz.latitude
         (application as Aplicacion).posicionActual.longitud = localiz.longitude
      }
   }

   fun hayPermisoLocalizacion() = (ActivityCompat.checkSelfPermission(
      this,Manifest.permission.ACCESS_FINE_LOCATION)
           == PackageManager.PERMISSION_GRANTED)
*/
}
