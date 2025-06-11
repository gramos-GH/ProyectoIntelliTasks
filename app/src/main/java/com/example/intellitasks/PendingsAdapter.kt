package com.example.intellitasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp

data class Task(
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val isCompleted: Boolean = false,
    val createdAt: com.google.firebase.Timestamp? = null
)

// *** CLASE PendingsAdapter MODIFICADA ***
// Ahora el adaptador recibe una List<Task>
class PendingsAdapter(private var taskList: List<Task>) : // CAMBIO: tasks -> taskList y List<String> -> List<Task>
    RecyclerView.Adapter<PendingsAdapter.TaskViewHolder>() {

    // ViewHolder: Contiene las vistas de un solo ítem de la lista
    // *** ACTUALIZACIÓN DE IDs DE VISTA ***
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.tvTaskName) // CAMBIO DE ID: taskTitleTextView -> tvTaskName
        val taskDescriptionTextView: TextView = itemView.findViewById(R.id.tvTaskDescription) // ADICIÓN: para la descripción
        val taskDateTextView: TextView = itemView.findViewById(R.id.tvTaskDate) // ADICIÓN: para la fecha
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox) // Este ID lo mantienes si existe en tu item_pendiente o nuevo layout
    }

    // onCreateViewHolder: Infla el layout para cada ítem de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    // onBindViewHolder: Vinculalos  datos a las vistas de cada ítem
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position] // Ahora trabajamos con un objeto Task

        holder.taskNameTextView.text = currentTask.name // Vincula el nombre de la tarea
        holder.taskDescriptionTextView.text = currentTask.description // Vincula la descripción
        holder.taskDateTextView.text = currentTask.date // Vincula la fecha
        holder.taskCheckBox.isChecked = currentTask.isCompleted // Vincula el estado del checkbox

        // Manejar el clic en el CheckBox
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(holder.itemView.context, "Tarea '${currentTask.name}' completada", Toast.LENGTH_SHORT).show()
                // Lógica para actualizar en Firestore (futuro)
            } else {
                Toast.makeText(holder.itemView.context, "Tarea '${currentTask.name}' desmarcada", Toast.LENGTH_SHORT).show()
                // Lógica para actualizar en Firestore (futuro)
            }
        }

        // Opcional: Manejar clic en el ítem completo
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Clic en: ${currentTask.name}", Toast.LENGTH_SHORT).show()
            // Navegar a una pantalla de detalle de tarea, por ejemplo
        }
    }

    // getItemCount: Retorna el número total de ítems en la lista
    override fun getItemCount() = taskList.size // tasks -> taskList

    // *** ADICIÓN: Método para actualizar la lista de tareas ***
    fun updateTaskList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado y debe redibujarse
    }
}