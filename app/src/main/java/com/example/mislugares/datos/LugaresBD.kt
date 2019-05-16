package com.example.mislugares.datos

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.preference.PreferenceManager
import android.database.SQLException
import com.example.mislugares.Aplicacion
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import java.lang.Exception

class LugaresBD(val contexto: Context) :
   SQLiteOpenHelper(contexto, "lugares", null, 1),
   RepositorioLugares {

   override fun onCreate(bd: SQLiteDatabase) {
      bd.execSQL(
         "CREATE TABLE lugares (" +
                 "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "nombre TEXT, " +
                 "direccion TEXT, " +
                 "longitud REAL, " +
                 "latitud REAL, " +
                 "tipo INTEGER, " +
                 "foto TEXT, " +
                 "telefono INTEGER, " +
                 "url TEXT, " +
                 "comentario TEXT, " +
                 "fecha BIGINT, " +
                 "valoracion REAL)"
      )
      bd.execSQL(
         ("INSERT INTO lugares VALUES (null, " +
                 "'Escuela Politécnica Superior de Gandía', " +
                 "'C/ Paranimf, 1 46730 Gandia (SPAIN)', -0.166093, 38.995656, " +
                 TipoLugar.EDUCACION.ordinal + ", '', 962849300, " +
                 "'http://www.epsg.upv.es', " +
                 "'Uno de los mejores lugares para formarse.', " +
                 System.currentTimeMillis() + ", 3.0)")
      )
      bd.execSQL(
         ("INSERT INTO lugares VALUES (null, 'Al de siempre', " +
                 "'P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)', " +
                 " -0.190642, 38.925857, " + TipoLugar.BAR.ordinal + ", '', " +
                 "636472405, '', " + "'No te pierdas el arroz en calabaza.', " +
                 System.currentTimeMillis() + ", 3.0)")
      )
      bd.execSQL(
         ("INSERT INTO lugares VALUES (null, 'androidcurso.com', " +
                 "'ciberespacio', 0.0, 0.0," + TipoLugar.EDUCACION.ordinal + ", '', " +
                 "962849300, 'http://androidcurso.com', " +
                 "'Amplia tus conocimientos sobre Android.', " +
                 System.currentTimeMillis() + ", 5.0)")
      )
      bd.execSQL(
         ("INSERT INTO lugares VALUES (null,'Barranco del Infierno'," +
                 "'Vía Verde del río Serpis. Villalonga (Valencia)', -0.295058, " +
                 "38.867180, " + TipoLugar.NATURALEZA.ordinal + ", '', 0, " +
                 "'http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-" +
                 "rio.html', 'Espectacular ruta para bici o andar', " +
                 System.currentTimeMillis() + ", 4.0)")
      )
      bd.execSQL(
         ("INSERT INTO lugares VALUES (null, 'La Vital', " +
                 "'Avda. La Vital,0 46701 Gandia (Valencia)',-0.1720092,38.9705949," +
                 TipoLugar.COMPRAS.ordinal + ", '', 962881070, " +
                 "'http://www.lavital.es', 'El típico centro comercial', " +
                 System.currentTimeMillis() + ", 2.0)")
      )
   }

   override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
   }

   override fun elemento(id: Int): Lugar {
      val cursor = readableDatabase.rawQuery("SELECT * FROM lugares WHERE _id = $id", null)
      try {
         if (cursor.moveToNext())
            return extraeLugar(cursor)
         else
            throw SQLException("Error al acceder al elemento _id = $id")

      } catch (e:Exception) {
         throw e
      } finally {
         cursor?.close()
      }
   }


   override fun añade(lugar: Lugar) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

override fun nuevo():Int {
   var _id = -1
   val lugar = Lugar(nombre = "")
   writableDatabase.execSQL("INSERT INTO lugares (nombre, direccion, " +
      "longitud, latitud, tipo, foto, telefono, url, comentario, fecha, " +
      "valoracion) VALUES ('', '', ${lugar.posicion.longitud}, " +
      "${lugar.posicion.latitud}, ${lugar.tipoLugar.ordinal}, '', 0, '', " +
      "'', ${lugar.fecha},0 )")
   val c = readableDatabase.rawQuery((
      "SELECT _id FROM lugares WHERE fecha = " + lugar.fecha), null)
   if (c.moveToNext()) _id = c.getInt(0)
   c.close()
   return _id
}

   override fun borrar(id: Int) {
      writableDatabase.execSQL("DELETE FROM lugares WHERE _id = $id")
   }

   override fun tamanyo(): Int {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

override fun actualiza(id:Int, lugar: Lugar) = with(lugar){
   writableDatabase.execSQL("UPDATE lugares SET " +
      "nombre = '$nombre', direccion = '$direccion', " +
      "longitud = ${posicion.longitud}, latitud = ${posicion.latitud}, "+
      "tipo = ${tipoLugar.ordinal}, foto = '$foto', telefono = $telefono, "+
      "url = '$url', comentario = '$comentarios', fecha = $fecha, "+
      "valoracion = $valoracion  WHERE _id = $id")
}

   fun extraeLugar(cursor: Cursor) = Lugar(
      nombre = cursor.getString(1),
      direccion = cursor.getString(2),
      posicion = GeoPunto(
         cursor.getDouble(3),
         cursor.getDouble(4)
      ),
      tipoLugar = TipoLugar.values()[cursor.getInt(5)],
      foto = cursor.getString(6),
      telefono = cursor.getInt(7),
      url = cursor.getString(8),
      comentarios = cursor.getString(9),
      fecha = cursor.getLong(10),
      valoracion = cursor.getFloat(11)
   )

  fun extraeCursor(): Cursor {
//   readableDatabase.rawQuery("SELECT * FROM lugares WHERE valoracion>1.0 ORDER BY nombre LIMIT 4",null)
      val pref = PreferenceManager.getDefaultSharedPreferences(contexto)
      var consulta = when (pref.getString("orden", "0")) {
         "0" -> "SELECT * FROM lugares "
         "1" -> "SELECT * FROM lugares ORDER BY valoracion DESC"
         else -> {
            val lon = (contexto.getApplicationContext() as Aplicacion)
               .posicionActual.longitud
            val lat = (contexto.getApplicationContext() as Aplicacion)
               .posicionActual.latitud
            "SELECT * FROM lugares ORDER BY " +
                    "($lon - longitud)*($lon - longitud) + " +
                    "($lat - latitud )*($lat - latitud )"
         }
      }
      consulta += " LIMIT ${pref.getString("maximo", "12")}"
      return readableDatabase.rawQuery(consulta, null)
   }

}
