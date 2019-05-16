package com.example.mislugares.presentacion

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import kotlinx.android.synthetic.main.elemento_lista.view.*

open class AdaptadorLugares(
   val lugares: RepositorioLugares
   /*, open var onClick: (View) -> Unit*/) :
                  RecyclerView.Adapter<AdaptadorLugares.ViewHolder>() {

   /*open*/ lateinit var onClick: (View) -> Unit
   //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
   class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

      // Personalizamos un ViewHolder a partir de un lugar
      fun personaliza(lugar: Lugar, onClick: (View) -> Unit) = with(itemView) {
         nombre.text = lugar.nombre
         direccion.text = lugar.direccion
         foto.setImageResource(
            when (lugar.tipoLugar) {
               TipoLugar.RESTAURANTE -> R.drawable.restaurante
               TipoLugar.BAR -> R.drawable.bar
               TipoLugar.COPAS -> R.drawable.copas
               TipoLugar.ESPECTACULO -> R.drawable.espectaculos
               TipoLugar.HOTEL -> R.drawable.hotel
               TipoLugar.COMPRAS -> R.drawable.compras
               TipoLugar.EDUCACION -> R.drawable.educacion
               TipoLugar.DEPORTE -> R.drawable.deporte
               TipoLugar.NATURALEZA -> R.drawable.naturaleza
               TipoLugar.GASOLINERA -> R.drawable.gasolinera
               TipoLugar.OTROS -> R.drawable.otros
            }
         )
         foto.setScaleType(ImageView.ScaleType.FIT_END)
         valoracion.rating = lugar.valoracion
         setOnClickListener { onClick(itemView) }

//         if (ActivityCompat.checkSelfPermission(context,
//               android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//                              PackageManager.PERMISSION_GRANTED) {

            val posicion = (context.applicationContext as Aplicacion).posicionActual
            if (posicion== GeoPunto.SIN_POSICION || lugar.posicion== GeoPunto.SIN_POSICION) {
               distancia.text = "... Km"
            } else {
               val d = posicion.distancia(lugar.posicion).toInt()
               distancia.text = if (d < 2000) "$d m"
                                else          "${(d / 1000)} Km"

         }
      }
   }

   // Creamos el ViewHolder con la vista de un elemento sin personalizar
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      // Inflamos la vista desde el xml
      val v = LayoutInflater.from(parent.context)
         .inflate(R.layout.elemento_lista, parent, false)
      //v.setOnClickListener({ listener(item) })
      return ViewHolder(v)
   }

   // Usando como base el ViewHolder y lo personalizamos
   override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
      val lugar = lugares.elemento(posicion)
      holder.personaliza(lugar, onClick)
      holder.view.tag = posicion.toString()  ///////////////////
   }


/*   // Personalizamos un ViewHolder a partir de un lugar
   fun personalizaVista(holder: ViewHolder, lugar: Lugar) {
      holder.nombre.text = lugar.nombre
      holder.direccion.text = lugar.direccion
      var pos = R.drawable.otros
      when (lugar.tipoLugar) {
         TipoLugar.RESTAURANTE -> pos = R.drawable.restaurante
         TipoLugar.BAR -> pos = R.drawable.bar
         TipoLugar.COPAS -> pos = R.drawable.copas
         TipoLugar.ESPECTACULO -> pos = R.drawable.espectaculos
         TipoLugar.HOTEL -> pos = R.drawable.hotel
         TipoLugar.COMPRAS -> pos = R.drawable.compras
         TipoLugar.EDUCACION -> pos = R.drawable.educacion
         TipoLugar.DEPORTE -> pos = R.drawable.deporte
         TipoLugar.NATURALEZA -> pos = R.drawable.naturaleza
         TipoLugar.GASOLINERA -> pos = R.drawable.gasolinera
      }
      holder.foto.setImageResource(pos)
      holder.foto.setScaleType(ImageView.ScaleType.FIT_END)
      holder.valoracion.rating = lugar.valoracion
   }*/

   // Indicamos el número de elementos de la lista
   override fun getItemCount() = lugares.tamanyo()
}
