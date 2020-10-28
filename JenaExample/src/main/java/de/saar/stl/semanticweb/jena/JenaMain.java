package de.saar.stl.semanticweb.jena;

import java.util.Iterator;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

public class JenaMain {
	public static void main(String [] args) {
		JenaMain jenaMain = new JenaMain();
		jenaMain.useReasoner();
	}
	
	public void workWithRdfData() {
		Model model = ModelFactory.createDefaultModel();
		Property hasNameProperty = model.createProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_NAME);
		Property hasAgeProperty = model.createProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_AGE);
		Property singsProperty = model.createProperty(MusicVocabulary.OBJECT_PROPERTY_SINGS);
		Resource madAboutYou = model.createResource(MusicVocabulary.INDIVUDUAL_NAME_MAD_ABOUT_YOU);
		Property hasTitleProperty = model.createProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_TITLE);
		madAboutYou.addLiteral(hasTitleProperty, "Mad about you");
		
		Resource sting = model.createResource(MusicVocabulary.INDIVIDUAL_NAME_STING)
				.addProperty(hasNameProperty, "Sting")
				.addProperty(hasAgeProperty, "71")
				.addProperty(singsProperty, madAboutYou);
		StmtIterator statementIterator = model.listStatements();
		
		while (statementIterator.hasNext()) {
		    Statement statement = statementIterator.nextStatement();
		    Resource  subject   = statement.getSubject();  
		    Property  predicate = statement.getPredicate();  
		    RDFNode   object    = statement.getObject();     
		
		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        System.out.print(" \"" + object.toString() + "\"");
		    }
		
		    System.out.println(" .");
		}
		
		ResIterator sung = model.listSubjectsWithProperty(singsProperty);
		System.out.println("Alles, was gesungen wird");
		if (sung.hasNext()) {
            while (sung.hasNext()) {
                System.out.println("  " + sung.nextResource()
                                              .getRequiredProperty(singsProperty));
            }
        } 
		
		NodeIterator stingsSongs = model.listObjectsOfProperty(sting, singsProperty);
		
		System.out.println("Stings Songs");
		if (stingsSongs.hasNext()) {
            while (stingsSongs.hasNext()) {
                System.out.println("  " + stingsSongs.nextNode());
            }
        } 
		
		model.write(System.out);
	}
	
	public void workWithOntology() {
		OntModel ontModel = ModelFactory.createOntologyModel();
		OntClass singerClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_SINGER);
		OntClass vocalistClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_VOCALIST);
		OntClass musicianClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_MUSICIAN);
		OntClass guitaristClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_GUITARIST);
		singerClass.addEquivalentClass(vocalistClass);
		singerClass.addSuperClass(musicianClass);
		singerClass.addDisjointWith(guitaristClass);
		Individual sting = ontModel.createIndividual(MusicVocabulary.INDIVIDUAL_NAME_STING, 
				singerClass);
		Individual freddy = ontModel.createIndividual(MusicVocabulary.INDIVIDUAL_NAME_FREDDY, 
				vocalistClass);
		
		OntClass artefact = ontModel.getOntClass(MusicVocabulary.CLASS_NAME_MUSICIAN);
		for (Iterator<OntClass> i = artefact.listSubClasses(); i.hasNext(); ) {
		  OntClass c = i.next();
		  System.out.println( c.getURI() );
		}
	}
	
	public void useReasoner() {
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		Model model = createModelForReasoning();
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);
		Resource sting = infModel.getResource(MusicVocabulary.INDIVIDUAL_NAME_STING);
		Resource song = infModel.getResource(MusicVocabulary.CLASS_NAME_SONG);
		if (infModel.contains(sting, RDF.type, song)) {
			System.out.println("Aussage enthalten!");
		} else {
			System.out.println("Aussage nicht enthalten!");
		}
		
		ValidityReport validity = infModel.validate();
		if (validity.isValid()) {
		    System.out.println("Keine Fehler gefunden!");
		} else {
		    System.out.println("Fehler!");
		    for (Iterator i = validity.getReports(); i.hasNext(); ) {
		        System.out.println(" - " + i.next());
		    }
		}	
	}
	
	private Model createModelForReasoning() {
		OntModel ontModel = ModelFactory.createOntologyModel();
		OntClass singerClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_SINGER);
		OntClass vocalistClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_VOCALIST);
		OntClass musicianClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_MUSICIAN);
		OntClass guitaristClass = ontModel.createClass(MusicVocabulary.CLASS_NAME_GUITARIST);
		OntClass song = ontModel.createClass(MusicVocabulary.CLASS_NAME_SONG);
		
		singerClass.addEquivalentClass(vocalistClass);
		singerClass.addSuperClass(musicianClass);
		singerClass.addDisjointWith(guitaristClass);
		Individual sting = ontModel.createIndividual(MusicVocabulary.INDIVIDUAL_NAME_STING, singerClass);
		Individual freddy = ontModel.createIndividual(MusicVocabulary.INDIVIDUAL_NAME_FREDDY, vocalistClass);
		Individual theBoss = ontModel.createIndividual(MusicVocabulary.INDIVIDUAL_NAME_THE_BOSS, singerClass);
		sting.addOntClass(guitaristClass);
		
		DatatypeProperty hasName = ontModel.createDatatypeProperty(MusicVocabulary.DATATYPE_PROPERTY_HAS_NAME);
		hasName.addDomain(singerClass);
		hasName.addRange(XSD.integer);
		
		ObjectProperty sings = ontModel.createObjectProperty(MusicVocabulary.OBJECT_PROPERTY_SINGS);
		sings.addDomain(singerClass);
		sings.addRange(song);
		
		return ontModel;
	}
}
