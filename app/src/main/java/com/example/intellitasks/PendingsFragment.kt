/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/*Clase PendingsFragment*/
class PendingsFragment : Fragment(), FragmentCommunicator, TaskActionListener {

    private lateinit var menuIcon: ImageView
    private lateinit var addIcon: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var recyclerViewPendientes: RecyclerView
    private lateinit var emptyView: View
    private lateinit var progressBar: ProgressBar

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
        emptyView = view.findViewById(R.id.emptyView)
        progressBar = view.findViewById(R.id.progressBar)

        menuIcon.setOnClickListener {
            Log.d("PendingsFragment", "Icono de menú presionado")
            showToast("Menú presionado (ej. abrir Drawer)")
        }

        addIcon.setOnClickListener {
            Log.d("PendingsFragment", "Añadir nueva tarea")
            showToast("Añadir nueva tarea")

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddTaskFragment())
                .addToBackStack(null)
                .commit()
        }

        recyclerViewPendientes.layoutManager = LinearLayoutManager(context)

        val currentUser = varAuth.currentUser
        val userId = currentUser?.uid ?: ""

        // Pasamos 'this' tanto para TaskActionListener como FragmentCommunicator
        pendingsAdapter = PendingsAdapter(mutableListOf(), requireContext(), userId, this, this)
        recyclerViewPendientes.adapter = pendingsAdapter

        loadTasksFromFirestore()
    }

    private fun loadTasksFromFirestore() {
        showLoader()

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
                        task.id = document.id
                        taskList.add(task)
                    }
                    pendingsAdapter.updateTaskList(taskList)
                    updateEmptyView(taskList.isEmpty())
                    hideLoader()
                }
                .addOnFailureListener { e ->
                    Log.e("PendingsFragment", "Error al cargar tareas", e)
                    showToast("Error al cargar tareas.")
                    hideLoader()
                }
        } else {
            Log.w("PendingsFragment", "Usuario no autenticado")
            showToast("Inicia sesión para ver tus tareas.")
            hideLoader()
        }
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerViewPendientes.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    /*Metodo para abrir UpdateTaskFragment*/
    override fun openUpdateTaskFragment(task: Task, userId: String) {
        val updateFragment = UpdateTaskFragment()
        val bundle = Bundle().apply {
            putString("name", task.name)
            putString("description", task.description)
            putString("date", task.date)
            putString("userId", userId)
            putString("taskId", task.id)
        }
        updateFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, updateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun onTaskDeleted() {
        loadTasksFromFirestore()
    }

    override fun onTaskUpdated() {
        loadTasksFromFirestore()
    }

    /*Navegar a TaskDetailFragment al hacer click en tarea*/
    override fun onTaskClicked(task: Task) {
        val bundle = Bundle().apply {
            putString("name", task.name)
            putString("description", task.description)
            putString("date", task.date)
        }

        val taskDetailFragment = TaskDetailFragment()
        taskDetailFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, taskDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
