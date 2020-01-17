package com.example.mislugares.presentacion

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import java.text.DateFormat
import java.util.*
import android.widget.Toast
import com.example.mislugares.*
import com.example.mislugares.modelo.Lugar
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.example.mislugares.casos_uso.CasosUsoLugarFecha
import kotlinx.android.synthetic.main.vista_lugar.*

class VistaLugarFragment : Fragment() {
   val RESULTADO_EDITAR = 1
   val RESULTADO_GALERIA = 2
   val RESULTADO_FOTO = 3

   val lugares   by lazy { (activity!!.application as Aplicacion).lugares }
   val adaptador by lazy { (activity!!.application as Aplicacion).adaptador }
   val usoLugar by lazy { CasosUsoLugarFecha(activity!! /*as AppCompatActivity*/, this,  lugares, adaptador) }  //**

   /*private*/ var pos: Int = 0
   var _id = -1
   private lateinit var lugar: Lugar
   private var uriUltimaFoto: Uri? = null

/*   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.vista_lugar)
      pos = intent.extras?.getInt("pos", 0) ?: 0
      actualizaVistas()
   }*/

   override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?,
                             savedInstanceState: Bundle? ): View? {
      setHasOptionsMenu(true)
      return inflador.inflate(R.layout.vista_lugar, contenedor, false)
/*      vista.findViewById<LinearLayout>(R.id.barra_mapa).setOnClickListener {
         usoLugar.verMapa(lugar) }
      vista.findViewById<LinearLayout>(R.id.barra_url).setOnClickListener {
         usoLugar.verPgWeb(lugar) }
      vista.findViewById<LinearLayout>(R.id.barra_telefono).setOnClickListener {
         usoLugar.llamarTelefono(lugar) }
      vista.findViewById<View>(R.id.camara).setOnClickListener{
         uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO) }
      vista.findViewById<View>(R.id.galeria).setOnClickListener {
         usoLugar.ponerDeGaleria(RESULTADO_GALERIA) }
      vista.findViewById<View>(R.id.eliminar_foto).setOnClickListener {
         usoLugar.ponerFoto(pos, "", foto) }
      vista.findViewById<View>(R.id.icono_hora).setOnClickListener {
         usoLugar.cambiarHora(pos) }
      vista.findViewById<View>(R.id.hora).setOnClickListener {
         usoLugar.cambiarHora(pos) }
      vista.findViewById<View>(R.id.icono_fecha).setOnClickListener {
         usoLugar.cambiarFecha(pos) }
      vista.findViewById<View>(R.id.fecha).setOnClickListener {
         usoLugar.cambiarFecha(pos) }
      return vista*/
   }

   override fun onActivityCreated(state: Bundle?) {
      super.onActivityCreated(state)
      pos = activity?.intent?.extras?.getInt("pos", 0) ?: 0
      _id = adaptador.idPosicion(pos)
      barra_mapa.setOnClickListener { usoLugar.verMapa(lugar) }
      barra_url.setOnClickListener { usoLugar.verPgWeb(lugar) }
      barra_telefono.setOnClickListener {usoLugar.llamarTelefono(lugar) }
      icono_hora.setOnClickListener { usoLugar.cambiarHora(pos) /*, hora*/ }
      hora.setOnClickListener { usoLugar.cambiarHora(pos)/*, hora*/ }
      icono_fecha.setOnClickListener { usoLugar.cambiarFecha(pos) }
      fecha.setOnClickListener { usoLugar.cambiarFecha(pos) }
      camara.setOnClickListener { usoLugar.tomarFoto(RESULTADO_FOTO) }
      galeria.setOnClickListener { usoLugar.ponerDeGaleria(RESULTADO_GALERIA) }
      eliminar_foto.setOnClickListener { usoLugar.ponerFoto(pos, "", foto) }
      //lugar = adaptador.lugarPosicion(pos)
      actualizaVistas()
   }

   fun actualizaVistas() {
      if (adaptador.itemCount == 0) return
      lugar = adaptador.lugarPosicion(pos)
      nombre.setText(lugar.nombre)
      logo_tipo.setImageResource(lugar.tipoLugar.recurso)
      tipo.setText(lugar.tipoLugar.texto)
      direccion.setText(lugar.direccion)
      if (lugar.telefono == 0) {
         telefono.setVisibility(View.GONE)
      } else {
         telefono.setVisibility(View.VISIBLE)
         telefono.setText(Integer.toString(lugar.telefono))
      }
      url.setText(lugar.url)
      comentario.text =lugar.comentarios
      fecha.text= DateFormat.getDateInstance().format(Date(lugar.fecha))
      hora.text= DateFormat.getTimeInstance().format(Date(lugar.fecha))
      valoracion.setOnRatingBarChangeListener { _, _, _ -> }
      valoracion.setRating(lugar.valoracion)
      valoracion.setOnRatingBarChangeListener { _, valor, _ ->
         lugar.valoracion = valor
         usoLugar.actualizaPosLugar(pos, lugar)
         pos = adaptador.posicionId(_id)

         //usoLugar.ponerValoracion(pos, valor)
         /*lugar.valoracion = valor
         actualizaPosLugar()
         val pref = PreferenceManager.getDefaultSharedPreferences(this)
         if (pref.getString("orden", "0")=="1") finish()*/
      }
//      usoLugar.ponerFoto(pos, lugar.foto, foto)
      usoLugar.visualizarFoto(lugar, foto)
   }

