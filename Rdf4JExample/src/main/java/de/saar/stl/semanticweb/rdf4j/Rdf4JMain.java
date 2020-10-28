package de.saar.stl.semanticweb.rdf4j;

import java.io.IOException;

import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

public class Rdf4JMain {
	public static final void main(String[] args) {
		MusicOntology musicOntology = new MusicOntology();
		musicOntology.buildAndPrintSimpleModel();
		musicOntology.buildAndPrintExampleModel();
		musicOntology.filterAllSingers();
		musicOntology.filterAllGuitarists();
		musicOntology.filterAllSingersSingingKingOfPain();
		musicOntology.filterStingSong();
		try {
			musicOntology.saveInDatabase();
			musicOntology.saveInFile();
			musicOntology.readFileAndPrintToScreen();
		} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
