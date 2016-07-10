package uk.ac.qmul.pv.control;

import com.google.gson.JsonArray;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.qmul.pv.db.DatabaseAccess;
import uk.ac.qmul.pv.model.PSMRecord;
import uk.ac.qmul.pv.util.JavaToJSON;

/**
 *
 * @author sureshhewapathirana
 */
public class PSMExtractor implements Runnable, DataExtractor {

    private String inputfile;
    private String outputfile;
    // private MzIdentMLUnmarshaller unmash;
    DatabaseAccess db;

    public PSMExtractor(String inputfile, String outputfile) {
        this.inputfile = inputfile;
        this.outputfile = outputfile;

        try {
            // this.unmash = DatabaseAccess.getUnmarshaller(this.inputfile);
            db = DatabaseAccess.getInstance(inputfile);
        } catch (Exception e) {
            System.err.println("File Reading Error: " + e.getMessage());
        }
    }

    @Override
    public void export() {

//        Map<String, SpectrumIdentificationItem> siiMap = DatabaseAccess.getSiiHashMap(unmash);
        Map<String, SpectrumIdentificationItem> siiMap = db.getSiiHashMap();
        JsonArray psmList = new JsonArray();
        PSMRecord psmRecord;
        
//        Iterator<SpectrumIdentificationItem> it = siiMap.values().iterator();
//        while (it.hasNext()) {
//            SpectrumIdentificationItem sii = it.next();
        for(SpectrumIdentificationItem sii:siiMap.values()){
            
            
            List<PeptideEvidenceRef> pepEvidenceRefList = sii.getPeptideEvidenceRef();
            for (PeptideEvidenceRef pepEvidenceRef : pepEvidenceRefList) {
                psmRecord = new PSMRecord();
                psmRecord.setId(pepEvidenceRef.getPeptideEvidenceRef());
                psmRecord.setCalculatedMassToCharge(sii.getCalculatedMassToCharge());
                psmRecord.setExperimentalMassToCharge(sii.getExperimentalMassToCharge());
                psmRecord.setChargeState(sii.getChargeState());

//                psmRecord.setRank(sii.getRank());
                psmList.add(JavaToJSON.psmToJsonArray(psmRecord));
            }
        }
        // Convert Protein list to JSON file
        JavaToJSON.toJSON(psmList, this.outputfile);
    }

    /**
     * Run PSM as a separate thread in parallel to protein
     */
    @Override
    public void run() {
        System.out.println(LocalDateTime.now() + ": Peptide Spectrum Match : Started!!!");
        long start = System.currentTimeMillis();
        export();
        long end = System.currentTimeMillis();
        System.out.println(LocalDateTime.now() + ": Peptide Spectrum Match : Finished!!!");
        System.out.println("Peptide Spectrum Match : Data extraction took " + (end - start) + " milliseconds");

    }
}
