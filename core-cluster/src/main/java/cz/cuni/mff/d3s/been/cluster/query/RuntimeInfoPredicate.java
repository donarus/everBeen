package cz.cuni.mff.d3s.been.cluster.query;

import com.hazelcast.query.Predicate;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;
import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Selects Host Runtimes based on an XPath expression.
 * 
 * @author Kuba Brecka
 */
public class RuntimeInfoPredicate implements Predicate<String, RuntimeInfo> {

	/** logging */
	private static final Logger log = LoggerFactory.getLogger(RuntimeInfoPredicate.class);

	/** the XPath expression */
	private final String xpath;

	/**
	 * Creates RuntimeInfoPredicate.
	 * 
	 * @param xpath
	 *          expression to be used to filter out Host Runtimes
	 */
	public RuntimeInfoPredicate(String xpath) {
		this.xpath = xpath;
	}

	@Override
	public boolean apply(Map.Entry<String, RuntimeInfo> mapEntry) {
		RuntimeInfo info = mapEntry.getValue();

		JXPathContext context = JXPathContext.newContext(info);
		List list = context.selectNodes(".[" + xpath + "]");
		return (list != null) && (list.size() > 0);
	}

}
