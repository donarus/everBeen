package cz.cuni.mff.d3s.been.cluster.action;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBException;

import cz.cuni.mff.d3s.been.cluster.context.Benchmarks;
import cz.cuni.mff.d3s.been.cluster.context.TaskContexts;
import org.xml.sax.SAXException;

import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.benchmark.BenchmarkEntry;
import cz.cuni.mff.d3s.been.core.jaxb.BindingParser;
import cz.cuni.mff.d3s.been.core.jaxb.ConvertorException;
import cz.cuni.mff.d3s.been.core.jaxb.XSD;
import cz.cuni.mff.d3s.been.core.task.TaskContextDescriptor;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.socketworks.twoway.Request;

/**
 * An {@link Action} that handles a request for submitting a new task context
 * within a benchmark.
 * 
 * @author Kuba Brecka
 */
public class ContextSubmitAction implements Action {

	/** the request to handle */
	private final Request request;

	private final TaskContexts taskContexts;
	private final Benchmarks benchmarks;

	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 */
	public ContextSubmitAction(Request request, TaskContexts taskContexts, Benchmarks benchmarks) {
		this.request = request;
		this.taskContexts = taskContexts;
		this.benchmarks = benchmarks;
	}

	@Override
	public Reply handle() {
		String benchmarkId = request.getSelector();
		String serializedDescriptor = request.getValue();

		TaskContextDescriptor taskContextDescriptor;
		try {
			BindingParser<TaskContextDescriptor> bindingComposer = XSD.TASK_CONTEXT_DESCRIPTOR.createParser(TaskContextDescriptor.class);
			taskContextDescriptor = bindingComposer.parse(new ByteArrayInputStream(serializedDescriptor.getBytes()));
		} catch (ConvertorException | JAXBException | SAXException e) {
			return Replies.createErrorReply("Cannot deserialize task context descriptor.");
		}

		String taskContextEntryId = taskContexts.submit(taskContextDescriptor, benchmarkId);

		IMap<String, BenchmarkEntry> benchmarksMap = benchmarks.getBenchmarksMap();
		benchmarksMap.lock(benchmarkId);
		try {
			BenchmarkEntry entry = benchmarksMap.get(benchmarkId);
			entry.setGeneratedContextCount(entry.getGeneratedContextCount() + 1);
			benchmarksMap.put(benchmarkId, entry);
		} finally {
			benchmarksMap.unlock(benchmarkId);
		}

		return Replies.createOkReply(taskContextEntryId);
	}

}
