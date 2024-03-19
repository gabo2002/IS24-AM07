package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;

public final class SideFrontRes extends SideFront {

    public SideFrontRes(int id,
                        SideFieldRepresentation fieldRepresentation, int associatedScore
    ) {
        super(id, fieldRepresentation, new ResourceHolder(), associatedScore);
    }

}
