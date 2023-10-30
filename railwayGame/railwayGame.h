/*
  Copyright (c) 2023
  Swarthmore College Computer Science Department, Swarthmore PA
  J. Brody, A. Danner, L. Fontes, L. Meeden, Z. Palmer, A. Soni
  Distributed as course material for Spring 2023
  CPSC 035: Data Structures and Algorithms
*/

#pragma once

#include <string>
#include <vector>

using std::string;
using std::vector;

#include "railwayGUI.h"
#include "player.h"



/**
 * An instance of this class represents a single game of Railway.  An object of
 * type RailwayGame does not represent the user interface, but does represent
 * the idea of a game of Railway, including the state of the board and all of
 * the information pertaining to both players.  It also includes the logic for
 * making moves in the game and enforces the game's rules.
 */
class RailwayGame {
  public:
    /**
     * Create a new RailwayGame object to keep track of the state of the game.
     * @param graph The input graph that represents the game board.
     */
    RailwayGame(Graph<string, int, int>* graph);

    /**
     * Destructor, destroy the RailwayGame object.
     */
    ~RailwayGame();

    /**
     * Simulate the player's turn when they choose to pass.
     * @param playerNum indicates whose turn it is, 1 or 2.
     */
    void pass(int playerNum);

    /**
     * Simulate the player's turn when they choose an edge.
     * @param playerNum indicates whose turn it is, 1 or 2.
     * @param nextMoveVector the return of GUI getNextMove function; indicates 
                              which edge the player has chosen.
     * @return a message indicating if the Player can choose the edge.
     */
    string edge(int playerNum, vector<string> nextMoveVector);

    /**
     * Get the player's current score.
     * @param playerNum indicates whose turn it is, 1 or 2.
     * @return the player's current score.
     */
    int getScore(int playerNum);

    /**
     * Get the number of player's current tracks left.
     * @param playerNum indicates whose turn it is, 1 or 2.
     * @return the number of player's current tracks.
     */
    int getTracks(int playerNum);

    /**
     * Get the three random goals of the player
     * @param playerNum indicates whose turn it is, 1 or 2.
     * @return a vector of goal object pointers
     */
    vector<Goal*> getGoals(int playerNum);

    void createGoals(int playerNum);

    bool endGame();

  private:
    Graph<string, int, int>* graph;
    Player* player1;
    Player* player2; 
    

    // You can safely ignore the following code.  This eliminates some default
    // class routines, preventing you from using them accidentally.
    // Specifically, we are disabling the use of the copy constructor and the
    // copy assignment operator.  You can read more here:
    //   http://www.cplusplus.com/articles/y8hv0pDG/
    RailwayGame(const RailwayGame& game) = delete;
    RailwayGame& operator=(const RailwayGame& game) = delete;
};


/*
Player object?
  - number of tracks left
  - current score
  - paths owned

1. Before game starts
  graph:    // all edges labels = 0
  Player 1: 
    number of tracks left: 35
    current score: 0
    random generated goals and their corresponding points (breadth first search)
    no paths owned
  Player 2:
    number of tracks left: 50
    current score: 0
    random generated goals and their corresponding points (breadth first search)
    no paths owned

2. Game starts
  While the window isn't closed
    Player 1's turn:
      a. place a route      //update graph, edge label = 1
        if the route is
          - unowned
          - connect to owned routes (except the first one)
          - weight of edge < owned tracks
          player 1 owns the route
        status update:
          number of tracks left: current - edge weight
          current score: 
            if goal accomplished, add corresponding points and 1
            if goal not accomplished
              if first turn, + 2
              else, + 1
          owned path + 1
          goal status (points, completed, impossible)
      b. pass
        status update:
          number of tracks left: current + 2
          current score doesn't change
          owned path doesn't change
          goal status doesn't change
    Player 2's turn:
      the same as Player 1





*/