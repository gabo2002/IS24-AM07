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
package it.polimi.ingsw.am07.client.cli.input;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadInputReader {

    private final Thread inputThread;
    private final BlockingQueue<String> inputs;
    private final Scanner scanner;

    /**
     * Constructs a new ThreadInputReader and starts the input reading thread.
     * The thread continuously reads input from the console and adds it to the queue.
     */
    public ThreadInputReader() {
        scanner = new Scanner(System.in);
        inputs = new LinkedBlockingQueue<>();

        inputThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String input = scanner.nextLine();
                    inputs.put(input);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        inputThread.start();
    }
    /**
     * Stops the input reading thread.
     * This method should be called when the ThreadInputReader is no longer needed to free up system resources.
     */
    public void stop() {
        inputThread.interrupt();
    }

    /**
     * Retrieves and removes the next input string from the queue.
     * This method blocks if the queue is empty until an element becomes available.
     *
     * @return the next input string from the queue
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public String getInput() throws InterruptedException {
        return inputs.take();
    }

    /**
     * Retrieves the next input as an integer.
     * This method repeatedly prompts the user until a valid integer is entered.
     *
     * @return the next input as an integer
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public int getInt() throws InterruptedException {
        while (true) {
            try {
                return Integer.parseInt(getInput());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    /**
     * Retrieves the next input as an integer within a specified range.
     * This method repeatedly prompts the user until a valid integer within the specified range is entered.
     *
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @return the next input as an integer within the specified range
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public int getInt(int min, int max) throws InterruptedException {
        while (true) {
            int input = getInt();
            if (input >= min && input <= max) {
                return input;
            } else {
                System.out.println("Please enter a number between " + min + " and " + max);
            }
        }
    }
}
