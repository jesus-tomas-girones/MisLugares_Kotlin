package com.example.mislugares

import android.app.Application
import com.example.mislugares.presentacion.AdaptadorLugaresBD
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.GeoPunto

class Aplicacion : Application() {

//   val TAG = "MisLugares"
//   val lugares = LugaresLista()

   val lugares = LugaresBD(this)
   val adaptador  by lazy {
      AdaptadorLugaresBD(lugares, lugares.extraeCursor()/*, {}*/)
      /*{
         val pos = it.tag as Int
         val i = Intent(applicationContext, VistaLugarActivity::class.java)
         i.putExtra("pos", pos);
         i.flags = FLAG_ACTIVITY_NEW_TASK
         startActivity(i);
      }*/
   }
   val posicionActual = GeoPunto(0.0, 0.0)

/*   override fun onCreate() {
      super.onCreate()
//      lugares.añadeEjemplos()

   }*/
}
