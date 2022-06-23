package works.weave.socks.queuemaster.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import works.weave.socks.queuemaster.ShippingTaskHandler;

@Configuration
public class ShippingConsumerConfiguration 
{
	protected final String queueName = "shipping-task";

	@Autowired
	private RabbitMqConfiguration rabbitMqConfig;

    @Autowired
    private ShippingTaskHandler shippingTaskHandler;

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(rabbitMqConfig.connectionFactory());
		template.setDefaultReceiveQueue(this.queueName);
        template.setMessageConverter(rabbitMqConfig.jsonMessageConverter());
		return template;
	}

    @Bean
	public Queue queueName() {
		return new Queue(this.queueName, false);
	}

	@Bean
	public SimpleMessageListenerContainer listenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitMqConfig.connectionFactory());
		container.setQueueNames(this.queueName);
		container.setMessageListener(messageListenerAdapter());

		return container;
	}

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(shippingTaskHandler, rabbitMqConfig.jsonMessageConverter());
    }
}
