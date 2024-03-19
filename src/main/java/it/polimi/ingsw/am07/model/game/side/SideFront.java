package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;

public sealed abstract class SideFront extends Side permits SideFrontGold, SideFrontStarter, SideFrontRes {
    private final int associatedScore;

    protected SideFront(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources, int associatedScore) {
        super(id, fieldRepresentation, resources);
        this.associatedScore = associatedScore;
    }

    public int getAssociatedScore() {
        return associatedScore;
    }

}
