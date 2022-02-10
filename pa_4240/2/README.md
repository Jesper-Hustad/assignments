## TDT4240 Programvarearkitektur
# Exercise 2 Patterns

## Step 1: Implementation of a program

I chose to use my existing pong game code from the first exercise.  


## Step 2: Implement the Singleton pattern
*Secondly, implement the Singleton pattern in the chose program. You can choose yourself what you should use the Singleton pattern
for.*

For the singleton pattern i chose to use it for containing collision objects.  
I named this class `CollideSingleton` and implemented the singleton instance code.  

This class is used as a single source of truth location for all collidable objects.  

This meant that the `paddle` class (that implements the player controlled paddle in the game pong) could interface with the singleton directly instead of the bounding box needing to be retrived from the main method/loop.  

It also meant that the ball could have one universal way to handle collisions instead of different implementations for the walls, paddles, and any future collidable objects.

The overlapsWith function gives a standardized method to find if a rectangle overlaps with any collidable objects. This means that the singleton could not be replaced with a global variable, which is a common pitfall in the singleton design pattern. 


**CollideSingleton**  
```java
public class CollideSingleton {

    private static CollideSingleton instance;
    ArrayList<Rectangle> collidable;

    private CollideSingleton() { collidable = new ArrayList<>(); }

    public static CollideSingleton getInstance() {
        if (instance == null) instance = new CollideSingleton();
        return instance;
    }

    public void addRectangle(Rectangle r){ collidable.add(r); }

    public boolean overlapsWith(Rectangle r){
        for(Rectangle c : collidable)
            if(c.overlaps(r)) return true;

        return false;
    }

}
```


**Universal ball collision logic**  
```java
// update position
ball.x += ballVx;
ball.y += ballVy;

// calculate potential new position
Rectangle newPosX = new Rectangle(ball.x + ballVx, ball.y, ball.width, ball.height);
Rectangle newPosY = new Rectangle(ball.x, ball.y + ballVx, ball.width, ball.height);

// find if collision occurs
boolean collidesX = collideSingleton.overlapsWith(newPosX);
boolean collidesY = collideSingleton.overlapsWith(newPosY);

// react to collision
if(collidesX) ballVx *= -1;
if(collidesY) ballVy *= -1;

```

**Simpler implementation of ceiling and floor**  
```java
// ceiling and floor bounding box
collideSingleton.addRectangle(new Rectangle(-1, -1, screenX, 0));
collideSingleton.addRectangle(new Rectangle(0, screenY, screenX, screenY + 1));
```

## Step 3: Implementation of pattern(s) from the list

### Observable
I chose to implement the observable pattern. For the codebase the best use i could think of was for score changes (the pong game points).

### Entity component system
This is a simple implementation of an ECS.

## Step 4


### 4.a)
*For the patterns listing in Step3, which are architectural patterns, and which are design
patterns?*

**Architectural:**
Pipe and filter, Entity Component System, Model View Controller

**Design:**
Singleton, Abstract Factory, Template Method, Observer, State  

_______________

*What are the relationships and differences of architectural patterns and design patterns?*


**Architectural patterns** are a set of interaction mechanisms between element types. It gives structure and hierarchy to the software. These provide subsystems that when implemented create a common arrangement increasing predictability and giving a set of rules/guidelines for new implementations.

**We use Design patters** to solve problems that occur often. With these tools we can quickly recognize these common issues and implement effective solutions. A object or class that interfaces with others to solve these aforementioned issues are what design patterns describe.

If one where to compare these two patterns the biggest difference lies in where the focus is. Architectural patterns deal with the wider aspect of structuring systems, creating guidelines for where and how the code is implemented in the big picture. 

Design patters instead focus on the lower level programming logic. It ensures that commonly recurring pitfalls are avoided by giving structure to this aspect of the system. These patterns are also more specific to their language while architectural are more universal. 

The relationship between architectural and design patterns are that they both try to solve commonly occuring problems in software design and that their ultimate goal is to make creating and maintaining software easier.

### 4.b)

*How is the pattern you chose realized in your code? (Which class(es) works as the
pattern you chose?)*

### Observor

The observer pattern is based on the interaction between an observor and observable. The observable notifies when there are changes and notifies the observor. This can be useful in message oriented applications where a state is changing and all parts need to be up to date.

For the codebase the best use i could think of was for score changes (the pong game points). I moved all logic for the ball from the main method into it's own Ball class so this implementation would make a bit more sense. So the main class is the observor while the ball is the observable.

First i went for the Observable class in java, but found out that this has been depricated. The closest replacement i could find was the `PropertyChangeListener`.  


  
**Here is how the ball class notifies the observable:**  

```java

public class Ball {

    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void render(){

        ...

        // p1 scores point
        if(r.x - 10 > (screenX/6) * 5){

            changes.firePropertyChange("PLAYER 1", p1Points, ++p1Points);
            ballVx *= -1;
        }

        // p2 scores point
        if(r.x +10 < (screenX/6) + 34){

            changes.firePropertyChange("PLAYER 2", p2Points, ++p2Points);
            ballVx *= -1;
        }
    }
```


**Here is the method `propertyChange` implemented in the main class:**

```java

public class MyGdxGame extends ApplicationAdapter implements PropertyChangeListener {

    ...

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		String player = propertyChangeEvent.getPropertyName();
		int points = (int) propertyChangeEvent.getNewValue();

		if(points < 21) return;

		winningScreen = player + " WINS!";
		playerWon = true;
	}

}
```


### Entity Component System

Pretty simple implementation used by both the paddle and ball class. More features like adding the shape to the collidable singleton or storing more data about position and textures could also be implemented. The drawback of these choices is the reduction in possible implementations for classes that use it.

The ball and paddle classes extend this entity class.

```java
abstract class Entity {

    Rectangle shape;

    Entity() {}

    public abstract void render(SpriteBatch batch);
}
```



### 4.c)
*Is there any advantages in using this pattern in this program? (What are the
advantages/disadvantages?)*


### Observable

As this project is not that big in scope or complexity architectural patterns, which are supposed to reduce complexity as codebases grow, aren't as clear-cut. Tough we can still see how it could be an advantage in the future.  

First of all the logic is moved. This can improve readability. Tough it increases direct coupling some changes can be easier. For example if the ball class was moved and initialized somewhere else it would be easier to implement the observer methods instead of moving the logic.

Another great benefit is that we can also trust that the observer has the latest information instead of relying on the next time our class checks which may lead to unruly and hard to identify bugs.

The biggest downside is cost for this project was cost of implementation and now increased coupling. For bigger projects race conditions could occur if observors can trigger each other.

### Entity component system

A drawback is the difficulty in changing the entity component as the refactoring required can make it infeasible.

The benefits are that it creates a unified system which all new entities can follow. This means a future software engineer will be helped by the architectural pattern in knowing which functions are required in a new entity. It also means that subsystems can be designed around every entity having a standard set of features.