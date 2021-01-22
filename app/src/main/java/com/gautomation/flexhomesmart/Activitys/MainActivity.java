package com.gautomation.flexhomesmart.Activitys;

import android.annotation.SuppressLint;
import static com.gautomation.flexhomesmart.Class.Constants.qtdBotoes;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.gautomation.flexhomesmart.Class.PahoMqttClient;
import com.gautomation.flexhomesmart.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    boolean statos_conexao = false;
    boolean aux1 = false;
    int qtd_de_FHS = 3;
    private final Button[] BTN = new Button[40];
    private final TextView[] TextFHS = new TextView[10];
    private final LinearLayout[] LayoutFHS = new LinearLayout[10];
    public String[] DispositivosFHS = new String[10];
    String servidor,
            porta,
            usuario,
            senha,
            cliente;
    boolean flag;
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient client;
    private  String MENSAGENS;
    private String TAG;
    private String[] TOPICO_PUBLICAO = new String[10];;
    private String[] TOPICO_ESCRICAO = new String[10];;
    private String FHS1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextFHS[0] = findViewById(R.id.TextFHS1);
        LayoutFHS[0] = findViewById(R.id.LayoutFHS1);
        BTN[0] = findViewById(R.id.btn1);
        BTN[1] = findViewById(R.id.btn2);
        BTN[2] = findViewById(R.id.btn3);
        BTN[3] = findViewById(R.id.btn4);

        TextFHS[1] = findViewById(R.id.TextFHS2);
        LayoutFHS[1] = findViewById(R.id.LayoutFHS2);
        BTN[4] = findViewById(R.id.btn5);
        BTN[5] = findViewById(R.id.btn6);
        BTN[6] = findViewById(R.id.btn7);
        BTN[7] = findViewById(R.id.btn8);

        TextFHS[2] = findViewById(R.id.TextFHS3);
        LayoutFHS[2] = findViewById(R.id.LayoutFHS3);
        BTN[8] = findViewById(R.id.btn9);
        BTN[9] = findViewById(R.id.btn10);
        BTN[10] = findViewById(R.id.btn11);
        BTN[11] = findViewById(R.id.btn12);

        for (int i = 0; i < qtd_de_FHS; i++) {
                LayoutFHS[i].setVisibility(View.GONE);
        }

        SharedPreferences Sharad = getSharedPreferences("confiServidor", Context.MODE_PRIVATE);
        servidor = Sharad.getString("Servidor", "postman.cloudmqtt.com");
        porta = Sharad.getString("Porta", "17122");
        usuario = Sharad.getString("Usuario", "sqrwrxka");
        senha = Sharad.getString("Senha", "DeJrHIUkqgXZ");
        cliente = Sharad.getString("Cliente", "Cliente_FHS");
        for(int i = 0; i < 10; i++){
            DispositivosFHS[i] = Sharad.getString("DISPOSITIVO_FHS"+i, "FHS"+(i+1));
        }
        for(int i = 0; i < 10; i++) {
            TOPICO_PUBLICAO[i] = "Envia/" + DispositivosFHS[i];
            TOPICO_ESCRICAO[i] = "Recebe/" + DispositivosFHS[i];
        }

        //================ Configurações MQTT ============================================
        String clientid = cliente; // id do criente.

        Random aleatorio = new Random();
        int numeroID_1 = aleatorio.nextInt(100) + 1;
        int numeroID_2 = aleatorio.nextInt(100) + 1;
        int numeroID_3 = aleatorio.nextInt(100) + 1;

        pahoMqttClient = new PahoMqttClient();

        client = pahoMqttClient.getMqttClient(getApplicationContext(),// Connect to MQTT Broker
                "tcp://"+servidor+":"+porta,
                clientid + numeroID_1*numeroID_2/numeroID_3,
                usuario,
                senha
        );
        //Create listener for MQTT messages.
        mqttCallback();
        //Create Timer to report MQTT connection status every 1 second
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScheduleTasks();
                status();
                dados();
            }

        }, 0, 3000);


        for(int i = 0; i < 4; i++){
            int I = i;
            BTN[i].setOnClickListener(v -> {
                publish("D"+(I+1), 0, TOPICO_PUBLICAO[0]);
            });
        }
        for(int i = 4; i < 8; i++){
            int I = i-3;
            BTN[i].setOnClickListener(v -> {
                publish("D"+(I), 0, TOPICO_PUBLICAO[1]);
            });
        }
        for(int i = 8; i < 12; i++){
            int I = i-7;
            BTN[i].setOnClickListener(v -> {
                publish("D"+(I), 0, TOPICO_PUBLICAO[2]);
            });
        }

    }

    void status(){
        for(int i = 0; i < qtd_de_FHS; i++) {
            if (statos_conexao) {
                publish("Status", 0, TOPICO_PUBLICAO[i]);
            }
        }
    }
    void dados() {
        if(statos_conexao && !aux1) {
            for(int i = 0; i < qtd_de_FHS; i++) {
                publish("Dados", 0, TOPICO_PUBLICAO[i]);
            }

        }
    }

    public void publish(String MSG, int QOS, String TOPICO ){
        try {
            pahoMqttClient.publishMessage(client, MSG, QOS, TOPICO);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    // =================== Menus =================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_conf, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.confservidor) {
            Intent trocatela=new Intent(MainActivity.this, ConfigServidor.class);
            startActivity(trocatela);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ===========================================================
    private void ScheduleTasks()
    {
        this.runOnUiThread(RunScheduledTasks);
    }
    private final Runnable RunScheduledTasks = new Runnable() {
        public void run() {
            TextView status  = findViewById(R.id.statusconexao);
            String msg_new;
            if(pahoMqttClient.mqttAndroidClient.isConnected() ) {
                msg_new = "Conectado\r\n";
                statos_conexao = true;
                status.setTextColor(0xFF00FF00); //Green if connected
                status.setTextSize( TypedValue.COMPLEX_UNIT_SP, 14);
                try {
                    for(int i = 0; i < 10; i++) {
                        if(DispositivosFHS[i].length() > 10){
                            pahoMqttClient.subscribe(client, TOPICO_ESCRICAO[i], 0);
                            pahoMqttClient.subscribe(client, TOPICO_ESCRICAO[i] + "/Status", 1);
                            pahoMqttClient.subscribe(client, TOPICO_ESCRICAO[i] + "/Dados", 1);
                        }
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                msg_new = "Desconectado\r\n";
                statos_conexao = false;
                status.setTextColor(0xFFFF0000); //Red if not connected
                status.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            status.setText(msg_new);
        }
    };
    // Called when a subscribed message is received
    protected void mqttCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //msg("Connection lost...");
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                int j = 0;
                for (int I = 0; I < qtd_de_FHS; I++) {
                    j = I*4;
                    if (topic.equals(TOPICO_ESCRICAO[I] + "/Status")) {
                        String[] msgStatus = message.toString().split("&");
                        for (int i = 0; i < 4; i++) {
                            if (msgStatus[i].contains("Desligado")) {
                                Drawable draw = getResources().getDrawable(R.drawable.btn_off);
                                BTN[j+i].setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
                            } else if (msgStatus[i].contains("Ligado")) {
                                Drawable draw = getResources().getDrawable(R.drawable.btn_on);
                                BTN[j+i].setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
                            }
                        }
                    }
                }
                int f = 0;
                for (int I = 0; I < qtd_de_FHS; I++) {
                    if (topic.equals(TOPICO_ESCRICAO[I] + "/Dados")) {
                        f = I*4;
                        String[] msgDados = message.toString().split("&");
                        FHS1 = msgDados[1];
                        TextFHS[I].setText(msgDados[0]);
                        BTN[f].setText(msgDados[2]);
                        BTN[f+1].setText(msgDados[3]);
                        BTN[f+2].setText(msgDados[4]);
                        BTN[f+3].setText(msgDados[5]);
                        if (DispositivosFHS[I].equals(FHS1)) {
                                LayoutFHS[I].setVisibility(View.VISIBLE);
                            } else {
                                LayoutFHS[I].setVisibility(View.GONE);
                        }

                    }
                }
                if (!FHS1.equals("")) {
                    //aux1 = true;
                }
                int t = 0;
                for (int I = 0; I < qtd_de_FHS; I++) {
                    t = I*4;
                    if (topic.equals(TOPICO_ESCRICAO[I])) {
                        String msg = message.toString();
                        //tvMessage.append( msg);
                        MENSAGENS = msg;
                        for (int i = 0; i < 4; i++) {
                            if (MENSAGENS.contains("Desligado")) {
                                    if (MENSAGENS.contains("D" + (i + 1))) {
                                        Drawable draw = getResources().getDrawable(R.drawable.btn_off);
                                        BTN[t + i].setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
                                    }
                            } else if (MENSAGENS.contains("Ligado")) {
                                    if (MENSAGENS.contains("D" + (i + 1))) {
                                        Drawable draw = getResources().getDrawable(R.drawable.btn_on);
                                        BTN[t + i].setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
                                    }

                            }
                        }
                    }

                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}