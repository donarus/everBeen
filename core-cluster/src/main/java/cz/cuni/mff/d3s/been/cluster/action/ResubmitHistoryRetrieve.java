package cz.cuni.mff.d3s.been.cluster.action;

import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import cz.cuni.mff.d3s.been.cluster.context.Benchmarks;
import org.xml.sax.SAXException;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.benchmark.ResubmitHistory;
import cz.cuni.mff.d3s.been.core.jaxb.BindingComposer;
import cz.cuni.mff.d3s.been.core.jaxb.XSD;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.task.checkpoints.CheckpointRequest;

/**
 * An {@link Action} that handles a request for retrieving the resubmit history
 * for a benchmark.
 * 
 * @author Kuba Brecka
 */
public class ResubmitHistoryRetrieve implements Action {

	/** the request to handle */
	private final CheckpointRequest request;
	private Benchmarks benchmarks;


	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 */
	public ResubmitHistoryRetrieve(CheckpointRequest request, Benchmarks benchmarks) {
		this.request = request;
		this.benchmarks = benchmarks;
	}

	@Override
	public Reply handle() {
		String benchmarkId = this.request.getSelector();
		ResubmitHistory history = benchmarks.get(benchmarkId).getResubmitHistory();
		String s = resubmitHistoryToXml(history);
		return Replies.createOkReply(s);
	}

	/**
	 * Serializes the specified resubmit history into a XML string.
	 * 
	 * @param history
	 *          the resubmit history to serialize
	 * @return the serialized representation
	 */
	public static String resubmitHistoryToXml(ResubmitHistory history) {
		try {
			BindingComposer<ResubmitHistory> composer = XSD.STORAGE.createComposer(ResubmitHistory.class);
			StringWriter writer = new StringWriter();
			composer.compose(history, writer);
			return writer.toString();
		} catch (SAXException | JAXBException e) {
			throw new IllegalArgumentException("Cannot serialize Storage to XML", e);
		}
	}

}
