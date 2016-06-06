package at.conradi.helloworld.listener;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import at.conradi.helloworld.R;

/**
 * Created by georgconradi on 02.05.16.
 * Listener used to capture dragging events
 */
public class DragListener  implements View.OnDragListener {
    private AppCompatActivity usedActivity = null;

    public DragListener(AppCompatActivity activity){
        usedActivity = activity;
    }

    @Override
    public boolean onDrag(View droppedAtView, DragEvent event) {
        Button draggedButton = (Button) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing - but feel free to implement something
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                // do nothing - but feel free to implement something
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // do nothing - but feel free to implement something
                break;
            case DragEvent.ACTION_DROP:
                // Dropped - do whatever needs to be done
                draggedButton.setVisibility(View.VISIBLE);
                    /* Get parent group, if you want to switch elements between groups e.g.*/
                //ViewGroup owner = (ViewGroup) draggedButton.getParent();
                //owner.removeView(draggedButton);

                Button droppedAtButton = (Button) droppedAtView;
                int droppedID = droppedAtButton.getId();
                int draggedID = draggedButton.getId();

                String txtTrap = usedActivity.getBaseContext().getResources().getString(R.string.game_field_with_trap);
                String txtNoTrap = usedActivity.getBaseContext().getResources().getString(R.string.game_field_wout_trap);

                if (droppedAtButton.getText() == txtTrap
                        || droppedAtButton.getText() == "?" + txtTrap
                        || droppedAtButton.getText() == txtNoTrap
                        || droppedAtButton.getText() == "?" + txtNoTrap){
                    Toast.makeText(usedActivity, "Move already made for drop destination!"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    draggedButton.setBackgroundColor(Color.GREEN);

                    Toast.makeText(usedActivity, "Drop@:" + droppedID + ", Drag@:" + draggedID
                            , Toast.LENGTH_SHORT).show();

                    droppedAtButton.setBackgroundColor(Color.RED);
                    droppedAtButton.setText("?" + txtTrap); // still need to implement that switching functionality
                    draggedButton.setText("?" + txtNoTrap); // still need to implement that switching functionality
                    // Remove listener, as cheating has been already performed and thus needs to be disabled
                    draggedButton.setOnTouchListener(null);

                        /*
                        * Get parent group, if you want to switch elements between groups e.g.
                        * but be careful in respect to the type of element needed to cast
                        * */
                    //LinearLayout container = (LinearLayout) droppedAtView.getParent();
                    //container.addView(draggedButton);
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                draggedButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return true;
    }
}