package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.Symbol;

public record SideFieldRepresentation(
        Symbol topLeft,
        Symbol topRight,
        Symbol bottomLeft,
        Symbol bottomRight
) { }
