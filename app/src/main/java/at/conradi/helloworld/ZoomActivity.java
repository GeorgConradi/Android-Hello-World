package at.conradi.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import at.conradi.helloworld.layouts.ZoomableRelativeLayout;
import at.conradi.helloworld.listener.OnPinchListener;

/**
 * Source code adapted from discussion on Stackoverflow:
 *  http://stackoverflow.com/questions/10013906/android-zoom-in-out-relativelayout-with-spread-pinch
 */
public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        // Retrieve container - the element declared with ZoomableRelativeLayout
        ZoomableRelativeLayout zoomView = (ZoomableRelativeLayout)findViewById(R.id.zoomLinearLayoutWrapper);
        // Attach listener to enable pinch to zoom
        zoomView.setOnTouchListener(new OnPinchListener(zoomView));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }
}
