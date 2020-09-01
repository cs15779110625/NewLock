package swu.cs.newlock

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class DotInfo (val cx: Float,val cy: Float, val radius: Float,val tag: Int ){
    val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    fun setColor(color: Int){
        paint.color = color
    }

    val rect = Rect().apply {
        right = (cx + radius).toInt()
        left = (cx - radius).toInt()
        top = (cy - radius).toInt()
        bottom = (cy + radius).toInt()
    }
}