package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.utils.Matrix;

public record SideFieldRepresentation(
        Matrix<Symbol> corners
) { }
