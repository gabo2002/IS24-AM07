package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;

public sealed abstract class SideFront extends Side permits SideFrontGold, SideFrontStarter, SideFrontRes {

    protected SideFront(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources) {
        super(id, fieldRepresentation, resources);
    }

}
