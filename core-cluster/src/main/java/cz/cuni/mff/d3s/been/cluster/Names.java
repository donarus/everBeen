package cz.cuni.mff.d3s.been.cluster;

/**
 * 
 * @author Martin Sixta
 */
public class Names {

	// MAP NAMES
	/**
	 * In the map with this name should be stored information about all registered
	 * Host Runtimes.
	 */
	public static final String HOSTRUNTIMES_MAP_NAME = "BEEN_HOSTRUNTIMES";

	/**
	 * In the map with this name should be stored information about important
	 * services (Software Repository, Results Repository etc..).
	 */
	public static final String SERVICES_MAP_NAME = "BEEN_SERVICES";

	/**
	 * In the map with this name should be stored information about all Tasks
	 * (including their actual state).
	 */
	public static final String TASKS_MAP_NAME = "BEEN_MAP_TASKS";

	/**
	 * Map that contains named Task Descriptors.
	 */
	public static final String NAMED_TASK_DESCRIPTORS_MAP_NAME = "BEEN_MAP_NAMED_TASK_DESCRIPTORS";

	/**
	 * Map that contains named Task Context Descriptors.
	 */
	public static final String NAMED_TASK_CONTEXT_DESCRIPTORS_MAP_NAME = "BEEN_MAP_NAMED_TASK_CONTEXT_DESCRIPTORS";

	/**
	 * Map storing current task contexts.
	 */
	public static final String TASK_CONTEXTS_MAP_NAME = "BEEN_MAP_TASK_CONTEXTS";

	/**
	 * Map storing current benchmark entries.
	 */
	public static final String BENCHMARKS_MAP_NAME = "BEEN_MAP_BENCHMARKS";

	/**
	 * Name of the map of answers on persistence queries
	 */
	public static final String PERSISTENCE_QUERY_ANSWERS_MAP_NAME = "BEEN_PERSISTENCE_ANSWERS";

	/**
	 * Map with last log for a task.
	 */
	public static final String LOGS_TASK_MAP_NAME = "been.task.logs.task";

	/**
	 * Map with last log for a context.
	 */
	public static final String LOGS_CONTEXT_MAP_NAME = "been.task.logs.context";

	/**
	 * Map with last log for a benchmark.
	 */
	public static final String LOGS_BENCHMARK_MAP_NAME = "been.task.logs.benchmark";

	// QUEUE NAMES

	/**
	 * Name of the task action queue.
	 */
	public static final String ACTION_QUEUE_NAME = "been.hostruntime.actions";

	/**
	 * Name of the queue of persist requests
	 */
	public static final String PERSISTENCE_QUEUE_NAME = "been.persistence.entries";

	/**
	 * Name of the queue of queries to persistence layers
	 */
	public static final String PERSISTENCE_QUERY_QUEUE_NAME = "BEEN_PERSISTENCE_QUERIES";

	// MAP KEYS

	/**
	 * All benchmarks run in a special task context with this ID.
	 */
	public static final String BENCHMARKS_CONTEXT_ID = "00000000-0000-dead-1337-c0ffee00babe";

	public static final String BEEN_MAP_COMMAND_ENTRIES = "BEEN_MAP_COMMAND_ENTRIES";

	/** Name of the global Host Runtime Topic. */
	public static final String BEEN_GLOBAL_TOPIC = "BEEN_GLOBAL_TOPIC";
}
