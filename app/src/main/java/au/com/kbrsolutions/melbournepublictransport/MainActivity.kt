package au.com.kbrsolutions.melbournepublictransport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
/*

Get accurate information about Melbourne (Australia) train network. Written in Kotlin and using Jetpack.

# Capstone-Project written from scratch in Kotlin and using Android Architecture Components

Melbourne Public Transport app will help anybody in Melbourne (Australia, Victoria) to get accurate information about the transport network timetables - trains, busses and trams.

It is using PTV API version 3. It can be accessed  from [here](http://timetableapi.ptv.vic.gov.au/swagger/ui/index#/)

To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.

 */
