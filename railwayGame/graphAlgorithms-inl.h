/*
  Copyright (c) 2023
  Swarthmore College Computer Science Department, Swarthmore PA
  J. Brody, A. Danner, L. Fontes, L. Meeden, Z. Palmer, A. Soni
  Distributed as course material for Spring 2023
  CPSC 035: Data Structures and Algorithms
*/

#include <stdexcept>
#include "adts/stlStack.h"
#include "adts/stlHashTable.h"
#include "adts/stlQueue.h"
#include "adts/stlMinPriorityQueue.h"
#include "adts/stlList.h"


template <typename V, typename E, typename W>
bool reachableDFS(V src, V dest, Graph<V, E, W>* g) {
    STLStack<V> stack;
    STLHashTable<V, bool> visited;
    stack.push(src);
    visited.insert(src, true);
    while(!stack.isEmpty()){
        V current = stack.pop();
        if (current == dest){
            return true;
        }
        vector<V> neighbors = g->getNeighbors(current);
        for (int i=0; i<neighbors.size(); i++){
            if (!visited.contains(neighbors[i])){
                visited.insert(neighbors[i], true);
                stack.push(neighbors[i]);
            }
        }
    }
    return false;

}

template <typename V, typename E, typename W>
vector<V> shortestLengthPathBFS(V src, V dest, Graph<V, E, W>* g) {
    vector<V> path;
    STLQueue<V> queue;
    STLHashTable<V,V> previous;
    STLList<V> returnList;
    queue.enqueue(src);
    previous.insert(src, src);
    while (!queue.isEmpty()){
        V current = queue.dequeue();
        if (current == dest){
            V curr = dest;
            while (curr != src){
                returnList.insertFirst(curr);
                curr = previous.get(curr);
            }
            returnList.insertFirst(src);
            path = returnList.toVector();
            return path;
        }
        vector<V> neighbors = g->getNeighbors(current);
        for (int i=0; i<neighbors.size(); i++){
            if (!previous.contains(neighbors[i])){
                previous.insert(neighbors[i], current);
                queue.enqueue(neighbors[i]);
            }
        }
    }
    throw runtime_error("Error: no path found");
}

template <typename V, typename E, typename W>
Dictionary<V, W>* singleSourceShortestPath(V src, Graph<V, E, W>* g) {
    Dictionary<V,W>* cost = new STLHashTable<V,W>();
    PriorityQueue<W,V>* pq = new STLMinPriorityQueue<W,V>();
    cost->insert(src, 0);
    pq->insert(0, src);
    while (!pq->isEmpty()){
        W currPriority = pq->peekPriority();
        V currVertex = pq->remove();
        W currCost = cost->get(currVertex);
        if (currCost == currPriority){
            vector<Edge<V,E,W>> outgoings = g->getOutgoingEdges(currVertex);
            for (int i=0; i<outgoings.size(); i++){
                Edge<V,E,W> e = outgoings[i];
                V nextVertex = e.getDestination();
                W newCost = currCost + e.getWeight();
                if (!cost->contains(nextVertex)){
                    cost->insert(nextVertex, newCost);
                    pq->insert(newCost, nextVertex);
                } else if (newCost < cost->get(nextVertex)){
                    cost->update(nextVertex, newCost);
                    pq->insert(newCost, nextVertex);
                }
            }
        }
    }
    delete pq;
    return cost;

}