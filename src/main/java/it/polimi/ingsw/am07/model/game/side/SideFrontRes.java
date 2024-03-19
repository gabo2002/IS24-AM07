package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;

public final class SideFrontRes extends SideFront {

    public SideFrontRes(int id,
                        SideFieldRepresentation fieldRepresentation, int associatedScore, Symbol color
    ) {
        super(id, fieldRepresentation, new ResourceHolder(), associatedScore,color);
    }

}
