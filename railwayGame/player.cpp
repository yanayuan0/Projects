#include <string>
#include <vector>
#include <cstdlib>
#include "player.h"
#include "goal.h"


using std::vector;
using std::string;



Player::Player(int tracks, Graph<string, int, int>* graph){
  this->tracks = tracks;
  this->score = 0;
  this->goals = vector<Goal*>();
  this->g = new AdjacencyListUndirectedGraph<string, int, int>();
  this->possibleMoves = new AdjacencyListUndirectedGraph<string, int, int>();
  this->graph = graph;
  
  //make a copy of the big graph so that the original graph won't be mutated.
  vector<string> vertices = graph->getVertices();
  vector<Edge<string, int, int>> edges = graph->getEdges();
  for (int i=0; i<vertices.size(); i++){
    this->possibleMoves->insertVertex(vertices[i]);
  }
  for (int i=0; i<edges.size(); i++){
    this->possibleMoves->insertEdge(edges[i].getSource(), edges[i].getDestination(), edges[i].getLabel(), edges[i].getWeight());
  }

}

Player::~Player(){
  delete this->g;
  delete this->possibleMoves;
  for (int i=0; i<this->goals.size(); i++){
    delete this->goals[i];
  }
  
}

int Player::getNumofCities(){
  return this->g->getVertices().size();
}

int Player::getTracks(){
  return this->tracks;
}

void Player::updateTracks(int newTracks){
  this->tracks = newTracks;
}

int Player::getScore(){
  return this->score;
}

void Player::updateScore(int newScore){
  this->score = newScore;
}

Graph<string, int, int>* Player::getOwnGraph(){
  return this->g;
}

Graph<string, int, int>* Player::getPossibleGraph(){
  return this->possibleMoves;
}


bool Player::contains(string v){
  return this->g->containsVertex(v);
}


vector<Goal*> Player::updateGoals(){
  for (int i=0; i<this->goals.size(); i++){
    Goal* currentGoal = this->goals[i];
    if (currentGoal->getMessage() != "Impossible" && currentGoal->getMessage() != "Completed"){ ////
      string location1 = currentGoal->getLocation(1);
      string location2 = currentGoal->getLocation(2);
      //check if completed
      if (reachableDFS(location1, location2, this->g)){
        int point = currentGoal->getPoint(); ///
        delete currentGoal;
        Goal* newGoal = new Goal(location1, location2, "Completed");
        this->goals[i] = newGoal;
        this->updateScore(this->score + point); ///
      } 

      //check if impossible
      Dictionary<string, int>* SSSP = singleSourceShortestPath(location1, this->possibleMoves);
      if (!SSSP->contains(location2)){
        delete currentGoal;
        Goal* newGoal = new Goal(location1, location2, "Impossible");
        this->goals[i] = newGoal;

      }
      delete SSSP;
    }
  }  
  return this->goals;
}

void Player::generateGoals(){
  while (this->goals.size() < 4){
    vector<string> vertices = this->graph->getVertices();
    int random1= rand() % vertices.size();
    int random2= rand() % vertices.size();
    string location1 = vertices[random1];
    string location2 = vertices[random2];
    vector<string> shortestpath = shortestLengthPathBFS(location1, location2, this->graph);
    while (shortestpath.size() < 4){ ////
      random1= rand() % vertices.size();
      random2= rand() % vertices.size();
      location1 = vertices[random1];
      location2 = vertices[random2];
      shortestpath = shortestLengthPathBFS(location1, location2, this->graph);
    }
    int points = 0;
    for (int i=0; i<shortestpath.size()-1; i++){
      int weight = this->graph->getEdge(shortestpath[i], shortestpath[i+1]).getWeight();
      points += weight;
    }
    string message = std::to_string(points/4);
    Goal* newGoal = new Goal(location1, location2, message + " points");
    newGoal->updatePoint(points/4); ///
    bool repeat = false;
    for (int i=0; i<this->goals.size(); i++){
      Goal* goal = this->goals[i];
      if (goal->getLocation(1) == location1 && goal->getLocation(2) == location2){
        repeat = true;
        delete newGoal;
      } if (goal->getLocation(1) == location2 && goal->getLocation(2) == location1){
        repeat = true;
        delete newGoal;
      }
    }
    if (!repeat){
      this->goals.push_back(newGoal);
    }
  }

}
