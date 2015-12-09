package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.domain.SaleAssistant;
import edu.upc.essi.gps.ecommerce.repositories.SaleAssistantRepository;
import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Validations;

import java.util.List;

import static edu.upc.essi.gps.utils.Validations.checkNotNull;

public class SaleAssistantService {

    private SaleAssistantRepository saleAssistantRepository;

    public SaleAssistantService(SaleAssistantRepository saleAssistantRepository) {
        this.saleAssistantRepository = saleAssistantRepository;
    }

    //TODO: this is a stub, not a functionality
    public void insert(String name, String password, long id){
        checkNotNull(name, "name");
        checkNotNull(password, "password");

        SaleAssistant c = new SaleAssistant(name, Validations.crypt(password), id);
        saleAssistantRepository.insert(c);
    }

    public long newAssistant(String name, String password) {
        long id = saleAssistantRepository.newId();
        SaleAssistant c = new SaleAssistant(name, Validations.crypt(password), id);
        saleAssistantRepository.insert(c);
        return id;
    }

    public List<SaleAssistant> list(){
        return saleAssistantRepository.list();
    }

    public List<SaleAssistant> listByName(){
        return saleAssistantRepository.list(Comparators.byName);
    }

    public SaleAssistant findById(long caixerId) {
        return saleAssistantRepository.findById(caixerId);
    }

    public void validate(long id, String password) {
        SaleAssistant saleAssistant = findById(id);
        if (saleAssistant == null)
            throw new IllegalStateException("El nom d'usuari o el password és incorrecte");
        if (!Validations.validPassword(password, saleAssistant.getEncryptedPass()))
            throw new IllegalStateException("El nom d'usuari o el password és incorrecte");

    }

}
