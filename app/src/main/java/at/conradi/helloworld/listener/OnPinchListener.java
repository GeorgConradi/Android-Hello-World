package at.conradi.helloworld.listener;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import at.conradi.helloworld.R;
import at.conradi.helloworld.layouts.ZoomableRelativeLayout;

/**
 * Created by georgconradi on 09.05.16.
 * Source code adapted from discussion on Stackoverflow:
 *  http://stackoverflow.com/questions/10013906/android-zoom-in-out-relativelayout-with-spread-pinch
 */
public class OnPinchListener implements View.OnTouchListener {
    ScaleGestureDetector scaleGestureDetector = null;
    ZoomableRelativeLayout layout = null;

    public OnPinchListener(ZoomableRelativeLayout mLayout){
        layout = mLayout;
        // Attach the custom gesture detector implementation to our layout
        scaleGestureDetector = new ScaleGestureDetector(layout.getContext(), new PinchGestureDetector());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }


    /**
     * Class used to detect a pinch gesture performed on a specific layout
     */
    private class PinchGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        float currentSpan;
        float startFocusX;
        float startFocusY;

        /**
         *
         * @param detector The scale gesture detector
         * @return
         */
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            // Determine and preserve the focus of the gesture
            currentSpan = detector.getCurrentSpan();
            startFocusX = detector.getFocusX();
            startFocusY = detector.getFocusY();
            return true;
        }

        /**
         *
         * @param detector The scale gesture detector
         * @return
         */
        public boolean onScale(ScaleGestureDetector detector)
        {
            ZoomableRelativeLayout zoomableRelativeLayout
                    = (ZoomableRelativeLayout) layout.findViewById(R.id.zoomLinearLayoutWrapper);

            // Set scale relative to the previous position
            zoomableRelativeLayout.relativeScale(detector.getCurrentSpan() / currentSpan, startFocusX, startFocusY);
            currentSpan = detector.getCurrentSpan();

            return true;
        }

        /**
         *
         * @param detector The scale gesture detector
         */
        public void onScaleEnd(ScaleGestureDetector detector)
        {
            ZoomableRelativeLayout zoomableRelativeLayout
                    = (ZoomableRelativeLayout) layout.findViewById(R.id.zoomLinearLayoutWrapper);
            zoomableRelativeLayout.release();
        }
    }
}
