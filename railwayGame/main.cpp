/*
  Copyright (c) 2023
  Swarthmore College Computer Science Department, Swarthmore PA
  J. Brody, A. Danner, L. Fontes, L. Meeden, Z. Palmer, A. Soni
  Distributed as course material for Spring 2023
  CPSC 035: Data Structures and Algorithms
*/

#include <cstdlib>
#include <ctime>
#include <iostream>
#include <string>

#include "ioUtils.h"
#include "railwayGUI.h"
#include "railwayGame.h"

using namespace std;

int main(int argc, char** argv) {
    // Check command line arguments and give up if necessary.
    if (argc != 2) {
        cerr << "Expected one argument:" << endl;
        cerr << "  Base filename of map data" << endl;
        cerr << "  e.g., test_data/Europe " << endl;
        return 1;
    }

    // Initialize randomizer.  This should happen before any random numbers are
    // selected.
    srand(time(nullptr));

    // Get command-line arguments.
    string baseName(argv[1]);
    string graphDataFilename = baseName + "_graphData.txt";
    string vertexPositionFilename = baseName + "_vertexPositions.txt";
    string backgroundImageFilename = baseName + "_background.png";

    // Retrieve vertex positions (so we know where each vertex goes on the map).
    Dictionary<string, pair<int, int>>* vertexPositions;
    try {
        vertexPositions = readVertexPositions(vertexPositionFilename);
    } catch (exception e) {
        cout << "Could not read vertex positions file "
             << vertexPositionFilename << ": " << e.what() << endl;
        return 1;
    }

    // Load the initial graph.
    Graph<string, int, int>* graph;
    try {
        graph = readRailwayGraph(graphDataFilename);
    } catch (exception e) {
        cout << "Could not read graph data file " << graphDataFilename << ": "
             << e.what() << endl;
        delete vertexPositions;
        return 1;
    }

    // Create the GUI object here.  This is statically allocated, so the GUI
    // will disappear the moment your program leaves the main function.
    // The GUI object should only be used by main, and not by any other function
    // or object.
    RailwayGUI gui(backgroundImageFilename, vertexPositions);

    // Tell the GUI about the graph we have.
    gui.updateRailwayMap(graph);

    RailwayGame game(graph);
    game.createGoals(1);
    game.createGoals(2);
    gui.updatePlayerStatus(1, 0, 35, game.getGoals(1)); 
    gui.updatePlayerStatus(2, 0, 50, game.getGoals(2));
    gui.updateMessage("Player 1: it is your turn");

    string nextMove = "";
    int playerNum = 1;
    while (nextMove != "close"){
        if (game.endGame()){
            cout << "All routes have been claimed! The game is over." << endl;
            break;
        }
        vector<string> nextMoveVector = gui.getNextMove();
        nextMove = nextMoveVector[0];
        if (nextMove == "close"){
            break;
        }
        // When Player chooses to pass
        if (nextMove == "pass"){
            game.pass(playerNum);
          // When player chooses an edge
        } else if (nextMove == "edge"){
            string message = game.edge(playerNum, nextMoveVector); 
            if (message != "Valid choice."){
                gui.updateMessage(message);
                continue;
            }
        }
        gui.updatePlayerStatus(1, game.getScore(1), game.getTracks(1), game.getGoals(1)); //goal?
        gui.updatePlayerStatus(2, game.getScore(2), game.getTracks(2), game.getGoals(2));
        gui.updateRailwayMap(graph);
        if (playerNum == 1){
            gui.updateMessage("Player 2: it is your turn");
            playerNum = 2;
        } else if (playerNum == 2){
            gui.updateMessage("Player 1: it is your turn");
            playerNum = 1;
        }
        
    }

    cout << "Final score: " << endl;
    cout << "Player 1: " << game.getScore(1) << endl;
    cout << "Player 2: " << game.getScore(2) << endl;
    if (game.getScore(1) > game.getScore(2)){
        cout << "Player 1 wins!" << endl;
    } else if (game.getScore(1) < game.getScore(2)){
        cout << "Player 2 wins!" << endl;
    } else{
        cout << "It's a tie!" << endl;
    }

    // Finally, clean up and exit successfully.

    delete graph;
    delete vertexPositions;
    return 0;
}
