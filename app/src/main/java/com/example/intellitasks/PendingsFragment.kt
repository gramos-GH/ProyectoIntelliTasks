package com.example.intellitasks

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment // Asegúrate de que este import esté
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PendingsFragment : Fragment(R.layout.fragment_pendings) {

    private lateinit var menuIcon: ImageView
    private lateinit var addIcon: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var recyclerViewPendientes: RecyclerView

    private lateinit var pendingsAdapter: PendingsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Obtener referencias a las vistas por sus IDs
        menuIcon = view.findViewById(R.id.menuIcon)
        addIcon = view.findViewById(R.id.addIcon)
        titleTextView = view.findViewById(R.id.titleTextView)
        recyclerViewPendientes = view.findViewById(R.id.recyclerViewPendientes)

        // 2. Configurar ClickListeners
        menuIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de menú presionado")
            Toast.makeText(context, "Menú presionado (ej. abrir Drawer)", Toast.LENGTH_SHORT).show()
        }

        addIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de añadir presionado. Navegando a AddTaskFragment.")
            Toast.makeText(context, "Añadir nueva tarea (cargando AddTaskFragment)", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddTaskFragment()) // Asegúrate que main_fragment_container sea el ID correcto en activity_list.xml
                .addToBackStack(null) // Esto permite al usuario volver a PendingsFragment con el botón de atrás
                .commit()
        }

        // 3. Configurar el RecyclerView
        recyclerViewPendientes.layoutManager = LinearLayoutManager(context)

        val dummyTaskList = listOf(
            "Comprar víveres",
            "Preparar presentación",
            "Llamar a Juan",
            "Pagar recibos",
            "Hacer ejercicio",
            "Estudiar para el examen",
            "Enviar reporte",
            "Planificar viaje",
            "Leer libro"
        )
        pendingsAdapter = PendingsAdapter(dummyTaskList)
        recyclerViewPendientes.adapter = pendingsAdapter
    }
}