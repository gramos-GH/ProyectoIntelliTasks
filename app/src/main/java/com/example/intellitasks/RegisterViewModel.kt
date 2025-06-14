/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


/*Requisito: "Viewmodels para LoginFragment y RegisterFragment"*/
class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /*Publisher para el resultado del registro (éxito/fracaso)*/
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    /*Publisher para mensajes de error*/
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /*Requisito: "En cada viewModel deberán crear los publishers para el loader"*/
    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    /*Método para manejar la lógica de registro de usuario*/
    fun registerUser(email: String, password: String) {
        /*Requisito: "crear los publishers para el loader"*/
        _showLoader.value = true /*Mostrar loader antes de la operación de Firebase*/

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showLoader.value = false /*Ocultar loader después de la operación de Firebase*/

                if (task.isSuccessful) {
                    Log.d("RegisterViewModel", "Usuario registrado exitosamente.")
                    _registrationResult.value = true /*Publicar éxito*/
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido al registrarse."
                    Log.w("RegisterViewModel", "Fallo el registro de usuario: $errorMessage", task.exception)
                    _registrationResult.value = false /*Publicar fallo*/
                    _error.value = errorMessage /*Publicar mensaje de error*/
                }
            }
    }
}