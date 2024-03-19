package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;

import java.util.Optional;

public final class SideFrontGold extends SideFront {

    private final Symbol multiplier;

    private final ResourceHolder requirements;

    public SideFrontGold(int id,
                            SideFieldRepresentation fieldRepresentation,
                            ResourceHolder resources,
                            int associatedScore,
                            Symbol multiplier,
                            ResourceHolder requirements,
                            Symbol color
    ) {
        super(id, fieldRepresentation, resources, associatedScore, color);
        this.multiplier = multiplier;
        this.requirements = requirements;
    }


    public ResourceHolder requirements() {
        return requirements;
    }


    public Symbol getMultiplier() {
        return multiplier;
    }
}
