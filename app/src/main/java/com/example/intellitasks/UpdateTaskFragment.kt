/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

/*Clase UpdateTaskFragment*/
class UpdateTaskFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var taskId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        /*Se recuperan los argumentos*/
        val name = arguments?.getString("name") ?: ""
        val description = arguments?.getString("description") ?: ""
        val date = arguments?.getString("date") ?: ""
        userId = arguments?.getString("userId") ?: ""
        taskId = arguments?.getString("taskId") ?: ""

        val nameEditText = view.findViewById<EditText>(R.id.etTaskName)
        val descEditText = view.findViewById<EditText>(R.id.etTaskDescription)
        val dateEditText = view.findViewById<EditText>(R.id.etTaskDate)
        val btnSave = view.findViewById<Button>(R.id.btnSaveTask)

        nameEditText.setText(name)
        descEditText.setText(description)
        dateEditText.setText(date)

        btnSave.setOnClickListener {
            val updatedData = mapOf(
                "name" to nameEditText.text.toString(),
                "description" to descEditText.text.toString(),
                "date" to dateEditText.text.toString()
            )

            db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(taskId)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()  // Regresa
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
