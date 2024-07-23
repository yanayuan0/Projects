import numpy as np
from project_neural_ga import *
import gym
from project_neural_net import *
from time import sleep
from gym import Env
from gym.spaces import Discrete, Box
import logic_NGA


class Env2048(Env):
    def __init__(self):
        """
        In order to do RL learning, need the env and number of steps to
        run the environment when determining fitness.  Also need, all
        of the network specifications. 
        """

        super(Env2048, self).__init__()
        self.action_space = ["w", "s", "a", "d"]
        # self.observation_space = Box(low=0, high=2048, shape=(4, 4), dtype=int)
        self.state = logic_NGA.start_game()

    def reset(self):
        self.state = logic_NGA.start_game()
        return self.state
    
    def step(self, state, action):
        direction = self.action_space[action]
        mat = []
        mat.append(state[0:4])
        mat.append(state[4:8])
        mat.append(state[8:12])
        mat.append(state[12:16])
        new_state = logic_NGA.move(mat, direction)
        new_state = np.array(new_state).flatten()
        reward = logic_NGA.printboard(new_state) - logic_NGA.printboard(state)
        done = logic_NGA.done(new_state)
        # if done:
        #     print(f"Move: {direction}, Failed to merge significant tiles.")
        # else:
        #     print(f"Move: {direction}, New State: {new_state}, Reward: {reward}")
        return new_state, reward, done

    # def render(self, mode="human"):
    #     for row in self.state:
    #         print(row)
    #     print()                                  
                                        
class NeuralGA2048(NeuralGA):

    def __init__(self, env, steps, in_size, out_size, hid_sizes, popsize):
        """
        In order to do RL learning, need the env and number of steps to
        run the environment when determining fitness.  Also need, all
        of the network specifications. 
        """
        self.env = env
        self.steps = steps

        # Call the parent class constructor
        super(NeuralGA2048, self).__init__(in_size, out_size, hid_sizes,
                                         popsize)

    def fitness(self, chromosome, render=False, wtsFile=None):
        """
        Creates a neural network of the right size using the NeuralGA
        class variables in_size, out_size, and hid_sizes.

        If the wtsFile is not None, reads the weights from that file.
        Otherwise sets the weights based on the given chromosome.

        Tests out the neural network for one episode. When render is
        True, show what's happening in the env.

        Returns: Total reward received while executing steps
        """
        network = Network(self.in_size, self.out_size, self.hid_sizes)
        if wtsFile is not None:
            network.readWeights(wtsFile)
        else:
            network.setWeights(chromosome)
        state = self.env.reset()
        state = np.array(state).flatten()
        total_reward = 0
        for i in range(self.steps):
            # if render: self.env.render()
            output = network.predict(state)
            action = np.argmax(output)
            result = self.env.step(state, action)
            next_state, reward, done = result
            # next_state = np.array(next_state).flatten()
            total_reward += reward
            state = next_state
            if done:
                break

        # print(f"Total reward for this chromosome: {total_reward}")
        if total_reward>self.bestEverScore:
            self.bestEverScore = total_reward
            print("-----------")
            print("A new best was found. Its score is " + str(total_reward))
            for i in range(0, 16, 4):
                row = next_state[i:i + 4]
                row_str = "[" + "".join(f"{num:>{4}}" for num in row) + "]"
                print(row_str)
            network.saveWeights()
        return total_reward
                               
    def isDone(self):
        """
        Stop when the best score ever found is equal to the maximum fitness.
        """
        if self.bestEverScore >= 5000:
            return True

    ########
    def evolve(self, maxGen, pCrossover, pMutation):
        """
        Stores the probabilites in appropriate class variables.
        
        Runs a series of generations until maxGen is reached or
        self.isDone() returns True.

        Returns the best chromosome ever found over the course of
        the evolution, which is stored in self.bestEver.
        """
        self.pCrossover = pCrossover
        self.pMutation = pMutation
        self.initializePopulation()
        self.evaluatePopulation()
        for i in range(maxGen):
            self.oneGeneration()
            self.evaluatePopulation()
            if self.isDone(): break
        return self.bestEver

def main():
    env = Env2048()
    in_size = 16
    out_size = 4
    hid_sizes = [6]
    # def __init__(self, env, steps, in_size, out_size, hid_sizes, popsize):
    ga = NeuralGA2048(env, 250, in_size, out_size, hid_sizes, 40)
    bestFound = ga.evolve(10, 0.8, 0.75)
    env.close()
    ga.plotStats("2048 Game")

if __name__ == '__main__':
    main()