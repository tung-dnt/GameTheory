# Game Theory Solver

**Game theory** is the study of mathematical models of conflict and cooperation between intelligent rational
decision-makers.
Game theory is mainly used in economics, political science, and psychology, as well as logic, computer science and
biology.

### Table of Contents

- [Game Representations](#game-representations)
- [Classification of Games](#classification-of-games)
- [Nash Equilibrium](#nash-equilibrium)
- [Problem Description](#problem-description)

---

### Game Representations

**Players**: who are the decision makers?
- People? Governments? Companies? Somebody employed by a Company?...

**Strategies**: what can the players do?
- Enter a bid in an auction? Decide whether to end a strike? Decide when to sell a stock? Decide how to vote?...

**Properties**: attributes make up a strategy

**Weights**: a unit to evaluate efficiency of a property

**Payoffs**: what motivates players?
- Do they care about some profit? Do they care about other players?...

#### Normal form

- Lists what payoffs players get as a function of their actions.
- Does not incorporate any notion of sequence, or time, of the actions of the players.
- Usually represented by a matrix.

|                 | **Stay silent**       | **Betray**          |
| ---             | ---                   | ---                 |
| **Stay silent** | -1,-1                 | -3,0                |
| **Betray**      | 0,-3                  | **-2,-2**           |

#### Extensive form

- Includes timing of moves.
- Usually represented by a tree.

[Extensive Form Games](#extensive-form-games)

---

**Positive affine transformation**: au + b, where a > 0
and b is any real number. Expected utilities are identical to positive affine transformations.

---

### Classification of Games

<table style="table-layout:fixed">
  <colgroup>
    <col style="width:50%"/>
    <col style="width:50%"/>
  </colgroup>
  <tr>
    <td>
      <p><b>Simultaneous games</b> (a.k.a. Strategy games) - games where both players move simultaneously, or if they do
       not move simultaneously, the later players are unaware of the earlier players' actions (making them effectively
        simultaneous).
      <p>Usually normal form is used to represent simultaneous games.
    </td>
    <td>
      <p><b>Sequential games</b> (a.k.a. Extensive game) - games where later players have some knowledge about earlier 
      actions.
      <p>Usually extensive form is used to represent sequential games.
    </td>
  </tr>
  <tr>
    <td>
      <b>Cooperative games</b> - games where the players are able to form binding commitments externally enforced (e.g.
      through contract law).
    </td>
    <td>
      <b>Non-cooperative games</b> - games where players cannot form alliances or if all agreements need to be
      self-enforcing (e.g. through credible threats).
    </td>
  </tr>
  <tr>
    <td>
      <b>Zero-sum games</b> - games in which each participant's gain or loss of utility is exactly balanced by the
      losses or gains of the utility of the other participants.
    </td>
    <td>
      <b>Non-zero-sum games</b> - games in which the interacting parties' aggregate gains and losses can be less
      than or more than zero.
    </td>
  </tr>
  <tr>
    <td>
      <b>Perfect information games</b> - games in which all players know the moves previously made by all other players.
    </td>
    <td>
      <b>Imperfect information games</b> - games in which some players don't know the moves previously made by other 
      players.
    </td>
  </tr>
  <tr>
    <td>
      <b>Complete information games</b> - games in which all players know the strategies and payoffs available to the 
      other players.
    </td>
    <td>
      <b>Incomplete information games</b> - games in which some players don't know the strategies or payoffs available
      to the other players.
    </td>
  </tr>
  <tr>
    <td>
      <b>Finite games</b> - games that last for finite number of moves.
    </td>
    <td>
      <b>Infinite games</b> - games that last for infinite number of moves.
    </td>
  </tr>
</table>

---

### Nash Equilibrium

**Nash Equilibrium** - a set of strategies, one for each player, such that no player has incentive to change
his strategy given what the other players are doing.

**Nash Equilibrium** (alternative definition) - a set of strategies, one for each player, such that every player's
strategy is the best response to what the other players are doing.

**Best Response** - a strategy such that a player cannot gain more utility from switching to a different strategy,
given what all other players are doing.

**Mixed Strategy** - a probability distribution over two or more pure strategies, that is, the players choose
randomly among their options in equilibrium.

**Mixed Strategy Nash Equilibrium** - a set of mixed strategies, one for each player, such that no player has
incentive to change his strategy given what the other players are doing.

**Dominant Strategy** - a strategy that is always better than any other strategy, for any profile of other players'
actions.
- Strictly Dominant Strategy - same as Dominant Strategy
- Weakly Dominant Strategy - a strategy that is always better than or equal to any other strategy, for any profile of
  other players' actions.

**Dominant Strategy Nash Equilibrium** (equilibrium in dominant strategies) - a Nash equilibrium in which all strategies
are strictly dominant. If it exists can be found by elimination of strictly dominated strategies.

**Dominated Strategy** - a strategy, such that, regardless of what any other players do, the strategy earns a player
a smaller payoff than some other strategy.
- Strictly Dominated Strategy - same as Dominated Strategy
- Weakly Dominated Strategy - a strategy, such that, regardless of what any other players do, the strategy earns a
  player a smaller than or equal to some other strategy payoff.

---

### Problem Description

**Traffic Control**
1) Context: Decision-making in route choosing from A-B to optimize travel time, cost, consumed energy of commuters
  
  ***Sample Map***
  
  ![Screenshot from 2022-06-02 23-06-39](https://user-images.githubusercontent.com/71514053/171674924-d5a71334-cef1-4a46-9cfa-8e6c51a8176c.png)
  
   + 4 different commuters with their target is to reach destination B with the lowest cost:
        - Player 1: represent for player choosing route A to C to B
        - Player 2: represent for player choosing route A to C to D to B
        - Player 3: represent for player choosing route A to D to B
        - Player 4: represent for player choosing route A to D to C to B
   + Number of players: 1000 commuters
   + <strong>Rule</strong>: There are 2 context in this game:
        a) When 1000 players all use OP1 pattern (1-4-9-15-19)
        b) When 500 players use OP1 (1-4-9-15-19) while the other 500 players use OP2 pattern (1-5-8-17-15-19)
  
2) Game Model: 

![image](https://user-images.githubusercontent.com/71514053/172880667-e629cad8-c575-4bb1-ac03-870e79e5aec6.png)

3) Input Data Implementation: 

  ***Special Players(SP) Input***
  a) Properties of SP:
  Public Transportation Profit | Emission Cost | Infrastructure Loss 
  
  b) Weight Calculation: 

  ![image](https://user-images.githubusercontent.com/71514053/175828050-320ba6a0-6f25-4fe0-a6f1-f51039aad9a0.png)

  
  ***Normal Players(NP) Input***
  a) 2 strategy/player:
    - First Strategy: in the first context (all player use OP1 route)
    - Second Strategy: in second context (choose either OP1 or OP2)

  b) Properties of a strategy:
  | Free-flow Travel Time (Travel time in ideal condition) | Average Link Capacity among Nodes | Average Number of drivers using edges per hour | Average Energy consumed  |
  
  c) Weight calculation:
  
  ![image](https://user-images.githubusercontent.com/71514053/175827970-068c6bde-ef17-4481-b9f6-ae5cf7e1ff18.png)


---

**Oil Management**
1) Context: Oil can bring great revenues for the economy, along with those benefits, there are potential conflicts due to the interest of many agents involved in the exploration process.
This cheatsheet may help to manage conflicts between countries sharing the same oil resources field by deciding to create an alliance or not.
  - ***Condition***: Each player can have only one strategy at a time
