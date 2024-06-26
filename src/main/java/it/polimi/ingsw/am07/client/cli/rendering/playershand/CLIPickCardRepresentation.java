/*
 * Codex Naturalis - Final Assignment for the Software Engineering Course
 * Copyright (C) 2024 Andrea Biasion Somaschini, Roberto Alessandro Bertolini, Omar Chaabani, Gabriele Corti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Please note that the GNU General Public License applies only to the
 * files that contain this license header. Other files within the project, such
 * as assets and images, are property of the original owners and may be
 * subject to different copyright terms.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.polimi.ingsw.am07.client.cli.rendering.playershand;

import it.polimi.ingsw.am07.client.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.client.cli.rendering.common.side.CLISideRepresentation;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.Side;

public class CLIPickCardRepresentation implements CLIElement {

    private final GameCard[] visibleResCards;
    private final GameCard[] visibleGoldCards;
    private final Side backResCard;
    private final Side backGoldCard;

    /**
     * Constructs a CLIPickCardRepresentation. It represents the cards that the player can pick from.
     * @param visibleResCards
     * @param visibleGoldCards
     * @param backResCard
     * @param backGoldCard
     */
    public CLIPickCardRepresentation(GameCard[] visibleResCards, GameCard[] visibleGoldCards, Side backResCard, Side backGoldCard) {
        this.visibleResCards = visibleResCards;
        this.visibleGoldCards = visibleGoldCards;
        this.backResCard = backResCard;
        this.backGoldCard = backGoldCard;
    }

    /**
     * Renders the representation of the cards that the player can pick from.
     * @return the string representation of the cards that the player can pick from
     */
    @Override
    public String render() {

        int counter = 0;
        StringBuilder builder = new StringBuilder();

        builder.append("You can pick from the following cards:\n");
        builder.append("Resources cards:\n");
        for (GameCard card : visibleResCards) {
            builder.append("Card " + counter + ":\n");
            builder.append(renderCard(card));
            counter++;
        }

        // backResCard is the back of the resource cards
        builder.append("Card " + counter + ":\n");
        builder.append("Back:");
        builder.append(new CLISideRepresentation.Factory(backResCard).large().render());
        builder.append("\n");
        counter++;

        builder.append("Gold cards:\n");
        for (GameCard card : visibleGoldCards) {
            builder.append("Card " + counter + ":\n");
            builder.append(renderCard(card));
            counter++;
        }

        // backGoldCard is the back of the gold cards
        builder.append("Card " + counter + ":\n");
        builder.append("Back:");
        builder.append(new CLISideRepresentation.Factory(backGoldCard).large().render());
        builder.append("\n");

        return builder.toString();

    }


    private String renderCard(GameCard card) {
        StringBuilder builder = new StringBuilder();
        CLISideRepresentation frontRepresentation = new CLISideRepresentation.Factory(card.front()).large();
        CLISideRepresentation backRepresentation = new CLISideRepresentation.Factory(card.back()).large();

        builder.append("Front:").append(" ".repeat(10)).append("Back:\n");

        for(int i = 0; i < frontRepresentation.height(); i++) {
            for(int j = 0; j < frontRepresentation.width(); j++) {
                builder.append(frontRepresentation.getMatrix().get(j, i).render());
            }

            //Add a space between the front and the back of the card
            builder.append(" ");

            for(int j = 0; j < backRepresentation.width(); j++) {
                builder.append(backRepresentation.getMatrix().get(j, i).render());
            }

            builder.append("\n");
        }
        return builder.toString();
    }
}
