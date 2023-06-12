package com.example.proyecto2juegomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btnJugar;
    ImageView ivPersonaje;
    EditText nickname;
    TextView puntuacion;

    MediaPlayer mp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbarMainView));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        btnJugar = findViewById(R.id.btn_jugar);
        ivPersonaje = findViewById(R.id.ivFruta);
        nickname = findViewById(R.id.et_nombreJugador);
        puntuacion = findViewById(R.id.tvPuntuacion);


        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

        //Abrir comunicacion con la  base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db", null, 1);

        //Manipular la base de datos en modo de escritura
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null
        );

        if(consulta.moveToFirst()){
         String temp_nombre = consulta.getString(0);
         String temp_score = consulta.getString(1);

            puntuacion.setText("Record: " + temp_score + " de " + "temp_nombre");
        }
        BD.close();



        int numAleatorio = (int) (Math.random() * 10);

        int idImg;

        switch (numAleatorio){
            case 0: case 10:
                idImg = getResources().getIdentifier("mango", "drawable", getPackageName());
                ivPersonaje.setImageResource(idImg);
                break;
            case 1: case 9:
                idImg = getResources().getIdentifier("fresa", "drawable", getPackageName());
                ivPersonaje.setImageResource(idImg);
                break;
            case 2: case 8:
                idImg = getResources().getIdentifier("manzana", "drawable", getPackageName());
                ivPersonaje.setImageResource(idImg);
                break;
            case 3: case 7:
                idImg = getResources().getIdentifier("sandia", "drawable", getPackageName());
                ivPersonaje.setImageResource(idImg);
                break;
            case 4: case 5: case 6:
                idImg = getResources().getIdentifier("uva", "drawable", getPackageName());
                ivPersonaje.setImageResource(idImg);
                break;

        }
    }

    public void jugar(View view){
        String nombre = nickname.getText().toString();

        if(!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, MainActivity2_Nivel1.class);
            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, "Debe escribir su nombre", Toast.LENGTH_SHORT).show();

            nickname.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(nickname, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}