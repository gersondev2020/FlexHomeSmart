package com.gautomation.flexhomesmart.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gautomation.flexhomesmart.Class.PahoMqttClient;
import com.gautomation.flexhomesmart.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Rotular extends AppCompatActivity {
    String servidor, porta, usuario, senha, cliente, cabecalho;
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient client;
    private  String MENSAGENS;
    private String TOPICO_PUBLICAO = "Envia/sub_do_esp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotular);

        SharedPreferences Sharad = getSharedPreferences("confiServidor", Context.MODE_PRIVATE);
        servidor = Sharad.getString("Servidor", "postman.cloudmqtt.com");
        porta = Sharad.getString("Porta", "17122");
        usuario = Sharad.getString("Usuario", "sqrwrxka");
        senha = Sharad.getString("Senha", "DeJrHIUkqgXZ");
        cliente = Sharad.getString("Cliente", "Cliente_FHS");
        cabecalho = Sharad.getString("Cabecalho", "Recebe/");

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
            }

        }, 0, 2000);

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
                status.setTextColor(0xFF00FF00); //Green if connected
                status.setTextSize( TypedValue.COMPLEX_UNIT_SP, 14);
                try {
                    pahoMqttClient.subscribe(client, "Recebe/pub_do_esp/Dados", 1);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                msg_new = "Desconectado\r\n";
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
                //TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);
                if(topic.equals("Recebe/pub_do_esp/Dados")) {

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
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

            Toast.makeText(getApplicationContext(), "Configuração Salvas:)", Toast.LENGTH_SHORT).show();

            Intent trocatela=new Intent(Rotular.this, MainActivity.class);
            startActivity(trocatela);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ===========================================================
}