/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.intellitasks.Task

/*Clase PendingsAdapter*/
class PendingsAdapter(
    private var taskList: MutableList<Task>,  /*Se utiliza el modelo Task*/
    private val context: Context,
    private val userId: String,
    private val taskListener: TaskActionListener,
    private val communicator: FragmentCommunicator
) : RecyclerView.Adapter<PendingsAdapter.TaskViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.tvTaskName)
        val taskDescriptionTextView: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val taskDateTextView: TextView = itemView.findViewById(R.id.tvTaskDate)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val btnDeleteTask: ImageButton = itemView.findViewById(R.id.btnDeleteTask)
        val btnEditTask: ImageButton = itemView.findViewById(R.id.btnEditTask)
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
            if (currentTask.id.isNotEmpty()) {
                db.collection("users").document(userId)
                    .collection("tasks")
                    .document(currentTask.id)
                    .update("isCompleted", isChecked)
            }
        }

        holder.btnDeleteTask.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar tarea")
                .setMessage("¿Quieres eliminar '${currentTask.name}'?")
                .setPositiveButton("Sí") { _, _ ->
                    deleteTask(position)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        holder.btnEditTask.setOnClickListener {
            communicator.openUpdateTaskFragment(currentTask, userId)
        }
    }

    override fun getItemCount() = taskList.size

    fun updateTaskList(newList: List<Task>) {
        taskList = newList.toMutableList()
        notifyDataSetChanged()
    }

    private fun deleteTask(position: Int) {
        val task = taskList[position]
        if (task.id.isNotEmpty()) {
            db.collection("users").document(userId)
                .collection("tasks")
                .document(task.id)
                .delete()
                .addOnSuccessListener {
                    taskList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    taskListener.onTaskDeleted()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun refreshTaskData(taskId: String, position: Int) {
        if (taskId.isEmpty()) return

        db.collection("users").document(userId)
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
                        taskListener.onTaskUpdated()
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
