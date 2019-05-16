package com.example.mislugares.presentacion

import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsoLugar
import kotlinx.android.synthetic.main.fragment_selector.*


class SelectorFragment : Fragment() {
   val lugares by lazy { (activity!!.application as Aplicacion).lugares }
   val adaptador by lazy { (activity!!.application as Aplicacion).adaptador }
   val usoLugar by lazy { CasosUsoLugar(activity!!, this, lugares, adaptador) } //**
   lateinit var recyclerView: RecyclerView

   override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?,
                             savedInstanceState: Bundle? ): View? {
      val vista = inflador.inflate(R.layout.fragment_selector, contenedor, false)
      recyclerView = vista.findViewById(R.id.recyclerView) //En Kotlin no habría que ponerlo, pero si no no funciona
      return vista
   }

   override fun onActivityCreated(state: Bundle?) {
      super.onActivityCreated(state)
      recyclerView.apply {
         setHasFixedSize(true)
         layoutManager = LinearLayoutManager(context)
         adapter = adaptador
      }
//      recyclerView.invalidate()
      adaptador.onClick = {
         val pos = it.tag as Int
         usoLugar.mostrar(pos)
      }
   }
}