- Normal Player: 4 countries [ A, B, C, D ]
  - ***Player 1 - Country A***:
    + Possesses 40% of the continent's area including both sea and land areas, but the sea area has little oil, and the mainland is mainly mountainous, difficult to exploit, or has no exploitation ability.
    + The continent's largest population, an unstable economy, mainly come from marine exploitation
    + A does not have crude oil processing technology
    + Imports from B at a high price, C at a low price
    + A is in a sea dispute with D because of oil and gas resources in the sea
  - ***Player 2 - Country B***:
    + Own less with only 20%. However, B's oil resources are the second largest of the four countries
    + B has a strong economy with an average population, but because of its rich oil background, it has the strongest economic foundation
    + B does not have crude oil processing technology
    + B's main customer is A, followed by D
    + B transfers crude oil to D at a low price, buys back gasoline at a high price
    + B competes with C in the path of oil export
  - ***Player 3 - Country C***:
    + Owns 30% but has no sea, is located deep in the delta, has the 3rd largest oil and gas resource base in 4 countries
    + The population in C is large, the economy is behind B
    + C has crude oil processing technology, so it sells a lot, sells it at a cheap price to partners, the biggest customer is A, followed by D
  - ***PLayer 4 - Country D***:
    + Own 10%, have the most petroleum resources
    + The population in D is small, the economy is developed
    + D has crude oil processing technology
    + Despite possessing a large area, oil resources are rare, so for many years, A has been in dispute with D as its neighboring country because the oil resources on D are extremely large.
- Properties: 2
  + Amount of (crude) oil consumed
  + Amount of gasoline produced
- For each player, there are 4 strategies: 1 member alliance, 2 member alliances, 3 member alliances, 4 member alliances
2) Game Model:  

  ![image](https://user-images.githubusercontent.com/71514053/172658129-6e724342-526d-484b-b70c-b9beac169d09.png)

3) Input Data Implementation:

---
