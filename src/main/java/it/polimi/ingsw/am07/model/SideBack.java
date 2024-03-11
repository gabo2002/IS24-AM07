package it.polimi.ingsw.am07.model;

import java.util.Optional;

public final class SideBack extends Side {

    public SideBack(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources) {
        super(id, fieldRepresentation, resources);
    }

    @Override
    public String resourceID() {
        return "back_" + this.id + ".png";
    }

    @Override
    Optional<ResourceHolder> requirements() {
        return Optional.empty();    //wrong: starter's SideBack may contain resources
    }

    @Override
    int calculateContributingScore(ResourceHolder gameResources) {
        return 0;
    }

}
