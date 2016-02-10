package org.epnoi.learner.relations.corpus;

import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;

import java.util.logging.Logger;

public class RelationalSentencesCorpusViewer {
	private static final Logger logger = Logger
			.getLogger(RelationalSentencesCorpusViewer.class.getName());

	private String relationalSentencesCorpusURI;
	private RelationalSentencesCorpus relationalSentencesCorpus;

	public void init(String relationalSentencesCorpusURI) {
		this.relationalSentencesCorpusURI = relationalSentencesCorpusURI;
	}

	private RelationalSentencesCorpus _retrieveRelationalSentencesCorpus() {

		// TODO
		logger.severe("pending to implement by using UDM");
//		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core.getInformationHandler().get(relationalSentencesCorpusURI,RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

		if (relationalSentencesCorpus == null) {
			logger.info("The relational sentences corpus "
					+ relationalSentencesCorpusURI + "could not be found");

		} else {

			logger.info("The relational sentences corpus has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");

		}

		return relationalSentencesCorpus;
	}

	private void show() {
		this.relationalSentencesCorpus = _retrieveRelationalSentencesCorpus();
		for (RelationalSentence relationalSentence : this.relationalSentencesCorpus
				.getSentences()) {
			_showRelationalSentenceInfo(relationalSentence);
		}
	}

	private void _showRelationalSentenceInfo(
			RelationalSentence relationalSencente) {

		String source = relationalSencente
				.getSentence()
				.subSequence(
						relationalSencente.getSource().getStart().intValue(),
						relationalSencente.getSource().getEnd().intValue())
				.toString();

		String target = relationalSencente
				.getSentence()
				.subSequence(
						relationalSencente.getTarget().getStart().intValue(),
						relationalSencente.getTarget().getEnd().intValue())
				.toString();

		System.out.println("[" + source + "," + target + "]>"
				+ relationalSencente.getSentence());

	}

	public static void main(String[] args) {

		RelationalSentencesCorpusViewer viewer = new RelationalSentencesCorpusViewer();
		viewer.init("http://drInventorFirstReview/relationalSentencesCorpus");
		viewer.show();

	}
}
