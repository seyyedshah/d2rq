package de.fuberlin.wiwiss.d2rq.nodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.db.expr.Expression;
import org.d2rq.db.schema.ColumnName;
import org.d2rq.values.BlankNodeIDValueMaker;
import org.d2rq.values.TemplateValueMaker;
import org.d2rq.values.Translator;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.core.Var;


public class DetermineNodeType implements NodeSetFilter {

	private final Log logger = LogFactory.getLog(DetermineNodeType.class);
	
	private boolean limitedToURIs       = false;
	private boolean limitedToBlankNodes = false;
	private boolean limitedToLiterals   = false;
	
	private RDFDatatype datatype = null;
	private String      language = null;
	
	public boolean isLimittedToURIs() {
		return limitedToURIs;
	}
		
	public RDFDatatype getDatatype() {
		return datatype;
	}
		
	public String getLanguage() {
		return language;
	}

	public boolean isLimittedToBlankNodes() {
		return limitedToBlankNodes;
	}
	
	public boolean isLimittedToLiterals() {
		return limitedToLiterals;
	}
	
	public void limitTo(Node node) {
		logger.debug("limitting to " + node);

		if (node.isURI())
			limitedToURIs = true;
		else if (node.isLiteral())
			limitedToLiterals = true;
		else if (Var.isBlankNodeVar(node))
			limitedToBlankNodes = true;
	}

	public void limitToBlankNodes() {
		logger.debug("limitting to blank nodes");

		limitedToBlankNodes = true;
	}

	public void limitToEmptySet() {
		// FIXME DetermineNodeType.limitToEmptySet()
		logger.warn("TODO DetermineNodeType.limitToEmptySet()");
	}

	public void limitToLiterals(String language, RDFDatatype datatype) {
		logger.debug("limitting to literals");
		
		limitedToLiterals = true;
		this.datatype = datatype;
		this.language = language;
	}

	public void limitToURIs() {
		logger.debug("limitting to URIs");
		
		limitedToURIs = true;
	}

	// FIXME Implement!
	public void limitValues(String constant) {
		logger.warn("TODO DetermineNodeType.limitValues() " + constant);
	}

	// FIXME Implement!
	public void limitValuesToColumn(ColumnName column) {
		logger.warn("TODO DetermineNodeType.limitValuesToColumn() " + column);
	}

	// FIXME Implement!
	public void limitValuesToBlankNodeID(BlankNodeIDValueMaker id) {
		logger.warn("TODO DetermineNodeType.limitValuesToBlankNodeID() " + id);
	}

	// FIXME Implement!
	public void limitValuesToExpression(Expression expression) {
		logger.warn("TODO DetermineNodeType.limitValuesToExpression() " + expression);
	}

	// FIXME Implement!
	public void limitValuesToPattern(TemplateValueMaker pattern) {
		logger.warn("TODO DetermineNodeType.limitValuesToPattern() " + pattern);
	}

	// FIXME Implement!
	public void setUsesTranslator(Translator translator) {
		if (translator != Translator.IDENTITY) {
			logger.warn("TODO DetermineNodeType.setUsesTranslator() " + translator);
		}
	}
}
