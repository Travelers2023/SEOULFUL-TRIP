package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.seoulfultrip.seoulfultrip.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    lateinit var binding:FragmentMyBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)

        if(MyApplication.checkAuth()){
            binding.useremail.text = "${MyApplication.email}"
            binding.username.text = "${MyApplication.name()}"
            MyApplication.db.collection("users").document(MyApplication.auth.uid.toString())
                .update(mapOf("userName" to "${MyApplication.name()}"))
                .addOnSuccessListener { Log.d("SWProject-username","${MyApplication.name()}")}
                .addOnFailureListener { e ->
                    Log.d("SWProject-username","${e.message}")
                }
        }

        binding.logoutBtn.setOnClickListener{
            logout()
        }

        val mainActivity = activity as MainActivity

        binding.btnSavepath.setOnClickListener{
            //val newFragment = HomeFragment()
            /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.h_fragment, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()*/

            mainActivity.loadFragment(HomeFragment())
            mainActivity.updateIcons(mainActivity.getBottomNavigationView().menu.findItem(R.id.nav1), R.drawable.home_1)
        }

        binding.btnSaveplace.setOnClickListener {
            mainActivity.loadFragment(SaveFragment())
            mainActivity.updateIcons(mainActivity.getBottomNavigationView().menu.findItem(R.id.nav3), R.drawable.star_1)
        }

        binding.btnReport.setOnClickListener {
            sendEmail()
        }

        binding.btnFeedback.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

    private fun sendEmail() {
        val recipientEmail = "travelers202309@gmail.com" // 수신자 이메일 주소를 여기에 입력합니다.
        val subject = "[Android][Seoulful Trip]Report"
        val message = """
            
            
            
            
                        ------------------------------
                        - App Version: 1.0.0
                        - User Account: ${MyApplication.email}
                    """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        val gmailPackage = "com.google.android.gm"

        // Gmail 앱이 설치되어 있으면 Gmail로 열도록 설정
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            intent.setPackage(gmailPackage)
            startActivity(intent)
        } else {
            // Gmail 앱이 없는 경우, 사용자에게 메시지 표시
            Toast.makeText(requireContext(), "Gmail 앱을 찾을 수 없습니다. 직접 전송해주세요.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun logout() {
        MyApplication.auth.signOut()
        MyApplication.email = null
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }
}