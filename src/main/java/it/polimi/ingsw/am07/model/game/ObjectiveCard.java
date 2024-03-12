package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPattern;

record ObjectiveCard(
        int associatedScore,
        ResourceHolder requirements,
        GameFieldPattern pattern
) { }
