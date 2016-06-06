package at.conradi.helloworld;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.conradi.helloworld.listener.DragListener;
import at.conradi.helloworld.listener.ShakeDetector;
import at.conradi.helloworld.listener.TouchListener;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;
    private String txtTrap = "";
    private String txtNoTrap = "";

    private int fieldsAccessed = 0;
    private static final int AMOUNT_OF_BOARD_ROWS = 3;
    private static final int AMOUNT_OF_BOARD_COLUMNS = 3;
    private List<Integer> traps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        txtTrap = getResources().getString(R.string.game_field_with_trap);
        txtNoTrap = getResources().getString(R.string.game_field_wout_trap);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button newGame = new Button(toolbar.getContext());
        newGame.setText(R.string.start_new_game);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

        //Toast.makeText(MainActivity.this, "Test!", Toast.LENGTH_SHORT).show();
        toolbar.addView(newGame);

        setSupportActionBar(toolbar);

        initGame();
    }

    private void initGame() {
        fieldsAccessed = 0;
        traps = new ArrayList<>();
        generateTraps(AMOUNT_OF_BOARD_ROWS * AMOUNT_OF_BOARD_COLUMNS + 1);
        String logMessage = getResources().getString(R.string.log_btn_at);

        TableLayout tblLayout = (TableLayout) findViewById(R.id.content);
        for(int i = 0; i < AMOUNT_OF_BOARD_ROWS; i++)
        {
            TableRow row = (TableRow)tblLayout.getChildAt(i);
            for(int j = 0; j < AMOUNT_OF_BOARD_COLUMNS; j++){
                Button button = (Button) row.getChildAt(j); // get child index on particular row
                String buttonText = button.getText().toString();
                button.setText("");

                button.setOnDragListener(new DragListener(MainActivity.this));

                button.setOnClickListener(new HandleTrapListener(i, j));
                Log.i(logMessage + (i + j), buttonText);
            }
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();
    }

    public void startShowCube(MenuItem item) {
        Intent intent = new Intent(this, DiceSpinnerActivity.class);
        startActivity(intent);
    }

    public void startShowZoom(MenuItem item) {
        Intent intent = new Intent(this, ZoomActivity.class);
        startActivity(intent);
    }

    private class HandleTrapListener implements View.OnClickListener {
        int rowId = 0;
        int columnId = 0;

        HandleTrapListener(int rowId, int columnId) {
            this.rowId = rowId;
            this.columnId = columnId;
        }

        @Override
        public void onClick(View v) {
            // zero-based IDs, so we need to increment by 1
            int rowOffset = rowId * AMOUNT_OF_BOARD_COLUMNS;
            int expectedAt = rowOffset + (1 + columnId);
            Button buttonToCheck = (Button)v;
            String buttonText = buttonToCheck.getText().toString();
            String logMessage = getResources().getString(R.string.log_trap_checked);

            if (!buttonText.equals(txtTrap) && !buttonText.equals(txtNoTrap)) {
                Log.i(logMessage, Integer.toString(expectedAt));
                if (traps.contains(expectedAt)) {
                    Button button = (Button) v;
                    button.setText(txtTrap);
                    // enable cheating

                    // Assign the touch listener to your view which you want to move
                    button.setOnTouchListener(new TouchListener());
                }
                else
                    ((Button) v).setText(txtNoTrap);
                fieldsAccessed++;

                if (fieldsAccessed == AMOUNT_OF_BOARD_ROWS * AMOUNT_OF_BOARD_COLUMNS){
                    Toast.makeText(MainActivity.this, R.string.no_moves
                            , Toast.LENGTH_SHORT).show();

                    // enable shaking only when game has been finished
                    mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {

                        public void onShake() {
                            Toast.makeText(MainActivity.this, R.string.shaked_for_new_game
                                    , Toast.LENGTH_SHORT).show();
                            initGame();
                        }
                    });
                }
            }
            else{
                Toast.makeText(MainActivity.this, R.string.already_moved
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateTraps(int limit){
        int amountOfButtons = limit;
        int trapNumber = newRandomNumberBetween(amountOfButtons);
        String logMessage = getResources().getString(R.string.log_trap_id);

        int amountOfTraps = 3;
        for (int i = 0; i < amountOfTraps; i++) {
            while (traps.contains(trapNumber)) {
                trapNumber = newRandomNumberBetween(amountOfButtons);
            }
            Log.i(logMessage, Integer.toString(trapNumber));
            traps.add(trapNumber);
        }
    }

    /**
     * Returns new random number
     * @param limit upper boundary for random number generation - exclusive
     * @return a new random number
     */
    private static int newRandomNumberBetween(int limit){

        Random r = new Random();
        return r.nextInt(limit - 1) + 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}