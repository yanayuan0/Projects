import random
import numpy as np

def printboard(mat):
	score = 0
	dic = {
		0 : 0,
		2 : 2,
		4 : 4,
		8 : 16,
		16: 48,
		32: 128,
		64: 320,
		128: 768,
		256: 1792,
		512: 4096,
		1024: 9216,
		2048: 20480,
	}
	for i in range(len(mat)):
		# print(mat[i])
		# for j in range(len(mat[0])):
			score += dic[mat[i]]
	# print("Total score: " + str(score))
	return score


def start_game():

	# declaring an empty list then
	# appending 4 list each with four
	# elements as 0.
	mat =[]
	for i in range(4):
		mat.append([0] * 4)

	# printing controls for user
	# print("Commands are as follows : ")
	# print("'W' or 'w' : Move Up")
	# print("'S' or 's' : Move Down")
	# print("'A' or 'a' : Move Left")
	# print("'D' or 'd' : Move Right")

	# calling the function to add
	# a new 2 in grid after every step
	mat[2][0] = 2
	mat[2][3] = 2
	# printboard(mat)
	return mat

def start_random_game():
	mat =[]
	for i in range(4):
		mat.append([0] * 4)
	add_new_2()
	add_new_2()

	return mat	

# function to add a new 2 in
# grid at any random empty cell
def add_new_2(mat):
	r = random.randint(0, 3)
	c = random.randint(0, 3)
	board = r*4 + c
	while(mat[board] != 0):
		r = random.randint(0, 3)
		c = random.randint(0, 3)
		board = r*4 + c

	mat[board] = 2

# function to get the current
# state of game
def get_current_state(mat):
	for i in range(4):
		for j in range(4):
			board = i*4 + j
			if(mat[board]== 2048):
				return 'WON'

	for i in range(4):
		for j in range(4):
			board = i*4 + j
			if(mat[board]== 0):
				return 'GAME NOT OVER'

	# or if no cell is empty now
	# but if after any move left, right,
	# up or down, if any two cells
	# gets merged and create an empty
	# cell then also game is not yet over
	for i in range(3):
		for j in range(3):
			board = i*4 + j
			# if(mat[i][j]== mat[i + 1][j] or mat[i][j]== mat[i][j + 1]):
			if(mat[board]== mat[board+4] or mat[board]== mat[board+1]):
				return 'ONE DIRECTION'

	for j in range(3):
		# if(mat[3][j]== mat[3][j + 1]):
		if(mat[3*4 + j]== mat[3 * 4 + j +1]):
			return 'ONE DIRECTION'

	for i in range(3):
		# if(mat[i][3]== mat[i + 1][3]):
		if(mat[i*4 + 3]== mat[(i + 1)*4 + 3]):
			return 'ONE DIRECTION'

	# else we have lost the game
	return 'LOST'

def done(mat):
	for i in range(4):
		for j in range(4):
			board = i*4 + j
			if(mat[board]== 2048):
				return True

	for i in range(4):
		for j in range(4):
			board = i*4 + j
			if(mat[board]== 0):
				return False

	for i in range(3):
		for j in range(3):
			# if(mat[i][j]== mat[i + 1][j] or mat[i][j]== mat[i][j + 1]):
			board = i*4 + j
			if(mat[board]== mat[board+4] or mat[board]== mat[board+1]):
				return False

	for j in range(3):
		# if(mat[3][j]== mat[3][j + 1]):
		if(mat[3*4 + j]== mat[3 * 4 + j +1]):
			return False

	for i in range(3):
		# if(mat[i][3]== mat[i + 1][3]):
		if(mat[i*4 + 3]== mat[(i + 1)*4 + 3]):
			return False

	# else we have lost the game
	return True


# all the functions defined below
# are for left swap initially.

