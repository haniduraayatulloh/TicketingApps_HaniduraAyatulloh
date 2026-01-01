package com.example.ticketingapps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

class SeatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var seat: Seat? = null

    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    private val seats: List<Seat> = listOf(
        Seat(id = 1, name = "A1", isBooked = false),
        Seat(id = 2, name = "A2", isBooked = false),
        Seat(id = 3, name = "B1", isBooked = false),
        Seat(id = 4, name = "B2", isBooked = false), // Nama diperbaiki dari A4 ke B2 agar urut
        Seat(id = 5, name = "C1", isBooked = false),
        Seat(id = 6, name = "C2", isBooked = false),
        Seat(id = 7, name = "D1", isBooked = false),
        Seat(id = 8, name = "D2", isBooked = false)
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val halfOfWidth = w / 2F
        val halfOfHeight = h / 2F
        var value = -600F

        seats.forEachIndexed { index, seat ->
            if (index.mod(2) == 0) {
                seat.apply {
                    x = halfOfWidth - 250F
                    y = halfOfHeight + value
                }
            } else { // Kolom kanan
                seat.apply {
                    x = halfOfWidth + 50F
                    y = halfOfHeight + value
                }
                value += 300F
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (seat in seats) {
            drawSeat(canvas, seat)
        }

        val text = "Silakan Pilih Kursi"
        titlePaint.apply {
            textSize = 50F
        }
        val textWidth = titlePaint.measureText(text)
        canvas.drawText(text, (width / 2F) - (textWidth / 2F), 100F, titlePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            seats.forEachIndexed { index, seat ->
                if (event.x >= seat.x!! && event.x <= seat.x!! + 200F &&
                    event.y >= seat.y!! && event.y <= seat.y!! + 200F) {
                    booking(index)
                    return@forEachIndexed
                }
            }
        }
        return true
    }

    private fun drawSeat(canvas: Canvas, seat: Seat) {
        if (seat.isBooked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        canvas.save()

        canvas.translate(seat.x!!, seat.y!!)


        val backgroundPath = Path().apply {
            addRect(0F, 0F, 200F, 200F, Path.Direction.CCW)
            addCircle(100F, 50F, 75F, Path.Direction.CCW)
        }
        canvas.drawPath(backgroundPath, backgroundPaint)


        val armrestPath = Path().apply {
            addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        }
        canvas.drawPath(armrestPath, armrestPaint)
        canvas.translate(150F, 0F)
        canvas.drawPath(armrestPath, armrestPaint)

        canvas.translate(-150F, 175F)
        val bottomSeatPath = Path().apply {
            addRect(0F, 0F, 200F, 25F, Path.Direction.CCW)
        }
        canvas.drawPath(bottomSeatPath, bottomSeatPaint)


        canvas.translate(0F, -175F)
        numberSeatPaint.apply {
            textSize = 50F
            getTextBounds(seat.name, 0, seat.name.length, mBounds)
        }
        canvas.drawText(seat.name, 100F - mBounds.centerX(), 100F, numberSeatPaint)

        canvas.restore()
    }

    private fun booking(position: Int) {

        for (seat in seats) {
            seat.isBooked = false
        }

        seats[position].apply {
            seat = this
            isBooked = true
        }

        invalidate()
    }
}
