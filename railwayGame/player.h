#pragma once

#include <string>
#include <vector>


using std::vector;
using std::string;

#include "adts/graph.h"
#include "adts/edge.h"
#include "adjacencyListUndirectedGraph.h"
#include "goal.h"
#include "graphAlgorithms.h"

/**
 * An instance of this class represents a single player.  An object of
 * type Player represents one player.
 * 
 * [TO BE DELETED] the idea of a game of Railway, including the state of the board and all of
 * the information pertaining to both players.  It also includes the logic for
 * making moves in the game and enforces the game's rules.
 */
class Player {
  public:
    /**
     * Create a new player and keep track of their status
     * @param tracks the initial tracks the player owns, 35 for player 1 and 50 for
                     player 2.
     * @param graph indicates all paths the Players could choose.
     */
    Player(int tracks, Graph<string, int, int>* graph);

    /**
     * Destructs the Player object.
     */
    ~Player();

    Graph<string, int, int>* getOwnGraph();

    Graph<string, int, int>* getPossibleGraph();

    int getNumofCities();

    /**
     * Get the number of tracks of the Player.
     * @return the Player's number of tracks.
     */
    int getTracks();

    /**
     * Update the Player's number of tracks.
     * @param newTracks the new number of tracks to update.
     */
    void updateTracks(int newTracks);

    /**
     * Determines the current score of the Player.
     * @return The number of scores owned by the Player.
     */
    int getScore();

    /**
     * Update the Player's current score.
     * @param newScore the new score to update.
     */
    void updateScore(int newScore);

    /**
     * Keep track of the status of the Player's goals
     * @return a vector of pointers to updated goals (indicating impossible/complete/points).
     */
    vector<Goal*> updateGoals();

    /**
     * Check if the Player already owns the city
     * @return true if Player owns the city, false otherwise.
     */
    bool contains(string v);
  

    /**
     * Generate three random goals
     */
    void generateGoals();


  
  private:
    /** A number representing the number of tracks owned by the Player. */
    int tracks;
    /** A number representing the current score of the Player. */
    int score;
    /** A vector containing the three goals of the Player*/
    vector<Goal*> goals;
    /** A graph tracking all the vertices and edges the Player owns*/
    Graph<string, int, int>* g;
    /** A graph that deletes any edges already chosen*/
    Graph<string, int, int>* possibleMoves;

    Graph<string, int, int>* graph;

};




/**
 * a graph representing the map:
 * @vertice: <string city name, bool owned>
 * @edgeLabel: status of the route -- "owned by Player 1 or 2", "unowned"
 * @edgeWeight: points of the route
 * 
 * 
 * 
*/

    /** A vector containing the statuso of three goals of the Player: incomplete, completed, or impossible. */
    //vetor<string> goalStatus;
    /** A number representing the number of cities connected by the Player. */
    //int cityCount;



    /**
     * Determines the number of cities connected by the Player.
     * @return The number of cities connected by the Player.
     */
    //int getCityCount();

    /**
     * [to be delieted]
     * Adds an item with given priority and value to this priority queue.  The
     * meaning of this priority is dependent upon the implementation.
     * @param priority The priority of the item being added.
     * @param value The value to store in the queue at that priority.
     */
    //virtual void insert(P priority, V value) = 0;  