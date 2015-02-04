package fr.eseo.sensor.simon_eseo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    int nbMax = 40;
    int sequence[] = new int[nbMax];
    int currentIndex = -1;
    int nbCoupsReussis;
    int vitesse = 800;
    int nbPoints;


    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    Button buttonStart;
    Button buttonStop;

    Button levelButtonArray[] = new Button[8];

    Double timeDivision = 1.25;
    int levelSelected;
    int nbCoupPourAcceleration[] = new int[] {10, 9, 8, 7, 6, 5, 4, 3};
    boolean stopGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpButtons();
        readyAllButton();
        addLevelListeners();
        removeListeners();

        levelSelected = 3;
        levelButtonArray[levelSelected].setBackgroundColor(Color.RED);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeListeners();
                addLevelListeners();
                readyAllButton();
                stopGame = true;

            }
        });
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

    public void setUpButtons(){
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);
        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStop = (Button)findViewById(R.id.buttonStop);

        levelButtonArray[0] = (Button)findViewById(R.id.buttonTeletobies);
        levelButtonArray[1] = (Button)findViewById(R.id.buttonNewbie);
        levelButtonArray[2] = (Button)findViewById(R.id.buttonEasyMoney);
        levelButtonArray[3] = (Button)findViewById(R.id.buttonBanal);
        levelButtonArray[4] = (Button)findViewById(R.id.buttonWeshWesh);
        levelButtonArray[5] = (Button)findViewById(R.id.buttonBeauGosse);
        levelButtonArray[6] = (Button)findViewById(R.id.buttonPapa);
        levelButtonArray[7] = (Button)findViewById(R.id.buttonDemoniaque);
    }

    public void newGame(){
        stopGame = false;
        removeListeners();
        removeLevelListeners();
        vitesse = 1000;
        nbCoupsReussis = 0;
        currentIndex = -1;
        nbPoints = 0;
        setSequence();
        showSequence();
    }

    public void setSequence(){
        for(int i = 0; i < nbMax; i++){
            sequence[i] = (int) (Math.random() * 4) + 1;
        }
    }

    public void showSequence(){
        currentIndex++;
        if (currentIndex <= nbCoupsReussis){
            switch (sequence[currentIndex]){
                case 1:
                    pressButtonWait(buttonA);
                    break;
                case 2:
                    pressButtonWait(buttonB);
                    break;
                case 3:
                    pressButtonWait(buttonC);
                    break;
                case 4:
                    pressButtonWait(buttonD);
                    break;
                default:
                    break;
            }
        }else{
            currentIndex = -1;
            addListeners();
        }

    }

    public void verifyEntry(int valueEntered){
        currentIndex++;
        if (currentIndex <= nbCoupsReussis){
            if(valueEntered != sequence[currentIndex]){
                endOfGameLose();
            }else if(currentIndex == nbCoupsReussis){
                removeListeners();
                nbCoupsReussis++;
                nbPoints+=nbCoupsReussis*(1000/vitesse);
                currentIndex = -1;
                if ((nbCoupPourAcceleration[levelSelected] != 10) && (nbCoupsReussis % nbCoupPourAcceleration[levelSelected]) == 0){
                    acceleration();
                }else{
                    showSequence();
                }
            }
        }else if (nbCoupsReussis >= nbMax){
            endOfGameWin();
        }
    }

    public void endOfGameWin(){
        readyAllButton();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You beat the game congratulation, you are the best !!! \n Number of points: "+nbPoints)
                .setTitle("YOU WON")
                .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        addLevelListeners();
                    }
                })
                .setPositiveButton("NEW GAME", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void endOfGameLose(){
        readyAllButton();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You lost the game but made it up to "+nbPoints+" points.")
                .setTitle("GAME OVER")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addLevelListeners();
                    }
                })
                .setPositiveButton("NEW GAME", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void acceleration(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vous êtes forts dit donc, nous allons devoir accélérer !!")
                .setTitle("BRAVO")
                .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        vitesse/=timeDivision;
                        showSequence();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void readyAllButton(){
        buttonA.setBackgroundColor(Color.GREEN);
        buttonB.setBackgroundColor(Color.RED);
        buttonC.setBackgroundColor(Color.YELLOW);
        buttonD.setBackgroundColor(Color.BLUE);
    }

    public void unpressButton(Button buttonToPress){
        switch (buttonToPress.getId()){
            case R.id.buttonA:
                buttonToPress.setBackgroundColor(Color.GREEN);
                break;
            case R.id.buttonB:
                buttonToPress.setBackgroundColor(Color.RED);
                break;
            case R.id.buttonC:
                buttonToPress.setBackgroundColor(Color.YELLOW);
                break;
            case R.id.buttonD:
                buttonToPress.setBackgroundColor(Color.BLUE);
                break;
            default:
                break;
        }
    }

    public void pressButton(Button buttonToUnpress){
        buttonToUnpress.setBackgroundColor(Color.WHITE);
    }

    public void addListeners(){
        buttonA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressButton((Button)v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    unpressButton((Button)v);
                    verifyEntry(1);
                }
                return true;
            }
        });

        buttonB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressButton((Button) v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    unpressButton((Button)v);
                    verifyEntry(2);
                }
                return true;
            }
        });

        buttonC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressButton((Button) v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    unpressButton((Button) v);
                    verifyEntry(3);
                }
                return true;
            }
        });

        buttonD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressButton((Button) v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    unpressButton((Button) v);
                    verifyEntry(4);
                }
                return true;
            }
        });
    }

    public void removeListeners(){
        buttonA.setOnTouchListener(null);
        buttonB.setOnTouchListener(null);
        buttonC.setOnTouchListener(null);
        buttonD.setOnTouchListener(null);
    }

    public void addLevelListeners(){
        for (int i = 0; i < levelButtonArray.length; i++){
            final double level = (double)i;
            levelButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    levelButtonArray[levelSelected].setBackgroundColor(Color.WHITE);
                    levelButtonArray[(int)level].setBackgroundColor(Color.RED);
                    levelSelected = (int)level;
                }
            });
        }
    }

    public void removeLevelListeners(){
        for (int i = 0; i < levelButtonArray.length; i++){
            levelButtonArray[i].setOnClickListener(null);
        }
    }

    public void pressButtonWait(final Button button){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!stopGame){
                    try {
                        Thread.sleep(vitesse);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pressButton(button);
                            }
                        });
                        Thread.sleep(vitesse);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                unpressButton(button);
                                showSequence();
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
