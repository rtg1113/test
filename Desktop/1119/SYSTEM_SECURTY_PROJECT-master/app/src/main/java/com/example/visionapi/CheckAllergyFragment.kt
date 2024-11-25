package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionapi.databinding.FragmentCheckAllergyBinding

class CheckAllergyFragment : Fragment() {
    private lateinit var binding: FragmentCheckAllergyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckAllergyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CheckAllergy Activity 호출
        binding.checkAllergyButton.setOnClickListener {
            val intent = Intent(requireContext(), CheckAllergy::class.java)
            startActivity(intent)
        }
        binding.allergyDescription.text = "알레르기 정보를 설정하고 관리합니다."
    }
}
