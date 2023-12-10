package com.routine_mate.mobile_programing.views

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.routine_mate.mobile_programing.R
import com.routine_mate.mobile_programing.repository.RoutineRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val starPercentile = intent.getDoubleExtra("star_percentile", 0.0)
        val percentileTextView: TextView = findViewById(R.id.tv_star_percentile)
        percentileTextView.text = "당신은 상위\n$starPercentile%\n유저입니다!\n계속 별을 모아봐요!"


        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        val userIdTextView: TextView = findViewById(R.id.user_id_text_view)
        userIdTextView.text = auth.currentUser?.email

        val logoutButton: ImageButton = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener {
                sendBroadcast(Intent("com.routine_mate.mobile_programing.ACTION_LOGOUT"))
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val deleteAccountButton: ImageButton = findViewById(R.id.delete_account_button)
        deleteAccountButton.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                AlertDialog.Builder(this)
                    .setTitle("계정 삭제")
                    .setMessage("계정을 삭제하면 모든 데이터가 삭제됩니다. 계속하시겠습니까?")
                    .setPositiveButton("예") { _, _ ->
                        val routineRepository = RoutineRepository()
                        routineRepository.deleteAllRoutinesByUserId(userId)

                        auth.currentUser?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                sendBroadcast(Intent("com.routine_mate.mobile_programing.ACTION_LOGOUT"))
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle possible errors.
                            }
                        }
                    }
                    .setNegativeButton("아니오", null)
                    .show()
            }
        }
    }
}