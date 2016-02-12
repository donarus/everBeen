package cz.cuni.mff.d3s.been.cluster.action;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBException;

import cz.cuni.mff.d3s.been.cluster.context.Benchmarks;
import org.xml.sax.SAXException;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.benchmark.BenchmarkEntry;
import cz.cuni.mff.d3s.been.core.benchmark.Storage;
import cz.cuni.mff.d3s.been.core.jaxb.BindingParser;
import cz.cuni.mff.d3s.been.core.jaxb.ConvertorException;
import cz.cuni.mff.d3s.been.core.jaxb.XSD;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.socketworks.twoway.Request;

/**
 * An {@link Action} that handles a request for persisting the benchmark
 * key-value storage.
 * 
 * @author Kuba Brecka
 */
public class StoragePersistAction implements Action {

	/** the request to handle */
	private final Request request;
	private Benchmarks benchmarks;

	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 */
	public StoragePersistAction(Request request, Benchmarks benchmarks) {
		this.request = request;
		this.benchmarks = benchmarks;
	}

	@Override
	public Reply handle() {
		String benchmarkId = this.request.getSelector();
		Storage storage = storageFromXml(this.request.getValue());

		BenchmarkEntry entry = benchmarks.get(benchmarkId);
		entry.setStorage(storage);
		benchmarks.put(entry);

		return Replies.createOkReply("");
	}

	/**
	 * Deserializes the key-value storage from an XML string.
	 * 
	 * @param xml
	 *          the XML string to deserialize
	 * @return the deserialized key-value storage
	 */
	public static Storage storageFromXml(String xml) {
		Storage s;
		try {
			BindingParser<Storage> bindingComposer = XSD.STORAGE.createParser(Storage.class);
			s = bindingComposer.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (ConvertorException | JAXBException | SAXException e) {
			throw new IllegalArgumentException("Cannot parse Storage xml", e);
		}

		return s;
	}

}
