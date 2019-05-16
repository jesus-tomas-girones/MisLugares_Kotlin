package com.example.mislugares.datos

import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar

interface RepositorioLugares {
   fun elemento(id: Int): Lugar  //Devuelve el elemento dado su pos
   fun añade(lugar: Lugar)  //Añade el elemento indicado
   fun nuevo(): Int  //Añade un elemento en blanco y devuelve su pos
   fun borrar(id: Int)  //Elimina el elemento con el pos indicado
   fun tamanyo(): Int  //Devuelve el número de elementos
   fun actualiza(id: Int, lugar: Lugar)  //Reemplaza un elemento

   fun añadeEjemplos() {
      añade(
         Lugar(
            "Escuela Politécnica Superior de Gandía",
            "C/ Paranimf, 1 46730 Gandia (SPAIN)", GeoPunto(-0.166093, 38.995656),
            TipoLugar.EDUCACION, "", 962849300, "http://www.epsg.upv.es",
            "Uno de los mejores lugares para formarse.", valoracion = 3f
         )
      )
      añade(
         Lugar(
            "Al de siempre",
            "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
            GeoPunto(-0.190642, 38.925857),
            TipoLugar.BAR,
            "",
            636472405,
            "",
            "No te pierdas el arroz en calabaza.",
            valoracion = 3f
         )
      )
      añade(
         Lugar(
            "androidcurso.com",
            "ciberespacio",
            GeoPunto(0.0, 0.0),
            TipoLugar.EDUCACION,
            "",
            962849300,
            "http://androidcurso.com",
            "Amplia tus conocimientos sobre Android.",
            valoracion = 5f
         )
      )
      añade(
         Lugar(
            "Barranco del Infierno",
            "Vía Verde del río Serpis. Villalonga (Valencia)",
            GeoPunto(-0.295058, 38.867180),
            TipoLugar.NATURALEZA,
            "",
            0,
            "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-" + "verde-del-rio.html",
            "Espectacular ruta para bici o andar",
            valoracion = 4f
         )
      )
      añade(
         Lugar(
            "La Vital",
            "Avda. de La Vital, 0 46701 Gandía (Valencia)", GeoPunto(
               -0.1720092,
               38.9705949
            ), TipoLugar.COMPRAS, "", 962881070,
            "http://www.lavital.es/", "El típico centro comercial", valoracion = 2f
         )
      )
   }

/*   companion object {
      fun añadeEjemplos(lugares: RepositorioLugares) = with(lugares) {
         añade(
            Lugar(
               "Escuela Politécnica Superior de Gandía",
               "C/ Paranimf, 1 46730 Gandia (SPAIN)", GeoPunto(-0.166093, 38.995656),
               TipoLugar.EDUCACION, "", 962849300, "http://www.epsg.upv.es",
               "Uno de los mejores lugares para formarse.", valoracion = 3f
            )
         )
         añade(
            Lugar(
               "Al de siempre",
               "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
               GeoPunto(-0.190642, 38.925857), TipoLugar.BAR, "", 636472405, "",
               "No te pierdas el arroz en calabaza.", valoracion = 3f
            )
         )
         añade(         Lugar(
            "androidcurso.com",
            "ciberespacio", GeoPunto(0.0, 0.0), TipoLugar.EDUCACION, "",
            962849300, "http://androidcurso.com",
            "Amplia tus conocimientos sobre Android.", valoracion = 5f
         ))
         añade(Lugar(
            "Barranco del Infierno",
            "Vía Verde del río Serpis. Villalonga (Valencia)",
            GeoPunto(-0.295058, 38.867180),
            TipoLugar.NATURALEZA,
            "",
            0,
            "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-" + "verde-del-rio.html",
            "Espectacular ruta para bici o andar",
            valoracion = 4f
         ))
         añade(
         Lugar(
            "La Vital",
            "Avda. de La Vital, 0 46701 Gandía (Valencia)", GeoPunto(
               -0.1720092,
               38.9705949
            ), TipoLugar.COMPRAS, "", 962881070,
            "http://www.lavital.es/", "El típico centro comercial", valoracion = 2f
         ))

      }
   }*/
}
