package com.example.dailylook

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.dailylook.databinding.ActivitySignupBinding
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var db: ProfileDatabase
    var sex: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectSpinner() //spinner 연결
        connectRadioBtn()   // radiobtnonchecklistener 연결

        // room db 해볼게요
        val db = ProfileDatabase.getInstance(applicationContext)!!
        binding.signupSignupBtn.setOnClickListener {
            addUser()
            intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun connectRadioBtn(){
        binding.signupSexRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.signup_sex_man_btn -> sex = "man"
                R.id.signup_sex_woman_btn -> sex = "woman"
            }
        }
    }
    private fun connectSpinner() {
        val yearlist = resources.getStringArray(R.array.year_spinner)
        val monthlist = resources.getStringArray(R.array.month_spinner)
        val datelist = resources.getStringArray(R.array.date_spinner)

        val yearadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yearlist)
        val monthadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthlist)
        val dateadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, datelist)

        binding.signupYearSpinner.adapter = yearadapter
        binding.signupMonthSpinner.adapter = monthadapter
        binding.signupDateSpinner.adapter = dateadapter
    }
    private fun addUser() {
        var newUser = Profile(
            binding.signupIdEt.text.toString(),
            binding.signupPwEt.text.toString(),
            binding.signupNameEt.text.toString(),
            binding.signupYearSpinner.selectedItem.toString() + binding.signupMonthSpinner.selectedItem.toString() + binding.signupDateSpinner.selectedItem.toString(),
            sex.toString()
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.profileDao().insert(newUser)
        }
    }
}