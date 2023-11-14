package com.ruderarajput.foodiehut.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Model.UserModel
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUserData()

        binding.etName.isEnabled = false
        binding.etAddress.isEnabled = false
        binding.etEmail.isEnabled = false
        binding.etPhoneNumber.isEnabled = false

        binding.editProfile.setOnClickListener {
            binding.etName.isEnabled = !binding.etName.isEnabled
            binding.etAddress.isEnabled = !binding.etAddress.isEnabled
            binding.etEmail.isEnabled = !binding.etEmail.isEnabled
            binding.etPhoneNumber.isEnabled = !binding.etPhoneNumber.isEnabled
            binding.etName.requestFocus()
        }

        binding.saveInfoBtn.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()

            saveUserData(name, email, phone, address)
        }
        return binding.root
    }

    private fun saveUserData(name: String, email: String, phone: String, address: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference: DatabaseReference = database.reference.child("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Update Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference: DatabaseReference = database.getReference("user").child(userId)


            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.etName.setText(userProfile.name)
                            binding.etAddress.setText(userProfile.address)
                            binding.etEmail.setText(userProfile.email)
                            binding.etPhoneNumber.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

}