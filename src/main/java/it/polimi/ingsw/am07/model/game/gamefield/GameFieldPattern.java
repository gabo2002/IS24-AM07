package it.polimi.ingsw.am07.model.game.gamefield;

import it.polimi.ingsw.am07.model.game.Symbol;

import java.util.ArrayList;

public record GameFieldPattern(
    ArrayList<ArrayList<Symbol>> pattern
) {

}
