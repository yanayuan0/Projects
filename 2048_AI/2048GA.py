from ga_2048 import *
import random
import logic_ga


class GA2048(GeneticAlgorithm):
    def __init__(self, length, pop_size):
        self.bestBoard = None
        self.pCrossover = 0.05
        self.pMutation = 0.1
        # Call the parent class constructor
        super(GA2048, self).__init__(length, pop_size)

    def fitness(self, chromosome):
        mat = logic_ga.start_game()
        for x in chromosome:
            if(x == 'W' or x == 'w'):
                mat, flag = logic_ga.move_up(mat)
                status = logic_ga.get_current_state(mat)
                if(status == 'GAME NOT OVER'):
                    logic_ga.add_new_2(mat)
                else:
                    break

            elif(x == 'S' or x == 's'):
                mat, flag = logic_ga.move_down(mat)
                status = logic_ga.get_current_state(mat)
                if(status == 'GAME NOT OVER'):
                    logic_ga.add_new_2(mat)
                else:
                    break

            elif(x == 'A' or x == 'a'):
                mat, flag = logic_ga.move_left(mat)
                status = logic_ga.get_current_state(mat)	
                if(status == 'GAME NOT OVER'):
                    logic_ga.add_new_2(mat)
                else:
                    break

            elif(x == 'D' or x == 'd'):
                mat, flag = logic_ga.move_right(mat)
                status = logic_ga.get_current_state(mat)
                if(status == 'GAME NOT OVER'):
                    logic_ga.add_new_2(mat)
                else:
                    break
                
        score = logic_ga.printboard(mat)
        return score,mat

    def evaluatePopulation(self):
        self.scores = []
        for i,pop in enumerate(self.population):
            fit = self.fitness(pop)
            fitness = fit[0]
            self.scores.append(fitness)
            if fitness > self.bestEverScore:
                self.bestEverScore = fitness
                self.bestEver = pop
                self.bestBoard = fit[1]
                print("A new best was found. Its score is " + str(fitness))
        self.totalFitness = sum(self.scores)
        self.bestList.append(self.bestEverScore)
        self.avgList.append(float(self.totalFitness)/self.popSize)        

    def isDone(self):
        if self.bestEverScore>=5000:
            return True

    def randomDirection(self):
        """
        Generate a random direction to be executed in 2048
        """
        directions = ['w','a','s','d']
        index = random.randint(0,3)
        return directions[index]

    def initializePopulation(self):
        """
        Override the GA's method of initilizePopulation
        Initialize each chromodom in self.population with random directions

        Returns: None
        """
        self.population = []
        for i in range(self.popSize):
            chro = []
            for j in range(self.length):
                direction = self.randomDirection()
                chro.append(direction)
            self.population.append(chro)

    def mutation(self, chromosome):
        """
        Override the GA's method of mutation
        With probability self.pMutation, mutate positions in the
        chromosome by switching a direction
        """
        for i,direction in enumerate(chromosome):
            prob = random.random()
            if prob<self.pMutation:
                chromosome[i] = self.getDifferentDir(direction)

    def getDifferentDir(self, direction):
        directions = ['w','a','s','d']
        directions.remove(direction)
        index = randint(0,2)
        return directions[index]

def main():
    ga = GA2048(250,40)
    # def evolve(self, maxGen, pCrossover, pMutation)
    bestFound = ga.evolve(30,0.007,0.8)
    # print("Best Chromosome: " + str(ga.bestEver))
    mat = ga.bestBoard
    for row in mat:
        print("[" + " ".join(f"{num:4}" for num in row) + "]")
    ga.plotStats("2048")

if __name__ == '__main__':
    main()
    
    