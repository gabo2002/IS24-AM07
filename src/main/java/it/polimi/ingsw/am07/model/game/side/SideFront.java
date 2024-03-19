package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;

public sealed abstract class SideFront extends Side permits SideFrontGold, SideFrontStarter, SideFrontRes {
    private final int associatedScore;

    protected SideFront(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources, int associatedScore, Symbol color) {
        super(id, fieldRepresentation, resources,color);
        this.associatedScore = associatedScore;
    }

    public int getAssociatedScore() {
        return associatedScore;
    }

}
