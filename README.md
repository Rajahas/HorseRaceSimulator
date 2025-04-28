# Horse Racing Simulator

This project consists of two main components: the **Horse** class and the **Race** class.

## Horse Class

Each `Horse` object represents an individual race participant and includes the following features:

### Attributes

- **Name and Symbol**:  
  Each horse has a unique name and a single-character symbol.

- **Race Participation**:  
  Horses can fall during the race, leading to their elimination from that race.

- **Confidence Rating**:  
  Each horse has a confidence level between 0 and 1:
  - A higher confidence level makes the horse run faster but increases the risk of falling.
  - A lower confidence level makes the horse run slower but improves stability.

- **Performance**:
  - Winning a race slightly increases a horse’s confidence.
  - Falling during a race slightly decreases a horse’s confidence.

## Race Class

The `Race` class manages the races by using `Horse` objects and provides a textual command-line visual representation of the race.

### Race Configuration

- The race length is configurable
- The number of lanes is adjustable, and some lanes may remain empty.

### Race Execution

- Horses are assigned to lanes before the race begins.
- The race runs in real-time with an animated display in the command-line terminal.
- Weather conditions (Sunny, Rainy, Windy) are randomly selected and impact horse performance.
- Horses move based on their confidence ratings and may fall during the race.

### Race Completion

- Once the race finishes:
- The winner is announced.
- The final race results and horse statistics are displayed.
- Horses’ performance history (wins, races, confidence) are saved to a file.


## How It Works

1. Create a race with a specified distance and number of lanes.
2. Load horses from a file or generate random horses to fill empty lanes.
3. User selects choices from a menu option to interact and customize the race.
4. Simulate the race, with horses moving, falling, and racing based on their attributes.
5. Display the winner and update horse statistics.
