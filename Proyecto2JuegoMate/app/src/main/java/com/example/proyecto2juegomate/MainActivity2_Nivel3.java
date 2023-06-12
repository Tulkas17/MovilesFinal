package com.example.proyecto2juegomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2_Nivel3 extends AppCompatActivity {

    TextView tv_nombre, tv_score;
    ImageView iv_Auno, iv_Ados, iv_vidas;
    EditText et_respuesta;
    MediaPlayer mp, mpGreat, mpBad;

    int score, numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_nivel3);

        Toast.makeText(this, "Nivel 2 - Sumas Intermedias", Toast.LENGTH_SHORT).show();

        tv_nombre = findViewById(R.id.tv_Nombre);
        tv_score = findViewById(R.id.tv_Score);
        iv_vidas = findViewById(R.id.imageView_Manzanas);
        iv_Auno = findViewById(R.id.imageViewNumero1);
        iv_Ados = findViewById(R.id.imageViewNumeroDos);
        et_respuesta = findViewById(R.id.editTextResultado);

        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);

        switch (vidas){
            case 3:
                iv_vidas.setImageResource(R.drawable.tresvidas);
                break;
            case 2:
                iv_vidas.setImageResource(R.drawable.dosvidas);
                break;
            case 1:
                iv_vidas.setImageResource(R.drawable.unavida);
                break;
        }

        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        setSupportActionBar(findViewById(R.id.myToolbar_Nivel1));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mpGreat = MediaPlayer.create(this, R.raw.wonderful);
        mpBad = MediaPlayer.create(this, R.raw.bad);

        numeroAleatorio();
    }

    public void comparar(View vista){
        String respuesta = et_respuesta.getText().toString();
        if(!respuesta.equals("")){
            int respuestaJugador = Integer.parseInt(respuesta);
            if(resultado == respuestaJugador){
                mpGreat.start();
                score++;
                tv_score.setText("Score: " + score);
            }else{
                mpBad.start();
                vidas--;
                switch (vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        Toast.makeText(this, "Quedan 2 Manzanas", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        iv_vidas.setImageResource(R.drawable.unavida);
                        Toast.makeText(this, "Quedan 1 Manzana", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(this, "Perdiste todas tus Manzanas", Toast.LENGTH_SHORT).show();
                        mp.stop();
                        mp.release();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
            baseDeDatos();
            et_respuesta.setText("");
            numeroAleatorio();
        }else{
            Toast.makeText(this, "Debe dar una respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    public void baseDeDatos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * " +
                "from puntaje " +
                "where score = " +
                "(select max(score) " +
                "from puntaje)", null);

        if(consulta.moveToFirst()){
            String temp_Nombre = consulta.getString(0);
            String temp_Score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_Score);

            if(score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);
                BD.update("puntaje", modificacion, "score=" + bestScore, null);
            }
        }else{
            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);
            BD.insert("puntaje", null, insertar);
        }
        BD.close();
    }

    private void numeroAleatorio() {
        if(score <= 29){
            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);
            resultado = numAleatorio_uno - numAleatorio_dos;

            if(resultado >= 0){
                for(int i = 0; i < numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

                    if(numAleatorio_uno == i){
                        iv_Auno.setImageResource(id);
                    }
                    if(numAleatorio_dos == i){
                        iv_Ados.setImageResource(id);
                    }
                }
            }else{
                numeroAleatorio();
            }

        }else{
            //si la puntuación es mayor a 9 pasamos al nivel 2 del juego

            Intent intent = new Intent(this, MainActivity2_Nivel4.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);

            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            mp.stop();
            mp.release();
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed(){

    }
}