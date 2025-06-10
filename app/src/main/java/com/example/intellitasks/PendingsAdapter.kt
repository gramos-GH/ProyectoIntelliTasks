package com.example.intellitasks // ¡Asegúrate de que este paquete sea correcto!

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast // Para interactividad básica
import androidx.recyclerview.widget.RecyclerView

// Asume que Task es una clase de datos simple, por ahora solo String
// Más adelante, podrías tener una clase data class Task(val id: String, val title: String, var isCompleted: Boolean)
class PendingsAdapter(private val tasks: List<String>) :
    RecyclerView.Adapter<PendingsAdapter.TaskViewHolder>() {

    // ViewHolder: Contiene las vistas de un solo ítem de la lista
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitleTextView: TextView = itemView.findViewById(R.id.taskTitleTextView)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
    }

    // onCreateViewHolder: Infla el layout para cada ítem de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pendiente, parent, false)
        return TaskViewHolder(view)
    }

    // onBindViewHolder: Vincula los datos a las vistas de cada ítem
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.taskTitleTextView.text = currentTask
        // holder.taskCheckBox.isChecked = currentTask.isCompleted // Descomentar cuando Task tenga isCompleted

        // Manejar el clic en el CheckBox
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // Aquí puedes actualizar el estado de la tarea en tu lista de datos
            // Y si estás usando Firebase, actualizarlo en la base de datos
            if (isChecked) {
                Toast.makeText(holder.itemView.context, "Tarea '${currentTask}' completada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, "Tarea '${currentTask}' desmarcada", Toast.LENGTH_SHORT).show()
            }
            // Después de actualizar el modelo, podrías notificar al adaptador: notifyItemChanged(position)
        }

        // Opcional: Manejar clic en el ítem completo
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Clic en: ${currentTask}", Toast.LENGTH_SHORT).show()
            // Navegar a una pantalla de detalle de tarea, por ejemplo
        }
    }

    // getItemCount: Retorna el número total de ítems en la lista
    override fun getItemCount() = tasks.size
}