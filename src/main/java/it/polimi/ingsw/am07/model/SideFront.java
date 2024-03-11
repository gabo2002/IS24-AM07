package it.polimi.ingsw.am07.model;

public sealed abstract class SideFront extends Side permits SideFrontGold, SideFrontStarter, SideFrontRes {

    protected SideFront(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources) {
        super(id, fieldRepresentation, resources);
    }

    @Override
    public String resourceID() {
        return "front_" + id + ".png";
    }

}