/*   fun actualizaPosLugar()  {
      val _id = adaptador.idPosicion(pos);
      lugares.actualiza(_id, lugar);
      adaptador.cursor = lugares.extraeCursor()
      adaptador.notifyDataSetChanged()
   }*/

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//      super.onActivityResult(requestCode, resultCode, data)  //<<<<<<<<<<<<<<<<<<<<<<<<<<
      /*if (requestCode == RESULTADO_PREFERENCIAS) {
         adaptador.cursor = lugares.extraeCursor()
         adaptador.notifyDataSetChanged()

      } else*/
      if (requestCode == RESULTADO_EDITAR) {
         lugar = lugares.elemento(_id)    //***********  Quitar, se hace en actualizaVistas
         //adaptador.cursor = lugares.extraeCursor()
         pos = adaptador.posicionId(_id)

         actualizaVistas()   // OJO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
///         scrollView1.invalidate()
      } else if (requestCode == RESULTADO_GALERIA) {
         if (resultCode == RESULT_OK) {
            usoLugar.ponerFoto(pos, data?.dataString ?: "", foto)
         } else {
            Toast.makeText(activity, "Foto no cargada", Toast.LENGTH_LONG).show()
         }
      } else if (requestCode == RESULTADO_FOTO) {
         if (resultCode == Activity.RESULT_OK && uriUltimaFoto!=null) {
            usoLugar.ponerFoto(pos, uriUltimaFoto.toString(), foto)
         } else {
            Toast.makeText(activity, "Error en captura", Toast.LENGTH_LONG).show()
         }
      }
   }

/*   override fun onCreateOptionsMenu(menu: Menu): Boolean {
      menuInflater.inflate(R.menu.vista_lugar, menu)
      return true
   }*/

   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      inflater.inflate(R.menu.vista_lugar, menu)
      super.onCreateOptionsMenu(menu, inflater)
   }


   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when (item.getItemId()) {
         R.id.accion_compartir -> {
            usoLugar.compartir(lugar)
            /*startActivity(Intent(Intent.ACTION_SEND).apply {
               type = "text/plain"
               putExtra(Intent.EXTRA_TEXT, "$(lugar.nombre) -  $(lugar.url)")
            })*/
            return true
         }
         R.id.accion_llegar -> {
            usoLugar.verMapa(lugar)
            return true
         }
         R.id.accion_editar -> {
            usoLugar.editar(pos, RESULTADO_EDITAR)
            /*val i = Intent(this, EdicionLugarActivity::class.java)
            i.putExtra("pos", pos);
            startActivityForResult(i, RESULTADO_EDITAR)*/
            return true
         }
         R.id.accion_borrar -> {
            val _id = adaptador.idPosicion(pos)
            usoLugar.borrar(_id)
            //finish()
            return true
         }
         else -> return super.onOptionsItemSelected(item)
      }
   }

