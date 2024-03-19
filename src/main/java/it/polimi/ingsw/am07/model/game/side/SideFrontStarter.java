package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;

public final class SideFrontStarter extends SideFront {

    public SideFrontStarter(int id,
                            SideFieldRepresentation fieldRepresentation, int associatedScore
    ) {
        super(id, fieldRepresentation, new ResourceHolder(), associatedScore);
    }

}
