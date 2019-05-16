package com.example.mislugares.datos

import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar

class LugaresLista : RepositorioLugares {
//    protected var listaLugares: MutableList<Lugar> = ejemploLugares()
   //lateinit var listaLugares:  MutableList<Lugar>

    val listaLugares = mutableListOf<Lugar>()

    init {
       // listaLugares = ejemploLugares()
       //listaLugares = mutableListOf()
       añadeEjemplos()
    }

    override fun elemento(id: Int): Lugar {
        return listaLugares[id]
    }

    override fun añade(lugar: Lugar) {
        listaLugares.add(lugar)
    }

    override fun nuevo(): Int {
        val lugar = Lugar("Nuevo lugar")
        listaLugares.add(lugar)
        return listaLugares.size - 1
    }

    override fun borrar(id: Int) {
        listaLugares.removeAt(id)
    }

    override fun tamanyo(): Int {
        return listaLugares.size
    }

    override fun actualiza(id: Int, lugar: Lugar) {
        listaLugares[id] = lugar
    }

    companion object {
        fun ejemploLugares() = mutableListOf(
           Lugar(
              "Escuela Politécnica Superior de Gandía",
              "C/ Paranimf, 1 46730 Gandia (SPAIN)", GeoPunto(-0.166093, 38.995656),
              TipoLugar.EDUCACION, "", 962849300, "http://www.epsg.upv.es",
              "Uno de los mejores lugares para formarse.", valoracion = 3f
           ),
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
           ),
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
           ),
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
           ),
           Lugar(
              "La Vital",
              "Avda. de La Vital, 0 46701 Gandía (Valencia)", GeoPunto(
                 -0.1720092,
                 38.9705949
              ), TipoLugar.COMPRAS, "", 962881070,
              "http://www.lavital.es/", "El típico centro comercial", valoracion = 2f
           )
        )

/*            lugares.add(  Lugar("Escuela Politécnica Superior de Gandía",
             "C/ Paranimf, 1 46730 Gandia (SPAIN)", GeoPunto(-0.166093, 38.995656),
              TipoLugar.EDUCACION, "", 962849300, "http://www.epsg.upv.es",
             "Uno de los mejores lugares para formarse.", valoracion = 3f)            )
            lugares.add(
                Lugar(
                    "Al de siempre",
                    "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
                    GeoPunto(-0.190642, 38.925857), TipoLugar.BAR, "", 636472405, "",
                    "No te pierdas el arroz en calabaza.", valoracion = 3f
                )
            )
            lugares.add(
                Lugar(
                    "androidcurso.com",
                    "ciberespacio", GeoPunto(0.0, 0.0), TipoLugar.EDUCACION, "",
                    962849300, "http://androidcurso.com",
                    "Amplia tus conocimientos sobre Android.", valoracion =5f
                )
            )
            lugares.add(
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
            lugares.add(
                Lugar(
                    "La Vital",
                    "Avda. de La Vital, 0 46701 Gandía (Valencia)", GeoPunto(
                        -0.1720092,
                        38.9705949
                    ), TipoLugar.COMPRAS, "", 962881070,
                    "http://www.lavital.es/", "El típico centro comercial", valoracion =2f
                )
            )*/
        //          return lugares
        //      }
    }
}
