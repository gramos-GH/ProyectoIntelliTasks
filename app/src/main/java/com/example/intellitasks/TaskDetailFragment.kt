/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/*Clase TaskDetailFragment*/
class TaskDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvTaskName)
        val tvDescription = view.findViewById<TextView>(R.id.tvTaskDescription)
        val tvDate = view.findViewById<TextView>(R.id.tvTaskDate)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val name = arguments?.getString("name") ?: "Sin nombre"
        val description = arguments?.getString("description") ?: "Sin descripción"
        val date = arguments?.getString("date") ?: "Sin fecha"

        tvName.text = name
        tvDescription.text = description
        tvDate.text = "Fecha: $date"

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        fun createBundle(name: String, description: String, date: String): Bundle {
            return Bundle().apply {
                putString("name", name)
                putString("description", description)
                putString("date", date)
            }
        }
    }
}
