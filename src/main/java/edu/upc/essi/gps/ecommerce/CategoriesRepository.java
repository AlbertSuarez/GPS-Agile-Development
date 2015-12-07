package edu.upc.essi.gps.ecommerce;


import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Repository;

import java.util.List;

public class CategoriesRepository extends Repository<Category> {

    public List<Category> lookForName(final String name) {
        return list(Matchers.containsNameMatcher(name), Comparators.byName);
    }

    public Category findByName(final String name) {
        return find(Matchers.nameMatcher(name));
    }

    @Override
    protected void checkInsert(final Category category) throws RuntimeException {
        if (findByName(category.getName()) != null)
            throw new IllegalArgumentException("Ja existeix una categoria amb aquest nom");
    }
}
