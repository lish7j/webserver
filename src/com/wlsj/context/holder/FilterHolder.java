package com.wlsj.context.holder;

import com.wlsj.filter.Filter;

public class FilterHolder {
    private Filter filter;
    private String fileterClass;

    public FilterHolder(String fileterClass) {
        this.fileterClass = fileterClass;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getFileterClass() {
        return fileterClass;
    }

    public void setFileterClass(String fileterClass) {
        this.fileterClass = fileterClass;
    }
}
