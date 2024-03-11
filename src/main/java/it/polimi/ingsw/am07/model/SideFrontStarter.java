package it.polimi.ingsw.am07.model;

import java.util.Optional;

public final class SideFrontStarter extends SideFront {

    public SideFrontStarter(int id,
                            SideFieldRepresentation fieldRepresentation
    ) {
        super(id, fieldRepresentation, new ResourceHolder());
    }

    @Override
    public Optional<ResourceHolder> requirements() {
        return Optional.empty();
    }

    @Override
    public int calculateContributingScore(ResourceHolder resources) { return 0; }

}
