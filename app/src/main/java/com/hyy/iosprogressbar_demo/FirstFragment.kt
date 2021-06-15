package com.hyy.iosprogressbar_demo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyy.iosprogressbar.IOSProgressBar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<IOSProgressBar>(R.id.ios_progress_bar_horizontal)
            .setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
                Log.d(TAG, "onProgressChangedListener: $progress actionUp-->$actionUp")
            }

        view.findViewById<IOSProgressBar>(R.id.ios_progress_bar_vertical)
            .setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
                if (progress < 1) {
                    iosProgressBar.setProgress(1)
                }
            }
    }

    companion object {
        const val TAG = "FirstFragment"
    }
}