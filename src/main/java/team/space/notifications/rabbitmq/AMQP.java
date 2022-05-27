package team.space.notifications.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class AMQP {

   public  static Set<Thread> threads  = new HashSet<>();
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel = null;

    public AMQP() {
        factory.setHost("13.246.49.140");
        factory.setPassword("test");
        factory.setUsername("test");
        factory.setPort(5672);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
//onMessageSaved_fb875efe-603d-4065-8e7f-2bbdaa424027__to_fb875efe-603d-4065-8e7f-2bbdaa424027
//onMessageSaved_fb875efe-603d-4065-8e7f-2bbdaa424027_to_fb875efe-603d-4065-8e7f-2bbdaa424027

    public void setToListenToCapture(final String queueName) {
        var chck = threads.stream().anyMatch(th-> th.getName().equals(queueName));
        if(chck)
        {
            System.out.println("already listening to queue :: " + queueName);
            return;
        }
        var t = new Thread( () -> {
            try {





                channel.queueDeclare(queueName, false, false, false, null);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out
                            .println(" [x] Received '" + message + "'");
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                    System.out.println(" [x] Received 222");

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setName(queueName);
        t.start();
        threads.add(t);
    }



}
