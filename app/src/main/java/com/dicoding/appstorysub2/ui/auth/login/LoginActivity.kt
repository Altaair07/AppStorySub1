package com.dicoding.appstorysub2.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dicoding.appstorysub2.databinding.ActivityLoginBinding
import com.dicoding.appstorysub2.ui.auth.register.RegisterActivity
import com.dicoding.appstorysub2.ui.main.MainActivity
import com.dicoding.appstorysub2.util.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding.contentLogin) {
            btnLogin.setOnClickListener {
                doLogin()
            }

            btnRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun doLogin() {
        with(binding.contentLogin) {
            val email = edLoginEmail.text.toString().trim()
            val password = edLoginPassword.text.toString().trim()

            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        Snackbar.make(root, result.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            pbLoading.isVisible = isLoading
        }
    }
}