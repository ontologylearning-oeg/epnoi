package org.epnoi.uia.learner.relations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.OntologyLearningWorkflowParameters;
import org.epnoi.uia.learner.knowledgebase.KnowledgeBase;
import org.epnoi.uia.learner.knowledgebase.KnowledgeBaseFactory;
import org.epnoi.uia.learner.knowledgebase.KnowledgeBaseParameters;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.learner.knowledgebase.wordnet.WordNetHandlerParameters;
import org.epnoi.uia.learner.terms.TermsRetriever;
import org.epnoi.uia.learner.terms.TermsTable;

/**
 * 
 * @author
 *
 */

public class RelationsHandler {
	private static final Logger logger = Logger
			.getLogger(RelationsHandler.class.getName());
	private Core core;
	private Map<String, RelationsTable> relationsTable;// Map to store the
														// RelationsTable of
														// each domain
	private Map<String, TermsTable> termsTable;// Map to store the TermsTable of
												// each domain
	private KnowledgeBase knowledgeBase;// The curated Knowledge Base
	private RelationsHandlerParameters parameters;

	private List<Domain> consideredDomains;
	private Set<String> retrievedDomains;// Set of the domains which
											// Realtions/TermsTables have been
											// successfully retrieved

	// ---------------------------------------------------------------------------------------------------------------------

