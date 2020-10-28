package de.saar.stl.semanticweb.owlapi;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

public class OwlApiMain {
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private IRI ontologyIri;
	
	public static void main(String [] args) {
		OwlApiMain owlApiMain = new OwlApiMain();
		try {
			owlApiMain.createOntology();
			owlApiMain.addClassAssertions();
			owlApiMain.createPropertyAssertions();
			owlApiMain.addSubClasses();
			owlApiMain.useEquivalentClasses();
			owlApiMain.useReasoner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createOntology() throws Exception {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		IRI ontologyIRI = IRI.create(MusicVocabulary.ONTOLOGY_IRI);
		OWLOntology ontology = manager.createOntology(ontologyIRI);
		System.out.println(ontology);
    }
	
	public void addClassAssertions() throws Exception {
		manager = OWLManager.createOWLOntologyManager();
		ontologyIri = IRI.create(MusicVocabulary.ONTOLOGY_IRI);
		OWLDataFactory dataFactory = manager.getOWLDataFactory();
		PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
		
		OWLClass saenger = dataFactory.getOWLClass(MusicVocabulary.CLASS_NAME_SINGER, prefixManager);
		OWLClass gitarrist = dataFactory.getOWLClass(MusicVocabulary.CLASS_NAME_GUITARIST, prefixManager);
		
		OWLNamedIndividual sting = dataFactory.getOWLNamedIndividual(MusicVocabulary.INDIVIDUAL_NAME_STING, prefixManager);
		OWLNamedIndividual miller = dataFactory.getOWLNamedIndividual(MusicVocabulary.INDIVIDUAL_NAME_MILLER, prefixManager);
		OWLNamedIndividual freddy = dataFactory.getOWLNamedIndividual(MusicVocabulary.INDIVIDUAL_NAME_FREDDY, prefixManager);
		
		OWLClassAssertionAxiom classAssertionSting = dataFactory.getOWLClassAssertionAxiom(saenger, sting);
		OWLClassAssertionAxiom classAssertionFreddy = dataFactory.getOWLClassAssertionAxiom(saenger, freddy);
		OWLClassAssertionAxiom classAssertionMiller = dataFactory.getOWLClassAssertionAxiom(gitarrist, miller);
		
		ontology = manager.createOntology(IRI.create(MusicVocabulary.ONTOLOGY_IRI));
		manager.addAxiom(ontology, classAssertionSting);
		manager.addAxiom(ontology, classAssertionMiller);
		manager.addAxiom(ontology, classAssertionFreddy);
		
        manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
        System.out.println(ontology);
    }	
	
	public void createPropertyAssertions() throws Exception {
        OWLDataFactory factory = manager.getOWLDataFactory();
        PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
        OWLNamedIndividual sting = factory.getOWLNamedIndividual(MusicVocabulary.INDIVIDUAL_NAME_STING, prefixManager);
        OWLNamedIndividual madAboutYou = factory.getOWLNamedIndividual(MusicVocabulary.INDIVUDUAL_NAME_MAD_ABOUT_YOU, prefixManager);
        OWLObjectProperty sings = factory.getOWLObjectProperty(MusicVocabulary.OBJECT_PROPERTY_SINGS, prefixManager);
        OWLObjectPropertyAssertionAxiom singsPropertyAssertion =
            factory.getOWLObjectPropertyAssertionAxiom(sings, sting, madAboutYou);
        manager.addAxiom(ontology, singsPropertyAssertion);

        OWLDataProperty hasNameDatatypeProperty = factory.getOWLDataProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_NAME, prefixManager);
        OWLDataPropertyAssertionAxiom hasNameDataPropertyAssertion =
                factory.getOWLDataPropertyAssertionAxiom(hasNameDatatypeProperty, sting, "Sting");
        
        OWLDatatype integerDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
        OWLLiteral hasAgeLiteral = factory.getOWLLiteral("71", integerDatatype);
        
        OWLDataProperty hasAgeDataProperty = factory.getOWLDataProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_AGE, prefixManager);
        OWLDataPropertyAssertionAxiom hasAgeDataPropertyAssertion =
            factory.getOWLDataPropertyAssertionAxiom(hasAgeDataProperty, sting, hasAgeLiteral);
        manager.addAxiom(ontology, hasAgeDataPropertyAssertion);
        manager.addAxiom(ontology, hasNameDataPropertyAssertion);
        
        manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
        System.out.println(ontology);
    }
	
