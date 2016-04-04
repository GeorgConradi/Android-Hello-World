package at.conradi.helloworld;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int amountOfTraps = 3;
    List<Integer> traps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button newGame = new Button(toolbar.getContext());
        newGame.setText("Start new game");
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

        toolbar.addView(newGame);

        setSupportActionBar(toolbar);

        initGame();
    }

    private void initGame() {
        traps = new ArrayList<Integer>();
        generateTraps(1, 10);

        TableLayout tblLayout = (TableLayout) findViewById(R.id.content);
        for(int i = 0; i < 3; i++)
        {
            TableRow row = (TableRow)tblLayout.getChildAt(i);
            for(int j=0; j < 3; j++){
                Button button = (Button) row.getChildAt(j); // get child index on particular row
                String buttonText = button.getText().toString();
                button.setText("");

                button.setOnClickListener(new HandleTrapListener(i, j));
                Log.i("Button index: " + (i + j), buttonText);
            }
        }
    }

    private class HandleTrapListener implements View.OnClickListener {
        int rowId = 0;
        int columnId = 0;

        HandleTrapListener(int rowId, int columnId){
            this.rowId = rowId;
            this.columnId = columnId;
        }

        @Override
        public void onClick(View v) {
            // zero-based IDs, so we need to increment by 1
            int rowOffset = (1 * rowId) * 3;
            int expectedAt = rowOffset + (1 + columnId);

            Log.i("Trap checked for", expectedAt + "");
            if (traps.contains(expectedAt))
                ((Button) v).setText("X");
            else
                ((Button) v).setText("O");
        }
    }

    protected void generateTraps(int startAt, int limit){
        int amountOfButtons = 10;
        int trapNumber = newRandomNumberBetween(1, amountOfButtons);

        for (int i = 0; i < amountOfTraps; i++) {
            while (traps.contains(trapNumber)) {
                trapNumber = newRandomNumberBetween(1, amountOfButtons);
            }
            Log.i("Trap for id", trapNumber + "");
            traps.add(trapNumber);
        }
    }

    /**
     *
     * @param startAt lower boundary for random number generation - inclusive
     * @param limit upper boundary for random number generation - exclusive
     * @return a new random number
     */
    protected int newRandomNumberBetween(int startAt, int limit){

        Random r = new Random();
        return r.nextInt(limit - startAt) + startAt;
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
}
