package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Validations;

import java.util.List;

public class TPVService {

    private static final String MASTER_PASS = "someMasterPass";

    private TPVRepository tpvRepository;

    public TPVService(TPVRepository tpvRepository) {
        this.tpvRepository = tpvRepository;
    }

    public void newTPV(String shop, int pos){
        TPV tpv = new TPV(shop, pos, tpvRepository.newId());
        tpvRepository.insert(tpv);
    }

    public List<TPV> listTPV(){
        return tpvRepository.list();
    }

    public List<TPV> listTPVById(){
        return tpvRepository.list(Comparators.byId);
    }

    public TPV findById(long tpvId) {
        return tpvRepository.findById(tpvId);
    }

    public TPV findByShopPos(String shop, int pos) {
        return tpvRepository.findByShopPos(shop, pos);
    }

    public void validation(long id, boolean validated) {
        TPV tpv = findById(id);
        if (validated) {
            tpv.setnIntents(0);
            tpv.setState(TPVState.IDLE);
        }
        else {
            if (tpv.getnIntents() == 4) {
                tpv.setnIntents(0);
                tpv.setState(TPVState.BLOCKED);
            }
            else tpv.setnIntents(tpv.getnIntents()+1);
        }
    }

    public boolean validate(long id, String password) {
        TPV tpv = findById(id);
        if (Validations.validPassword(password, MASTER_PASS)) {
            tpv.setState(TPVState.AVAILABLE);
            return true;
        }
        return false;
    }

}
