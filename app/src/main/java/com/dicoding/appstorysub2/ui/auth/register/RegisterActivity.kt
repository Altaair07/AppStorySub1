package com.dicoding.appstorysub2.ui.auth.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dicoding.appstorysub2.databinding.ActivityRegisterBinding
import com.dicoding.appstorysub2.util.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding.contentRegister) {
            btnRegister.setOnClickListener {
                doRegister()
            }

            btnLogin.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun doRegister() {
        with(binding.contentRegister) {
            val name = edRegisterName.text.toString().trim()
            val email = edRegisterEmail.text.toString().trim()
            val password = edRegisterPassword.text.toString().trim()

            viewModel.register(name, email, password).observe(this@RegisterActivity) { result ->
                when (result) {
                    is Result.Error -> {
                        Snackbar.make(root, result.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        Snackbar.make(root, result.data.message.toString(), Snackbar.LENGTH_LONG).show()
                        onBackPressedDispatcher.onBackPressed()
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