# function to compress the grid
# after every step before and
# after merging cells.
def compress(mat):

	# bool variable to determine
	# any change happened or not
	changed = False

	# empty grid 
	new_mat = []

	# with all cells empty
	for i in range(4):
		new_mat.append([0] * 4)
	new_mat = np.array(new_mat).flatten()
		
	# here we will shift entries
	# of each cell to it's extreme
	# left row by row
	# loop to traverse rows
	for i in range(4):
		pos = 0

		# loop to traverse each column
		# in respective row
		for j in range(4):
			board = i*4 + j
			if(mat[board] != 0):
				
				# if cell is non empty then
				# we will shift it's number to
				# previous empty cell in that row
				# denoted by pos variable

				# new_mat[i][pos] = mat[i][j]
				new_mat[i*4 +pos] = mat[board]
				
				if(j != pos):
					changed = True
				pos += 1

	# returning new compressed matrix
	# and the flag variable.
	return new_mat, changed

# function to merge the cells
# in matrix after compressing
def merge(mat):
	
	changed = False
	
	for i in range(4):
		for j in range(3):

			# if current cell has same value as
			# next cell in the row and they
			# are non empty then

			board = i*4 + j
			# if(mat[i][j] == mat[i][j + 1] and mat[i][j] != 0):
			if(mat[board] == mat[board + 1] and mat[board] != 0):

				# double current cell value and
				# empty the next cell

				# mat[i][j] = mat[i][j] * 2
				# mat[i][j + 1] = 0
				mat[board] = mat[board] * 2
				mat[board + 1] = 0

				# make bool variable True indicating
				# the new grid after merging is
				# different.
				changed = True

	return mat,changed

# function to reverse the matrix
# means reversing the content of
# each row (reversing the sequence)
def reverse(mat):
	new_mat =[]
	for i in range(4):
		new_mat.append([])
		for j in range(4):
			# new_mat[i].append(mat[i][3 - j])
			new_mat[i].append(mat[i*4 + 3 - j])
	new_mat = np.array(new_mat).flatten()
	return new_mat


# function to get the transpose
# of matrix means interchanging
# rows and column
def transpose(mat):
	new_mat = []
	for i in range(4):
		new_mat.append([])
		for j in range(4):
			# new_mat[i].append(mat[j][i])
			new_mat[i].append(mat[j*4 + i])
	new_mat = np.array(new_mat).flatten()
	return new_mat

def move(grid,direction):
	if direction == "w":
		new_grid, change = move_up(grid)
		status = get_current_state(grid)
		if(status == 'GAME NOT OVER'):
			add_new_2(new_grid)
	elif direction == "s":
		new_grid, change = move_down(grid)
		status = get_current_state(grid)
		if(status == 'GAME NOT OVER'):
			add_new_2(new_grid)
	elif direction == "a":
		new_grid, change = move_left(grid)
		status = get_current_state(grid)
		if(status == 'GAME NOT OVER'):
			add_new_2(new_grid)
	elif direction == "d":
		new_grid, change = move_right(grid)
		status = get_current_state(grid)
		if(status == 'GAME NOT OVER'):
			add_new_2(new_grid)
	else:
		print("Incorrect Direction")
	return new_grid

# function to update the matrix
# if we move / swipe left
def move_left(grid):

	# first compress the grid
	new_grid, changed1 = compress(grid)

	# then merge the cells.
	new_grid, changed2 = merge(new_grid)
	
	changed = changed1 or changed2

	# again compress after merging.
	new_grid, temp = compress(new_grid)

	# return new matrix and bool changed
	# telling whether the grid is same
	# or different
	return new_grid, changed

# function to update the matrix
# if we move / swipe right
def move_right(grid):

	# to move right we just reverse
	# the matrix 
	new_grid = reverse(grid)

	# then move left
	new_grid, changed = move_left(new_grid)

	# then again reverse matrix will
	# give us desired result
	new_grid = reverse(new_grid)
	return new_grid, changed

# function to update the matrix
# if we move / swipe up
def move_up(grid):

	# to move up we just take
	# transpose of matrix
	new_grid = transpose(grid)

	# then move left (calling all
	# included functions) then
	new_grid, changed = move_left(new_grid)

	# again take transpose will give
	# desired results
	new_grid = transpose(new_grid)
	return new_grid, changed

# function to update the matrix
# if we move / swipe down
def move_down(grid):

	# to move down we take transpose
	new_grid = transpose(grid)

	# move right and then again
	new_grid, changed = move_right(new_grid)

	# take transpose will give desired
	# results.
	new_grid = transpose(new_grid)
	return new_grid, changed

# this file only contains all the logic
# functions to be called in main function
# present in the other file
