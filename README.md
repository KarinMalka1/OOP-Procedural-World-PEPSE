Q1) To implement the Avatar, we created a dedicated pepse.world.avatar package
containing the Avatar and EnergyMeter classes. The Avatar class extends
GameObject and handles physics, input processing, and movement logic. To
adhere to clean code principles, we separated the UI logic into the EnergyMeter
class. The relationship between them is maintained via a functional callback
(Consumer<Float>); the Avatar triggers this callback whenever its internal energy
state changes, allowing the EnergyMeter to update the display without the Avatar
holding a direct reference to the UI object.
We designed state changes by monitoring the Avatar's velocity and input within
the update() loop. We distinguished between four main states: Idle (no velocity),
Running (horizontal velocity), Jumping/Falling (vertical velocity), and Flying
(triggered by specific key combinations). Based on these conditions, we dynamically
swapped the object's Renderable (animation) to visually reflect the current physical
state, ensuring immediate feedback to the player.
Energy is modeled as a numeric value (0-100) managed internally by the Avatar.
It depletes during flight and regenerates while idle. We avoided tight coupling by
passing a callback function from the EnergyMeter to the Avatar upon initialization.
When the energy value changes, the Avatar invokes this callback, which updates the
text on the EnergyMeter. Additionally, the meter includes visual logic to change the
text color to red when energy drops below 20%, providing an intuitive warning to the
user.

Q2) The pepse.world.trees package was implemented using a separation of concerns
between generation and object behavior. The Flora class acts as the manager, determining
tree positions based on the terrain height and a deterministic seed. It delegates the
construction of individual trees to the Tree class, which assembles Leaf and Fruit
objects. The Leaf class manages its own wind simulation using scheduled transitions,
while the Fruit class handles collision logic to restore the Avatar's energy.

Q3) We introduced a new class, InfiniteWorld, to handle the procedural generation
of terrain and trees. This class monitors the Avatar's position and dynamically
loads new "chunks" of the world while removing distant objects to maintain performance.

Q4) We made several changes to the API to support the new features. In the Avatar class,
we added the public method addEnergy(float amount) to allow external objects (like Fruits)
modify the avatar's energy state safely. In the Sky class, we added a new static method,
createSkyObjects, to separate the creation of the static background from dynamic elements
like clouds and birds.

