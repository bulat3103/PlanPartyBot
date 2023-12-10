package com.example.eventbot.configs;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.example.eventbot.bot.NotificationListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class MessageQueueConfig {
    @Value("${yandex.mq.url}")
    private String url;
    @Value("${yandex.mq.region}")
    private String region;
    @Value("${yandex.mq.name}")
    private String name;

    @Bean
    public SQSConnectionFactory getSQSConnectionFactory() {
        return new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(region)
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
        );
    }

    @Bean
    public SQSConnection sqsConnection(SQSConnectionFactory connectionFactory) throws JMSException {
        return connectionFactory.createConnection();
    }

    @Bean
    public Session getSession(SQSConnection connection) throws JMSException {
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        if( !client.queueExists(name) ) {
            client.createQueue(name);
        }
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public Queue getQueue(Session session) throws JMSException {
        return session.createQueue(name);
    }

    @Bean
    public MessageProducer messageProducer(Session session, Queue queue) throws JMSException {
        return session.createProducer(queue);
    }

    @Bean
    public MessageConsumer messageConsumer(Session session, Queue queue, NotificationListener listener, SQSConnection connection) throws JMSException {
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(listener);
        connection.start();
        return consumer;
    }
}
