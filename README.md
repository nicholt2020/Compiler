# Compiler
How To Use This Compiler:

This project compiles MountC (an extremely stripped down version of C, a high-level language) into Pep/9 (an educational assembly language). The specifications for the MountC language is laid out with the rest of the project specification at the following site: http://raider.mountunion.edu/csc/CSC420/Fall2017/project.htm

The initial files you will start with will include MountC.g4, Compiler.java, CompilerListener.java, and test files ending in .c that contain MountC code.

To start the process, you will open a terminal in the folder containing those files, or cd into that location. Then you will run:
antlr4 MountC.g4

This command produces many other .java files in that location. To compile all the new files and the Compiler.java and CompilerListener.java files that were already in the folder you can run:
javac *.java

Now that these files are compiled, they can be used with MountC files by running:
java Compiler test.c

where "test.c" can be replaced with whatever the name of the MountC file you're trying to compile is named.

This will output runnable Pep/9 code. You can also redirect the output to another file by using a command EX: java Compiler example.c > output.txt. This can be useful if the produced PEP9 code is so long that it runs past the terminal window, which can make it difficult to copy/paste into the Pep/9 program to run.

