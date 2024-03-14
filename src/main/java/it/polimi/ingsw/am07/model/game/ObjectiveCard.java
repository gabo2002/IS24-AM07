package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPattern;

public record ObjectiveCard(
        int associatedScore,
        ResourceHolder requirements,
        GameFieldPattern pattern
) { }
