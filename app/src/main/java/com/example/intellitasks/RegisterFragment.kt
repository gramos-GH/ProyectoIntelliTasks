/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment(R.layout.fragment_register) {

    // Se declara el ViewModel para el registro
    private lateinit var registerViewModel: RegisterViewModel

    // Se declara una variable para el comunicador.
    private var communicator: FragmentCommunicator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa el ViewModel
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Asegúrate de que la actividad anfitriona implementa FragmentCommunicator
        if (activity is FragmentCommunicator) {
            communicator = activity as FragmentCommunicator
        } else {
            // Esto debería lanzar un error si la actividad no implementa la interfaz,
            // lo que es bueno para detectar problemas en tiempo de desarrollo.
            throw RuntimeException("$context must implement FragmentCommunicator")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a los EditText
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)

        // Se obtiene el botón de registro
        val btnRegister = view.findViewById<Button>(R.id.btnContinue) // Confirmado el ID btnContinue

        //Se obtiene el ícono de regreso
        val backButton = view.findViewById<ImageView>(R.id.btnBack)

        // --- Observadores de LiveData del ViewModel ---
        registerViewModel.registrationResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                // Requisito: "Si un registro resulta exitoso se deberá mover al usuario a la pantalla de login."
                Toast.makeText(context, "¡Registro exitoso! Por favor, inicia sesión.", Toast.LENGTH_SHORT).show()
                // NAVEGACIÓN CORRECTA Y ÚNICA DESPUÉS DE REGISTRO EXITOSO
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                // Si estás usando Safe Args, sería:
                // findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }

        registerViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, "Error en el registro: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        // Requisito: "Declarar los observers en cada vista" para el loader
        registerViewModel.showLoader.observe(viewLifecycleOwner) { show ->
            if (show) {
                Log.d("RegisterFragment", "Mostrando loader...")
                communicator?.showLoader() // Llama al comunicador para mostrar el loader
            } else {
                Log.d("RegisterFragment", "Ocultando loader...")
                communicator?.hideLoader() // Llama al comunicador para ocultar el loader
            }
        }

        // --- Click Listener para el botón de registro ---
        btnRegister.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Requisito: "validación de campos a sus pantallas para que no me deje iniciar un proceso si cualquiera de los campos está vacío"
            if (name.isEmpty()) {
                nameEditText.error = "El nombre no puede estar vacío"
                nameEditText.requestFocus()
                return@setOnClickListener
            }
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

            // Llamar al ViewModel para manejar el registro de usuario
            registerViewModel.registerUser(email, password)
        }

        // --- Click Listener para el botón de regreso ---
        backButton.setOnClickListener {
            // Se navega de regreso a la pantalla anterior en la pila de navegación
            findNavController().navigateUp()
        }
    }
}