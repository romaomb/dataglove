package romao.matheus.dataglove.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/*
 * Created by romao on 21/04/18
 */

class CustomViewPager : ViewPager {

    private var swipeable: Boolean = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.swipeable = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.swipeable) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.swipeable) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setSwipeable(swipeable: Boolean) {
        this.swipeable = swipeable
    }
}