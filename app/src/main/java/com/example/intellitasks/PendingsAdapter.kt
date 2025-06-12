package com.example.intellitasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val isCompleted: Boolean = false,
    val createdAt: com.google.firebase.Timestamp? = null
)

interface TaskActionListener {
    fun onTaskDeleted()
    fun onTaskUpdated()
}

class PendingsAdapter(
    private var taskList: MutableList<Task>,
    private val context: Context,
    private val userId: String,
    private val listener: TaskActionListener  // ✅ Necesario para comunicar con el fragmento
) : RecyclerView.Adapter<PendingsAdapter.TaskViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.tvTaskName)
        val taskDescriptionTextView: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val taskDateTextView: TextView = itemView.findViewById(R.id.tvTaskDate)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val btnDeleteTask: ImageButton = itemView.findViewById(R.id.btnDeleteTask)
        val btnRefreshTask: ImageButton = itemView.findViewById(R.id.btnRefreshTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]

        holder.taskNameTextView.text = currentTask.name
        holder.taskDescriptionTextView.text = currentTask.description
        holder.taskDateTextView.text = currentTask.date

        holder.taskCheckBox.setOnCheckedChangeListener(null)
        holder.taskCheckBox.isChecked = currentTask.isCompleted
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val taskId = currentTask.id
            if (taskId.isNotEmpty()) {
                db.collection("users").document(userId)
                    .collection("tasks")
                    .document(taskId)
                    .update("isCompleted", isChecked)
            }
        }

        holder.btnDeleteTask.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar tarea")
                .setMessage("¿Quieres eliminar '${currentTask.name}'?")
                .setPositiveButton("Sí") { _, _ ->
                    deleteTask(holder.adapterPosition)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        holder.btnRefreshTask.setOnClickListener {
            refreshTaskData(currentTask.id, position)
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Clic en: ${currentTask.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount() = taskList.size

    fun updateTaskList(newList: List<Task>) {
        taskList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int) {
        val task = taskList[position]
        if (task.id.isNotEmpty()) {
            db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.id)
                .delete()
                .addOnSuccessListener {
                    taskList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(
                        context,
                        "Tarea eliminada",
                        Toast.LENGTH_SHORT
                    ).show()
                    listener.onTaskDeleted()  // ✅ Notifica al fragmento
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Error al eliminar: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun refreshTaskData(taskId: String, position: Int) {
        if (taskId.isEmpty()) return

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val updatedTask = document.toObject(Task::class.java)
                    if (updatedTask != null) {
                        taskList[position] = updatedTask
                        notifyItemChanged(position)
                        Toast.makeText(context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        listener.onTaskUpdated()  // ✅ Notifica al fragmento
                    }
                } else {
                    Toast.makeText(context, "No se encontró la tarea", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
