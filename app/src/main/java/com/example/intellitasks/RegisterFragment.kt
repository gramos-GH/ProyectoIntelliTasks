/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se obtiene el botón de registro
        val btnRegister = view.findViewById<Button>(R.id.btnContinue)

        btnRegister.setOnClickListener {
            //Navegación al MainActivity utilizando Navigation
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_registerFragment_to_mainActivity)
        }

        //Se obtiene el ícono de regreso
        val backButton = view.findViewById<ImageView>(R.id.btnBack)

        backButton.setOnClickListener {
            //Se navega de regreso al LoginFragment
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}
