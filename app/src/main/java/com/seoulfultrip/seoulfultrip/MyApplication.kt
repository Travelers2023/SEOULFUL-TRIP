package com.seoulfultrip.seoulfultrip

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyApplication : MultiDexApplication() {
    companion object{
        lateinit var auth : FirebaseAuth
        var email : String? = null
        fun checkAuth() : Boolean{ // 인증이 되었는지 체크하는 함수
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}