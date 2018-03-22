package ru.example.simbirsoft.common

import android.content.Context
import android.net.ConnectivityManager
import ru.example.simbirsoft.Application
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript




/**
 * Created by harri
 * on 20.02.2018.
 */
class Util {
    companion object {
        fun isConnected(): Boolean {
            val connectivityManager = Application.sApplicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnected ?: false
        }

        fun blur(context: Context, image: Bitmap): Bitmap {
            val bitmapScale = 0.2f
            val blurRadius = 10f

            val width = Math.round(image.width * bitmapScale)
            val height = Math.round(image.height * bitmapScale)

            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(blurRadius)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }
    }
}