# Pacman Go Home

During this pandemic time, people need to learn how to protect themselves from coronavirus. The key is to stay home, wear masks, and avoid contacting infected people. Therefore, the purpose of this game is to deliver this message to the players. We hope people can learn while playing PacmanGoHome.

The game is based on the original Pacman [game tutorial](http://zetcode.com/tutorials/javagamestutorial/pacman/) from [Zetcode](http://zetcode.com) and is further developed in Java by Linh Tang and Yolanda Jiang as the final project for "CSC207: Object-oriented programming, data structures, and algorithms" class at Grinnell College during Spring 2020. 

[Video demo of PacmanGoHome](https://drive.google.com/open?id=10DFG39CY1ilI5htYoLHSEVTmPcexf5cd).

## How to play?
Users can control Pacman with their 4 cursors keys to avoid viruses and try to reach home. There are three lives initially. In the maze, there are mask and hospital which can each save Pacman life once. There is also be an infected Pacman in the maze. Having contact with him or virus costs Pacman life. The game has two levels. The second level is more challenging as the number of viruses is double moving faster, representing the real-time unexpectedly fast and widespread waves of COVID-19.

## Installation & Usage

If you are not a developer, please download the appropriate ```zip``` file of the game corresponding to your operating system. Currently, we are supporting [macOS](https://github.com/LinhTangTD/PacmanGoHome/blob/master/PacmanGoHome_MacOS.zip) and [Windows](https://github.com/LinhTangTD/PacmanGoHome/blob/master/PacmanGoHome_Windows.zip) users with minimum [Java 12](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html) installed. We plan to support the Linux system in the near future. 

After download the compressed file, please unzip the folder. For macOS users, please change your `Security & Privacy` in `System Preferences` to allow the `PacmanGoHome.app` to run. For Windows users, click on `PacmanGoHome.exe` to play the game.

## For Developers

The source code of PacmanGoHome is available [here]((https://github.com/LinhTangTD/PacmanGoHome/tree/master/src) ). We use [Eclipse IDE](https://www.eclipse.org/) to build and test the program. If you want to run using command-line tools (java, javac), please refer to this [article](https://www.codejava.net/java-core/tools/how-to-compile-package-and-run-a-java-program-using-command-line-tools-javac-jar-and-java) for help.

The main method is saved at ```PacmanGoHome.java``` together with the initialization of the GUI. The ```screen.java``` contains all other implementations of the game. 

All the code has been well-documented. Further explanation of the development flows and algorithms is provided in [this document](https://github.com/LinhTangTD/PacmanGoHome/blob/master/PacmanGoHome.pdf).

## Contributing
The demo version of the game can be found in this repository. Please note that the game is still in progress of development.

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Contacts
[Linh Tang](https://github.com/LinhTangTD) - email: tanglinh@grinnell.edu

[Yolanda Jiang](https://github.com/yolandajhzm) - email: jianghui@grinnell.edu

## References
[Java Pacman Tutorial](http://zetcode.com/tutorials/javagamestutorial/pacman/)

[Original Pacman Music & Sound Effects](https://www.classicgaming.cc/classics/pac-man/sounds)
