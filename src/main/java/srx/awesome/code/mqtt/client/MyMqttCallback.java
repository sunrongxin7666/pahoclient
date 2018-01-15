package srx.awesome.code.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

class MyMqttCallback implements MqttCallback {
    public void connectionLost(Throwable arg0) {
        System.out.println("Connection Lost:"+arg0.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws MqttException {
        System.out.println(String.format("get Msg: %s from Topic: %s", mqttMessage, s));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
