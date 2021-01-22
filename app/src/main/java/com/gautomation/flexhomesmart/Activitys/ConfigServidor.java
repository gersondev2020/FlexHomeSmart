package com.gautomation.flexhomesmart.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.gautomation.flexhomesmart.R;

public class ConfigServidor extends AppCompatActivity {

    EditText Servidor, Porta, Usuario, Senha, Cliente;
    private final EditText[] EditFHS = new EditText[10];
    String servidor, porta, usuario, senha, cliente;
    public String[] DispositivosFHS = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_servidor);

        Servidor = findViewById( R.id.editURL);
        Porta =  findViewById( R.id.editPorta );
        Usuario = findViewById( R.id.editUsuario);
        Senha = findViewById( R.id.editSenha);
        Cliente = findViewById( R.id.editCliente);
        EditFHS[0] =  findViewById( R.id.editFHS1);
        EditFHS[1] =  findViewById( R.id.editFHS2);
        EditFHS[2] =  findViewById( R.id.editFHS3);
        EditFHS[3] =  findViewById( R.id.editFHS4);
        EditFHS[4] =  findViewById( R.id.editFHS5);
        EditFHS[5] =  findViewById( R.id.editFHS6);
        EditFHS[6] =  findViewById( R.id.editFHS7);
        EditFHS[7] =  findViewById( R.id.editFHS8);
        EditFHS[8] =  findViewById( R.id.editFHS9);
        EditFHS[9] =  findViewById( R.id.editFHS10);


        SharedPreferences Sharad = getSharedPreferences("confiServidor", Context.MODE_PRIVATE);
        servidor = Sharad.getString("Servidor", "postman.cloudmqtt.com");
        porta = Sharad.getString("Porta", "17122");
        usuario = Sharad.getString("Usuario", "sqrwrxka");
        senha = Sharad.getString("Senha", "DeJrHIUkqgXZ");
        cliente = Sharad.getString("Cliente", "Cliente_FHS");
        for(int i = 0; i < 10; i++){
            DispositivosFHS[i] = Sharad.getString("DISPOSITIVO_FHS"+i, "FHS"+(i+1));
        }

        Servidor.setText(servidor);
        Porta.setText(porta);
        Usuario.setText(usuario);
        Senha.setText(senha);
        Cliente.setText(cliente);
        for(int i = 0; i < 10; i++){
            EditFHS[i].setText(DispositivosFHS[i]);
        }

    }
    // =================== Menus =================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sairesalvar) {
            SharedPreferences Sharad = getSharedPreferences("confiServidor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = Sharad.edit();
            editor.putString("Servidor", Servidor.getText().toString());
            editor.putString("Porta",Porta.getText().toString());
            editor.putString("Usuario", Usuario.getText().toString());
            editor.putString("Senha", Senha.getText().toString());
            editor.putString("Cliente", Cliente.getText().toString());
            for(int i = 0; i < 10; i++) {
                editor.putString("DISPOSITIVO_FHS"+i, EditFHS[i].getText().toString());
            }
            editor.commit();
            Toast.makeText(getApplicationContext(), "Configuração Salvas:)", Toast.LENGTH_SHORT).show();

            Intent trocatela=new Intent(ConfigServidor.this, MainActivity.class);
            startActivity(trocatela);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ===========================================================

    @Override
    protected void onResume() {

        super.onResume();
    }
}