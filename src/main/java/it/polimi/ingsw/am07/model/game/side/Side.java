package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;

public sealed abstract class Side permits SideFront, SideBack {

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

    public SideFieldRepresentation fieldRepresentation() {
        return fieldRepresentation;
    }

    public ResourceHolder resources() {
        return resources;
    }


}
