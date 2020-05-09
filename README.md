<p align="center"><img src="logo.png"/></p>

# Pacman Go Home

PacmanGoHome is an educational game, based on the original Pacman [game tutorial](http://zetcode.com/tutorials/javagamestutorial/pacman/) from [Zetcode](http://zetcode.com). During this pandemic time, people need to learn how to protect themselves from coronavirus. The key is to stay home, wear masks while going out, and avoid contacting infected people. Therefore, the purpose of this game is to deliver this message to the players. We hope people can learn while playing PacmanGoHome.

The game is developed in Java by [Linh Tang](https://github.com/LinhTangTD) and [Yolanda Jiang](https://github.com/yolandajhzm) as the final project for `CSC207: Object-oriented programming, data structures, and algorithms` class at [Grinnell College](https://www.grinnell.edu/) during Spring 2020. For more information, please refer to [this presentation](PacmanGoHome.pdf).

[Video demo of PacmanGoHome](https://drive.google.com/open?id=10DFG39CY1ilI5htYoLHSEVTmPcexf5cd).

## How to play?
Users can control Pacman with their 4 cursors keys to avoid viruses and try to reach home. There are three initial lives. In the maze, there are one mask and hospital, each can save Pacman one life. There is also an infected Pacman, having contact with him or virus costs Pacman one life. The game has two levels. The second level is more challenging as the number of viruses is double and moving faster, representing the real-time unexpectedly fast and widespread waves of COVID-19.

<p align="center"><img src="demo_gif.gif" width="360" height="410"/></p>

## Installation & Usage

If you are not a developer, please download the appropriate version of the game corresponding to your operating system. Currently, we are supporting `macOS` and `Windows` users with minimum `Java 12` installed. We plan to support the `Linux` system in the near future. 

  ➤ [Download PacmanGoHome for macOS](PacmanGoHome_MacOS.zip)
  
  ➤ [Download PacmanGoHome for Windows](PacmanGoHome_Windows.zip)
  
  ➤ [Install the latest Java version](https://www.oracle.com/java/technologies/javase-downloads.html)

After downloading the compressed file, unzip the folder using unarchiver softwares in your device. For macOS users, please change your `Security & Privacy` in `System Preferences` to allow the `PacmanGoHome.app` to run. For Windows users, click on `PacmanGoHome.exe` to play the game.

## For Developers

The source code of PacmanGoHome is available [here](../../tree/master/src). We use [Eclipse IDE](https://www.eclipse.org/) to build and test the program. If you want to run using command-line tools (java, javac), please refer to this [article](https://www.codejava.net/java-core/tools/how-to-compile-package-and-run-a-java-program-using-command-line-tools-javac-jar-and-java) for help.

The main method is saved at ```PacmanGoHome.java``` together with the initialization of the GUI. The ```screen.java``` contains all other implementations of the game. 

All the code has been well-documented. Further explanation of the development flows and algorithms is provided in [this document](PacmanGoHome.pdf).

## Contributing
The demo version of the game can be found in this repository. Please note that the game is still in progress of development.

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Author & Contacts
[Linh Tang](https://github.com/LinhTangTD) - email: tanglinh@grinnell.edu

[Yolanda Jiang](https://github.com/yolandajhzm) - email: jianghui@grinnell.edu

## References
[Java Pacman Tutorial](http://zetcode.com/tutorials/javagamestutorial/pacman/)

[Original Pacman Music & Sound Effects](https://www.classicgaming.cc/classics/pac-man/sounds)
