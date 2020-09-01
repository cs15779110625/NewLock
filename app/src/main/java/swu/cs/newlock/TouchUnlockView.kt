package swu.cs.newlock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.updateLayoutParams
import java.lang.StringBuilder
import kotlin.math.log

class TouchUnlockView: View {
    private var passWord: StringBuilder = StringBuilder()

    private var radius = 0f

    private var padding = 0f

    private var dotInfos = arrayListOf<DotInfo>()

    private var selectedDotInfos = arrayListOf<DotInfo>()

    private var foreDot: DotInfo? = null

    private val linePath = Path()

    private var selectedLine = Path()

    private val linePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val CirclePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    constructor(context: Context):super(context){

    }
    constructor(context: Context,attrs: AttributeSet):super(context,attrs){

    }
    constructor(context: Context,attrs: AttributeSet,style: Int):super(context,attrs,style){

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(linePath,linePaint)
        canvas?.drawPath(selectedLine,linePaint)
        drawNineDots(canvas)
        drawSelectedDots(canvas)
    }

    fun drawNineDots(canvas: Canvas?){
        for(dot in dotInfos){
            canvas?.drawCircle(dot.cx,dot.cy,dot.radius,CirclePaint)
            canvas?.drawCircle(dot.cx,dot.cy,dot.radius,dot.paint)
        }
    }

    fun drawSelectedDots(canvas: Canvas?){
        for(dot in selectedDotInfos){
            canvas?.drawCircle(dot.cx,dot.cy,dot.radius/3,paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    private fun init(){
        var cx = 0f
        var cy = 0f
        if(measuredWidth > measuredHeight){
            radius = measuredHeight/10f
            padding = radius
            cx = (measuredWidth - measuredHeight)/2 + padding + radius
            cy = padding + radius
        }else{
            radius = measuredWidth/10f
            padding = radius
            cx = (measuredHeight - measuredWidth)/2 + padding + radius
            cy = padding + radius
        }
        for(row in 0..2){
            for(column in 0..2){
                DotInfo(cx + (2*radius + padding)*column,cy + (2*radius + padding)*row,radius,row*3+column+1).also {
                    dotInfos.add(it)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->{
                for(dot in dotInfos){
                    if(dot.rect.contains(event.x.toInt(), event.y.toInt()) && dot.paint.color == Color.BLACK){
                        dot.setColor(Color.RED)
                        selectedDotInfos.add(dot)
                        passWord.append(dot.tag)
                        foreDot = dot
                        linePath.moveTo(dot.cx,dot.cy)
                        selectedLine.moveTo(dot.cx,dot.cy)
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE ->{
                if(foreDot != null){
                    selectedLine.reset()
                    selectedLine.moveTo(foreDot!!.cx,foreDot!!.cy)
                    selectedLine.lineTo(event.x,event.y)
                    invalidate()
                }
                for(dot in dotInfos){
                    if(dot.rect.contains(event.x.toInt(), event.y.toInt()) ){
                        dot.setColor(Color.RED)
                        selectedDotInfos.add(dot)
                        passWord.append(dot.tag)
                        if(foreDot == null){
                            linePath.moveTo(dot.cx,dot.cy)
                            selectedLine.moveTo(dot.cx,dot.cy)
                            foreDot = dot
                        }else{
                            foreDot = dot
                            linePath.lineTo(dot.cx,dot.cy)
                            selectedLine.reset()
                            selectedLine.moveTo(dot.cx,dot.cy)
                        }
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{
                reset()
            }
        }
        return true
    }
    fun reset(){
        for(dot in selectedDotInfos){
            dot.setColor(Color.BLACK)
        }
        selectedDotInfos.clear()
        linePath.reset()
        selectedLine.reset()
        foreDot = null
        passWord.clear()
        invalidate()
    }
}