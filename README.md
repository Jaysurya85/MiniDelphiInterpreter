# Mini Delphi Interpreter

## Overview
This project implements a Mini Delphi interpreter by extending a Pascal grammar using ANTLR4 and building a Java-based interpreter that walks the generated AST.

**The interpreter supports object-oriented features including:**
- Classes and Objects
- Constructors
- Destructors
- Methods
- Inheritance 
- Interfaces 
The project demonstrates parsing, semantic validation, and runtime execution of a small object-oriented language.

## How to Run

```
To clean
make clean 

To generates all the antlr files and compiles the java files
make 

To Run
make run file=test1.pas (name of the pascal file you want to run should be in home directory) 
```

## Project Structure

```
project_root/
│
├── Delphi.g4              # ANTLR grammar
├── Makefile               # Build automation
├── antlr-4.13.1-complete.jar
│
├── src/
│   ├── Main.java
│   ├── Interpreter.java
│   ├── ClassDef.java
│   ├── ObjectInstance.java
│   ├── InterfaceDef.java
│   └── (generated ANTLR files)
│
├── bin/                   # Compiled .class files
│
├── test1.pas
├── test2.pas
├── test3.pas
└── README.md
```
## Design Notes
- The interpreter uses ANTLR’s Visitor pattern.
- Classes are stored in a Map<String, ClassDef>.
- Interfaces are stored in a Map<String, InterfaceDef>.
- Objects maintain their own field map.
- currentObject simulates this context.
- Interface validation occurs at class declaration time.


## Test Cases (Output)
- test1.pas (Basic Class + Constructor)
```
Registered class: Person
18
```
- test2.pas (Destructor)
```
Registered class: Person
18
```
- test3.pas (Inheretence)
```
Registered class: Person
Registered class: Student
18
10
```
- test4.pas (Interface)
```
Registered interface: Printable
Registered class: Person
18
```


