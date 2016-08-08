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
import uk.ac.qmul.pv.util.JavaToJSON;

/**
 * This class handles all the data access methods related to Metadata.
 *
 * @author Suresh Hewapathirana
 */
public class MetadataExtractor implements Runnable, DataExtractor {

    private String inputFile;
    private String outputFile;

    SpectrumIdentificationProtocol sip;
    String fixedModifications = "";
    String variableModifications = "";
    DataAccess db;

    public MetadataExtractor(String inputFile, String outputFile) {

        this.inputFile = inputFile;
        this.outputFile = outputFile;

        try {
            //   this.unmash = DataAccess.getUnmarshaller(this.inputFile);
            db = DataAccess.getInstance(inputFile);
        } catch (Exception e) {
            System.err.println("File Reading Error:" + e.getMessage());
        }

    }

    @Override
    public void export() {

        MetadataRecord metadata = new MetadataRecord();

        // find both fixed and variable modifications
        // sip = DataAccess.getSpectrumIdentificationProtocol(unmash);
        sip = db.getSpectrumIdentificationProtocol();
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
        ModificationParams mp = sip.getModificationParams();
        if (mp == null) {
            fixedModifications += "No modifications";
            variableModifications += "No modifications";
            return;
        }
//    <xsd:element name="SearchModification" type="SearchModificationType" maxOccurs="unbounded"/>
        List<SearchModification> modList = mp.getSearchModification();
        if (modList.isEmpty()) {
            fixedModifications += "No modifications";
            variableModifications += "No modifications";
            return;
        } else {
            for (SearchModification modification : modList) {
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
        StringBuilder sb = new StringBuilder();
        List<String> modificationName = modification.getResidues();
        List<CvParam> cvs = modification.getCvParam();
        
        for (CvParam cv : cvs) {
            if (cv.getCvRef().equals("PSI-MOD") || cv.getCvRef().equals("UNIMOD")) {
                sb.append(cv.getName());
                
//              <xsd:element name="SpecificityRules" type="SpecificityRulesType" minOccurs="0" maxOccurs="unbounded"/>
                List<SpecificityRules> rules = modification.getSpecificityRules();
                if (rules != null && rules.size() > 0) {
//                  <xsd:element name="cvParam" type="CVParamType" minOccurs="1" maxOccurs="unbounded"/>
                    for (CvParam specificityCV : rules.get(0).getCvParam()) {
                        //id: MS:1001189
                        //name: modification specificity peptide N-term
                        //id: MS:1001190
                        //name: modification specificity peptide C-term
                        //id: MS:1002057
                        //name: modification specificity protein N-term
                        //id: MS:1002058
                        //name: modification specificity protein C-term
                        if (specificityCV.getCvRef().equals("PSI-MS")) {
                            String acc = specificityCV.getAccession();
                            sb.append(" on ");
                            switch (acc) {
                                case "MS:1001189":
                                    sb.append("N-term ");
                                    break;
                                case "MS:1001190":
                                    sb.append("C-term ");
                                    break;
                                case "MS:1002057":
                                    sb.append("Protein N-term ");
                                    break;
                                case "MS:1002058":
                                    sb.append("Protein C-term ");
                            }
                        }
                        return sb.toString();
                    }
                }
                if (modificationName.size() > 0) {
                    sb.append(" on ");
                    String mod = modificationName.get(0);
                    if (mod.equals(".")) {//For N or C terminal modifications that can occur on any residue, the . character should be used to specify any
                        mod = "Any";
                    }
                    sb.append(mod);
                }
                break;
            }
        }
        return sb.toString();
    }

    // Search type 
    // <xsd:element name="SearchType" type="ParamType">  which means minOccurs=1 maxOccurs=1
    // <xsd:documentation> The type of search performed e.g. PMF, Tag searches, MS-MS
    String getSearchType() {
        Param searchTypeParam = sip.getSearchType();
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
        Enzymes enzymes = sip.getEnzymes();
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
        if (enzymeBuilder.length() == 0) {//no enzyme name contained
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
