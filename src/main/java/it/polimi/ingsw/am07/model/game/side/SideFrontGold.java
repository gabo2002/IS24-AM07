package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;

import java.util.Optional;

public final class SideFrontGold extends SideFront {

    private final int associatedScore;

    private final Symbol multiplier;

    private final ResourceHolder requirements;

    public SideFrontGold(int id,
                            SideFieldRepresentation fieldRepresentation,
                            ResourceHolder resources,
                            int associatedScore,
                            Symbol multiplier,
                            ResourceHolder requirements
    ) {
        super(id, fieldRepresentation, resources);
        this.associatedScore = associatedScore;
        this.multiplier = multiplier;
        this.requirements = requirements;
    }


    public ResourceHolder requirements() {
        return requirements;
    }


    public int calculateContributingScore(ResourceHolder gameResources, int coveredCorners) {
        return 1;
    }

}
