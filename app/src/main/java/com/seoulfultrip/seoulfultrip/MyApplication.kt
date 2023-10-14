package com.seoulfultrip.seoulfultrip

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MyApplication : MultiDexApplication() {
    companion object{
        lateinit var auth : FirebaseAuth
        var email : String? = null
        var name : String? = null
        lateinit var db : FirebaseFirestore
        fun checkAuth() : Boolean{ // 인증이 되었는지 체크하는 함수
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                name = currentUser.displayName
                currentUser.isEmailVerified
            } ?: let{
                false
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        if(true) {
            var userInfo = UserModel()

            userInfo.uid = auth.uid
            userInfo.userEmail = auth.currentUser?.email
            userInfo.userName = auth.currentUser?.displayName

            db.collection("users").document(auth.uid.toString())
                .get()
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val document = task.result
                        if (document.exists()){ Log.d("ToyProject", "이미 존재하는 계정입니다.") }
                        else {
                            db.collection("users").document(auth.uid.toString()).set(userInfo)
                            Log.d("ToyProject", "계정을 user collection에 추가했습니다.")
                        }
                    }
                }
        }
    }
}