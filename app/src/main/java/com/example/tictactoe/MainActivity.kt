package com.example.tictactoe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    enum class  Turn
    {
        NOUGHT ,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initBoard()
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    fun boartTapped(view: View) {

        if (view !is Button)
            return
        addToBoard(view)

        if (checkVictory(NOUGHT))
        {
            noughtsScore++
            binding.player2wonrounds.text = "Won Rounds: $noughtsScore"
            result("Player 2 Win!")
        }
        if (checkVictory(CROSS))
        {
            crossesScore++
            binding.player1wonrounds.text = "Won Rounds: $crossesScore"
            result("Player 1 Win!")
        }

        if (fullBoard())
        {
            result("Draw")
        }
    }

    private fun checkVictory(S: String): Boolean {
        //horizontal victory
        if (match(binding.a1,S) && match(binding.a2,S) && match(binding.a3,S))
            return true
        if (match(binding.b1,S) && match(binding.b2,S) && match(binding.b3,S))
            return true
        if (match(binding.c1,S) && match(binding.c2,S) && match(binding.c3,S))
            return true

        //virtical victory
        if (match(binding.a1,S) && match(binding.b1,S) && match(binding.c1,S))
            return true
        if (match(binding.a2,S) && match(binding.b2,S) && match(binding.c2,S))
            return true
        if (match(binding.a3,S) && match(binding.b3,S) && match(binding.c3,S))
            return true

        //diagonal victory
        if (match(binding.a1,S) && match(binding.b2,S) && match(binding.c3,S))
            return true
        if (match(binding.a3,S) && match(binding.b2,S) && match(binding.c1,S))
            return true
        return false
    }
    private fun match(button: Button , symbol :String):Boolean = button.text == symbol

    private fun result(title: String) {

        showCustomDialog(this,title)

    }

    private fun showCustomDialog(context: Context,title: String) {
        if (title == "Draw"){
            val dailogview = LayoutInflater.from(context).inflate(R.layout.winningdialog,null)

            val builder = AlertDialog.Builder(context)
            builder.setView(dailogview)

            val dailog = builder.create()
            dailog.show()

            val winingplayername = dailog.findViewById<TextView>(R.id.winningplayer)
            val resetbtn = dailogview.findViewById<Button>(R.id.reset_btn)

            winingplayername.apply {
                winingplayername!!.setText(title)
            }
            resetbtn.setOnClickListener {
                resetBoard()
                dailog.dismiss()
            }

        }else{
            val dailogview = LayoutInflater.from(context).inflate(R.layout.winningdialog,null)

            val builder = AlertDialog.Builder(context)
            builder.setView(dailogview)

            val dailog = builder.create()
            dailog.show()
            val winingplayername = dailog.findViewById<TextView>(R.id.winningplayer)
            val resetbtn = dailogview.findViewById<Button>(R.id.reset_btn)
            val congratulation = dailogview.findViewById<LottieAnimationView>(R.id.congratulation)
            val animationView = dailogview.findViewById<LottieAnimationView>(R.id.animationView)

            winingplayername.apply {
                winingplayername!!.setText(title)
            }

            congratulation.apply {
                playAnimation()
            }
            animationView.apply {
                playAnimation()
            }

            resetbtn.setOnClickListener {
                resetBoard()
                dailog.dismiss()
            }
        }

    }

    private fun resetBoard() {
        for (button in boardList)
        {
            button.text = ""
        }
        if (firstTurn == Turn.NOUGHT){
            firstTurn = Turn.CROSS
        }
        else if (firstTurn == Turn.CROSS){
            firstTurn = Turn.NOUGHT
            currentTurn = firstTurn
        }
    }

    private fun fullBoard(): Boolean {
        for (button in boardList){
            if (button.text =="")
                return false
        }
        return true

    }

    private fun addToBoard(button: Button)
    {
        if(button.text != "")
            return

        if (currentTurn == Turn.NOUGHT)
        {
            button.text =  NOUGHT
            button.setTextColor(ContextCompat.getColor(this,R.color.blue))
            currentTurn = Turn.CROSS
        }
        else if (currentTurn == Turn.CROSS)
        {
            button.text =  CROSS
            button.setTextColor(ContextCompat.getColor(this,R.color.red))
            currentTurn = Turn.NOUGHT
        }
        setTurnText()
    }

    private fun setTurnText() {
        var turtext = ""

        if (currentTurn == Turn.CROSS){
            turtext = "Turn $CROSS"
            val spannable = SpannableString(turtext)
            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF000000")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(1f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#F24D4F")), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(2.0f), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.turntext.text = spannable

        }
       else if (currentTurn == Turn.NOUGHT){
            turtext = "Turn $NOUGHT"

            val spannable = SpannableString(turtext)
            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF000000")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(1f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#4285F4")), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(2.0f), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.turntext.text = spannable


        }



    }

    companion object
    {
        const val NOUGHT = "o"
        const val CROSS = "x"
    }

}