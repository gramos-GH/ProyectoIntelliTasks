package com.example.intellitasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log // Agregado para logs de depuración
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast // Asegúrate de tener este import si usas Toast
import androidx.fragment.app.Fragment
// *** ELIMINA ESTE IMPORT! Ya no usamos NavController aquí. ***
// import androidx.navigation.fragment.findNavController
import com.example.intellitasks.databinding.FragmentAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// *** ADICIÓN DE IMPORTS DE FIRESTORE Y AUTH ***
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue // Para usar ServerTimestamp

class AddTaskFragment : Fragment() {

    // Variable para el ViewBinding
    private var _binding: FragmentAddTaskBinding? = null
    // Esta propiedad solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    // *** ADICIÓN: Instancias de Firestore y Auth ***
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // *** ADICIÓN: Inicialización de Firestore y Auth en onCreate ***
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance() // Obtiene la instancia de Firestore
        auth = FirebaseAuth.getInstance()     // Obtiene la instancia de FirebaseAuth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout usando ViewBinding
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el listener para el botón de regreso (usando binding)
        binding.btnBack.setOnClickListener {
            // *** CAMBIO AQUÍ: Usar parentFragmentManager para regresar ***
            parentFragmentManager.popBackStack() // Volver al fragmento anterior
        }

        // Configurar el listener para el DatePicker (ya lo tenías bien)
        binding.dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                c.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.dateEditText.setText(dateFormat.format(c.time))
            }, year, month, day)

            dpd.show()
        }

        // Configurar el listener para el botón "Agregar" (usando binding)
        binding.btnAdd.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()
            val date = binding.dateEditText.text.toString().trim()

            // *** CAMBIO/ADICIÓN AQUÍ: Lógica para guardar la nueva tarea en Firestore ***
            if (name.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                val currentUser = auth.currentUser // Obtiene el usuario actualmente autenticado

                if (currentUser != null) {
                    val userId = currentUser.uid // Obtiene el UID del usuario

                    // Crea un HashMap con los datos de la tarea
                    val task = hashMapOf(
                        "name" to name,
                        "description" to description,
                        "date" to date,
                        "isCompleted" to false, // Una nueva tarea no está completada por defecto
                        "createdAt" to FieldValue.serverTimestamp() // Marca de tiempo del servidor
                    )

                    // Guarda la tarea en la colección 'tasks' dentro del documento del usuario
                    db.collection("users").document(userId)
                        .collection("tasks")
                        .add(task) // .add() crea un nuevo documento con un ID automático
                        .addOnSuccessListener { documentReference ->
                            // Si la tarea se guardó con éxito
                            Toast.makeText(requireContext(), "Tarea agregada exitosamente!", Toast.LENGTH_SHORT).show()
                            Log.d("AddTaskFragment", "Tarea agregada con ID: ${documentReference.id}")
                            // *** CAMBIO AQUÍ: Usar parentFragmentManager para regresar ***
                            parentFragmentManager.popBackStack() // Regresa a PendingsFragment
                        }
                        .addOnFailureListener { e ->
                            // Si hubo un error al guardar la tarea
                            Toast.makeText(requireContext(), "Error al agregar tarea: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("AddTaskFragment", "Error al agregar tarea", e)
                        }
                } else {
                    // Si no hay un usuario autenticado
                    Toast.makeText(requireContext(), "No hay usuario autenticado. Por favor, inicia sesión.", Toast.LENGTH_LONG).show()
                    Log.w("AddTaskFragment", "Intento de agregar tarea sin usuario autenticado.")
                }
            } else {
                // Validación de campos vacíos (ya la tenías bien)
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                if (name.isEmpty()) binding.nameEditText.error = "Nombre requerido"
                if (description.isEmpty()) binding.descriptionEditText.error = "Descripción requerida"
                if (date.isEmpty()) binding.dateEditText.error = "Fecha requerida"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar el binding cuando la vista del fragmento es destruida
        _binding = null
    }
}