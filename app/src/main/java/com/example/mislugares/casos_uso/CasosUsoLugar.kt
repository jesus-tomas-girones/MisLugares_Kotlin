package com.example.mislugares.casos_uso

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mislugares.Aplicacion
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import com.example.mislugares.R
import com.example.mislugares.presentacion.*

open class CasosUsoLugar(
   open val actividad: FragmentActivity,//AppCompatActivity,
   open val fragment: Fragment?, //>>
   open val lugares: LugaresBD,
   open val adaptador: AdaptadorLugaresBD) /*: TimePickerDialog.OnTimeSetListener */ {
//   val lugares by lazy { (actividad.application as Aplicacion).lugares }
//   val adaptador by lazy { (actividad.application as Aplicacion).adaptador }
//   open val actividad = fragment.activity!!

   // FUNCIONES AUXILIARES

   fun actualizaPosLugar(pos: Int, lugar: Lugar) {
      val id = adaptador.idPosicion(pos)
      guardar(id, lugar)
      //lugares.actualiza(id, lugar);
      //adaptador.cursor = lugares.extraeCursor()
      //adaptador.notifyDataSetChanged()
   }

   // OPERACIONES BÁSICAS

   fun guardar(id: Int, nuevoLugar: Lugar) {
      lugares.actualiza(id, nuevoLugar)
      adaptador.cursor = lugares.extraeCursor()
      adaptador.notifyDataSetChanged()
// NO      actividad.finish()
   }

   fun obtenerFragmentVista(): VistaLugarFragment? {
      val manejador = actividad /*as AppCompatActivity*/.supportFragmentManager
      return manejador.findFragmentById(R.id.vista_lugar_fragment) as VistaLugarFragment?
   }

   fun obtenerFragmentSelector(): SelectorFragment? {
      val manejador = actividad.supportFragmentManager
      return manejador.findFragmentById(R.id.selector_fragment) as SelectorFragment?
   }

   fun mostrar(pos: Int) {
      val fragmentVista = obtenerFragmentVista()
      if (fragmentVista != null) {
         fragmentVista.pos = pos
         fragmentVista._id = adaptador.idPosicion(pos)  /////////////////
         fragmentVista.actualizaVistas()
      } else {
         val i = Intent(actividad, VistaLugarActivity::class.java)
         i.putExtra("pos", pos)
         actividad.startActivity(i)
      }
   }

   fun editar(pos: Int, codidoSolicitud: Int) {
      val i = Intent(actividad, EdicionLugarActivity::class.java)
      i.putExtra("pos", pos)
      fragment?.startActivityForResult(i, codidoSolicitud)
              ?:actividad.startActivityForResult(i, codidoSolicitud)
   }

   fun nuevo() {
      val _id = lugares.nuevo()
      //ponemos la posición actual en el nuevo lugar
      val posicion = (actividad.application as Aplicacion).posicionActual
      if (posicion != GeoPunto.SIN_POSICION) {
         val lugar = lugares.elemento(_id)
         lugar.posicion = posicion
         lugares.actualiza(_id, lugar)
      }
      val i = Intent(actividad, EdicionLugarActivity::class.java)
      i.putExtra("_id", _id)
      actividad.startActivity(i)
   }

   fun borrar(id: Int) =
      AlertDialog.Builder(actividad)
         .setTitle("Borrado de lugar")
         .setMessage("¿Estás seguro que quieres eliminar este lugar?")
         .setPositiveButton("Confirmar") { dialog, whichButton ->
            lugares.borrar(id)
            adaptador.cursor = lugares.extraeCursor()
            adaptador.notifyDataSetChanged()
            //actividad.finish()

            if (obtenerFragmentSelector()==null){
               //val manejador = actividad.supportFragmentManager
               //if (manejador.findFragmentById(R.id.selector_fragment) == null) {
               actividad.finish()
            } else {
               //val fragmentVista = manejador.findFragmentById(R.id.vista_lugar_fragment)
               mostrar(0)//, obtenerFragmentVista as VistaLugarFragment)
            }
         }
         .setNegativeButton("Cancelar", null)
         .show()

 /*  fun ponerValoracion(pos: Int, valor: Float) {
      val lugar = adaptador.lugarPosicion(pos)
      lugar.valoracion = valor
      actualizaPosLugar(pos, lugar)
      val pref = PreferenceManager.getDefaultSharedPreferences(actividad)
      if (pref.getString("orden", "0") == "1") actividad.finish()
   }*/

   fun refrescarAcaptador() {
      adaptador.cursor = (lugares.extraeCursor())
      adaptador.notifyDataSetChanged()
   }

// INTENCIONES

   fun compartir(lugar: Lugar) = actividad.startActivity(
      Intent(Intent.ACTION_SEND).apply {
         type = "text/plain"
         putExtra(Intent.EXTRA_TEXT, "$(lugar.nombre) -  $(lugar.url)")
      })

   fun llamarTelefono(lugar: Lugar) = actividad.startActivity(
      Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.telefono))
   )

   fun verPgWeb(lugar: Lugar) = actividad.startActivity(
      Intent(Intent.ACTION_VIEW, Uri.parse(lugar.url))
   )

   fun verMapa(lugar: Lugar) {
      val lat = lugar.posicion.latitud
      val lon = lugar.posicion.longitud
      val uri = if (lugar.posicion != GeoPunto.SIN_POSICION)//(lat != 0.0 || lon != 0.0) {
         Uri.parse("geo:$lat,$lon")
      else
         Uri.parse("geo:0,0?q=" + lugar.direccion)
      actividad.startActivity(Intent(Intent.ACTION_VIEW, uri))
   }

