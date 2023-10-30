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
            a) check valid or not 
        II) if roll
            a. roll two dice (get n1 and n2)
                i. score 2(n1 + n2) if n1 = n2 and n1 != 1
                ii. score (n1 + n2) if n1 != n2 and > 1
                iii. score 0 if n1 or n2 = 1
        III) if hold or score 0
                update counter and switch to another player
    B) Computer's round(input user's score)
        I) roll two dice
            i. score 2(n1 + n2) if n1 = n2 and n1 != 1
            ii. score (n1 + n2) if n1 != n2 and > 1
            iii. score 0 if n1 or n2 = 1
        II) hold or score 0
            a. if user's counter <100
                i. hold until counter of this round >20
                ii. update counter(score or 0) and switch player
            b. if user's counter >100
                i. hold until computer's counter > user's counter
                ii. update counter(score or 0) and end game

3) End Game (called when one counter exceeds 100)
    A) if user counter >100
        I) Computer's final round
        II) Compare both counters and print message
    B) if computer counter >100
        I) Print message
    
"""

from random import randrange, seed

def main():
    """
    1. welcome
    2. setup user and computer counters
    3. while both counters <100
        a. play game
        b. update counters
    4. end game
    """
    seed(1237)
    welcome()
    user = 0
    computer= 0 
    while user < 100 and computer < 100:
        print("")
        print("-" * 30)
        print("Player has %a and computer has %g" % (user, computer))
        user_score = user_play()
        user += user_score
        print("")
        print("-" * 30)
        print("Player has %a and computer has %g" % (user, computer))
        computer_score = computer_play(user, computer)
        computer += computer_score
    end_game(user, computer)
    

def welcome():
    """
    Print out a welcome message
    no Parameters
    no returns
    """
    print(\
    """\nWelcome to Big Pig, the dice rolling game where players try to be the
first get 100 points! Players (you and the computer) will take turns
rolling two dice as many times as they want, adding all roll results to
a running total. Players lose their gained score for the turn if they
roll a 1.""")


def user_play():
    """
    Simulate the user's round until Big Pig or the user chooses to hold. 
    The user can decide to roll or hold.
    no Parameters
    return: int - user's total score gained in all the rolls
    """
    counter = 0
    lst1 = ["Roll", "roll", "R", "r"]
    lst2 = ["Hold", "hold", "H", "h"]  
    choice = get_input()
    while choice in lst1:
        r = roll_2_dice()
        counter += r[2]
        if r[2] == 0:
                counter = 0
        print("human rolled [%a, %g], current round score: %i" \
        % (r[0], r[1], counter))
        if r[2] == 0:
            print("Big pig!")
            return 0
        choice = get_input()
        if choice in lst2:
            return counter


def get_input(): 
    """
    get a valid input (is a string and in list 
    ["h", "hold", "r", "roll"]
    no parameters
    return: answer - string - the valid input from the user
    """
    answer = input("What do you want to do: [r]oll or [h]old? ")
    lst = ["Hold", "hold", "H", "h", "Roll", "roll", "R", "r"]
    while answer not in lst:
        answer = input("What do you want to do: [r]oll or [h]old? ") 
    return answer


def roll_2_dice():
    """
    Calculate the points gained in each roll
    no parameters
    return: list of numbers - [a, b, score]
    """
    a = randrange(1, 7)
    b = randrange(1, 7)
    if (a > 1) and (b > 1) and (a != b):
        return [a, b, a + b]
    if (a > 1) and (a == b):
        return [a, b, 2 * (a + b)]
    if (a == 1) and (b == 1):
        return [1, 1, 25]
    if (a == 1) or (b == 1):
        return [a, b, 0]
    

def computer_play(user, computer):
    """
    Simulate the computer's round until Big Pig or hold, following 
    the computer's algorithms
    parameters: user - the user's current total score
               computer - the computer's current total score
    return: int - computer's total score gained in all the rolls
    """
    counter = 0
    if user >= 100:
        while ((computer + counter) < user):
            r = roll_2_dice()
            counter += r[2]
            if r[2] == 0:
                counter = 0
            print("computer rolled [%a, %g], current round score: %i" \
            % (r[0], r[1], counter))
            if r[2] == 0:
                print("Big pig!")
                return 0
        print("Computer holds.")
        return counter
    while counter < 20:
        r = roll_2_dice()
        counter += r[2]
        if r[2] == 0:
            counter = 0
        print("computer rolled [%a, %g], current round score: %i" \
        % (r[0], r[1], counter))
        if r[2] == 0:
            print("Big pig!")
            return 0
    print("Computer holds.")
    return counter


def end_game(user, computer):
    """
    determines who the winner is by comparing scores and prints out the scores
    parameters: the final score of user and computer before end_game
    no returns
    """
    if user > computer:
        print("Human wins [%a, %g]" % (user, computer))
    if user < computer:
        print("Computer wins [%a, %g]" % (user, computer))


main()