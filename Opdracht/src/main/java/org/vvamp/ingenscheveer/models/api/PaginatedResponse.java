package org.vvamp.ingenscheveer.models.api;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> results;
    private int total;

    public PaginatedResponse(List<T> results, int total) {
        this.results = results;
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
