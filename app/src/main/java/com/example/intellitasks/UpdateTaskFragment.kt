/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

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

        val name = arguments?.getString("name") ?: ""
        val description = arguments?.getString("description") ?: ""
        val date = arguments?.getString("date") ?: ""
        userId = arguments?.getString("userId") ?: ""
        taskId = arguments?.getString("taskId") ?: ""

        val nameEditText = view.findViewById<EditText>(R.id.etTaskName)
        val descEditText = view.findViewById<EditText>(R.id.etTaskDescription)
        val dateEditText = view.findViewById<EditText>(R.id.etTaskDate)
        val btnSave = view.findViewById<Button>(R.id.btnSaveTask)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        nameEditText.setText(name)
        descEditText.setText(description)
        dateEditText.setText(date)

        /*Se deshabilita el teclado del teléfono para que sólo se seleccione con DatePickerDialog*/
        dateEditText.inputType = android.text.InputType.TYPE_NULL
        dateEditText.isFocusable = false

        dateEditText.setOnClickListener {
            /*Obtener fecha inicial para el picker*/
            val calendar = Calendar.getInstance()

            /*Intentar usar la fecha ya puesta en el EditText*/
            val currentDate = dateEditText.text.toString()
            if (currentDate.isNotEmpty()) {
                val parts = currentDate.split("-")
                if (parts.size == 3) {
                    val year = parts[0].toIntOrNull() ?: calendar.get(Calendar.YEAR)
                    val month = parts[1].toIntOrNull()?.minus(1) ?: calendar.get(Calendar.MONTH)
                    val day = parts[2].toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
                    calendar.set(year, month, day)
                }
            }

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateEditText.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

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
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }
}
