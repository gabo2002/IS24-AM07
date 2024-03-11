package it.polimi.ingsw.am07.model;

import java.lang.invoke.MutableCallSite;
import java.util.HashMap;
import java.util.Map;

public class ResourceHolder {

    private final Map<Symbol, Integer> resources;

    public ResourceHolder() {
        resources = new HashMap<>();
    }

    public ResourceHolder(SideFieldRepresentation fieldRepresentation) {
        resources = new HashMap<>();
        resources.put(fieldRepresentation.topLeft(), 1);
        resources.put(fieldRepresentation.topRight(), 1);
        resources.put(fieldRepresentation.bottomLeft(), 1);
        resources.put(fieldRepresentation.bottomRight(), 1);
    }

    public void subtract(ResourceHolder other) throws RuntimeException{ //maybe a custom exception?

    }

    public boolean contains(ResourceHolder other) {
        return true;
    }

}
