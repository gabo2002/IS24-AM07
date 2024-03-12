package it.polimi.ingsw.am07.model;

import java.util.Optional;

public final class SideFrontRes extends SideFront {

    public SideFrontRes(int id,
                        SideFieldRepresentation fieldRepresentation
    ) {
        super(id, fieldRepresentation, new ResourceHolder());
    }


}