/*   fun borrarLugar(pos: Int) {
      AlertDialog.Builder(this)
         .setTitle("Borrado de lugar")
         .setMessage("¿Estás seguro que quieres eliminar este lugar?")
         .setPositiveButton("Confirmar") { dialog, whichButton ->
            lugares.borrar(pos)
            adaptador.cursor = lugares.extraeCursor()
            adaptador.notifyDataSetChanged()
            finish()
         }
         .setNegativeButton("Cancelar", null)
         .show()
   }*/
   fun verMapa(view: View?) = usoLugar.verMapa(lugar)
   /*fun verMapa(view: View?) {
      val lat = lugar.posicion.latitud
      val lon = lugar.posicion.longitud
      val uri = if (lat != 0.0 || lon != 0.0) {
         Uri.parse("geo:$lat,$lon")
      } else {
         Uri.parse("geo:0,0?q=" + lugar.direccion)
      }
      startActivity(Intent(Intent.ACTION_VIEW, uri))
   }*/
   fun llamarTelefono(view: View) = usoLugar.llamarTelefono(lugar)
   /*fun llamarTelefono(view: View) = startActivity(
      Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.telefono))
   )*/
   fun verPgWeb(view: View) = usoLugar.verPgWeb(lugar)
   /*fun verPgWeb(view: View) = startActivity(
      Intent(Intent.ACTION_VIEW, Uri.parse(lugar.url))
   )*/

   fun ponerDeGaleria(view: View) = usoLugar.ponerDeGaleria(RESULTADO_GALERIA)
   /*fun ponerDeGaleria(view: View) {
      val action = if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
         Intent.ACTION_OPEN_DOCUMENT
      } else {
         Intent.ACTION_PICK
      }
      val i = Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
         addCategory(Intent.CATEGORY_OPENABLE)
         type = "image/*"
      }
      startActivityForResult(i, RESULTADO_GALERIA)
   }*/*/

   fun tomarFoto(view: View) { uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO) }
   /* fun tomarFoto(view: View) {
      try {
         val file = File.createTempFile(
            "img_" + System.currentTimeMillis() / 1000, /* prefix */
            ".jpg", /* suffix */
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
         )

         if (Build.VERSION.SDK_INT >= 24) {
            uriUltimaFoto = getUriForFile(
               this,
               "es.upv.jtomas.mislugares.fileProvider", file
            )
         } else {
            uriUltimaFoto = Uri.fromFile(file)
         }
         val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         //flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
         intent.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto)
         startActivityForResult(intent, RESULTADO_FOTO)
      } catch (ex: IOException) {
         Toast.makeText(this, "Error al crear fichero de imagen", Toast.LENGTH_LONG).show()
      }
   }*/

/*   protected fun ponerFoto(imageView: ImageView, uri: String?) {
      if (!(uri == null || uri.isEmpty() || uri == "null")) {
         //imageView.setImageURI(Uri.parse(uri))
         imageView.setImageBitmap(reduceBitmap(this, uri, 1024,   1024))
      } else {
         imageView.setImageBitmap(null)
      }
      actualizaPosLugar()
   }*/

   fun eliminarFoto(view: View) = usoLugar.ponerFoto(pos, "", foto)
//   fun eliminarFoto(view: View) = usoLugar.eliminarFoto(pos, foto)
   /*fun eliminarFoto(view: View) {
      lugar.foto = ""
      ponerFoto(foto, null)
      actualizaPosLugar()
   }*/

/*   fun reduceBitmap(contexto: Context, uri: String, maxAncho: Int, maxAlto: Int): Bitmap? {
      try {
         var input: InputStream? = null
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
   }*/
}