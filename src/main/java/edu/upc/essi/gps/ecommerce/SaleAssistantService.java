package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Validations;

import java.util.List;

public class SaleAssistantService {

    private SaleAssistantRepository saleAssistantRepository;

    public SaleAssistantService(SaleAssistantRepository saleAssistantRepository) {
        this.saleAssistantRepository = saleAssistantRepository;
    }

    public long newCaixer(String name, String password){
        long id = saleAssistantRepository.newId();
        SaleAssistant c = new SaleAssistant(name, Validations.crypt(password), id);
        saleAssistantRepository.insert(c);
        return id;
    }

    public List<SaleAssistant> listAssistants(){
        return saleAssistantRepository.list();
    }

    public List<SaleAssistant> listAssistantsByName(){
        return saleAssistantRepository.list(Comparators.byName);
    }

    public List<SaleAssistant> listAssistantsById(){
        return saleAssistantRepository.list(Comparators.byId);
    }

    public SaleAssistant findById(long caixerId) {
        return saleAssistantRepository.findById(caixerId);
    }

    public List<SaleAssistant> findByName(String caixerName) {
        return saleAssistantRepository.list(Matchers.nameMatcher(caixerName));
    }

    public boolean validate(long id, String password) {
        return Validations.validPassword(password, findById(id).getEncryptedPass());
    }

}
