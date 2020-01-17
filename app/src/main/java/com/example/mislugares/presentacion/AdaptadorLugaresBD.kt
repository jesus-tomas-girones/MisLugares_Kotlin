package com.example.mislugares.presentacion

import android.database.Cursor
import android.view.View
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.Lugar


class AdaptadorLugaresBD(lugares: LugaresBD, var cursor: Cursor) : AdaptadorLugares(lugares) {

   fun lugarPosicion(posicion: Int): Lugar {
      cursor.moveToPosition(posicion)
      return (lugares as LugaresBD).extraeLugar(cursor)
   }

   fun idPosicion(posicion: Int): Int {
      cursor.moveToPosition(posicion)
      if (cursor.count>0)  return cursor.getInt(0)
      else return -1
   }

   fun posicionId(id: Int): Int {
      var pos = 0
      while (pos < itemCount && idPosicion(pos) != id) pos++
      return if (pos >= itemCount) -1
             else                  pos
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val lugar = lugarPosicion(position)
      holder.personaliza(lugar, onClick)
      holder.view.tag = position
   }

   override fun getItemCount(): Int {
      return cursor.getCount()
   }

}
