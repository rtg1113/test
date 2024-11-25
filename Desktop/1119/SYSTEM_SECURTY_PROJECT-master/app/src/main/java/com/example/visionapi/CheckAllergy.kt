package com.example.visionapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visionapi.databinding.ActivityCheckAllergyBinding

class CheckAllergy : AppCompatActivity() {
    private lateinit var binding: ActivityCheckAllergyBinding
    private val allergyList = listOf(
        "대두", "새우", "계란", "소고기", "돼지고기", "닭고기", "게", "오징어",
        "고등어", "조개", "우유", "땅콩", "호두", "잣", "복숭아", "토마토",
        "밀", "메밀", "아황산류", "아세트아미노펜", "나프록센"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 유저 데이터베이스 초기화
        val userDB = UserDatabase.getInstance(applicationContext)
        if (userDB!!.UserDao().getUserNum() <= 0) {
            val tmpUser = User(
                1, "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", ""
            )
            userDB.UserDao().insertUser(tmpUser)
        }

        // 기존 데이터 로드
        setAllergyBox(userDB.UserDao().getUser(1))
        Log.e("Allergy", "User allergy loaded")

        // 저장 버튼 클릭 이벤트
        binding.BtnSaveInfo.setOnClickListener {
            userDB.UserDao().updateUser(checkAllergyBox())
            Toast.makeText(this, "알러지가 수정되었습니다!", Toast.LENGTH_SHORT).show()
        }

        // 검색창 텍스트 변경 리스너
        binding.searchAllergy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCheckBoxes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // 체크박스 상태 저장
    private fun checkAllergyBox(): User {
        val alArray = Array(21) { "" }
        val checkBoxes = binding.checkBoxContainer.childCount

        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox?.isChecked == true) {
                alArray[i] = checkBox.text.toString()
            }
        }

        return User(
            1, alArray[0], alArray[1], alArray[2], alArray[3], alArray[4], alArray[5],
            alArray[6], alArray[7], alArray[8], alArray[9], alArray[10], alArray[11],
            alArray[12], alArray[13], alArray[14], alArray[15], alArray[16], alArray[17],
            alArray[18], alArray[19], alArray[20]
        )
    }

    // 데이터 로드 후 체크박스 초기화
    private fun setAllergyBox(user: User) {
        val alArray = listOf(
            user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7,
            user.al8, user.al9, user.al10, user.al11, user.al12, user.al13, user.al14,
            user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21
        )

        val checkBoxes = binding.checkBoxContainer.childCount
        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox != null && i < alArray.size) {
                checkBox.text = allergyList[i] // 알러지 항목 이름 설정
                checkBox.isChecked = alArray[i].isNotEmpty()
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    Log.d("CheckAllergy", "${checkBox.text} is ${if (isChecked) "checked" else "unchecked"}")
                }
            }
        }
    }

    // 검색 기능: 검색어에 맞는 체크박스만 보이게 처리
    private fun filterCheckBoxes(query: String) {
        val checkBoxes = binding.checkBoxContainer.childCount

        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox != null) {
                checkBox.visibility = if (checkBox.text.contains(query, true)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
