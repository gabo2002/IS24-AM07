package it.polimi.ingsw.am07.model;

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

    @Override
    public Optional<ResourceHolder> requirements() {
        return Optional.of(requirements);
    }

    @Override
    public int calculateContributingScore(ResourceHolder gameResources) {
        return 1;
    }

}
