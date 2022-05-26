package org.sdmlab;

import com.opencsv.*;
import org.eclipse.rdf4j.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an ontology instance for the lab
        Ontology ontology = new Ontology();
        // Build and export the TBOX as an .RDF file
        ontology.exportModel(ontology.buildTBox(), "src/main/resources/output/TBOX.rdf");
        // Build and export the ABOX as an .RDF file
        ontology.exportModel(ontology.buildABox(), "src/main/resources/output/ABOX.rdf");
    }
}