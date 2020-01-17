package com.example.mislugares.presentacion

import layout.PreferenciasFragment
import android.os.Bundle
import android.app.Activity

class PreferenciasActivity : Activity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      fragmentManager.beginTransaction()
         .replace(android.R.id.content, PreferenciasFragment())
         .commit()
   }
}
