package cz.cuni.mff.d3s.been.cluster.query;

import com.hazelcast.query.Predicate;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfos;
import cz.cuni.mff.d3s.been.core.task.TaskExclusivity;
import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static cz.cuni.mff.d3s.been.core.task.TaskExclusivity.NON_EXCLUSIVE;

/**
 *
 * Predicate for filtering RuntimeInfo based on XPath expression.
 *
 * @author Martin Sixta
 */
public final class XPathPredicate implements Predicate<String, RuntimeInfo> {

	private static final Logger log = LoggerFactory.getLogger(XPathPredicate.class);

	private final String xpath;
	private final TaskExclusivity taskExclusivity;
	private final String contextId;

	/**
	 * Creates new XPathPredicate
	 *
	 * @param contextId
	 *          ID of the context to take into account while filtering
	 * @param xpath
	 *          XPath expression to filter out Host Runtimes
	 * @param taskExclusivity
	 *          requested exclusivity of the task
	 */
	public XPathPredicate(String contextId, String xpath, TaskExclusivity taskExclusivity) {
		this.contextId = contextId;
		this.xpath = xpath;
		this.taskExclusivity = taskExclusivity;
	}

	@Override
	public boolean apply(Map.Entry<String, RuntimeInfo> mapEntry) {
		RuntimeInfo info = mapEntry.getValue();

		if (info.getExclusivity() == null) {
			// workaround for JAXB not setting default value on elements
			info.setExclusivity(NON_EXCLUSIVE.toString());
		}

		// Runtime Overload conditions
		if (RuntimeInfos.isMaxTasksReached(info)) {
			return false;
		}

		if (RuntimeInfos.isMemoryThresholdReached(info)) {
			return false;
		}

		TaskExclusivity runtimeExclusivity;

		try {
			runtimeExclusivity = TaskExclusivity.valueOf(info.getExclusivity());
		} catch (IllegalArgumentException e) {
			// something fishy is going on, just skip this host runtime
			runtimeExclusivity = TaskExclusivity.EXCLUSIVE;
		}

		switch (runtimeExclusivity) {
			case NON_EXCLUSIVE:
				boolean isExclusive = (taskExclusivity != NON_EXCLUSIVE);
				boolean hasTasks = (info.getTaskCount() > 0);
				if (isExclusive && hasTasks) {
					return false;
				}
				break;
			case CONTEXT_EXCLUSIVE:
				if (!contextId.equals(info.getExclusiveId())) {
					return false; // different context
				}
				break;
			case EXCLUSIVE:
				return false;
		}

		JXPathContext context = JXPathContext.newContext(info);
		List list = context.selectNodes(".[" + xpath + "]");
		return (list != null) && (list.size() > 0);
	}
}
