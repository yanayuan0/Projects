"""
Top Down Design - Big Pig
Dice game

Author: Yana
10/25
"""

"""
Top Down Design

1) Welcome

2) Play Game
    A) User's round
        I) get input (roll or hold)
            a. check valid or not 
        II) if roll
            a. roll two dice (get n1 and n2)
                i. score 2(n1 + n2) if n1 = n2 and n1 != 1
                ii. score (n1 + n2) if n1 != n2 and > 1
                iii. score 0 if n1 or n2 = 1
                iv. score 25 if n1 = n2 = 1
        III) if hold or score 0
                update counter and switch to another player
    B) Computer's round(input user's score)
        I) roll two dice
            i. score 2(n1 + n2) if n1 = n2 and n1 != 1
            ii. score (n1 + n2) if n1 != n2 and > 1
            iii. score 0 if n1 or n2 = 1
            iv. score 25 if n1 = n2 = 1
        II) hold or score 0
            a. if user's counter <100
                i. hold until counter of this round >20
                ii. update counter(score or 0) and switch player
            b. if user's counter >100
                i. hold until computer's total counter > user's total counter
                ii. update counter(score or 0) and end game

3) End Game (called when one counter exceeds 100)
    A) if user counter >100
        I) Computer's final round
        II) Compare both counters and print message
    B) if computer counter >100
        I) Print message
    
"""

from random import randrange

def main():
    """
    1. welcome
    2. setup user and computer counters
    3. while both counters <100
        a. play game
        b. update counters (accumulator)
    4. end game
    """
    welcome()
    user = 0
    computer= 0 
    while user < 100 and computer < 100:
        user_score = user_play()
        user += user_score
        computer_score = computer_play(user, computer)
        computer += computer_score
    end_game(user, computer)
    

def welcome():
    """
    Print out a welcome message
    No Parameters, no returns
    """
    print("in welcome")


def user_play():
    """
    Simulate the user's round until Big Pig or the user chooses to hold
    No Parameters
    1. Get input from the user (roll or hold)
    2. Test the input using get_input() function
    3. Roll
        a. roll two dice (get n1 and n2)
            i. score 2(n1 + n2) if n1 = n2 and n1 != 1, ask for input
            ii. score (n1 + n2) if n1 != n2 and > 1, ask for input
            iii. score 0 if n1 or n2 = 1, next player, return 0
            iv. score 25 if n1 = n2 = 1, ask for input
    4. Hold
        a. adds up all the score in previous roll and return
    return: int - user's total score gained in all the rolls
    """
    print("in user_play")
    return 10


def get_input(): 
    """
    get a valid input (is a string and in list 
    ["h", "hold", "H", "r", "roll", "R"]
    no parameters
    return: answer - string - the valid input from the user
    """
    print("in get_input")
    return "r"


def roll_2_dice():
    """
    Simulate each roll of either the user or the computer
        i. score 2(n1 + n2) if n1 = n2 and n1 != 1
        ii. score (n1 + n2) if n1 != n2 and > 1
        iii. score 0 if n1 or n2 = 1
        iv. score 25 if n1 = n2 = 1
    return: list of numbers - [n1, n2, score]
    """
    print("In roll 2 dice")
    return [2, 2, 8]
    

def computer_play(user, computer):
    """
    Simulate the computer's round until Big Pig or "hold"
    parameter: user - the user's current total score
               computer - the computer's current total score
    1. roll two dice (while score <20)
        i. score 2(n1 + n2) if n1 = n2 and n1 != 1, ask for input
        ii. score (n1 + n2) if n1 != n2 and > 1, ask for input
        iii. score 0 if n1 or n2 = 1, next player, return 0
        iv. score 25 if n1 = n2 = 1, ask for input
    2. hold
        a. if user's counter <100
            i. hold until counter of this round >20
            ii. return score of this round and switch player
        b. if user's counter >=100
            i. hold until computer's total counter > user's total counter
               , return score
            ii. end game
    return: int - computer's total score gained in all the rolls
    """
    print("in computer_play")
    return 20


def end_game(user, computer):
    """
    determines who the winner is by comparing scores and prints out the scores
    when one counter exceeds 100, end_game is called
    1. Compare the two parameters
    A) if user > computer
        I) Print messaage, user wins
    B) if computer > user
        I) Print message, computer wins
    parameters: user - integer- total score of the user before end game
                computer - integer- total score of the computer before end game
    no returns
    """
    print("in end_game")

main()