package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.auth
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.name
import com.seoulfultrip.seoulfultrip.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        changeVisibility(intent.getStringExtra("data").toString())

        if(MyApplication.checkAuth()){
            changeVisibility("login")
        } else {
            changeVisibility("init")
        }

        binding.goSignUpBtn.setOnClickListener{
            // 회원가입
            changeVisibility("signup")
        }

        binding.goSignInBtn.setOnClickListener{
            // 로그인
            changeVisibility("login")
        }

        binding.signUpBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email:String = binding.authEmailEditView.text.toString()
            val password:String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email,password) // 회원 가입 firebase 연동
                .addOnCompleteListener(this){ task ->
                    binding.authEmailEditView.text.clear() // user 입력 정보 지우기
                    binding.authPasswordEditView.text.clear()
                    binding.authConfirmPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        // 이메일 2차 인증 작업
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener{ sendTask ->
                                if(sendTask.isSuccessful){
                                    // Log.d("mobileApp", "회원가입 성공..이메일 확인")
                                    Toast.makeText(baseContext, "회원가입 성공..이메일 확인", Toast.LENGTH_LONG).show()
//                                    changeVisibility("login")
                                    finish()
                                }
                                else{
                                    // Log.d("mobileApp", "메일 전송 실패...")
                                    Toast.makeText(baseContext, "메일 전송 실패...", Toast.LENGTH_LONG).show()
                                    changeVisibility("logout")
                                }
                            }
                    }
                    else{
                        // Log.d("mobileApp", "회원가입 실패..")
                        Toast.makeText(baseContext, "회원가입 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                }
        }
        binding.loginBtn.setOnClickListener {
            // 이메일, 비밀번호 로그인
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        if (MyApplication.checkAuth()) {
                            MyApplication.email = email // 이메일 정보 저장
                            Toast.makeText(baseContext, "로그인 성공..", Toast.LENGTH_LONG).show()
                            finish()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent) // Main 액티비티로 인텐트 전환
                        } else {
                            Toast.makeText(baseContext, "이메일 인증 실패..", Toast.LENGTH_LONG).show()
                            changeVisibility("logout")
                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                }
        }



        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
            finish()
        }

//        binding.UnsubscribingBtn.setOnClickListener {
//            MyApplication.auth.currentUser!!.delete()
//
//            val userDocRef = MyApplication.db.collection("users").document(MyApplication.auth.uid.toString())
//            MyApplication.db.collection("users").document("${MyApplication.auth.uid}")
//                .get()
//                .addOnSuccessListener {  documentSnapshot ->
//                    if(documentSnapshot.exists()) {
//                        val currentEmail = documentSnapshot.getString("userEmail")
//                        currentEmail?.let {
//                            val updatedEmail = "Unsubscribed members"
//                            updateEmail(userDocRef, updatedEmail)
//                        }
//                        val currentImage = documentSnapshot.getString("imageUrl")
//                        currentImage?.let {
//                            val updatedImage = "https://firebasestorage.googleapis.com/v0/b/reviewmate-59794.appspot.com/o/profile_images%2Fimg_1.png?alt=media&token=eb7e37c7-bbc3-4ef5-9491-bbca0f8c60bc"
//                            updateProfile(userDocRef, updatedImage)
//                        }
//                    }
//                }
//
//            MyApplication.email = null
//            finish()
//        }

        val requestLauncher = registerForActivityResult( // 구글 인증
            ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // ApiException : Google Play 서비스 호출이 실패했을 때 태스크에서 반환할 예외
            try{
                val account = task.getResult(ApiException::class.java) // google account 인증 정보
                val credential = GoogleAuthProvider.getCredential(account.idToken, null) // 인증 되었는지 확인
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                             changeVisibility("login") // 로그인 화면으로 넘어감
                            Log.d("SWProject", "GoogleSingIn - Successful")
                            finish()
                        }
                        else {
                            changeVisibility("logout")
                            Log.d("SWProject", "GoogleSingIn - NOT Successful")
                        }
                    }
            } catch (e: ApiException){
                changeVisibility("logout")
                Log.d("SWProject", "GoogleSingIn - ${e.message}")
            }
        }
        binding.loginGoogle.setOnClickListener {
            //구글 로그인.................... 런처 실행
            val gso : GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent : Intent = GoogleSignIn.getClient(this, gso).signInIntent // 인텐트 실행 (구글 기본앱 실행)
            requestLauncher.launch(signInIntent)
        }
    }

//    fun updateEmail(docRef: DocumentReference, updatedValue: String) {
//        val updates = hashMapOf<String, Any>(
//            "userEmail" to updatedValue
//        )
//
//        docRef.update(updates)
//            .addOnSuccessListener {
//                // 업데이트 성공 처리
//            }
//            .addOnFailureListener { e ->
//                // 업데이트 실패 처리
//            }
//
//    }
//    fun updateProfile(docRef: DocumentReference, updatedValue: String) {
//        val updates = hashMapOf<String, Any>(
//            "imageUrl" to updatedValue
//        )
//
//        docRef.update(updates)
//            .addOnSuccessListener {
//                // 업데이트 성공 처리
//            }
//            .addOnFailureListener { e ->
//                // 업데이트 실패 처리
//            }
//
//    }

    fun changeVisibility(mode: String){
        if(mode.equals("init")){
            binding.run {
                authMainTextViewWelcom.visibility = View.VISIBLE
                authMainTextViewSeoulful.visibility = View.VISIBLE
                goSignInBtn.visibility = View.VISIBLE
                goSignUpBtn.visibility = View.VISIBLE
            }
        }else if(mode.equals("signup")){
            binding.run {
                authMainTextViewWelcom.visibility = View.GONE
                authMainTextViewSeoulful.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                goSignUpBtn.visibility = View.GONE

//                UnsubscribingBtn.visibility= View.VISIBLE
//                googleLoginBtn.visibility= View.GONE
                authTextViewSignUp.visibility = View.VISIBLE
                authEmailEditView.visibility= View.VISIBLE
                authPasswordEditView.visibility= View.VISIBLE
                authConfirmPasswordEditView.visibility= View.VISIBLE
                signUpBtn.visibility= View.VISIBLE
                lineTextSignup.visibility = View.VISIBLE
                loginWithBtn.visibility = View.VISIBLE
            }

        }else if(mode.equals("login")) {
            binding.run {
                authMainTextViewWelcom.visibility = View.GONE
                authMainTextViewSeoulful.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                goSignUpBtn.visibility = View.GONE

                authTextViewSignUp.visibility = View.GONE
                authConfirmPasswordEditView.visibility= View.GONE
                lineTextSignup.visibility = View.GONE
                signUpBtn.visibility= View.GONE

                authTextViewLogIn.visibility = View.VISIBLE
                authEmailEditView.visibility= View.VISIBLE
                authPasswordEditView.visibility= View.VISIBLE
                loginBtn.visibility= View.VISIBLE
                lineTextLogin.visibility= View.VISIBLE
                loginWithBtn.visibility= View.VISIBLE
            }

        } else if(mode.equals("logout")){
            binding.run {
                authMainTextViewWelcom.text = "로그아웃 상태입니다."
//                authMainTextViewWelcom.text
//                logoutBtn.visibility = View.GONE
//                goSignInBtn.visibility = View.VISIBLE
////                googleLoginBtn.visibility = View.VISIBLE
//                authEmailEditView.visibility = View.VISIBLE
//                authPasswordEditView.visibility = View.VISIBLE
//                signBtn.visibility = View.GONE
//                loginBtn.visibility = View.VISIBLE
            }
        }
    }
}