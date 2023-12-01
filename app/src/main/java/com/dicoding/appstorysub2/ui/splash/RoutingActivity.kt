package com.dicoding.appstorysub2.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.appstorysub2.databinding.ActivityRoutingBinding
import com.dicoding.appstorysub2.ui.auth.login.LoginActivity
import com.dicoding.appstorysub2.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {

    private val binding: ActivityRoutingBinding by lazy {
        ActivityRoutingBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<RoutingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getLoginState().observe(this) { isAlreadyLogIn ->
            lifecycleScope.launch {
                delay(3.seconds)

                val intent = if (isAlreadyLogIn) {
                    Intent(this@RoutingActivity, MainActivity::class.java)
                } else {
                    Intent(this@RoutingActivity, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}