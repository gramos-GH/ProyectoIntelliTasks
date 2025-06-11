package com.example.intellitasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PendingsFragment : Fragment() {

    private lateinit var menuIcon: ImageView
    private lateinit var addIcon: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var recyclerViewPendientes: RecyclerView

    private lateinit var pendingsAdapter: PendingsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var varAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        varAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pendings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuIcon = view.findViewById(R.id.menuIcon)
        addIcon = view.findViewById(R.id.addIcon)
        titleTextView = view.findViewById(R.id.titleTextView)
        recyclerViewPendientes = view.findViewById(R.id.recyclerViewPendientes)

        menuIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de menú presionado")
            showToast("Menú presionado (ej. abrir Drawer)")
        }

        addIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de añadir presionado. Navegando a AddTaskFragment.")
            showToast("Añadir nueva tarea (cargando AddTaskFragment)")

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddTaskFragment())
                .addToBackStack(null)
                .commit()
        }

        recyclerViewPendientes.layoutManager = LinearLayoutManager(context)
        pendingsAdapter = PendingsAdapter(emptyList())
        recyclerViewPendientes.adapter = pendingsAdapter

        loadTasksFromFirestore()
    }

    private fun loadTasksFromFirestore() {
        val currentUser = varAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("users").document(userId)
                .collection("tasks")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val taskList = mutableListOf<Task>()
                    for (document in querySnapshot) {
                        val task = document.toObject(Task::class.java)
                        taskList.add(task)
                    }
                    pendingsAdapter.updateTaskList(taskList)
                    Log.d("PendingsFragment", "Tareas cargadas: ${taskList.size}")
                    if (taskList.isEmpty()) {
                        showToast("No hay tareas pendientes.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("PendingsFragment", "Error al cargar tareas de Firestore", e)
                    showToast("Error al cargar tareas.")
                }
        } else {
            Log.w("PendingsFragment", "No hay usuario autenticado. No se pueden cargar las tareas.")
            showToast("No hay usuario autenticado. Por favor, inicia sesión.")
        }
    }

    // ✅ Función segura para mostrar Toasts
    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}
