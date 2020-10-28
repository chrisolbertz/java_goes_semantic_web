package de.saar.stl.semanticweb.rdf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class MusicOntology {
	private static final String RDF_FILE_PATH = "/home/christopher/music.rdf";
	
	/**
	 * Erzeugt ein Modell fuer unsere Beispiel-RDF-Daten.
	 */
	public void buildAndPrintSimpleModel() {
		ValueFactory valueFactory = SimpleValueFactory.getInstance();
		
		IRI hasTitle = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.DATATYPE_PROPERTY_HAS_TITLE);
		IRI sting = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.INDIVIDUAL_NAME_STING);
		IRI singer = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.CLASS_NAME_SINGER);
		IRI musician = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.CLASS_NAME_MUSICIAN);
		IRI madAboutYou = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.INDIVUDUAL_NAME_MAD_ABOUT_YOU);
		IRI sings = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.OBJECT_PROPERTY_SINGS);
		IRI freddy = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.INDIVIDUAL_NAME_FREDDY);
		
		Model model = new TreeModel();
		model.add(sting, RDF.TYPE, singer);
		model.add(singer, RDFS.SUBCLASSOF, musician);
		model.add(sting, sings, madAboutYou);
		model.add(freddy, RDF.TYPE, singer);
		
		final Literal literalMadAboutYou = valueFactory.createLiteral("Mad About You");
		model.add(madAboutYou, hasTitle, literalMadAboutYou);
		
		for (Statement statement : model) {
			System.out.println(statement);
		}
	}
	
	public void buildAndPrintExampleModel() {
		Model model = createExampleModel();
		for (Statement statement : model) {
			System.out.println(statement);
		}
	}
	
	/**
	 * Baut ein Modell fuer die anderen Beispiele.
	 * @return Das fertige Modell. 
	 */
	private Model createExampleModel() {
		ModelBuilder modelBuilder =  new ModelBuilder();
		Model model = modelBuilder.setNamespace(MusicVocabulary.PREFIX, MusicVocabulary.NAMESPACE)
				  .subject(MusicVocabulary.PREFIXED_CLASS_NAME_SINGER)
			  		.add(RDFS.SUBCLASSOF, MusicVocabulary.PREFIXED_CLASS_NAME_MUSICIAN)
			  	  .subject(MusicVocabulary.PREFIXED_CLASS_NAME_GUITARIST)
			  		.add(RDFS.SUBCLASSOF, MusicVocabulary.PREFIXED_CLASS_NAME_MUSICIAN)
			  	  .subject(MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_MAD_ABOUT_YOU)
			  		.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_SONG)
			  		.add(MusicVocabulary.PREFIXED_DATATYPE_PROPERTY_HAS_TITLE, "Mad About you")
			  	  .subject(MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_FIELDS_OF_GOLD)
				  	.add(RDF.TYPE, MusicVocabulary.CLASS_NAME_SONG)
				  	.add(MusicVocabulary.PREFIXED_DATATYPE_PROPERTY_HAS_TITLE, "Fields Of Gold")
				  .subject(MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_KING_OF_PAIN)
				  	.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_SONG)	
				  	.add(MusicVocabulary.PREFIXED_DATATYPE_PROPERTY_HAS_TITLE, "King Of Pain")
				  .subject(MusicVocabulary.PREFIXED_INDIVIDUAL_NAME_STING)
				  	.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_SINGER)
				  	.add(MusicVocabulary.PREFIXED_OBJECT_PROPERTY_SINGS, MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_MAD_ABOUT_YOU)
				  	.add(MusicVocabulary.PREFIXED_OBJECT_PROPERTY_SINGS, MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_FIELDS_OF_GOLD)
				  	.add(MusicVocabulary.PREFIXED_OBJECT_PROPERTY_SINGS, MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_KING_OF_PAIN)
				  .subject(MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_KING_OF_PAIN)
				  	.add(MusicVocabulary.PREFIXED_DATATYPE_PROPERTY_HAS_TITLE, "King Of Pain")
				  .subject(MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_FIELDS_OF_GOLD)
				  	.add(MusicVocabulary.PREFIXED_DATATYPE_PROPERTY_HAS_TITLE, "Fields Of Gold")
				  .subject(MusicVocabulary.PREFIXED_INDIVIDUAL_NAME_FREDDY)
				  	.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_SINGER)
				  .subject(MusicVocabulary.PREFIXED_INDIVIDUAL_NAME_THE_BOSS)
				  	.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_SINGER)
				  	.add(MusicVocabulary.PREFIXED_OBJECT_PROPERTY_SINGS, MusicVocabulary.PREFIXED_INDIVUDUAL_NAME_KING_OF_PAIN)
				  .subject(MusicVocabulary.PREFIXED_INDIVIDUAL_NAME_MILLER)
				  	.add(RDF.TYPE, MusicVocabulary.PREFIXED_CLASS_NAME_GUITARIST)
				  .build();
		return model;
	}
	
	public void filterAllSingers() {
		Model model = createExampleModel();
		
		ValueFactory valueFactory = SimpleValueFactory.getInstance();
		IRI singer = valueFactory.createIRI(MusicVocabulary.NAMESPACE, 
				MusicVocabulary.CLASS_NAME_SINGER);
		
		System.out.println("*******************Alle Saenger*******************");
		Set<Resource> allSingers = model.filter(null, RDF.TYPE, singer).
				subjects();
		for(Value value: allSingers) {
			System.out.println(value.stringValue());
		}
		System.out.println("**************************************************");
	}
	
	public void filterAllGuitarists() {
		Model model = createExampleModel();
		
		ValueFactory valueFactory = SimpleValueFactory.getInstance();
		IRI guitarist = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.CLASS_NAME_GUITARIST);
		
		System.out.println("*******************Alle Gitarristen *******************");
		Set<Resource> allGuitarists = model.filter(null, RDF.TYPE, guitarist).subjects();
		for(Value value: allGuitarists) {
			System.out.println(value.stringValue());
		}
		System.out.println("******************************************************");
	}
	
	public void filterAllSingersSingingKingOfPain() {
		Model model = createExampleModel();
		
		ValueFactory valueFactory = SimpleValueFactory.getInstance();
		IRI kingOfPain = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.INDIVUDUAL_NAME_KING_OF_PAIN);
		IRI sings = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.OBJECT_PROPERTY_SINGS);
		
		System.out.println("*******Alle Saenger, die King of Pain singen***********");
		Set<Resource> allSingersSingingKingOfPain = model.filter(null, sings, kingOfPain).subjects();
		for(Value value: allSingersSingingKingOfPain) {
			System.out.println(value.stringValue());
		}
		System.out.println("*******************************************************");
	}
	
	public void filterStingSong() {
		Model model = createExampleModel();
		
		ValueFactory valueFactory = SimpleValueFactory.getInstance();
		IRI sting = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.INDIVIDUAL_NAME_STING);
		IRI sings = valueFactory.createIRI(MusicVocabulary.NAMESPACE, MusicVocabulary.OBJECT_PROPERTY_SINGS);
		
		System.out.println("*******************Lieder von Sting *******************");
		Set<Value> stingsSongs = model.filter(sting, sings, null).objects();
		for(Value value: stingsSongs) {
			System.out.println(value.stringValue());
		}
		System.out.println("******************************************************");
	}
	
	public void saveInDatabase() throws RDFParseException, UnsupportedRDFormatException, IOException {
		Model model = createExampleModel();
		Repository db = new SailRepository(new MemoryStore());
	
		try (RepositoryConnection connection = db.getConnection()) {
			connection.add(model);
			
			try (RepositoryResult<Statement> result = connection.
					getStatements(null, null, null)) {
				for(Statement statement: result) {
					System.out.println("Die Datenbank enthaelt: " + statement);
				}
			}
		}
		
		finally {
			db.shutDown();
		}
	}
	
	public void saveInFile() {
		Model model = createExampleModel();
		File file = new File(RDF_FILE_PATH);
		
		try {
			file.createNewFile();
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(file);
			Rio.write(model, fileOutputStream, RDFFormat.RDFXML);
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public void readFileAndPrintToScreen() {
		FileInputStream fileInputStream;
		File file = new File(RDF_FILE_PATH);
		
		try {
			fileInputStream = new FileInputStream(file);
			Model modelFromFile = Rio.parse(fileInputStream, "", RDFFormat.RDFXML);
			for (Statement statement : modelFromFile) {
				System.out.println(statement);
			}
		} catch (RDFParseException | UnsupportedRDFormatException | IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} 
	}
}
