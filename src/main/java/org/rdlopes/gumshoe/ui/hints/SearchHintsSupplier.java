package org.rdlopes.gumshoe.ui.hints;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * User: ruilopes
 * Date: 03/05/2015
 * Time: 19:27
 */
public class SearchHintsSupplier implements Supplier<String[]> {

    private final String searchInput;

    public SearchHintsSupplier(String searchInput) {
        this.searchInput = Strings.emptyToNull(searchInput);
    }

    @Override
    public String[] get() {
        ArrayList<String> results = Lists.newArrayList();
        if (searchInput != null) {
            Stream.iterate(searchInput, UnaryOperator.<String>identity())
                    .limit(10).forEach(s -> results.add(s));
        }

        return results.toArray(new String[results.size()]);
    }

}
