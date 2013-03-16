package cz.cuni.mff.d3s.been.core;

import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * @author Martin Sixta
 */
public class TopicUtils {

	private ClusterContext clusterCtx;

	TopicUtils(ClusterContext clusterCtx) {
		// package private visibility prevents out-of-package instantiation	
		this.clusterCtx = clusterCtx;
	}

	public <E> ITopic<E> getTopic(String name) {
		return clusterCtx.getTopic(name);
	}

	public <E> void publish(String name, E message) {
		ITopic<E> topic = getTopic(name);
		topic.publish(message);
	}

	public <E> void addListener(String name, MessageListener<E> listener) {
		ITopic<E> topic = getTopic(name);
		topic.addMessageListener(listener);
	}

	public <E> void removeListener(String name, MessageListener<E> listener) {
		ITopic<E> topic = getTopic(name);
		topic.removeMessageListener(listener);
	}

}