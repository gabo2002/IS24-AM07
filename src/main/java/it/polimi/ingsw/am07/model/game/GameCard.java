package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFront;

public record GameCard(
        SideFront front,
        SideBack back
) {

}
