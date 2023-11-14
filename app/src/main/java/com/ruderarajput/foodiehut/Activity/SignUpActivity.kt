package com.ruderarajput.foodiehut.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ruderarajput.foodiehut.Model.UserModel
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSingInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this, googleSingInOptions)

        binding.signUpBtn.setOnClickListener {

            email = binding.etEmail.text.toString().trim()
            password = binding.etPassword.text.toString().trim()
            name = binding.etName.text.toString()

            if (!name.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if (!email.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
                binding.etEmail.requestFocus()
            } else if (!password.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
                binding.etPassword.requestFocus()
            } else {
                createAccount(email, password)
            }
        }

        binding.googleBtn.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this, "Google Sign Up Successfully", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else {
                            Toast.makeText(this, "Google Sign Up Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else {
                    Toast.makeText(this, "Google Sign Up Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        name = binding.etName.text.toString()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        val user = UserModel(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}