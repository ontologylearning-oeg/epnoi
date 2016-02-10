package org.epnoi.learner.relations.patterns.syntactic;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

import java.util.logging.Logger;

public class SyntacticRelationalModelCreator {
	private static final Logger logger = Logger
			.getLogger(SyntacticRelationalModelCreator.class.getName());
	private RelationalPatternsModelCreationParameters parameters;

	String relationalSentencesCorpusURI;
	private RelationalSentencesCorpus relationalSentencesCorpus;
	private RelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;

	private RelationalPatternsModelBuilder modelBuilder;
	private RelationalPatternsModel model;
	MockUpRelationalSentencesCorpusCreator relationSentencesCorpusCreator;
	private boolean store;
	private boolean verbose;
	private String path;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(LearningHelper helper, RelationalPatternsModelCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the SyntacticRealationalModelCreator with the following parameters");
		logger.info(parameters.toString());
		this.parameters = parameters;
		String relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(SyntacticRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER);
		this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
		this.patternsCorpusCreator.init(new SyntacticRelationalPatternGenerator());

		/*
		 * FUTURE RelationalSentencesCorpus relationalSentencesCorpus =
		 * (RelationalSentencesCorpus) this.core
		 * .getInformationHandler().get(relationalSentencesCorpusURI,
		 * RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		 */

		this.relationSentencesCorpusCreator = new MockUpRelationalSentencesCorpusCreator();

		try {
			relationSentencesCorpusCreator.init(helper);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		modelBuilder = new SyntacticRelationalPatternsModelBuilder(parameters);

		this.path = (String) parameters
				.getParameterValue(SyntacticRelationalModelCreationParameters.MODEL_PATH_PARAMETERS);

		this.store = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);
	}

	public void create() {

		_createCorpora();
		this.model = _createModel();
		if (this.verbose) {
			this.model.show();
		}
		if (this.store) {

			_storeModel();

		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------

	private void _storeModel() {
		logger.info("Storing the model at " + path);

		try {
			RelationalPatternsModelSerializer.serialize(path, model);

		} catch (EpnoiResourceAccessException e) {
			logger.severe("There was a problem trying to serialize the BigramSoftPatternModel at "
					+ path);
			logger.severe(e.getMessage());
		}

	}
	
	//------------------------------------------------------------------------------------------------------------------------

	private void _createCorpora() {
		this.relationalSentencesCorpus = relationSentencesCorpusCreator
				.createTestCorpus();

		if (relationalSentencesCorpus == null) {
			logger.severe("The RelationalSentecesCorpus was null, the model cannot be created!");
		} else {

			logger.info("The RelationalSencentcesCorpus has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			logger.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	private RelationalPatternsModel _createModel() {
		long startingTime = System.currentTimeMillis();
		logger.info("Adding all the patterns to the model");
		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			this.modelBuilder.addPattern(pattern);
		}
		logger.info("Building the model");
		RelationalPatternsModel model = this.modelBuilder.build();
		long totalTime = startingTime - System.currentTimeMillis();
		logger.info("It took " + Math.abs(totalTime) + " ms to build the model");
		return model;
	}

}
