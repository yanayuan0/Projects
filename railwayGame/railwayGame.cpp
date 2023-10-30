/*
  Copyright (c) 2023
  Swarthmore College Computer Science Department, Swarthmore PA
  J. Brody, A. Danner, L. Fontes, L. Meeden, Z. Palmer, A. Soni
  Distributed as course material for Spring 2023
  CPSC 035: Data Structures and Algorithms
*/

#include <string>
#include <vector>
#include <sstream>

#include "railwayGame.h"

using std::vector;
using std::string;
using std::cout;
using std::endl;

RailwayGame::RailwayGame(Graph<string, int, int>* graph){
  this->graph = graph;
  this->player1 = new Player(35, graph);
  this->player2 = new Player(50, graph);
}

RailwayGame::~RailwayGame(){
  delete this->player1;
  delete this->player2;
}

void RailwayGame::pass(int playerNum){
  if (playerNum == 1){
    player1->updateTracks(player1->getTracks() + player1->getNumofCities());
  } else{
    player2->updateTracks(player2->getTracks() + player2->getNumofCities());
  }
}

/**
 * The Player cannot claim an edge if
 *    1. The edge is already claimed (return "Already claimd")
 *    2. The number of tracks isn't enough (return "Number of tracks not enough")
 *    3. Not connect to current ones (return "Not connected to owned cities")
 * return "Valid choice" and change the graph
 */
string RailwayGame::edge(int playerNum, vector<string> nextMoveVector){
  Player* curplayer;
  Player* otherplayer;
  int label;

  //distribute player
  if (playerNum == 1){
    curplayer = this->player1;
    otherplayer = this->player2;
    label = 1;
  } else{
    curplayer = this->player2;
    otherplayer = this->player1;
    label = 2;
  }

  string location1 = nextMoveVector[1]; ////
  string location2 = nextMoveVector[2];
  Edge<string,int, int> edge = this->graph->getEdge(location1, location2);
  Graph<string, int, int>* playerGraph = curplayer->getOwnGraph();

  //case1: edge already claimed
  if (edge.getLabel() != 0){
    return "The edge is already claimed. Try again Player " + std::to_string(label);

  //case 2: not enough tracks
  } else if (edge.getWeight() > curplayer->getTracks()){
    return "Number of tracks not enough. Try again Player " + std::to_string(label);

  //case 3a: player's first round
  } else if (curplayer->getScore() == 0 || curplayer->contains(location1) || curplayer->contains(location2)){ 
      //update graph, edge label changed
      this->graph->removeEdge(location1, location2);
      this->graph->insertEdge(location1, location2, label, edge.getWeight());

      //update other Player's available paths
      otherplayer->getPossibleGraph()->removeEdge(location1, location2);

      //update Player's own graph
      int currentNum = curplayer->getNumofCities();
      playerGraph->insertVertex(location1);
      playerGraph->insertVertex(location2);
      playerGraph->insertEdge(location1, location2, label, edge.getWeight());

      //update Player's tracks and score
      curplayer->updateScore(curplayer->getScore() - currentNum + curplayer->getNumofCities());
      curplayer->updateTracks(curplayer->getTracks() - edge.getWeight());

      //update goals (in getGoals function)
      this->getGoals(1);
      this->getGoals(2);
      return "Valid choice.";

  //case 4: edge not connected to any owned cities
  } else { 
      return "Not connected to owned cities. Try again Player " + std::to_string(label);
  return "";
}
}

int RailwayGame::getScore(int playerNum){
  if (playerNum == 1){
    return this->player1->getScore();
  } else{
    return this->player2->getScore();
  }
}

int RailwayGame::getTracks(int playerNum){
  if (playerNum == 1){
    return this->player1->getTracks();
  } else{
    return this->player2->getTracks();
  }
}

vector<Goal*> RailwayGame::getGoals(int playerNum){
  if (playerNum == 1){
    return this->player1->updateGoals();
  } else{
    return this->player2->updateGoals();
  }
}

void RailwayGame::createGoals(int playerNum){
  if (playerNum == 1){
    this->player1->generateGoals();
  } else{
    this->player2->generateGoals();
  }
}

bool RailwayGame::endGame(){
  vector<Edge<string, int, int>> edges = this->graph->getEdges();
  for (int i=0; i<edges.size(); i++){
    if (edges[i].getLabel() == 0){
      //cout << i << endl;
      //cout << "return false" << endl;
      return false;
    }
  }
  return true;
}
