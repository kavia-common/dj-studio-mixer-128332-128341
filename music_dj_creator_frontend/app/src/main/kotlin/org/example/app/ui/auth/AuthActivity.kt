package org.example.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import org.example.app.R
import org.example.app.ui.MainActivity

/**
 * PUBLIC_INTERFACE
 * AuthActivity provides a simple local authentication UI.
 *
 * This demo uses in-memory validation. Replace with a proper backend or identity provider if needed.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutPassword: TextInputLayout
    private lateinit var btnSignIn: MaterialButton
    private lateinit var btnGuest: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.title = getString(org.example.app.R.string.title_auth)

        layoutEmail = findViewById(R.id.inputLayoutEmail)
        layoutPassword = findViewById(R.id.inputLayoutPassword)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnGuest = findViewById(R.id.btnGuest)

        btnSignIn.setOnClickListener {
            val email = inputEmail.text?.toString().orEmpty()
            val pass = inputPassword.text?.toString().orEmpty()
            if (email.contains("@") && pass.length >= 4) {
                goToApp()
            } else {
                layoutEmail.error = if (!email.contains("@")) "Invalid email" else null
                layoutPassword.error = if (pass.length < 4) "Min 4 chars" else null
            }
        }
        btnGuest.setOnClickListener { goToApp() }
    }

    private fun goToApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
