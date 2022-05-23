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

import static team.space.utils.Constants.QUEUE_ON_USER_SAVED;

public class AMQP {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
   public  static Set<Thread> threads  = new HashSet<>();

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


    public void setToListenToCapture(final String queueName) {
        var chck = threads.stream().anyMatch(th-> th.getName().equals(queueName));
        if(chck)
        {
            System.out.println("already listening to queue");
            return;
        }
        var t = new Thread( () -> {
            try {
                channel.queueDeclare(queueName, false, false, false, null);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "'");
                };
                channel.basicConsume(QUEUE_ON_USER_SAVED, true, deliverCallback, consumerTag -> {
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
