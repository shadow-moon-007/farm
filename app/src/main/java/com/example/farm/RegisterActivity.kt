package com.example.farm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farm.MainActivity.Companion.auth
import com.example.farm.daos.UserDao
import com.example.farm.databinding.ActivityRegisterBinding
import com.example.farm.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Register"

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty())
                MainActivity.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
//            TODO: add update ui activity

        }

        binding.googleBtn.setOnClickListener {
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        MainActivity.auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val auth = auth.signInWithCredential(credential).await()
//                    val firebaseUser = auth.user
//                    updateUI(firebaseUser)
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
//            }

        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null) {
            val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            val usersDao = UserDao()
            usersDao.addUser(user)
            val toast = Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG)
            toast.show()
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }

}