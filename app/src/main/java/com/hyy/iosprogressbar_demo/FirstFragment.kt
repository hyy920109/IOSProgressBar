package com.hyy.iosprogressbar_demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.hyy.iosprogressbar.IOSProgressBar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val textSizes = arrayOf(12f, 16f, 20f, 24f, 28f)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iosProgressBarHorizontal =
            view.findViewById<IOSProgressBar>(R.id.ios_progress_bar_horizontal)
        val tvProgress = view.findViewById<AppCompatTextView>(R.id.tv_progress)
        iosProgressBarHorizontal.setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
            "Current Progress is $progress".also { tvProgress.text = it }
        }

        val tvTest = view.findViewById<AppCompatTextView>(R.id.tv_test)
        view.findViewById<IOSProgressBar>(R.id.ios_progress_bar_vertical)
            .setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
                tvTest.textSize = textSizes[progress]
            }

        "Current Progress is ${iosProgressBarHorizontal.getProgress()}".also { tvProgress.text = it }
    }

    companion object {
        const val TAG = "FirstFragment"
    }
}