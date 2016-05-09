package at.conradi.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DiceSpinnerActivity extends AppCompatActivity {
    Canvas cube = new Canvas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_spinner);

        LinearLayout linearView = (LinearLayout)findViewById(R.id.diceLinearLayout);
        ViewTreeObserver vto = linearView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new MyGlobalLayoutListener(linearView));
    }

    /**
     * Observer used to listen and trigger if layout is already available.
     * This is necessary to ensure that width and height attributes are already generated/calculated
     * and thus can be used for further processing
     *
     */
    protected class MyGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{
        ViewGroup layout = null;
        MyGlobalLayoutListener(ViewGroup newLayout){
            layout = newLayout;
        }
        @Override
        public void onGlobalLayout() {
            this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            float width = layout.getMeasuredWidth()/2;
            float height = layout.getMeasuredHeight()/2;

            createSimpleSpinningDice(this.layout, width, height);
        }
    }

    private void createSimpleSpinningDice(ViewGroup layout, float mWidth, float mHeight) {
        Bitmap bmp1 = BitmapFactory.decodeResource(layout.getContext().getResources(),
                R.drawable.dice3d);

        ImageView iView = new ImageView(layout.getContext());
        iView.setImageBitmap(bmp1);

        // Use RelativeLayout to enable centered content
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)layout.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        lp.width = (int)mWidth;
        lp.height = (int)mHeight;
        iView.setLayoutParams(lp);

        /*
        * Add animation using our resource xml (the animation definition)
        * Source code adapted from Stackoverflow discussion:
        *   http://stackoverflow.com/questions/1634252/how-to-make-a-smooth-image-rotation-in-android
        */
        iView.startAnimation(
                AnimationUtils.loadAnimation(layout.getContext(), R.anim.rotate_indefinitely));
        layout.addView(iView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
