package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;

public final class SideBack extends Side {

    // maybe distinct between SideBacks (?) --> similar to SideFront (abstract)
    // might help handling the SideBack for resourceCards but also other sideback in general imo

    public SideBack(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources, Symbol color) {
        super(id, fieldRepresentation, resources,color);
    }


}
