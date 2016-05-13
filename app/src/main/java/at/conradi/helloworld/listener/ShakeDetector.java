package at.conradi.helloworld.listener;

/**
 * Source: http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
 * Author: peceps (http://stackoverflow.com/users/590531/peceps)
 *
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class ShakeDetector implements SensorEventListener {
    /** Minimum movement force to consider.
     * Adapted by: @georgconradi
     *
     * Adapted to lower force - initially it was 10!
     * */
    private static final int MIN_FORCE = 3;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     * Adapted by: @georgconradi
     *
     * Adapted to less direction changes - initially it was 3!
     */
    private static final int MIN_DIRECTION_CHANGE = 2;

    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

    /** Maximum allowed time for shake gesture. */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;

    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;

    /** OnShakeListener that is called when shake is detected. */
    private OnShakeListener mShakeListener;

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        float newX = se.values[0]; // Retrieve data for X
        float newY = se.values[1]; // Retrieve data for Y
        float newZ = se.values[2]; // Retrieve data for Z

        // calculate movement
        float totalMovement = Math.abs(newX + newY + newZ - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {
            handleMovementDetection(newX, newY, newZ);
        }
    }

    private void handleMovementDetection(float newX, float newY, float newZ) {
        // get time
        long now = System.currentTimeMillis();

        // store first movement time
        if (mFirstDirectionChangeTime == 0) {
            mFirstDirectionChangeTime = now;
            mLastDirectionChangeTime = now;
        }

        // check if the last movement was not long ago
        if (now - mLastDirectionChangeTime < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

            // store movement data
            mLastDirectionChangeTime = now;
            mDirectionChangeCount++;

            // store last sensor data
            lastX = newX;
            lastY = newY;
            lastZ = newZ;

            if (isShakeGestureWithinTime(now)) {
                try {
                    mShakeListener.onShake();
                    resetShakeParameters();
                }
                catch (NullPointerException nullPointerException){
                    Log.e("NullPointerException", nullPointerException.getMessage() != null
                            ? nullPointerException.getMessage()
                            : "No shake listener attached!");
                }
            }

        } else {
            resetShakeParameters();
        }
    }

    private boolean isShakeGestureWithinTime(long now) {
        // check how many movements are so far
        // and check total duration
        return (mDirectionChangeCount >= MIN_DIRECTION_CHANGE)
                && (now - mFirstDirectionChangeTime < MAX_TOTAL_DURATION_OF_SHAKE);
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}