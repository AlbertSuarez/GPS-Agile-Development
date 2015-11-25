package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.TPV;
import edu.upc.essi.gps.domain.TPVState;
import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Validations;

import java.util.List;

public class TPVService {

    private static final String MASTER_PASS = "someMasterPass";

    private TPVRepository tpvRepository;

    public TPVService(TPVRepository tpvRepository) {
        this.tpvRepository = tpvRepository;
    }

    public long newTPV(String shop, int pos){
        long id = tpvRepository.newId();
        TPV tpv = new TPV(shop, pos, id);
        tpvRepository.insert(tpv);
        return id;
    }

    public List<TPV> list(){
        return tpvRepository.list();
    }

    public List<TPV> listById(){
        return tpvRepository.list(Comparators.byId);
    }

    public TPV findById(long tpvId) {
        return tpvRepository.findById(tpvId);
    }

    public TPV findByShopPos(String shop, int pos) {
        return tpvRepository.findByShopPos(shop, pos);
    }

    public void validation(long TVPid, boolean validated) {
        TPV tpv = findById(TVPid);
        if (validated) tpv.setState(TPVState.IDLE);
        else {
            if (tpv.getnIntents() == 4) tpv.setState(TPVState.BLOCKED);
            else tpv.addNIntents(1);
        }
    }

    public boolean validateAdmin(long TVPid, String password) {
        TPV tpv = findById(TVPid);
        if (Validations.validPassword(password, MASTER_PASS)) {
            tpv.setState(TPVState.AVAILABLE);
            return true;
        }
        return false;
    }

}
