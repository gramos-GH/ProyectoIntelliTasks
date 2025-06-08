/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(R.layout.fragment_register) {

    // Declara una instancia de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa FirebaseAuth cuando se crea el fragmento
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a los EditText
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)

        //Se obtiene el botón de registro
        val btnRegister = view.findViewById<Button>(R.id.btnContinue)

        btnRegister.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            // --- Lógica de validación básica ---
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor, ingresa tu correo y contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Salir si los campos están vacíos
            }

            // --- Lógica de Firebase Authentication ---
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso
                        Log.d("RegisterFragment", "Usuario registrado exitosamente.")
                        val user = auth.currentUser
                        Toast.makeText(context, "¡Registro exitoso para ${user?.email}!", Toast.LENGTH_SHORT).show()

                        // Navegación al MainActivity SOLO después de un registro exitoso
                        val navController = Navigation.findNavController(view)
                        navController.navigate(R.id.action_registerFragment_to_mainActivity)

                    } else {
                        // Si el registro falla, muestra el error en Logcat y en un Toast
                        Log.w("RegisterFragment", "Fallo el registro de usuario.", task.exception)
                        val errorMessage = task.exception?.message ?: "Error desconocido al registrar."
                        Toast.makeText(context, "Error en el registro: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
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