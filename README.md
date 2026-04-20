# Project 3: Mini Delphi Compiler

## Overview

This project is a compiler for a procedural Pascal/Delphi subset. It uses ANTLR4 to parse source programs, builds an AST, lowers that AST to LLVM IR, and can then compile the generated IR either to native executables or to WebAssembly.

The main compiler pipeline is:

```text
.pas source
  -> ANTLR parser
  -> AstBuilder
  -> ProgramNode / AST
  -> LLVMGenerator
  -> .ll LLVM IR
```

Native execution then uses:

```text
.ll -> clang -> tests/out/*.out
```

The bonus WebAssembly path uses:

```text
.ll -> llc -> .o -> wasm-ld -> .wasm -> browser / Node
```

## Supported Language Subset

The compiler supports the procedural subset used for Project 3:

- integer literals
- integer variables
- global variable declarations
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
- `writeln(integer)`

For native LLVM output, `writeln(integer)` lowers to `printf`.

For WebAssembly mode, `writeln(integer)` lowers to an imported JavaScript function:

```llvm
declare void @print_i32(i32)
```

## Not Supported

These Project 2 features are intentionally rejected by the Project 3 compiler:

- classes / objects
- constructors / destructors
- methods
- inheritance
- interfaces
- object creation
- field access
- the old interpreter runtime

The parser still recognizes some of those constructs, but `AstBuilder` rejects them because this compiler focuses on LLVM code generation for the procedural subset.

## Requirements

The project expects these tools:

```bash
java
javac
make
clang
llc
wasm-ld
node
python3
```

ANTLR is included in the repository:

```text
lib/antlr-4.13.1-complete.jar
```

## Project Structure

```text
grammar/
  Delphi.g4                    ANTLR grammar

src/
  Main.java                    compiler entry point
  ast/                         AST node classes
  frontend/AstBuilder.java     parse tree to AST lowering
  frontend/grammar/            generated ANTLR files
  codegen/LLVMGenerator.java   AST to LLVM IR generator

tests/
  pas/                         Pascal input programs
  ll/                          generated LLVM IR files
  out/                         generated native executables
  wasm/                        WASM demo files and browser runtime
```

Generated directories/files such as `bin/`, `tests/ll/`, `tests/out/`, and `tests/wasm/*.wasm` are recreated by the Makefile.

## Clean Build

```bash
make clean
make
```

`make clean` removes:

```text
bin/
tests/ll/
tests/out/
tests/wasm/*.o
tests/wasm/*.wasm
generated ANTLR files
```

## Generate LLVM IR

Compile one Pascal file to LLVM IR:

```bash
make compile-ll file=tests/pas/assign_writeln.pas
```

Output:

```text
tests/ll/assign_writeln.ll
```

Compile every Pascal test into LLVM IR:

```bash
make compile-tests
```

## Compile LLVM IR To Native Executables

Build all `.ll` files into native `.out` executables:

```bash
make compile-outs
```

Output:

```text
tests/out/*.out
```

Run all native executables:

```bash
make run-outs
```

Expected representative output includes:

```text
5
5
7
42
10
1
1
```

Some tests intentionally produce no output, for example the `continue` test.

## Important Native Test Cases

Global variable used inside a procedure:

```bash
make compile-ll file=tests/pas/global_procedure.pas
clang tests/ll/global_procedure.ll -o tests/out/global_procedure.out
tests/out/global_procedure.out
```

Expected output:

```text
42
```

Global variable used inside a function:

```bash
make compile-ll file=tests/pas/global_function.pas
clang tests/ll/global_function.ll -o tests/out/global_function.out
tests/out/global_function.out
```

Expected output:

```text
7
```

Return inside a loop:

```bash
make compile-ll file=tests/pas/return_inside_loop.pas
clang tests/ll/return_inside_loop.ll -o tests/out/return_inside_loop.out
tests/out/return_inside_loop.out
```

Expected output:

```text
1
```

## WebAssembly Bonus

The project includes a WASM proof-of-concept and a browser demo.

### Minimal Hand-Written LLVM To WASM

The smallest test is:

```text
tests/wasm/minimal_run.ll
```

It exports:

```llvm
define i32 @run() {
entry:
  ret i32 42
}
```

Build it:

```bash
make wasm-tests
```

Verify it with Node:

```bash
make run-wasm-minimal
```

Expected output:

```text
42
```

The underlying LLVM to WASM commands are:

```bash
llc -march=wasm32 -filetype=obj tests/wasm/minimal_run.ll -o tests/wasm/minimal_run.o
wasm-ld --no-entry --export=run tests/wasm/minimal_run.o -o tests/wasm/minimal_run.wasm
```

### Compiler-Generated LLVM To WASM

This test uses real compiler output:

```text
tests/pas/wasm_run.pas
```

Run:

```bash
make run-compiler-wasm-demo
```

Pipeline:

```text
tests/pas/wasm_run.pas
  -> tests/ll/wasm_run.ll
  -> tests/wasm/wasm_run.o
  -> tests/wasm/wasm_run.wasm
  -> Node calls exported run()
```

Expected output:

```text
42
```

### Compiler-Generated WASM With Browser Output

This is the richer WASM demo:

```text
tests/pas/wasm_print.pas
```

Source:

```pascal
function run();
begin
  writeln(99);
  return 42;
end;

begin
end.
```

Run:

```bash
make run-compiler-wasm-print-demo
```

Expected output:

```text
print_i32: 99
run(): 42
```

In WASM mode, `Main` is called with:

```bash
java -cp ".:lib/antlr-4.13.1-complete.jar:bin" Main --wasm tests/pas/wasm_print.pas
```

This makes `writeln(99)` lower to:

```llvm
call void @print_i32(i32 99)
```

The JavaScript browser runtime provides that import.

## Browser Demo

Build the compiler-generated WASM print demo and serve the browser files:

```bash
make wasm-browser
```

Open:

```text
http://127.0.0.1:8080/
```

If port `8080` is busy:

```bash
make WASM_PORT=8081 wasm-browser
```

Open:

```text
http://127.0.0.1:8081/
```

Expected browser result:

```text
writeln output: 99
run(): 42
```

The browser files are:

```text
tests/wasm/index.html
tests/wasm/main.js
tests/wasm/style.css
```

## Full Demo Command Sequence

For a complete Project 3 demo:

```bash
make clean
make compile-tests
make run-outs
make run-compiler-wasm-print-demo
make WASM_PORT=8081 wasm-browser
```

Then open:

```text
http://127.0.0.1:8081/
```
