# ConGa: Constraint Game solver



This project is a minimal working version of **Con**straint **Ga**me solver (or Conga). Conga is used to model games and especially to find Pure Nash equilibrium in it. 
Conga is based on [Choco Solver](https://github.com/chocoteam/choco-solver) which is a  Constraint programming solver.

Using Conga should be easy for the ones who have already used a constraint solver. 
For the readers who have never used a Constraint solver, it is encouraged to read the Choco solver's documentation and its tutorial.

In the following, an example is provided to illustrate the new internal functions and behaviors in Conga. 
In this tutorial, the basic notions of constraint programming and game theory are supposed to be known.
The tutorial is organized as follow, we first describe the general principles  of the solver, then we show how to model a game, afterwards the possible customizations and to finally present how to extend the solver.  

## Requirements

<u>Tools</u>:

- JDK8+
- Maven 3+

<u>Libraries</u>:

- [Choco Solver](https://github.com/chocoteam/choco-solver)
- JUnit (tests only)
- [Gambit](http://www.gambit-project.org/) (tests only)

## Installation

To use Conga a jar including all the dependencies is provided on this repository.  Otherwise the sources can be compiled directly with the following instructions.

**Compiling from sources**

After having downloaded the sources, the project can be compiled by launching the following command at the root of the project:

```shell
mvn clean install -DskipTests
```

The above command compiles the sources and creates a jar which does not include the dependencies.  This jar can either be imported into another maven project, in your favorite IDE...

## Functioning principles

Conga Solver is not a solver itself, but instead, it is an interface to create a model in Choco solver for games.
Games or multi-agents systems are generally composed of two sides: the global situation and the agents' situation. That is why Conga is built around two solver instances. One is used to compute the players' deviations, while the second one explores all the possible global situations. A model is created in both solvers. To create and solve a game, two classes are crucial: **AbstractGameModel*** and ***CongaSolver***.

- ***CongaSolver*** is an interface to solve and to manage the solving process of games. It is used to customize games' model, but only the game part. Further information are provided in the solver extensions.
- ***AbstractGameModel***  is the interface to implement games.  It contains the necessary routines to create games' semantic.



## Modeling your first game

This section presents how to model a simple game with Conga. The Wolf Lamb Cabbage (WLC) game is a simple game (see description above) used to illustrate the internal functions and how to find the Pure Nash Equilibriums.  

#### Modelling

To model a game, the class ***AbstractGameModel*** has to be overridden.  It imposes to implement the ***buildModel***  and to call to the super constructor which takes as parameter the number of players.

***buildModel*** method is the internal way to construct a game. This method provides as a parameter a model object in order to build the variables and the constraints.
Also, when a game model is built, it has to call the super constructor. This call  defines the number of players in the game. Then, when it associated with an instance of **CongaSolver**, it builds the players data-structures and then create the players objects.  These players instances are used to define players behaviors, specify the players search strategies, their goals...

Note that to build a game you need absolutely to use these functions. Conga builds the constraints, data-structures, and players through it.

An optional method ***defineObjective*** can be overridden. This function enables to define a function which has to be optimized, while seeking for Nash Equilibriums.
Note also that the <u>players are indexed from 1 to n</u> in an array provided by the *AbstractGameModel*. 
<u>The player 0 is a flag for the data structures</u> or can be used to put any new behavior which cannot enter in the current framework (like a new player with a different behavior).

##### Wolf Lamb Cabbage Game (WLC) 

*Three agents, Wolf (W), Lamb (L) and Cabbage (C) receive an invitation for a party.  Each of them has the choice to come or not at this event.  Each agent has his own preferences about meeting the others participants.  Wolf would be happy to see Lamb but is indifferent about Cabbage's presence.  Lamb would like to see Cabbage but only if Wolf is not coming.  And Cabbage is a plant and is indifferent to everything.*  



The game 's model code is provided in the following and can be found in the examples folder of the project.    

```java
public class WolfLambCabbage extends AbstractGameModel {

	private final Short WOLF = 1, LAMB = 2, CABBAGE = 3;

	public WolfLambCabbage() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		BoolVar[] isComing = s.boolVarArray("coming", players.length - 1);
		IntVar[] utilities = s.intVarArray(players.length - 2, 0, 2);

		for (int i = 0; i < isComing.length; i++) {
			players[i + 1].own(isComing[i]);
		}

		players[WOLF].setObjective(
            ResolutionPolicy.MAXIMIZE,utilities[WOLF - 1]);
		players[LAMB].setObjective(
            ResolutionPolicy.MAXIMIZE,utilities[LAMB - 1]);

		Constraint lamb_s = s.arithm(isComing[LAMB - 1], "=", 1);
		Constraint cabbage_s = s.arithm(isComing[CABBAGE - 1], "=", 1);
		Constraint wolf_s = s.arithm(isComing[WOLF - 1], "=", 1);

		s.arithm(s.and(wolf_s, lamb_s).reify(), 
                 "=", utilities[WOLF - 1]).post();
		s.sum(new BoolVar[] { s.and(
            wolf_s.getOpposite(), lamb_s, cabbage_s).reify(),
		s.and(wolf_s, lamb_s.getOpposite()).reify() },
              "=", utilities[LAMB -1]).post();
	}

}

```



The code above shows the basic routines to model a game. The Choco's API is used to build the variables and the constraints. Then the game semantic is given by the function ***own(Variable...)***  which is callable from a player. The players are a protected field in the class **AbstractGameModel**.
For instance the line above specifies that the *player i* own the variable *isComing[i]*.

`players[i + 1].own(isComing[i]);`

In the same way, for each player, an objective can be added by using the function ***setObjective(ResolutionPolicy, Variable)***. For instance the following code specifies that the *WOLF* wants to maximize the value of the variable *utilities[WOLF-1]*.

`players[WOLF].setObjective(
            ResolutionPolicy.MAXIMIZE,utilities[WOLF - 1]);`

#### Solving

We show here how to solve a game.  The basic idea is to provide to an instance of the class *CongaSolver* a model to then  compute the Nash equilibriums in it. This last instruction is done by the function ***prepareAndGetSolver()*** which return a solver in the Choco sense.

Most of the time, to ease the modelization part, factories are provided. These factories can be found in the folder *src/factories*

The following example shows how to find the Pure Nash Equilibrium in the ***WolfLambCabbage*** game using the ***LAST_LEVEL*** constraint. This last one is a version of the propagator proposed in [1].  Another propagator named ***BOUND_CONSTRAINT*** can be used. This propagator is the new algorithm proposed in [2]. Also a possibility which can be helpful while debugging is to specify that the game has no constraints with ***NO_CONSTRAINT***.

Finally, the function solve from the solver to find the Nash equilibriums of the game can be called.

```java
public static void main(String[] args) {
	CongaSolver cg = new CongaSolver(new WolfLambCabbage());
	cg.setConstraintBuilder(ConstraintFactory.LAST_LEVEL);
	Solver s =cg.prepareAndGetSolver();
	s.solve();
}
```


  Multiples interfaces can be redefined to customize the solver behavior. A non exhaustive list is given here:

  - ***ConstraintBuilder***:  defines how to build for instance a Nash constraint (see ***ConstraintFactory*** for multiples examples)
  - ***IEquilibriumConcept***:  defines the concept which has to be computed. Most of the time it is the Nash equilibrium.  It can be useful to extend the solver to other multi agent problems.
  - ***ISearchPolicy***: defines the search policy: how the exploration is made in the players' deviations search space. This Interface can be useful to define incomplete search or any new kind of search like Pareto Best response.
  - ***PlayerDependenciesUpdater*** : this interface allows to create a graphical game and limit the number of time a player's constraint is awakened.



## Going further

We list here some important class which may be useful to modify the solver behavior.

- ***VarHelperImpl*** is the implementation of the interface ***Varhelper*** which provides data structures to retrieves the variables and objectives of the different players. It can be useful to know  if a variable or an objective is shared. You can then ask within this class if such case happens in the game.
- ***Executable*** is the interface to implement more general algorithms. You can find the examples in the enum ***AlgorithmFactory***  with for instance the algorithms **NASH, IBR, PARETO_NASH**, and the pricing function such **POA** or **POS**.

## Gambit reader

If you are really attached to gambit solver, we provide a way to solve gambit representation through our solver.
To use gambit you first have to define the global variable *GAMBIT_PATH* to specify where gambit executable is located.
More information of this interface can be found in the class ***GambitNFGParser*** which take as input a normal form game as defined in gambit, and output a model in constraint game. The transformation used is quite simple, the matrix is transformed into table constraints.

## References

[1] TVA Nguyen, Lallouet, Arnaud. "A Complete Solver for Constraint Games." *International Conference on Principles and Practice of Constraint Programming*. Springer, Cham, 2014.

[2]Palmieri, Anthony, and Arnaud Lallouet. "Constraint games revisited." *International Joint Conference on Artificial Intelligence, IJCAI 2017*. 2017.