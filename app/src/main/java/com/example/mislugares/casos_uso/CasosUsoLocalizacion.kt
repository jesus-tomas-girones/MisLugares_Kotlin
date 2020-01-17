package com.example.mislugares.casos_uso

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mislugares.Aplicacion

class CasosUsoLocalizacion(val actividad: Activity,
                           val codigoPermiso: Int) : LocationListener {
   val TAG = "MisLugares"
   val posicionActual = (actividad.application as Aplicacion).posicionActual
   val manejadorLoc = actividad.getSystemService(
      AppCompatActivity.LOCATION_SERVICE) as LocationManager
   val adaptador = (actividad.application as Aplicacion).adaptador
   var mejorLoc: Location? = null

   init {
      ultimaLocalizazion()
   }

   fun activar() {
      if (hayPermisoLocalizacion()) activarProveedores()
   }

   fun desactivar() {
      if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this)
   }

   fun permisoConcedido() {
      ultimaLocalizazion()
      activarProveedores()
      adaptador.notifyDataSetChanged()
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
            codigoPermiso, actividad)
      }
   }

   @SuppressLint("MissingPermission")
   private fun ultimaLocalizazion() {
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
         }
      } else {
            solicitarPermiso(
               Manifest.permission.ACCESS_FINE_LOCATION,
               "Sin el permiso localización no puedo mostrar la distancia" + " a los lugares.",
               codigoPermiso, actividad)
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
            .setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
               ActivityCompat.requestPermissions(
                  actividad, arrayOf(permiso), requestCode )
            }).show()
      } else {
         ActivityCompat.requestPermissions(
            actividad,arrayOf(permiso), requestCode)
      }
   }

   override fun onLocationChanged(location: Location) {
      Log.d(TAG, "Nueva localización: $location")
      actualizaMejorLocaliz(location)
      adaptador.notifyDataSetChanged()
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
         posicionActual.latitud = localiz.latitude
         posicionActual.longitud = localiz.longitude
      }
   }

   fun hayPermisoLocalizacion() = (ActivityCompat.checkSelfPermission(
      actividad, Manifest.permission.ACCESS_FINE_LOCATION)
           == PackageManager.PERMISSION_GRANTED)
}