	public void addSubClasses() throws OWLOntologyStorageException {
		OWLDataFactory factory = manager.getOWLDataFactory();
		PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
		OWLClass singer = factory.getOWLClass(MusicVocabulary.
				CLASS_NAME_SINGER, prefixManager);
		OWLClass guitarist = factory.getOWLClass(MusicVocabulary.
				CLASS_NAME_GUITARIST, prefixManager);
		OWLClass musician = factory.getOWLClass(MusicVocabulary.
				CLASS_NAME_MUSICIAN, prefixManager);
		
		OWLSubClassOfAxiom singerSubclassOfMusician = factory.
				getOWLSubClassOfAxiom(singer, musician);
		OWLSubClassOfAxiom guitaristSubclassOfMusician = factory.
				getOWLSubClassOfAxiom(guitarist, musician);
		
		manager.addAxiom(ontology, singerSubclassOfMusician);
		manager.addAxiom(ontology, guitaristSubclassOfMusician);
        for (OWLAxiom c : ontology.getAxioms()) {
            System.out.println(c.toString());
        }
        manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
	}
	
	public void makeOntologyInconsistent() throws OWLOntologyStorageException {
		OWLDataFactory factory = manager.getOWLDataFactory();
		PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
		OWLClass drummer = factory.getOWLClass(MusicVocabulary.CLASS_NAME_DRUMMER, prefixManager);
		OWLClass guitarist = factory.getOWLClass(MusicVocabulary.CLASS_NAME_GUITARIST, prefixManager);
		OWLDisjointClassesAxiom drummerDisjointGuitarist = factory.getOWLDisjointClassesAxiom(drummer, guitarist);
		
		OWLNamedIndividual miller = factory.getOWLNamedIndividual(MusicVocabulary.INDIVIDUAL_NAME_MILLER, prefixManager);
		OWLClassAssertionAxiom classAssertionMillerAsGuitarist = factory.getOWLClassAssertionAxiom(guitarist, miller);
		OWLClassAssertionAxiom classAssertionMillerAsDrummer = factory.getOWLClassAssertionAxiom(drummer, miller);
		manager.addAxiom(ontology, drummerDisjointGuitarist);
		manager.addAxiom(ontology, classAssertionMillerAsGuitarist);
		manager.addAxiom(ontology, classAssertionMillerAsDrummer);
		
        manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
        System.out.println(ontology);
	}
	
	public void useEquivalentClasses() throws OWLOntologyStorageException {
		OWLDataFactory factory = manager.getOWLDataFactory();
		PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
		OWLClass singer = factory.getOWLClass(MusicVocabulary.CLASS_NAME_SINGER, prefixManager);
		OWLClass vocalist = factory.getOWLClass(MusicVocabulary.CLASS_NAME_VOCALIST, prefixManager);
		OWLEquivalentClassesAxiom singerAndVocalistEquivalent = factory.getOWLEquivalentClassesAxiom(singer, vocalist);
		
		manager.addAxiom(ontology, singerAndVocalistEquivalent);
		manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
	}
	
	public void useReasoner() throws Exception {
		PrefixManager prefixManager = new DefaultPrefixManager(ontologyIri.toString());
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
		ConsoleProgressMonitor progressMonitor = new
				ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(
				progressMonitor);
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences();
		boolean consistent = reasoner.isConsistent();
		System.out.println("Konsistent: " + consistent);
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass musician = factory.getOWLClass(MusicVocabulary.CLASS_NAME_MUSICIAN, prefixManager);
		NodeSet<OWLClass> subClassesNodeSet = reasoner.getSubClasses(musician, false);
		Set<OWLClass> subclasses = subClassesNodeSet.getFlattened();

		System.out.println("Unterklassen von Musician");
		for (OWLClass aClass : subclasses) {
			System.out.println(" " + aClass);
		}
		
		OWLClass singer = factory.getOWLClass(MusicVocabulary.CLASS_NAME_SINGER, prefixManager);
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(singer, false);
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		for (OWLNamedIndividual ind : individuals) {
			System.out.println(" " + ind);
		}
    }
}
