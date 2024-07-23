This project focuses on enhancing AI performance in playing the classic puzzle game 2048.
The AI algorithms employed include Genetic Algorithm (GA) and Direct Policy Learning via GA.
The Genetic Algorithm is primarily based on the principles of natural selection, while 
Direct Policy Learning via GA incorporates specific characteristics inherent to the 2048 game.

The project is explained and evaluated in detail in paper.pdf.

To run GA:
----------

python3 2048GA.py

Parameters are set in main() in 2048GA.py. In order for quick code run, the chromosome
length is set to 250 and population is set to 40. A tile of 256 is expected to be
created in the game board. Fitness learning curve of GA is shown in 
FitnessByGeneration.png.
logic of 2048 game is imported in 2048GA.py as logic_ga. Detailed codes are in
logic_ga.py.


To run Direct Policy Learning via GA:
-------------------------------------

source /usr/swat/bin/CS63env

python3 2048_neural_ga.py

Parameters are set in main() in 2048_neural_ga.py. The length of the chromosome is
set to 250 and the population size is set to 40. The round of evolutions is set to
10 for a quick code run. After approximately 20 seconds of wait a 128 tile is 
expected to be created in the game board. Fitness learning curve is shown in 
FitnessByGeneration_NGA.png.
logic of 2048 game is imported in 2048_neural_ga.py as logic_NGA. Detailed codes
of logic are in logic_NGA.py. 

