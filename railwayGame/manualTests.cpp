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
  /*
  srand(time(nullptr));

  // Get command-line arguments.
  string baseName = "test_data/Europe";
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



  int playerNum = 1;
  vector<string> nextMoveVector;

  nextMoveVector[0] = "edge";
  nextMoveVector[1] = "BRN";
  nextMoveVector[2] = "FRK";


  Player* curplayer = new Player(35, graph);
  Player* otherplayer = new Player(50, graph);
  
  int label;

  string location1 = nextMoveVector[1]; ////
  string location2 = nextMoveVector[2];
  Edge<string,int, int> edge = graph->getEdge(location1, location2);
  Graph<string, int, int>* playerGraph = curplayer->getOwnGraph();

  cout << edge.getSource() << edge.getSource() << endl;

  return 0;
  */
}
