package srx.awesome.code.mqtt.client;

import org.eclipse.paho.client.mqttv3.*;

public class PahoTest {
    static String topic        = "MQTT Examples";
    static String content      = "Hello MQTT!!!!!";
    static int qos             = 2;
    static String broker       = "tcp://iot.eclipse.org:1883";
    static String clientId     = "JavaSample";
    static String userName     = "admin";
    static String passWord     = "password";
    public static void main(String[] args) {
        try {
            //创建客户端
            MqttClient sampleClient = new MqttClient(broker, clientId, null);
            //配置回调函数
            sampleClient.setCallback(new MyMqttCallback());

            //创建连接选择
            MqttConnectOptions connOpts = getMqttConnectOptions(userName, passWord);
            System.out.println("Connecting to broker: "+broker);
            //创建服务连接
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            //关注主题，质量等级为2
            sampleClient.subscribe(topic, qos);

            //在另一个线程中发送消息
            Thread thread = new Thread(() -> {
                try {
                    publishMsg(topic, content, qos, sampleClient);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            thread.join();
            //断开服务连接
            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private static void publishMsg(String topic, String content, int qos, MqttClient sampleClient) throws MqttException {
        //循环发送10次消息
        for (int times =0 ;times<10; times++) {
            System.out.println(String.format("%d time Publishing message: %s", times, content));
            //创建消息内容
            MqttMessage message = new MqttMessage(content.getBytes());
            //设置质量级别
            message.setQos(qos);
            //发送消息
            sampleClient.publish(topic, message);
            //System.out.println("Message published");
        }
    }

    private static MqttConnectOptions getMqttConnectOptions(String userName, String passWord) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        connOpts.setAutomaticReconnect(true);
        // 设置连接超时时间, 单位为秒,默认30
        connOpts.setConnectionTimeout(30);
        // 设置会话心跳时间,单位为秒,默认20
        connOpts.setKeepAliveInterval(20);
        return connOpts;
    }

}
