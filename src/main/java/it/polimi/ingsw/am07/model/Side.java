package it.polimi.ingsw.am07.model;

import java.util.Optional;

sealed abstract class Side permits SideFront, SideBack {

    protected final int id;

    protected final SideFieldRepresentation fieldRepresentation;

    protected final ResourceHolder resources;

    protected Side(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources) {
        this.id = id;
        this.fieldRepresentation = fieldRepresentation;
        this.resources = resources;
    }

    public int id() {
        return id;
    }

    abstract String resourceID();

    public SideFieldRepresentation fieldRepresentation() {
        return fieldRepresentation;
    }

    public ResourceHolder resources() {
        return resources;
    }

    abstract Optional<ResourceHolder> requirements();

    abstract int calculateContributingScore(ResourceHolder gameResources);

}
