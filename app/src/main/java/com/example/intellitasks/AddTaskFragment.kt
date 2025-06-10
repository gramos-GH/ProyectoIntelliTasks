package com.example.intellitasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.intellitasks.databinding.FragmentAddTaskBinding
import android.app.DatePickerDialog //Import necesario para el selector de fecha
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment() {

    // Variable para el ViewBinding
    private var _binding: FragmentAddTaskBinding? = null
    // Esta propiedad solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

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
            findNavController().navigateUp() // Volver al fragmento anterior
        }

        // Configurar el listener para el botón "Agregar" (usando binding)
        binding.btnAdd.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()
            val date = binding.dateEditText.text.toString().trim()

            // Aquí iría la lógica para guardar la nueva tarea
            // (Por ahora, solo un Toast de ejemplo)
            if (name.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Tarea agregada: $name", android.widget.Toast.LENGTH_SHORT).show()
                // Después de agregar, podrías volver a la lista de tareas
                findNavController().navigateUp()
            } else {
                android.widget.Toast.makeText(requireContext(), "Por favor, completa todos los campos", android.widget.Toast.LENGTH_SHORT).show()
                // O muestra errores específicos en cada campo
                if (name.isEmpty()) binding.nameEditText.error = "Nombre requerido"
                if (description.isEmpty()) binding.descriptionEditText.error = "Descripción requerida"
                if (date.isEmpty()) binding.dateEditText.error = "Fecha requerida"
            }
        }
        // *** LÓGICA PARA EL DATEPICKER: ESTO ES LO QUE NECESITAS AÑADIR/CAMBIAR ***
        binding.dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear la fecha seleccionada y ponerla en el EditText
                c.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Puedes cambiar el formato aquí
                binding.dateEditText.setText(dateFormat.format(c.time))
            }, year, month, day)

            dpd.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar el binding cuando la vista del fragmento es destruida
        _binding = null
    }
}