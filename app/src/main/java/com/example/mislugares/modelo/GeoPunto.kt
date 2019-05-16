package com.example.mislugares.modelo

data class GeoPunto(var longitud: Double, var latitud: Double) {

    companion object {
       val SIN_POSICION = GeoPunto(0.0, 0.0)
    }
/*    override fun toString(): String {
        return "longitud:$longitud, latitud:$latitud"
    }*/

    fun distancia(punto: GeoPunto): Double {
        val RADIO_TIERRA = 6371000.0 // en metros
        val dLat = Math.toRadians(latitud - punto.latitud)
        val dLon = Math.toRadians(longitud - punto.longitud)
        val lat1 = Math.toRadians(punto.latitud)
        val lat2 = Math.toRadians(latitud)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return c * RADIO_TIERRA
    }
}
