## TDT4240 Programvarearkitektur
# Exercise 2 Patterns

## Step 1: Implementation of a program

I chose to use my existing pong game code from the first exercise.  


## Step 2: Implement the Singleton pattern
For the singleton pattern i chose to use it for containing collision objects.  
I named this class `CollideSingleton` and implemented the singleton instance code.  

This class is used as a single source of truth location for all collidable objects.  

This meant that the `paddle` class (that implements the player controlled paddle in the game pong) could intefrace with the singleton directly instead of the bounding box needing to be retrived from the main method/loop.  

It also meant that the ball could have one universal way to handle collisions instead of different implementations for the walls, paddles, and any future collidable objects.


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


### Pipe and filter
As a fan of functional programming i wanted to implement this. This was implemented in the singleton collision processing.

## Step 4


### 4.a)
*For the patterns listing in Step3, which are architectural patterns, and which are design
patterns? What are the relationships and differences of architectural patterns and design
patterns?*

Abstract factory.

The Observor, Entity Component System, Model View Controller

State, Observor, Template method, Pipe and filter, 


### 4.b)

*How is the pattern you chose realized in your code? (Which class(es) works as the
pattern you chose?)*

### Observor

The observer pattern is based on the interaction between an observor and observable. The observable notifies when there are changes and notifies the observor. This can be useful in message oriented applications where a state is changing and all parts need to be up to date.

For the codebase the best use i could think of was for score changes (the pong game points). I moved all logic for the ball from the main method into it's own Ball class so this implementation would make a bit more sense.

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

### Pipe and filter

As a fan of functional programming i wanted to implement this, but Unfortunately i couldn't get streams to work with my java version. So this is just how i would have implemented this pattern. The newly implemted singleton for coordinating collision detection was a good target.  

These two implementations achieve the same result. Tough it must be said that the pipe and filter implementation could actually be replaced with the shorter `.matchAny(c -> c.overlaps(r))`, but i wanted to show off the use of map and filter as these are the key feautres that build up the pipe and filter pattern.

If we had been using kotlin this could be simplified even further. Can you tell i love kotlin?


**Old implementation (for loops)**
```java
public boolean overlapsWith(Rectangle r){
        for(Rectangle c : collidable)
            if(c.overlaps(r)) return true;

        return false;
    }
```

**New implementatino (pipe and filter)**
```java
public boolean overlapsWith(Rectangle r){

        return collidable
            .stream()
            .map(c -> c.overlaps(r))
            .filter(c -> c == true)
            .findAny();
    }
```


**Possible Kotlin implementation**
```Kotlin
fun overlapsWith(r: Rectangle) = collidable.any { it.overlaps(r) }
```

### 4.c)
*Is there any advantages in using this pattern in this program? (What are the
advantages/disadvantages?)*


### Observable

As this project is not that big in scope or complexity architectural patterns, which are supposed to reduce complexity as codebases grow, aren't as clear-cut. Tough we can still see how it could be an advantage in the future.  

First of all the logic is moved. This improves readability.  
It also makes changes easier. For example if the ball class was moved and initialized somewhere else it would be a lot quicker to implement the observer methods instead of moving the logic.

Another great benefit is that we can also trust that the observer always has the latest information instead of relying on the next time it checks which may lead to unruly and hard to identify bugs.

The biggest downside is cost for this project was cost of implementation. For bigger projects though the biggest dangers are race conditions and alike that occur when updates can trigger each other.

### Pipe and filter

Functional programming is in many ways more of a preference than black and white. The biggest upsides are reduction of code which improves readability. The biggest downsides are is a bit steeper learning curve (debatable) and performance. Because imperative is by nature closer to machine code it is usually faster. Tough this may change in the future.

