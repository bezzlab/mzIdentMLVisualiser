/*
 * @(#) MetadataExtractor    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Enzyme;
import uk.ac.ebi.jmzidml.model.mzidml.Enzymes;
import uk.ac.ebi.jmzidml.model.mzidml.ModificationParams;
import uk.ac.ebi.jmzidml.model.mzidml.Param;
import uk.ac.ebi.jmzidml.model.mzidml.ParamList;
import uk.ac.ebi.jmzidml.model.mzidml.SearchModification;
import uk.ac.ebi.jmzidml.model.mzidml.SpecificityRules;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.qmul.pv.db.DataAccess;
import uk.ac.qmul.pv.model.MetadataRecord;
import uk.ac.qmul.pv.util.CV;
import uk.ac.qmul.pv.util.JavaToJSON;

/**
 * This class handles all the data access methods related to Metadata.
 * 
 * Some functions of this class was originally written by
 * Chan, A.S.L. (2015) New Tool for Visualising Proteomics Results. Unpublished
 * MSc thesis. Queen Mary - University of London.
 * 
 * They were modified and optimised.
 *
 * @author Suresh Hewapathirana
 */
public class MetadataExtractor implements Runnable, DataExtractor {

    private String outputFile;

    SpectrumIdentificationProtocol protocol;
    String fixedModifications = "";
    String variableModifications = "";
    DataAccess db;

    public MetadataExtractor(String inputFile, String outputFile) {

        this.outputFile = outputFile;

        try {
            db = DataAccess.getInstance(inputFile);
        } catch (Exception e) {
            System.err.println("ProExtractor INFO : File Reading Error:" + e.getMessage());
        }

    }

    @Override
    public void export() {

        MetadataRecord metadata = new MetadataRecord();

        // find both fixed and variable modifications
        protocol = db.getSpectrumIdentificationProtocol();
        findModifications();

        // assign vaules to the MetadataRecord object
        metadata.setSearchType(getSearchType());
        metadata.setSoftwareList(getSoftwareName());
        metadata.setEnzymes(getEnzymesUsed());
        metadata.setFixedModifications(fixedModifications);
        metadata.setVariableModifications(variableModifications);

        // Convert metadata to JSON file
        JavaToJSON.metadataToJSON(metadata, this.outputFile);

    }

    private void findModifications() {
//    <xsd:element name="ModificationParams" type="ModificationParamsType" minOccurs="0"/>
        ModificationParams modifParams = protocol.getModificationParams();
        if (modifParams == null) {
            fixedModifications += "No modifications";
            variableModifications += "No modifications";
            return;
        }
//    <xsd:element name="SearchModification" type="SearchModificationType" maxOccurs="unbounded"/>
        List<SearchModification> modifList = modifParams.getSearchModification();
        if (modifList.isEmpty()) {
            fixedModifications += "No modifications";
            variableModifications += "No modifications";
            return;
        } else {
            for (SearchModification modification : modifList) {
                boolean fixed = modification.isFixedMod();
                String modStr = getModificationString(modification);
                if (fixed) {
                    fixedModifications += modStr + "<br/> ";
                } else {
                    variableModifications += modStr + "<br/> ";
                }
            }
        }
        
        if (fixedModifications.length() > 2) {
            fixedModifications = fixedModifications.substring(0, fixedModifications.length() - 2);
        }
        if (variableModifications.length() > 2) {
            variableModifications = variableModifications.substring(0, variableModifications.length() - 2);
        }
    }

