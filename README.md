## Procedural-World-PEPSE
**Package:** `pepse.world.avatar`

To implement the Avatar and adhere to clean code principles, we separated the logic into two main classes:
* **`Avatar`**: Extends `GameObject` and handles physics, input processing, and movement logic.
* **`EnergyMeter`**: Manages and encapsulates the UI logic.

**State Management & Animation:**
State changes were designed by monitoring the Avatar's velocity and input within the `update()` loop.
We distinguished between four main physical states:

1. **Idle**: No velocity.
2. **Running**: Horizontal velocity.
3. **Jumping/Falling**: Vertical velocity.
4. **Flying**: Triggered by specific key combinations.

Based on these conditions, we dynamically swapped the object's `Renderable` (animation)
to visually reflect the current physical state, ensuring immediate feedback to the player.

**Energy System & UI Decoupling:**
* Energy is modeled as a numeric value (0-100) managed internally by the `Avatar`. It depletes during flight and regenerates while idle.
* **Callback Mechanism:** We avoided tight coupling by passing a functional callback (`Consumer<Float>`) from the `EnergyMeter` to the `Avatar` upon initialization. 
* Whenever the internal energy state changes, the `Avatar` invokes this callback. This allows the `EnergyMeter` to update the display text without the `Avatar` holding a direct reference to the UI object.
* **Visual Warnings:** The meter includes visual logic to change the text color to red when energy drops below 20%, providing an intuitive warning to the user.


## Flora & Environment
**Package:** `pepse.world.trees`

The flora system was implemented using a strict separation of concerns between generation and object behavior:
* **`Flora`**: Acts as the manager. It determines tree positions based on the terrain height and a deterministic seed, delegating the construction of individual trees to the `Tree` class.
* **`Tree`**: Responsible for assembling `Leaf` and `Fruit` objects.
* **`Leaf`**: Manages its own wind simulation using scheduled transitions.
* **`Fruit`**: Handles collision logic to restore the Avatar's energy.


## Infinite World Generation
We introduced a new class, **`InfiniteWorld`**, to handle the procedural generation of terrain and trees seamlessly. 
* This class actively monitors the Avatar's position.
* It dynamically loads new "chunks" of the world while removing distant objects to maintain optimal performance and memory usage.



## API & Architecture Updates
We made several changes to the API to effectively support the new features:
* **`Avatar`**: Added the public method `addEnergy(float amount)` to allow external objects (like `Fruit`s) to modify the avatar's energy state safely.
* **`Sky`**: Added a new static method, `createSkyObjects`, to cleanly separate the creation of the static background from dynamic environmental elements like clouds and birds.
