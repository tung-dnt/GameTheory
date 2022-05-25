# Game Theory Solver

**Game theory** is the study of mathematical models of conflict and cooperation between intelligent rational
decision-makers.
Game theory is mainly used in economics, political science, and psychology, as well as logic, computer science and
biology.

### Table of Contents

- [Game Representations](#game-representations)
- [Classification of Games](#classification-of-games)
- [Nash Equilibrium](#nash-equilibrium)
- [Social Choice](#social-choice)

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

### Social Choice

**Social Choice Function** - a function that, given a set of linear orderings on the outcomes, tells which
outcome should be chosen.

**Social Welfare Function** - a function that, given a set of linear orderings on the outcomes, tells which
ordering should be chosen.

**Voting Schemes**:
- **Plurality** - pick the outcome which is most preferred by the most people
- **Cumulative voting**
    - distribute e.g., 5 votes each
    - possible to vote for the same outcome multiple times
- **Approval voting** - vote for as many outcomes as you "like" (used e.g. in electing new members into a club or society)
- **Plurality with elimination** ("instant runoff", "transferable voting")
    - if some outcome has a majority, it is the winner
    - otherwise, the outcome with the fewest votes is eliminated (may need some tie-breaking procedure)
    - repeat until there is a winner.
- **Borda Rule**, **Borda Count**
    - assign each outcome a number.
    - the most preferred outcome gets a score of `n - 1`, the next most preferred gets `n - 2`, down to the `n`th
      outcome which gets 0.
    - sum scores for each outcome, and choose one with highest score.
- **Successive elimination**
    - in advance, decide an ordering of alternatives
    - everyone votes for the first or second, and the loser is eliminated.

---
