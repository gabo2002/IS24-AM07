# Prova Finale di Ingegneria del Software - AA 2023-2024

## Gruppo AM07
 - Andrea Biasion Somaschini (@AndreaBiasion)
 - Gabriele Corti (@gabo2002)
 - Roberto Alessandro Bertolini (@MrIndeciso)
 - Omar Chaabani (@Chaba02)

## Advanced Functionality implemented
- **Multiple games**: the server can handle multiple games at the same time. Each game is independent from the others and can have different players.
- **Reconnection**: players can reconnect to the server after a disconnection. The game will be restored to the state it was before the disconnection.
- **Chat**: players can send messages to each other during the game. The chat is available both in CLI and GUI mode.
- **Persistence**: the server saves the state of the game in a file every time a player makes a move. This allows the server to restore the game in case of a crash.

## Usage
To start the game, you need to run one the jar files in the `deliverables` folder, according to your OS.
We have provided two different jar files:
- `deliverables/codex-linux.jar` for Linux users
- `deliverables/codex-macos.jar` for MacOS users

To run the game, you need to connect to a server. You can either run your own server or connect to the one we have provided.
To run your own server, you need to run the following command:

```bash
java -jar codex-linux.jar server
```

After this, your server will be up and running and it will listen for TCP connections on port 12345.
It will also listen on port 23456 for RMI connections.


To connect to the server and play the game, you can play in two different modes:
- CLI mode
- GUI mode

To play in CLI mode, you need to run the following command:

```bash
java -jar codex-linux.jar cli
```

To play in GUI mode, you need to run the following command:

```bash
java -jar codex-linux.jar gui
```

You can also add the `-refresh=True` flag to specify if you want to update your identifier or not.
Every time you connect to the server, you will be assigned an identifier that will be saved in the `identity.txt` file.
