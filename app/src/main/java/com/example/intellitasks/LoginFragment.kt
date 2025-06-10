package com.example.intellitasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) { // Asegúrate que tu layout se llama fragment_login

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa el ViewModel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val btnLogin = view.findViewById<Button>(R.id.btnContinue) // Asumiendo que el ID en login es btnLogin
        val registerTextView = view.findViewById<TextView>(R.id.registerText)

        // Observar eventos de login exitoso/fallido desde el ViewModel
        loginViewModel.loginResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
                // Requisito: "Al hacer un login exitosamente deberá pasarme a esa nueva actividad."
                val intent = Intent(activity, ListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish() // Finaliza MainActivity para que el usuario no pueda volver con el botón atrás
            }
        }

        // Observar errores desde el ViewModel
        loginViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, "Error al iniciar sesión: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        // Requisito: "Declarar los observers en cada vista" para el loader
        loginViewModel.showLoader.observe(viewLifecycleOwner) { show ->
            if (show) {
                Log.d("LoginFragment", "Mostrando loader...")
                (activity as? FragmentCommunicator)?.showLoader()
            } else {
                Log.d("LoginFragment", "Ocultando loader...")
                (activity as? FragmentCommunicator)?.hideLoader()
            }
        }

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Requisito: "validación de campos a sus pantallas para que no me deje iniciar un proceso si cualquiera de los campos está vacío"
            if (email.isEmpty()) {
                emailEditText.error = "El correo no puede estar vacío"
                emailEditText.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEditText.error = "La contraseña no puede estar vacía"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Llamar al ViewModel para manejar el login
            loginViewModel.loginUser(email, password)
        }

        registerTextView.setOnClickListener {
            // Requisito: "Si un registro resulta exitoso se deberá mover al usuario a la pantalla de login." -> Esto es la acción de registro (en RegisterFragment se maneja el retorno)
            // Aquí se maneja la navegación desde Login a Register
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment) // Asegúrate que el ID de la acción es correcto
        }
    }
}