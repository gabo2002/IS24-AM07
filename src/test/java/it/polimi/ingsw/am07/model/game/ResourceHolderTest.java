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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceHolderTest {

    @Test
    void testSubtract() {
        ResourceHolder emptyHolder = new ResourceHolder();
        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.PURPLE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        // Subtracting a holder from itself should result in an empty holder
        ResourceHolder test = new ResourceHolder(side);
        test.subtract(test);
        assertEquals(emptyHolder, test);

        // Subtracting an empty holder from a non-empty holder should result in the same holder
        test = new ResourceHolder(side);
        test.subtract(emptyHolder);
        assertEquals(new ResourceHolder(side), test);

        // Subtracting a non-empty holder from an empty holder should throw an exception
        assertThrows(IllegalArgumentException.class, () -> emptyHolder.subtract(new ResourceHolder(side)));

        // Subtracting a non-empty holder from another non-empty holder should result in the difference
        ResourceHolder holder = new ResourceHolder(side, List.of(Symbol.PURPLE, Symbol.BLUE));
        ResourceHolder subtracted = new ResourceHolder(side, List.of(Symbol.PURPLE));
        holder.subtract(subtracted);
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.BLUE) {
                assertEquals(1, holder.countOf(symbol));
            } else {
                assertEquals(0, holder.countOf(symbol));
            }
        }
    }

    @Test
    void testContains() {
        ResourceHolder emptyHolder = new ResourceHolder();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.PURPLE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        // An empty holder should contain another empty holder
        assertTrue(emptyHolder.contains(emptyHolder));

        // An empty holder should not contain a non-empty holder
        assertFalse(emptyHolder.contains(new ResourceHolder(side)));

        // A non-empty holder should contain an empty holder
        assertTrue(new ResourceHolder(side).contains(emptyHolder));

        // A non-empty holder should contain a holder with the same resources
        assertTrue(new ResourceHolder(side).contains(new ResourceHolder(side)));

        // A non-empty holder should contain a holder with fewer resources
        assertTrue(new ResourceHolder(side, List.of(Symbol.PURPLE)).contains(new ResourceHolder(side)));

        // A non-empty holder should not contain a holder with more resources
        assertFalse(new ResourceHolder(side).contains(new ResourceHolder(side, List.of(Symbol.PURPLE))));
    }

    @Test
    void testAdd() {
        ResourceHolder emptyHolder = new ResourceHolder();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.PURPLE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        // Adding an empty holder to another empty holder should result in an empty holder
        ResourceHolder test = new ResourceHolder();
        test.add(emptyHolder);
        assertEquals(emptyHolder, test);

        // Adding an empty holder to a non-empty holder should result in the same holder
        test = new ResourceHolder(side);
        test.add(emptyHolder);
        assertEquals(new ResourceHolder(side), test);

        // Adding a non-empty holder to an empty holder should result in the non-empty holder
        test = new ResourceHolder();
        test.add(new ResourceHolder(side));
        assertEquals(new ResourceHolder(side), test);

        // Adding a non-empty holder to another non-empty holder should result in the sum
        ResourceHolder holder = new ResourceHolder(side, List.of(Symbol.FLASK, Symbol.SCROLL));
        ResourceHolder added = new ResourceHolder(side, List.of(Symbol.FLASK));
        holder.add(added);
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.RED || symbol == Symbol.BLUE || symbol == Symbol.GREEN || symbol == Symbol.PURPLE) {
                assertEquals(2, holder.countOf(symbol));
            } else if (symbol == Symbol.FLASK) {
                assertEquals(2, holder.countOf(symbol));
            } else if (symbol == Symbol.SCROLL) {
                assertEquals(1, holder.countOf(symbol));
            } else {
                assertEquals(0, holder.countOf(symbol));
            }
        }
    }

    @Test
    void testCountOf() {
        ResourceHolder holder = new ResourceHolder();
        for (Symbol symbol : Symbol.values()) {
            assertEquals(0, holder.countOf(symbol));
        }

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.PURPLE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        // A holder with no additional symbols should have the symbols of the side
        holder = new ResourceHolder(side);
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.RED || symbol == Symbol.BLUE || symbol == Symbol.GREEN || symbol == Symbol.PURPLE) {
                assertEquals(1, holder.countOf(symbol));
            } else {
                assertEquals(0, holder.countOf(symbol));
            }
        }

        // A holder with additional symbols should have the symbols of the side and the additional symbols
        holder = new ResourceHolder(side, List.of(Symbol.PURPLE, Symbol.BLUE));
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.RED || symbol == Symbol.GREEN) {
                assertEquals(1, holder.countOf(symbol));
            } else if (symbol == Symbol.PURPLE || symbol == Symbol.BLUE) {
                assertEquals(2, holder.countOf(symbol));
            } else {
                assertEquals(0, holder.countOf(symbol));
            }
        }
    }

}