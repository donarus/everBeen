package cz.cuni.mff.d3s.been.cluster;

/**
 *
 * Type of a node.
 *
 * There are several ways a node can connect to the cluster. The types are listed here.
 *
 * @author Martin Sixta
 */
public enum NodeType {
	/**
	 * The node is full member of a cluster.
	 *
	 * The node participate in data distribution among nodes. On such a node
	 * Task Manager can (and in fact must) run
	 */
	DATA,

	/**
	 * The node is member of a cluster but dows not owns any data partition.
	 */
	LITE
}