    private String getModificationString(SearchModification modification) {
        StringBuilder modificationString = new StringBuilder();
        List<String> modificationName = modification.getResidues();
        List<CvParam> cvs = modification.getCvParam();
        
        for (CvParam cv : cvs) {
            if (cv.getCvRef().equals("PSI-MOD") || cv.getCvRef().equals("UNIMOD")) {
                modificationString.append(cv.getName());
                
//              <xsd:element name="SpecificityRules" type="SpecificityRulesType" minOccurs="0" maxOccurs="unbounded"/>
                List<SpecificityRules> rules = modification.getSpecificityRules();
                if (rules != null && rules.size() > 0) {
//                  <xsd:element name="cvParam" type="CVParamType" minOccurs="1" maxOccurs="unbounded"/>
                    for (CvParam specificityCV : rules.get(0).getCvParam()) {
                        if (specificityCV.getCvRef().equals("PSI-MS")) {
                            String acc = specificityCV.getAccession();
                            modificationString.append(" on ");
                            switch (acc) {
                                case CV.N_TERM_PEPTIDE_MOD:
                                    modificationString.append("Peptide N-term ");
                                    break;
                                case CV.C_TERM_PEPTIDE_MOD:
                                    modificationString.append("Peptide C-term ");
                                    break;
                                case CV.N_TERM_PROTEIN_MOD:
                                    modificationString.append("Protein N-term ");
                                    break;
                                case CV.C_TERM_PROTEIN_MOD:
                                    modificationString.append("Protein C-term ");
                            }
                        }
                        return modificationString.toString();
                    }
                }
                if (modificationName.size() > 0) {
                    modificationString.append(" on ");
                    String mod = modificationName.get(0);
                    if (mod.equals(".")) {
                        //For N or C terminal modifications that can occur on any residue, 
                        // the . character should be used to specify any
                        mod = "Any";
                    }
                    modificationString.append(mod);
                }
                break;
            }
        }
        return modificationString.toString();
    }

    // Search type 
    // <xsd:element name="SearchType" type="ParamType">  which means minOccurs=1 maxOccurs=1
    // <xsd:documentation> The type of search performed e.g. PMF, Tag searches, MS-MS
    String getSearchType() {
        Param searchTypeParam = protocol.getSearchType();
        return searchTypeParam.getCvParam().getName();
    }

    // Name of analysis software used
    // <xsd:element name="AnalysisSoftware" type="AnalysisSoftwareType" maxOccurs="unbounded"/>
    // <xsd:documentation> The software packages used to perform the analyses  
    String getSoftwareName() {
        ArrayList<String> softwareNameList = new ArrayList();
//        Map<String, AnalysisSoftware> asList = DataAccess.getAnalysisSoftwareHashMap(unmash);
        Map<String, AnalysisSoftware> asList = db.getAnalysisSoftwareHashMap();
        for (AnalysisSoftware as : asList.values()) {
            Param softwareType = as.getSoftwareName();
            if (softwareType != null) {
                String softwareName = as.getSoftwareName().getCvParam().getName();
                if (!softwareNameList.contains(softwareName)) {
                    softwareNameList.add(as.getSoftwareName().getCvParam().getName());
                }
            }
        }
        if (softwareNameList.isEmpty()) {
            softwareNameList.add("Information not available");
        }

        return String.join("<br/>", softwareNameList);
    }

    // Enzyme(s) (if empty return "Information not available")
    // <xsd:element name="Enzymes" type="EnzymesType" minOccurs="0"/>
    String getEnzymesUsed() {
//        <xsd:element name="Enzymes" type="EnzymesType" minOccurs="0"/>
        Enzymes enzymes = protocol.getEnzymes();
        if (enzymes == null) {
            return "Information not available";
        }

//	<xsd:element name="Enzyme" type="EnzymeType" maxOccurs="unbounded"/>
//        implies that minOccurs = 1
        StringBuilder enzymeBuilder = new StringBuilder();
        for (Enzyme enzymeList : enzymes.getEnzyme()) {
//        <xsd:element name="EnzymeName" type="ParamListType" minOccurs="0">
//	  <xsd:documentation>The name of the enzyme from a CV.            
            ParamList enzymeName = enzymeList.getEnzymeName();
            if (enzymeName == null) {
                continue;
            }
            List<CvParam> enzymeParam = enzymeName.getCvParam();
            for (int i = 0; i < enzymeParam.size(); i++) {
                enzymeBuilder.append(enzymeParam.get(i).getName());
                enzymeBuilder.append(" ");
            }
        }
        if (enzymeBuilder.length() == 0) {//no enzyme name available
            return "Information not available";
        }
        enzymeBuilder.deleteCharAt(enzymeBuilder.length() - 1);

        return enzymeBuilder.toString();
    }

    @Override
    public void run() {
        System.out.println(LocalDateTime.now() + ": Metadata : Started!!!");
        long start = System.currentTimeMillis();
        export();
        long end = System.currentTimeMillis();
        System.out.println(LocalDateTime.now() + ": Metadata : Finished!!!");
        System.out.println("Metadata : Data extraction took " + (end - start) + " milliseconds");

    }
}
