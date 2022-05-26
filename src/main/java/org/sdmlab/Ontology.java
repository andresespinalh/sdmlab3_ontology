package org.sdmlab;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.opencsv.*;

public class Ontology {
    public static final Namespace namespace = Values.namespace("sdmupc", "http://sdmupc.org/lab3/");

    // IRI's for Classes
    public static final IRI person = Values.iri(namespace, "Person");
    public static final IRI chair = Values.iri(namespace, "Chair");
    public static final IRI editor = Values.iri(namespace, "Editor");
    public static final IRI author = Values.iri(namespace, "Author");
    public static final IRI reviewer = Values.iri(namespace, "Reviewer");
    public static final IRI venue = Values.iri(namespace, "Venue");
    public static final IRI conference = Values.iri(namespace, "Conference");
    public static final IRI journal = Values.iri(namespace, "Journal");
    public static final IRI workshop = Values.iri(namespace, "Workshop");
    public static final IRI symposium = Values.iri(namespace, "Symposium");
    public static final IRI expertGroup = Values.iri(namespace, "ExpertGroup");
    public static final IRI regularConference = Values.iri(namespace, "RegularConference");
    public static final IRI topic = Values.iri(namespace, "Topic");
    public static final IRI paper = Values.iri(namespace, "Paper");
    public static final IRI fullPaper = Values.iri(namespace, "FullPaper");
    public static final IRI shortPaper = Values.iri(namespace, "ShortPaper");
    public static final IRI demoPaper = Values.iri(namespace, "DemoPaper");
    public static final IRI poster = Values.iri(namespace, "Poster");
    public static final IRI decision = Values.iri(namespace, "Decision");

    // IRI's for Properties
    public static final IRI chairAssigns = Values.iri(namespace, "ChairAssigns");
    public static final IRI editorAssigns = Values.iri(namespace, "EditorAssigns");
    public static final IRI handlesConference = Values.iri(namespace, "HandlesConference");
    public static final IRI handlesJournal = Values.iri(namespace, "HandlesJournal");
    public static final IRI isAbout = Values.iri(namespace, "IsAbout");
    public static final IRI writes = Values.iri(namespace, "Writes");
    public static final IRI submittedTo = Values.iri(namespace, "SubmittedTo");
    public static final IRI reviews = Values.iri(namespace, "Reviews");
    public static final IRI decides = Values.iri(namespace, "Decides");

    public Ontology(){
    }

    public Model buildTBox() {
        ModelBuilder builder = new ModelBuilder();

        // RDFS.SUBCLASSOF
        builder.subject(this.chair).add(RDFS.SUBCLASSOF, this.person);
        builder.subject(this.editor).add(RDFS.SUBCLASSOF, this.person);
        builder.subject(this.author).add(RDFS.SUBCLASSOF, this.person);
        builder.subject(this.reviewer).add(RDFS.SUBCLASSOF, this.person);

        builder.subject(this.conference).add(RDFS.SUBCLASSOF, this.venue);
        builder.subject(this.journal).add(RDFS.SUBCLASSOF, this.venue);

        builder.subject(this.fullPaper).add(RDFS.SUBCLASSOF, this.paper);
        builder.subject(this.shortPaper).add(RDFS.SUBCLASSOF, this.paper);
        builder.subject(this.demoPaper).add(RDFS.SUBCLASSOF, this.paper);
        builder.subject(this.poster).add(RDFS.SUBCLASSOF, this.paper);

        builder.subject(this.workshop).add(RDFS.SUBCLASSOF, this.conference);
        builder.subject(this.symposium).add(RDFS.SUBCLASSOF, this.conference);
        builder.subject(this.expertGroup).add(RDFS.SUBCLASSOF, this.conference);
        builder.subject(this.regularConference).add(RDFS.SUBCLASSOF, this.conference);

        // RDFS.DOMAIN & RDFS.RANGE
        builder.subject(this.chairAssigns).add(RDFS.DOMAIN, this.chair);
        builder.subject(this.chairAssigns).add(RDFS.RANGE, this.reviewer);

        builder.subject(this.editorAssigns).add(RDFS.DOMAIN, this.editor);
        builder.subject(this.editorAssigns).add(RDFS.RANGE, this.reviewer);

        builder.subject(this.handlesConference).add(RDFS.DOMAIN, this.chair);
        builder.subject(this.handlesConference).add(RDFS.RANGE, this.conference);

        builder.subject(this.handlesJournal).add(RDFS.DOMAIN, this.editor);
        builder.subject(this.handlesJournal).add(RDFS.RANGE, this.journal);

        builder.subject(this.isAbout).add(RDFS.DOMAIN, this.venue);
        builder.subject(this.isAbout).add(RDFS.RANGE, this.topic);

        builder.subject(this.writes).add(RDFS.DOMAIN, this.author);
        builder.subject(this.writes).add(RDFS.RANGE, this.paper);

        builder.subject(this.submittedTo).add(RDFS.DOMAIN, this.paper);
        builder.subject(this.submittedTo).add(RDFS.RANGE, this.venue);

        builder.subject(this.reviews).add(RDFS.DOMAIN, this.reviewer);
        builder.subject(this.reviews).add(RDFS.RANGE, this.paper);

        builder.subject(this.decides).add(RDFS.DOMAIN, this.reviewer);
        builder.subject(this.decides).add(RDFS.RANGE, this.decision);

        return builder.build();
    }

