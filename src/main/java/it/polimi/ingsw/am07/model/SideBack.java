package it.polimi.ingsw.am07.model;

import java.util.Optional;

public final class SideBack extends Side {

    // maybe distinct between SideBacks (?) --> similar to SideFront (abstract)
    // might help handling the SideBack for resourceCards but also other sideback in general imo

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

        // consideration: starte's sideback  probably always contain resources (but we need to check all the cards)
        // but rescard's sideback always contain only 1 resource
    }

    @Override
    int calculateContributingScore(ResourceHolder gameResources) {
        return 0;
    }

}
