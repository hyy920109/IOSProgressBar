package com.hyy.iosprogressbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.GestureDetectorCompat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by heyangyang on 2021/6/12
 */
typealias OnProgressChangedListener = (iosProgressBar: IOSProgressBar, progress: Int, maxProgress: Int, minProgress: Int, actionUp: Boolean) -> Unit

class IOSProgressBar constructor(context: Context, attributeSet: AttributeSet?) :
	View(context, attributeSet) {
	//progress listener
	private var onProgressChangedListener: OnProgressChangedListener? = null

	private var viewWidth = 0
	private var viewHeight = 0
	
	private var progressRect = RectF()
	private var backgroundRect = Rect()
	private var viewBackgroundPath = Path()
	
	//progress conner round radius
	private var connerRadius = 0f
	//background color value
	private var progressBackgroundColor = "#FFC0C0C0".toColorInt()
	//progress color value
	private var progressColor = "#FF018786".toColorInt()
	//define conner rect
	private var connerRect = RectF()

	//define paints
	private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.FILL
	}
	private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.FILL
	}
	private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
	}

	//some progress variable
	private var maxProgress = 100
	private var minProgress = 0
	private var progress = 0

	//orientation vertical per progress height
	private var eachProgressHeight = 0f

	//orientation horizontal per progree width
	private var eachProgressWidth = 0f

	//orientation vertical curProgressTop
	private var curProgressTop = 0f

	//orientation horizontal curProgressRight
	private var curProgressRight = 0f

	//show Progress divider or not
	private var showProgressDivider = false

	private var dividerHeight = 0f

	//progress orientation
	private var orientation = ORIENTATION_VERTICAL

	private val gestureDetector= GestureDetectorCompat(
		context,
		object : GestureDetector.SimpleOnGestureListener() {
			override fun onDown(e: MotionEvent?): Boolean {
				curProgressTop = eachProgressHeight * (maxProgress - progress)
				return true
			}
			
			override fun onShowPress(e: MotionEvent?) {
			
			}
			
			override fun onSingleTapUp(e: MotionEvent?): Boolean {
				e?.let {
					curProgressTop = e.y
					curProgressRight = e.x
					updateCurrentProgress()
				}
				return true
			}
			
			override fun onScroll(
				e1: MotionEvent?,
				e2: MotionEvent?,
				distanceX: Float,
				distanceY: Float
			): Boolean {
				when(orientation) {
					ORIENTATION_VERTICAL -> {
						curProgressTop -= distanceY
						updateCurrentProgress()

					}
					ORIENTATION_HORIZONTAL -> {
						curProgressRight -= distanceX
						updateCurrentProgress()
					}
				}
				parent.requestDisallowInterceptTouchEvent(true)
				return true
			}
			
			override fun onLongPress(e: MotionEvent?) {
			
			}
			
			override fun onFling(
				e1: MotionEvent?,
				e2: MotionEvent?,
				velocityX: Float,
				velocityY: Float
			): Boolean {
				
				return true
			}
			
		})
	
	private fun updateCurrentProgress() {
		when(orientation) {
			ORIENTATION_VERTICAL -> {
				curProgressTop = min(curProgressTop, viewHeight.toFloat())
				curProgressTop = max(curProgressTop, 0f)

				val temp = ((viewHeight-curProgressTop)/eachProgressHeight).roundToInt()
				if (temp != progress) {
					progress = temp
					checkProgress()
					progressRect.top = viewHeight - progress * eachProgressHeight
					onProgressChangedListener?.invoke(this, progress, maxProgress, minProgress, false)
					invalidate()
				}
			}
			ORIENTATION_HORIZONTAL -> {
				curProgressRight = min(curProgressRight, viewWidth.toFloat())
				curProgressRight = max(curProgressRight, 0f)

				val temp = (curProgressRight/eachProgressWidth).roundToInt()
				if (temp != progress) {
					progress = temp
					checkProgress()
					progressRect.right = progress * eachProgressWidth
					onProgressChangedListener?.invoke(this, progress, maxProgress, minProgress, false)
					invalidate()
				}
			}
		}

	}
	
	init {
		val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.IOSProgressBar)
		typedArray.run {
			connerRadius = getDimension(
				R.styleable.IOSProgressBar_ipb_conner_radius,
				resources.getDimension(R.dimen.default_conner_radius)
			)
			progressBackgroundColor = getColor(
				R.styleable.IOSProgressBar_ipb_background_color,
				ContextCompat.getColor(context, R.color.default_background_color)
			)
			backgroundPaint.color = progressBackgroundColor
			
			progressColor = getColor(
				R.styleable.IOSProgressBar_ipb_progress_color,
				ContextCompat.getColor(context, R.color.default_progress_color)
			)
			progressPaint.color = progressColor
			
			//about progress
			maxProgress = getInteger(
				R.styleable.IOSProgressBar_ipb_progress_max,
				 100
			)
			minProgress = getInteger(
				R.styleable.IOSProgressBar_ipb_progress_min,
				0
			)
			//xml progress
			progress = getInteger(R.styleable.IOSProgressBar_ipb_progress, 0)


			showProgressDivider = getBoolean(R.styleable.IOSProgressBar_ipb_progress_show_divider, false)
			dividerHeight = getDimension(
				R.styleable.IOSProgressBar_ipb_progress_divider_height, resources.getDimension(
					R.dimen.default_divider_height))
			dividerPaint.strokeWidth = dividerHeight

			val dividerColor = getColor(
				R.styleable.IOSProgressBar_ipb_progress_divider_color,
				ContextCompat.getColor(context, R.color.default_background_color)
			)
			dividerPaint.color = dividerColor

			orientation = getInteger(R.styleable.IOSProgressBar_ipb_progress_bar_orientation, ORIENTATION_VERTICAL)
		}

		typedArray.recycle()
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		if (w != 0 && h != 0) {
			viewWidth = w
			viewHeight = h
			
			//per progress height
			when(orientation) {
				ORIENTATION_VERTICAL -> {
					eachProgressHeight = viewHeight / (maxProgress-minProgress).toFloat()
					curProgressTop = eachProgressHeight * (maxProgress - progress)

					progressRect.set(0f, curProgressTop, viewWidth.toFloat(), viewHeight.toFloat())
				}
				ORIENTATION_HORIZONTAL -> {
					eachProgressWidth = viewWidth / (maxProgress-minProgress).toFloat()
					curProgressRight = eachProgressWidth * progress
					progressRect.set(0f, 0f, curProgressRight, viewHeight.toFloat())
				}
			}

			backgroundRect.set(0, 0, viewWidth, viewHeight)
			
			initViewBackgroundPath()
		}
	}

	fun setShowProgressDivider(show: Boolean) {
		showProgressDivider = show
		invalidate()
	}

	fun setProgress(progress: Int) {
		if (this.progress == progress) return
		this.progress = progress
		checkProgress()
		when(orientation) {
			ORIENTATION_VERTICAL -> {
				progressRect.top = viewHeight - this.progress * eachProgressHeight
				invalidate()
			}
			ORIENTATION_HORIZONTAL -> {
				progressRect.right = this.progress * eachProgressWidth
				invalidate()
			}
		}
		onProgressChangedListener?.invoke(this, progress, maxProgress, minProgress, false)
	}

	fun setOnProgressChangeListener(onProgressChangedListener: OnProgressChangedListener) {
		this.onProgressChangedListener = onProgressChangedListener
	}

	private fun checkProgress() {
		progress = min(progress, maxProgress)
		progress = max(progress, minProgress)
	}
	
	private fun initViewBackgroundPath() {
		val left = 0f
		val top = 0f
		val right = viewWidth.toFloat()
		val bottom = viewHeight.toFloat()
		viewBackgroundPath.reset()
		viewBackgroundPath.moveTo(left, top + connerRadius)
		//add left top conner
		if (connerRadius > 0) {
			connerRect.set(left, top, connerRadius*2, connerRadius*2)
			viewBackgroundPath.arcTo(connerRect, -180f, 90f,false)
		}
		viewBackgroundPath.lineTo(left + connerRadius, top)
		viewBackgroundPath.lineTo(right - connerRadius, top)
		//add right top conner
		if (connerRadius > 0) {
			connerRect.set(right-connerRadius*2, top, right, connerRadius*2)
			viewBackgroundPath.arcTo(connerRect, -90f, 90f, false)
		}
		viewBackgroundPath.lineTo(right, top + connerRadius)
		viewBackgroundPath.lineTo(right, bottom - connerRadius)
		//add right bottom conner
		if (connerRadius > 0) {
			connerRect.set(right-connerRadius*2, bottom-connerRadius*2, right, bottom)
			viewBackgroundPath.arcTo(connerRect, 0f, 90f, false)
		}
		viewBackgroundPath.lineTo(right - connerRadius, bottom)
		viewBackgroundPath.lineTo(left + connerRadius, bottom)
		//add left bottom conner
		if (connerRadius > 0) {
			connerRect.set(left, bottom-connerRadius*2, connerRadius*2, bottom)
			viewBackgroundPath.arcTo(connerRect, 90f, 90f, false)
		}
		viewBackgroundPath.lineTo(left, bottom-connerRadius)
		viewBackgroundPath.lineTo(left, top + connerRadius)
	}
	
	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		canvas?.let {
			//clip canvas
			canvas.clipPath(viewBackgroundPath)
			//drawBackground
			canvas.drawRect(backgroundRect, backgroundPaint)
			//progress rect
			canvas.drawRect(progressRect, progressPaint)

			//check draw divider
			if (showProgressDivider && dividerHeight < eachProgressHeight) {
				when(orientation) {
					ORIENTATION_VERTICAL -> {
						var tempY = eachProgressHeight
						val endX = right.toFloat()
						while (tempY < height) {
							canvas.drawLine(0f, tempY, endX, tempY, dividerPaint)
							tempY += eachProgressHeight
						}
					}
					ORIENTATION_HORIZONTAL -> {
						var temX = eachProgressWidth
						val endY = right.toFloat()
						while (temX < height) {
							canvas.drawLine(temX, 0f, temX, endY, dividerPaint)
							temX += eachProgressWidth
						}
					}
				}

			}
		}
	}
	
	override fun onTouchEvent(event: MotionEvent?): Boolean {
		if (isEnabled.not()) return false
		val handled = gestureDetector.onTouchEvent(event)
		if (event?.action == MotionEvent.ACTION_UP) {
			onProgressChangedListener?.invoke(this, progress, maxProgress, minProgress, true)
		}
		return handled
	}

	
	companion object {
		const val TAG = "IOSProgressBar"
		const val ORIENTATION_VERTICAL = 1
		const val ORIENTATION_HORIZONTAL = 2
	}

}