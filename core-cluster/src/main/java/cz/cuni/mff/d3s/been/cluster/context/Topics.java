package cz.cuni.mff.d3s.been.cluster.context;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;
import cz.cuni.mff.d3s.been.cluster.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for topics-related handling.
 * 
 * @author Martin Sixta
 */
@Component
public class Topics {

	/** BEEN cluster connection */
	@Autowired
	private HazelcastInstance hazelcastInstance;

	/**
	 * Returns a Hazelcast topic with the specified name. If such a topic does not
	 * exist, it will be created.
	 * 
	 * @param name
	 *          name of the topic
	 * @param <E>
	 *          type of the topic items
	 * @return the topic with the specified name
	 */
	public <E> ITopic<E> getTopic(String name) {
		return hazelcastInstance.getTopic(name);
	}

	/**
	 * Published the specified message to the Hazelcast topic with the specified
	 * name.
	 * 
	 * @param name
	 *          name of the topic
	 * @param message
	 *          message to publish
	 * @param <E>
	 *          type of the topic items
	 */
	public <E> void publish(String name, E message) {
		ITopic<E> topic = getTopic(name);
		topic.publish(message);
	}

	/**
	 * Published the specified message to the global Hazelcast topic.
	 * 
	 * @param message
	 *          message to publish
	 * @param <E>
	 *          type of the topic items
	 */
	public <E> void publishInGlobalTopic(E message) {
		publish(Names.BEEN_GLOBAL_TOPIC, message);
	}

}
