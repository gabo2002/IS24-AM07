package it.polimi.ingsw.am07.model;

record ObjectiveCard(
        int associatedScore,
        ResourceHolder requirements,
        GameFieldPattern pattern
) { }
