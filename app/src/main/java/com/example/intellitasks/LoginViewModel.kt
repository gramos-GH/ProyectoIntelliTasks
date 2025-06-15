/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/*Clase LoginViewModel*/
class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /*Publisher para el resultado del login*/
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    /*Publisher para mensajes de error*/
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /*Publisher para mostrar/ocultar loader*/
    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    fun loginUser(email: String, password: String) {
        _showLoader.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showLoader.value = false

                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "Usuario logeado exitosamente.")
                    _loginResult.value = true
                } else {
                    val firebaseMessage = task.exception?.message ?: "Error desconocido al iniciar sesión."
                    Log.w("LoginViewModel", "Fallo el inicio de sesión: $firebaseMessage", task.exception)

                    // Personalizar mensajes según contenido del error original
                    val friendlyMessage = when {
                        firebaseMessage.contains("badly formatted", ignoreCase = true) ->
                            "El formato del correo electrónico es inválido."
                        firebaseMessage.contains("password is invalid", ignoreCase = true) ||
                                firebaseMessage.contains("wrong password", ignoreCase = true) ->
                            "La contraseña es incorrecta."
                        firebaseMessage.contains("no user record", ignoreCase = true) ||
                                firebaseMessage.contains("user-not-found", ignoreCase = true) ->
                            "No existe una cuenta con ese correo."
                        else -> firebaseMessage
                    }

                    _loginResult.value = false
                    _error.value = friendlyMessage
                }
            }
    }
}
