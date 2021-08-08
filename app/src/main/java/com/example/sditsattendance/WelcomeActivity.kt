package com.example.sditsattendance

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        supportActionBar?.hide()
        

        var lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottie_layer_name)
        lottieAnimationView.setAnimation("kehadiran.json")
        lottieAnimationView.playAnimation()

            lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    //Add your code here for animation end
                    val intent = Intent (applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
    }
}