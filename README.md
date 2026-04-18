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

To generate all the ANTLR files and compile the Java files
make

To run one test file from tests/
make run file=basic_class_constructor.pas

To run all test files
make run-all
```

## Project Structure

```
project_root/
│
├── grammar/
│   └── Delphi.g4          # ANTLR grammar
├── lib/
│   └── antlr-4.13.1-complete.jar
├── Makefile               # Build automation
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
├── tests/
│   ├── basic_class_constructor.pas
│   ├── class_destructor.pas
│   ├── class_inheritance_fields.pas
│   ├── interface_implementation.pas
│   ├── method_call_on_object.pas
│   ├── field_assignment_access.pas
│   ├── for_loop_iteration.pas
│   ├── for_loop_continue.pas
│   ├── while_loop_with_break.pas
│   ├── procedure_no_params.pas
│   ├── procedure_with_params.pas
│   ├── function_return.pas
│   ├── function_with_params.pas
│   ├── scope_global_variable_access.pas
│   ├── scope_parameter_shadowing.pas
│   ├── scope_function_parameter_shadowing.pas
│   ├── scope_loop_variable_not_visible_in_procedure.pas
│   └── scope_object_field_vs_global_variable.pas
└── README.md
```
## Design Notes
- The interpreter uses ANTLR’s Visitor pattern.
- Classes are stored in a Map<String, ClassDef>.
- Interfaces are stored in a Map<String, InterfaceDef>.
- Objects maintain their own field map.
- currentObject simulates this context.
- Interface validation occurs at class declaration time.

## Project 2 Additions
- Added support for `while` loops.
- Added support for `for` loops.
- Added `break` and `continue` using runtime control-flow signals.
- Added top-level procedures with positional parameters.
- Added top-level functions with positional parameters and `return`.
- Added function calls inside expressions.
- Added scenario-based tests instead of numbered test files.
- Reorganized the repository into `grammar/`, `lib/`, `src/`, and `tests/`.
- Updated the `Makefile` to work with the new directory structure.
- Added `make run-all` to run the full test suite.

## Bonus Work
- Constant propagation was not implemented.
- Formal parameter passing for procedures/functions was implemented, including correct scoping behavior for parameters.

## Current Supported Features
- Integer variable declarations and assignment
- Object creation and field access
- Class constructors and destructors
- Method calls on objects
- Single inheritance
- Interface declaration and implementation checks
- `while` loops
- `for` loops
- `break` and `continue`
- Procedures
- Functions
- Return statements
- Scope handling through nested runtime scopes


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

## Current Test Suite
- `basic_class_constructor.pas`: basic class creation and constructor execution
- `class_destructor.pas`: destructor execution through `Destroy`
- `class_inheritance_fields.pas`: inherited field initialization and child fields
- `interface_implementation.pas`: interface declaration and required method implementation
- `method_call_on_object.pas`: instance method dispatch
- `field_assignment_access.pas`: assignment to object fields and reading them back
- `while_loop_with_break.pas`: while-loop execution with `break`
- `for_loop_iteration.pas`: for-loop counting behavior
- `for_loop_continue.pas`: for-loop `continue` behavior
- `procedure_no_params.pas`: procedure call without parameters
- `procedure_with_params.pas`: procedure call with multiple parameters
- `function_return.pas`: function returning a value
- `function_with_params.pas`: function call with parameter passing
- `scope_global_variable_access.pas`: reading a global variable inside a procedure
- `scope_parameter_shadowing.pas`: procedure parameter shadowing a global variable
- `scope_function_parameter_shadowing.pas`: function parameter shadowing a global variable
- `scope_loop_variable_not_visible_in_procedure.pas`: invalid scope access from procedure to loop-local variable
- `scope_object_field_vs_global_variable.pas`: object field lookup versus same-named global variable