	public RelationsHandler() {
		this.relationsTable = new HashMap<>();
		this.termsTable = new HashMap<>();
		this.retrievedDomains = new HashSet<>();
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void init(Core core, RelationsHandlerParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the RelationsHandler with the following parameters:"
				+ parameters);
		this.parameters = parameters;
		this.core = core;

		this.consideredDomains = (List<Domain>) this.parameters
				.getParameterValue(RelationsHandlerParameters.CONSIDERED_DOMAINS);

		KnowledgeBaseParameters knowledgeBaseParameters = (KnowledgeBaseParameters) this.parameters
				.getParameterValue(RelationsHandlerParameters.KNOWLEDGE_BASE_PARAMETERS);

		KnowledgeBaseFactory knowledgeBaseCreator = new KnowledgeBaseFactory();
		try {
			knowledgeBaseCreator.init(core, knowledgeBaseParameters);
		} catch (EpnoiInitializationException e) {
			logger.severe("The KnowledgeBase couldn't be initialized");
			e.printStackTrace();

		}
		this.knowledgeBase = knowledgeBaseCreator.build();
		_initDomainsRelationsTables();

	}

	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * Method that retrieves for each considered domain the RelationsTable and
	 * TermsTable. If there exists any problem, the domain is
	 */
	private void _initDomainsRelationsTables() {
		TermsRetriever termsRetriever = new TermsRetriever(core);
		RelationsRetriever relationsRetriever = new RelationsRetriever(core);
		if (consideredDomains == null) {
			logger.info("The consideredDomains parameter was not set");
		} else if (consideredDomains.size() == 0) {
			logger.info("The consideredDomains parameter was empty");
		} else {
			for (Domain domain : this.consideredDomains) {
				logger.info("Retrieving infomration from the domain "
						+ domain.getLabel());
				try {
					TermsTable termsTable = termsRetriever.retrieve(domain);
					this.termsTable.put(domain.getURI(), termsTable);
					RelationsTable relationsTable = relationsRetriever
							.retrieve(domain);
					this.relationsTable.put(domain.getURI(), relationsTable);
					this.retrievedDomains.add(domain.getURI());
				} catch (Exception e) {
					logger.info("There was a problem retrieving the domain "
							+ domain.getURI() + " Terms/RelationsTable");
					e.printStackTrace();
					this.termsTable.put(domain.getURI(), null);
					this.relationsTable.put(domain.getURI(), null);
				}
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------
	/*
	 * 
	 */

	public List<Relation> getRelationsBySurfaceForm(
			String sourceTermSurfaceForm, String domain,
			double expansionProbabilityThreshold) {
		List<Relation> relations = new ArrayList<>();
		// First we retrieve the relations that we can find in the knowledge
		// base for the source term

		for (String targetTerm : this.knowledgeBase
				.getHypernyms(sourceTermSurfaceForm)) {

			relations
					.add(Relation.buildKnowledgeBaseRelation(
							sourceTermSurfaceForm, targetTerm,
							RelationHelper.HYPERNYM));
		}
		// If we have been able to retrieve the Terms/RelationsTable associated
		// with the domain, we also return these relations
		if (this.retrievedDomains.contains(domain)) {
			Term term = this.termsTable.get(domain).getTermBySurfaceForm(
					sourceTermSurfaceForm);

			// Afterthat we add those relations for such source term in the
			// relations table
			relations.addAll(relationsTable.get(domain).getRelations(
					term.getURI(), expansionProbabilityThreshold));
		}
		return relations;
	}

	// ---------------------------------------------------------------------------------------------------------------------
	/*
	 * 
	 */

	public List<Relation> getRelationsByURI(String sourceTermURI,
			String domain, double expansionProbabilityThreshold) {
		List<Relation> relations = new ArrayList<>();
		// First we retrieve the relations that we can find in the knowledge
		// base for the source term
		/*
		 * FIX LATER: Integrate surface forms-> uris and viceversa in knowledge
		 * base for (String targetTerm :
		 * this.knowledgeBase.getHypernyms(sourceTermURI)) {
		 * 
		 * relations.add(Relation.buildKnowledgeBaseRelation(sourceTermURI,
		 * targetTerm, RelationHelper.HYPERNYM)); }
		 */
		// If we have been able to retrieve the Terms/RelationsTable associated
		// with the domain, we also return these relations
		if (this.retrievedDomains.contains(domain)) {

			// Afterthat we add those relations for such source term in the
			// relations table
			relations.addAll(relationsTable.get(domain).getRelations(
					sourceTermURI, expansionProbabilityThreshold));
		}
		return relations;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	/**
	 * Method used to determine if there exists a relationship of an specific
	 * type on a given domain. It test also if the relation exists in the
	 * Knowledge Base, as we consider it domain-independent
	 * 
	 * @param sourceTermSurfaceForm
	 * @param targetTermSurfaceForm
	 * @param type
	 * @param domain
	 * @return
	 */

	public Double areRelated(String sourceTermSurfaceForm,
			String targetTermSurfaceForm, String type, String domain) {
		Double existenceProbability = 0.;
		if (this.knowledgeBase.areRelated(sourceTermSurfaceForm,
				targetTermSurfaceForm)) {
			existenceProbability = 1.;
		} else {
			if (this.relationsTable.get(domain) != null) {

				Term sourceTerm = this.termsTable.get(domain)
						.getTermBySurfaceForm(sourceTermSurfaceForm);
				Term targetTerm = this.termsTable.get(domain)
						.getTermBySurfaceForm(targetTermSurfaceForm);
				boolean found = false;

				Iterator<Relation> relationsIt = this.relationsTable
						.get(domain).getRelations(sourceTerm.getURI(), 0)
						.iterator();
				while (!found && relationsIt.hasNext()) {

					Relation relation = relationsIt.next();

					if (relation.getTarget().equals(targetTerm.getURI())) {
						existenceProbability = relation.getRelationhood();
					}

				}
			}

		}
		return existenceProbability;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the RelationsHandler test!");

		// Core initialization
		Core core = CoreUtility.getUIACore();

		String domainURI = "http://CGTestCorpus";

		Domain domain = null;

		if (core.getInformationHandler().contains(domainURI,
				RDFHelper.DOMAIN_CLASS)) {
			domain = (Domain) core.getInformationHandler().get(domainURI,
					RDFHelper.DOMAIN_CLASS);
		} else {
			domain = new Domain();
			domain.setLabel("CGTestCorpus");
			domain.setURI(domainURI);
			domain.setConsideredResource(RDFHelper.PAPER_CLASS);
		}

		List<Domain> consideredDomains = Arrays.asList(domain);

		String targetDomain = domainURI;

		Double hyperymExpansionMinimumThreshold = 0.7;
		Double hypernymExtractionMinimumThresohold = 0.1;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String hypernymsModelPath = "/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

		// First of all we initialize the KnowledgeBase
		KnowledgeBaseParameters knowledgeBaseParameters = new KnowledgeBaseParameters();
		WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

		WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
		wordnetParameters.setParameter(
				WordNetHandlerParameters.DICTIONARY_LOCATION,
				"/epnoi/epnoideployment/wordnet/dictWN3.1/");

		wikidataParameters.setParameter(
				WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				"http://wikidataView");
		wikidataParameters.setParameter(
				WikidataHandlerParameters.STORE_WIKIDATA_VIEW, false);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);
		wikidataParameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE,
				true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT, 10);
		wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				"/Users/rafita/Documents/workspace/wikidataParsingTest");

		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.WORDNET_PARAMETERS, wordnetParameters);

		knowledgeBaseParameters
				.setParameter(KnowledgeBaseParameters.WIKIDATA_PARAMETERS,
						wikidataParameters);

		RelationsHandlerParameters relationsHandlerParameters = new RelationsHandlerParameters();

		relationsHandlerParameters.setParameter(
				RelationsHandlerParameters.KNOWLEDGE_BASE_PARAMETERS,
				knowledgeBaseParameters);

		relationsHandlerParameters.setParameter(
				RelationsHandlerParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		OntologyLearningWorkflowParameters ontologyLearningParameters = new OntologyLearningWorkflowParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymExpansionMinimumThreshold);

		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						hyperymExpansionMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.HYPERNYM_MODEL_PATH,
				hypernymsModelPath);

		RelationsHandler relationsHandler = new RelationsHandler();
		try {

			relationsHandler.init(core, relationsHandlerParameters);

		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Are related? "
				+ relationsHandler.areRelated("dog", "canine",
						RelationHelper.HYPERNYM, "http://whatever"));
		System.out.println("Are related? "
				+ relationsHandler.areRelated("cat", "canine",
						RelationHelper.HYPERNYM, "http://whatever"));
		System.out.println("Ending the RelationsHandler Process!");
	}

}
