# Mini Delphi Compiler

## Overview
This project is a compiler for a Pascal/Delphi subset. It uses ANTLR4 for parsing, builds a small AST for the procedural subset, and generates LLVM IR (`.ll`) files.

The compiler focuses on approximately 70% of the Project 2 language, specifically the procedural subset needed for LLVM generation.

## Supported Subset
- integer literals
- integer variables
- variable declarations
- assignments
- arithmetic expressions: `+`, `-`, `*`, `/`
- `while` loops
- `for` loops
- `break`
- `continue`
- global procedures
- global functions
- formal parameters
- `return`
- procedure calls
- function calls
- `writeln(integer)` via `printf`

## Not Supported
- classes / objects
- constructors / destructors
- methods
- inheritance
- interfaces
- the earlier interpreter runtime

## Build
```bash
make clean
make
```

Important:
- `make clean` removes `bin/` and `tests/ll/`
- generated LLVM files are recreated the next time you run `make compile-ll ...` or `make compile-tests`

## Compile A Pascal File To LLVM IR
```bash
make compile-ll file=tests/pas/assign_writeln.pas
```

This writes the LLVM output into `tests/ll/` with the same base name:

```text
tests/pas/assign_writeln.pas -> tests/ll/assign_writeln.ll
```

You can also run the compiler directly:

```bash
java -cp ".:lib/antlr-4.13.1-complete.jar:bin" Main tests/pas/assign_writeln.pas
```

## Project Structure
```text
grammar/
  Delphi.g4              ANTLR grammar

src/
  Main.java              compiler entry point
  ast/                   AST node classes and AST printer
  frontend/              AST builder and generated ANTLR files
  codegen/               LLVM IR generator

tests/
  pas/                   Pascal test cases
  ll/                    generated LLVM IR outputs
```

## Representative Test Cases
- `tests/pas/assign_writeln.pas`
- `tests/pas/procedure_param.pas`
- `tests/pas/function_return.pas`
- `tests/pas/while_break.pas`
- `tests/pas/for_continue.pas`

Generate their LLVM IR with:

```bash
make compile-ll file=tests/pas/assign_writeln.pas
make compile-ll file=tests/pas/procedure_param.pas
make compile-ll file=tests/pas/function_return.pas
make compile-ll file=tests/pas/while_break.pas
make compile-ll file=tests/pas/for_continue.pas
```

Generate all representative `.ll` files with:
```bash
make compile-tests
```

## Main Makefile Commands
```bash
make
make clean
make compile-ll file=tests/pas/assign_writeln.pas
make compile-tests
```

## Verifying With LLVM Toolchain
If `clang` is installed, a generated `.ll` file can be compiled and executed:

```bash
clang tests/ll/assign_writeln.ll -o /tmp/assign_writeln.out
/tmp/assign_writeln.out
```

Expected output:

```text
5
```

## Compiler Pipeline
```text
Pascal source
-> ANTLR parse tree
-> AST
-> LLVM IR (.ll)
```

## Demo Plan
For the demo video:
1. Show one Pascal source file.
2. Run the compiler on it.
3. Show the generated `.ll` file.
4. Optionally compile the `.ll` with `clang` and execute it.
