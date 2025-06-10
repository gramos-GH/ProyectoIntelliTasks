package com.example.intellitasks

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView // Asegúrate de importar TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// *** CAMBIO AQUÍ: Renombra la clase a PendingsFragment ***
class PendingsFragment : Fragment(R.layout.fragment_pendings) {

    private lateinit var menuIcon: ImageView
    private lateinit var addIcon: ImageView
    private lateinit var titleTextView: TextView // Referencia al título "Pendientes"
    private lateinit var recyclerViewPendientes: RecyclerView

    // Aquí necesitarás una lista de datos y un adaptador para tu RecyclerView
    private lateinit var pendingsAdapter: PendingsAdapter // Tendremos que crear esta clase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Obtener referencias a las vistas por sus IDs
        menuIcon = view.findViewById(R.id.menuIcon)
        addIcon = view.findViewById(R.id.addIcon)
        titleTextView = view.findViewById(R.id.titleTextView)
        recyclerViewPendientes = view.findViewById(R.id.recyclerViewPendientes)

        // 2. Configurar ClickListeners
        menuIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de menú presionado") // Log actualizado
            Toast.makeText(context, "Menú presionado (ej. abrir Drawer)", Toast.LENGTH_SHORT).show()
            // Aquí podrías abrir un DrawerLayout si tu Activity lo tiene
        }

        addIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de añadir presionado") // Log actualizado
            Toast.makeText(context, "Añadir nueva tarea (navegar a AddTaskFragment)", Toast.LENGTH_SHORT).show()
            // Aquí navegarías a un fragmento para añadir una nueva tarea
            // findNavController().navigate(R.id.action_pendingsFragment_to_addTaskFragment) // Si usas NavComponent
        }

        // 3. Configurar el RecyclerView
        recyclerViewPendientes.layoutManager = LinearLayoutManager(context)

        // *** AQUI NECESITARÁS TU ADAPTER REAL Y TUS DATOS ***
        // Por ahora, creamos unos datos de prueba para que el RecyclerView se vea.
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