    public Model buildABox() {
        ModelBuilder builder = new ModelBuilder();

        // Read the source .CSV files
        List<String[]> authorWritesPaperFile = readCSVFile("src/main/resources/graphdata/author_writes_paper.csv");
        List<String[]> chairAssignsFile = readCSVFile("src/main/resources/graphdata/chair_assigns.csv");
        List<String[]> chairHandlesFile = readCSVFile("src/main/resources/graphdata/chair_handles.csv");
        List<String[]> conferenceFile = readCSVFile("src/main/resources/graphdata/conference.csv");
        List<String[]> editorAssignsFile = readCSVFile("src/main/resources/graphdata/editor_assigns.csv");
        List<String[]> editorHandlesFile = readCSVFile("src/main/resources/graphdata/editor_handles.csv");
        List<String[]> journalFile = readCSVFile("src/main/resources/graphdata/journal.csv");
        List<String[]> paperFile = readCSVFile("src/main/resources/graphdata/paper.csv");
        List<String[]> personFile = readCSVFile("src/main/resources/graphdata/person.csv");
        List<String[]> reviewsPaperFile = readCSVFile("src/main/resources/graphdata/reviewer_reviews_paper.csv");
        List<String[]> submittedToFile = readCSVFile("src/main/resources/graphdata/submitted_to.csv");
        List<String[]> venueFile = readCSVFile("src/main/resources/graphdata/venue.csv");
        List<String[]> topicFile = readCSVFile("src/main/resources/graphdata/topic.csv");
        List<String[]> decisionFile = readCSVFile("src/main/resources/graphdata/decision.csv");
        List<String[]> isAboutFile = readCSVFile("src/main/resources/graphdata/is_about.csv");

        // Paper
        paperFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(RDF.TYPE, this.paper)
                    .add(RDFS.LABEL, line[1]);

            switch (line[6]) {
                case "Short Paper":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.shortPaper);
                    break;
                case "Full Paper":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.fullPaper);
                    break;
                case "Poster":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.poster);
                    break;
                case "Demo Paper":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.demoPaper);
                    break;
            }
        });

        // Person
        personFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(RDF.TYPE, this.person)
                    .add(RDFS.LABEL, line[1]);

            switch (line[3]) {
                case "Chair":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.chair);
                    break;
                case "Editor":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.editor);
                    break;
                case "Author":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.author);
                    break;
                case "Reviewer":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.reviewer);
                    break;
            }
        });

        // Venue
        venueFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(RDF.TYPE, this.venue)
                    .add(RDFS.LABEL, line[2]);

            switch (line[1]) {
                case "Journal":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.journal);
                    break;
                case "Conference":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.conference);
                    break;
            }
        });

        // Conference
        conferenceFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(RDF.TYPE, this.conference)
                    .add(RDFS.LABEL, line[1]);

            switch (line[3]) {
                case "Workshop":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.workshop);
                    break;
                case "Expert Group":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.expertGroup);
                    break;
                case "Symposium":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.symposium);
                    break;
                case "Regular Conference":
                    builder.subject(Values.iri(namespace,line[0])).add(RDF.TYPE, this.regularConference);
                    break;
            }
        });

        // Topic
        topicFile.forEach(line -> {
            builder.subject(Values.iri(namespace, line[0]))
                    .add(RDF.TYPE, this.topic)
                    .add(RDFS.LABEL, line[1]);
        });

        // Decision
        decisionFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(RDF.TYPE, this.decision)
                    .add(RDFS.LABEL, line[1]);
        });

        // Is About
        isAboutFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0])).add(this.isAbout, Values.iri(namespace,line[1]));
        });

        // Writes
        authorWritesPaperFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0])).add(this.writes, Values.iri(namespace,line[1]));
        });

        // Submitted To
        submittedToFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0])).add(this.submittedTo, Values.iri(namespace,line[1]));
        });

        // Reviews & Decides
        reviewsPaperFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[1]))
                    .add(this.reviews, Values.iri(namespace,line[0]))
                    .add(RDFS.LABEL, line[2]);

            builder.subject(Values.iri(namespace,line[1]))
                    .add(this.decides, Values.iri(namespace,line[4]));
        });

        // Chair Assigns
        chairAssignsFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(this.chairAssigns, Values.iri(namespace,line[1]));
        });

        // Chair Handles
        chairHandlesFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(this.handlesConference, Values.iri(namespace,line[1]));
        });

        // Editor Assigns
        editorAssignsFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(this.editorAssigns, Values.iri(namespace,line[1]));
        });

        // Editor Handles
        editorHandlesFile.forEach(line -> {
            builder.subject(Values.iri(namespace,line[0]))
                    .add(this.handlesJournal, Values.iri(namespace,line[1]));
        });

        return builder.build();
    }

    // Export a model as a .RDF file
    public void exportModel(Model model, String filename) throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        try {
            Rio.write(model, out, RDFFormat.RDFXML);
        }
        finally {
            out.close();
        }
    }

    // Wrapper to read CSV files into a list of strings
    private static List<String[]> readCSVFile(String file) {
        try (
            FileReader fileReader = new FileReader(file);
            CSVReader reader = new CSVReaderBuilder(fileReader).withSkipLines(1).build()
        ) {
            return reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
