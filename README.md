**Note: Markdown done in jhub cells**
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
- Horses’ performance history (wins, races, confidence) are saved to a CSV file.


## How It Works

1. Create a race with a specified distance and number of lanes.
2. Load horses from a file or generate random horses to fill empty lanes.
3. User selects choices from a menu option to interact and customize the race.
4. Simulate the race, with horses moving, falling, and racing based on their attributes.
5. Display the winner and update horse statistics.

## Versions
### **Part 1**: Text-based in the terminal
- <img width="187" alt="image" src="https://github.com/user-attachments/assets/4e521350-e865-4cc4-b000-173d508b2e8d" />
- <img width="365" alt="image" src="https://github.com/user-attachments/assets/a081b269-2e97-4040-9b70-1ea29102cf3e" />
- <img width="92" alt="image" src="https://github.com/user-attachments/assets/ae20f845-6144-44ca-bb11-cfa01f789927" />
- <img width="362" alt="image" src="https://github.com/user-attachments/assets/89f3e5f5-e152-4af6-8910-1f1497f2c68c" />
### **Part 2**: GUI version using Java Swing for a graphical race visualization.
- <img width="198" alt="image" src="https://github.com/user-attachments/assets/8219f255-6854-4e55-bc41-2c65ecea8bdf" />
- <img width="955" alt="image" src="https://github.com/user-attachments/assets/196f6f3f-88b6-4b48-b7cc-1ca46badb1ff" />
- <img width="469" alt="image" src="https://github.com/user-attachments/assets/6ef9e6c3-519b-4c43-90d8-f3d54b609773" />

## Running the Program
### Part 1: Text-based Version
- Java Development Kit (JDK): Version 11 or higher
- No External Libraries Required
1. **Clone** the repository
2. **Compile** the source files
3. **Run** the text-based simulator

Note:
- `Main` is the entry point of the program and will need `Main.main(null);`
- `Main` holds `public static void main(String[] args)`

### Part 2: Graphical Version
- Java Development Kit (JDK): Version 11 or higher
- **Imports needed**
  - `import javax.swing.*;`
  - `import java.awt.*;`
  - `import java.awt.event.*;`
  - `import java.io.IOException;`
1. **Clone** the repository
2. **Compile** the source files
3. **Run** the graphical race simulator

Note:
- `Main2` is the entry point of the program and will need `Main2.main(null);`
- `Main2` holds `public static void main(String[] args)`