// FOTOFRAFIA

   fun ponerDeGaleria(codidoSolicitud: Int) {
      val action = if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
         Intent.ACTION_OPEN_DOCUMENT
      } else {
         Intent.ACTION_PICK
      }
      val i = Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
         addCategory(Intent.CATEGORY_OPENABLE)
         type = "image/*"
      }
      //actividad.startActivityForResult(i, codidoSolicitud)
      fragment?.startActivityForResult(i, codidoSolicitud)
         ?:actividad.startActivityForResult(i, codidoSolicitud)

   }

//   private lateinit var uriUltimaFoto: Uri

   fun tomarFoto(codidoSolicitud: Int): Uri? {
      try {
         val file = File.createTempFile(
            "img_" + System.currentTimeMillis() / 1000, /* prefix */
            ".jpg", /* suffix */
            actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
         )
         val uriUltimaFoto = if (Build.VERSION.SDK_INT >= 24)
            FileProvider.getUriForFile(
               actividad,
               "es.upv.jtomas.mislugares.fileProvider", file
            )
         else Uri.fromFile(file)
         val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         //flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
         i.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto)
         //actividad.startActivityForResult(intent, codidoSolicitud)
         fragment?.startActivityForResult(i, codidoSolicitud)
            ?:actividad.startActivityForResult(i, codidoSolicitud)

         return uriUltimaFoto
      } catch (ex: IOException) {
         Toast.makeText(actividad, "Error al crear fichero de imagen", Toast.LENGTH_LONG).show()
         return null
      }
   }

   fun ponerFoto(pos: Int, uri: String?, imageView: ImageView) {
      val lugar = adaptador.lugarPosicion(pos)
      lugar.foto = uri ?: ""
      visualizarFoto(lugar, imageView)
      /*if (!(uri == null || uri.isEmpty())) {
         //imageView.setImageURI(Uri.parse(uri))
         imageView.setImageBitmap(reduceBitmap(actividad, uri, 1024, 1024))
      } else {
         imageView.setImageBitmap(null)
      }*/
      actualizaPosLugar(pos, lugar)
   }

   fun visualizarFoto(lugar: Lugar, imageView: ImageView) {
      if (!(/*lugar.foto == null ||*/ lugar.foto.isEmpty())) {
//imageView.setImageURI(Uri.parse(uri))
         imageView.setImageBitmap(reduceBitmap(actividad, lugar.foto, 1024, 1024))
      } else {
         imageView.setImageBitmap(null)
      }
   }

   /*fun eliminarFoto(pos: Int, imageView: ImageView) {
      ponerFoto(pos, "", imageView)
   }*/

   private fun reduceBitmap(contexto: Context, uri: String, maxAncho: Int, maxAlto: Int): Bitmap? {
      try {
         var input: InputStream?
         val u = Uri.parse(uri)
         if (u.scheme == "http" || u.scheme == "https") {
            input = URL(uri).openStream()
         } else {
            input = contexto.getContentResolver().openInputStream(u)
         }
         val options = BitmapFactory.Options()
         options.inJustDecodeBounds = true
         options.inSampleSize = Math.max(
            Math.ceil((options.outWidth / maxAncho).toDouble()),
            Math.ceil((options.outHeight / maxAlto).toDouble())
         ).toInt()
         options.inJustDecodeBounds = false
         return BitmapFactory.decodeStream(input, null, options)
      } catch (e: FileNotFoundException) {
         Toast.makeText(
            contexto, "Fichero/recurso de imagen no encontrado",
            Toast.LENGTH_LONG
         ).show()
         e.printStackTrace()
         return null
      } catch (e: IOException) {
         Toast.makeText(
            contexto, "Error accediendo a imagen",
            Toast.LENGTH_LONG
         ).show()
         e.printStackTrace()
         return null
      }
   }


}
/*
   // FECHA Y HORA

   lateinit var lugar: Lugar
   lateinit var tHora: TextView
   var pos: Int = -1

   fun cambiarHora(pos: Int, tHora: TextView) {
      lugar = adaptador.lugarPosicion(pos)
      this.tHora = tHora
      this.pos = pos
      val dialogo = DialogoSelectorHora()
      dialogo.setOnTimeSetListener(this)
      val args = Bundle();
      args.putLong("fecha", lugar.fecha)
      dialogo.setArguments(args)
      dialogo.show((actividad /*as AppCompatActivity*/).supportFragmentManager
                   , "selectorHora")
   }

   override fun onTimeSet(vista: TimePicker?, hora: Int, minuto: Int) {
     val calendario = Calendar.getInstance()
     calendario.setTimeInMillis(lugar.fecha)
     calendario.set(Calendar.HOUR_OF_DAY, hora)
     calendario.set(Calendar.MINUTE, minuto)
     lugar.fecha = calendario.getTimeInMillis()
     actualizaPosLugar(pos, lugar)
     ///*TextView*/ val tHora = vista!!/*getView()*/.findViewById<TextView>(R.pos.hora);
     val formato = SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
     tHora.text = formato.format(Date(lugar.fecha))
   }

}*/