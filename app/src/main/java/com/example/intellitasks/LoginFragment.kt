/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Botón para continuar (hacer login)
        val btnLogin = view.findViewById<Button>(R.id.btnContinue)
        btnLogin.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_loginFragment_to_mainActivity)
        }

        //TextView para ir al fragmento de registro
        val registerText = view.findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
