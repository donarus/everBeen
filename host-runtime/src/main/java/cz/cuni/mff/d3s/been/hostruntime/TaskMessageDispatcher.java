package cz.cuni.mff.d3s.been.hostruntime;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.d3s.been.core.TaskMessageType;
import cz.cuni.mff.d3s.been.mq.*;

/**
 * Purpose of this service is to allow communication between HostRuntime and
 * running tasks.
 * 
 * Usage of this class
 * 
 * @author Tadeáš Palusga
 * 
 */
public class TaskMessageDispatcher {

	private static final Logger log = LoggerFactory.getLogger(TaskMessageDispatcher.class);

	/**
	 * THIS MESSAGE STRING IS FOR PRIVATE USE ONLY. Purpose of this message is to
	 * kill the receiver immediately.
	 */
	private static final String STOP_MESSAGE = "XX1456123_STOP_RECEIVER_MESSAGE";

	/**
	 * identification of not bounded port
	 */
	private static final int NOT_BOUND_PORT = -1;

	/**
	 * Random generated port on which the receiver is running
	 */
	private int receiverPort = NOT_BOUND_PORT;

	/**
	 * Message queue listening for message from tasks.
	 */
	IMessageQueue<String> taskMQ;

	/**
	 * Receiver of task messages.
	 */
	IMessageReceiver<String> receiver;

	/**
	 * Registered message listeners (key is messageType, value is listener itself)
	 */
	private Map<TaskMessageType, MessageListener> listeners = new HashMap<>();

	/**
	 * Starts new receiver thread if not already running.
	 */
	public void start() throws MessagingException {
		taskMQ = Messaging.createTcpQueue("localhost");
		receiver = taskMQ.getReceiver();

		receiverPort = receiver.getPort();

		new Thread() {
			@Override
			public void run() {
				boolean run = true;
				while (run) {
					String message;
					try {
						message = receiver.receive();
						log.debug(message);

						if (STOP_MESSAGE.equals(message)) {
							run = false;
						} else {
							processMessage(message);
						}
					} catch (MessagingException e) {
						log.error("Cannot receive message", e);
					}

				}
				synchronized (TaskMessageDispatcher.this) {
					taskMQ.terminate();
					taskMQ = null;
					receiver = null;
					receiverPort = NOT_BOUND_PORT;
				}
			}
		}.start();

		Runtime.getRuntime().addShutdownHook(createShutdownHook());

	}

	private Thread createShutdownHook() {
		return new Thread() {
			@Override
			public void run() {
				TaskMessageDispatcher.this.terminate();
			}
		};
	}

	/**
	 * Processes given message. Uses applicable registered message listener to
	 * process the message.
	 * 
	 * @param message
	 *          to be processed
	 */
	private void processMessage(String message) {
		String messageType = getMessageType(message);
		getMessageBody(message);
		MessageListener listener = listeners.get(messageType);
		if (listener != null) {
			listener.processMessage(message);
		} else {
			// FIXME possibility to resent message into HC cluster 
		}
	}

	/**
	 * Returns message type of the given message.
	 * 
	 * @param message
	 * @return message type
	 */
	private String getMessageType(String message) {
		// TODO ...

		return null;
	}

	/**
	 * Returns message type of the given message.
	 * 
	 * @param message
	 * @return message body
	 */
	private String getMessageBody(String message) {
		// TODO ...

		return null;
	}

	/**
	 * Terminates running receiver thread and underlying connection. Does nothing
	 * if receiver thread is not running.
	 */
	public void terminate() {
		synchronized (this) {
			if (taskMQ != null) {
				IMessageSender<String> sender = null;
				try {
					sender = taskMQ.createSender();
				} catch (MessagingException e) {
					e.printStackTrace();
				} finally {
					if (sender != null) {
						sender.close();
					}
				}
			}
		}
	}

	/**
	 * Registers given message listener. If listener of the same type (type
	 * property of listener) is already registered, it will be replaced.
	 * 
	 * @param listener
	 *          to be registered
	 * @throws NullPointerException
	 *           if the listener itself is NULL or type of the listener is NULL
	 */
	public void addMessageListener(MessageListener listener) throws NullPointerException {
		if (listener.getMessageType() == null) {
			throw new NullPointerException("Message type on listener can't be NULL");
		}
		listeners.put(listener.getMessageType(), listener);
	}

	/**
	 * Unregisters listener of given type.
	 * 
	 * @param listenerType
	 *          type of the listener to be removed
	 */
	public void removeMessageListener(TaskMessageType listenerType) {
		listeners.remove(listenerType);
	}

	/**
	 * Returns port on which the running receiver has been started
	 * 
	 * @return port on which the receiver is running
	 */
	public int getReceiverPort() {
		return receiverPort;
	}

	/**
	 * 
	 * Implementations of this listener are used in {@link TaskMessageDispatcher}
	 * and are responsible for processing of received messages.
	 * 
	 * @author Tadeáš Palusga
	 * 
	 */
	public static interface MessageListener {
		/**
		 * This method should {@link TaskMessageType} for which the listener is
		 * designed for
		 * 
		 * @return message type of the listener
		 */
		TaskMessageType getMessageType();

		/**
		 * Implement process logic in body of this method.
		 * 
		 * @param message
		 */
		void processMessage(String message);
	}

	/**
	 * This exception is thrown only by methods of {@link TaskMessageDispatcher}.
	 * 
	 * @author Tadeáš Palusga
	 * 
	 */
	public static class TaskLogProcessorException extends Exception {
		/**
		 * SERIAL VERSION UID
		 */
		private static final long serialVersionUID = 1L;

		private TaskLogProcessorException(String message) {
			//cctor is private because we don't want to allow external instantiation
			super(message);
		}
	}
}
