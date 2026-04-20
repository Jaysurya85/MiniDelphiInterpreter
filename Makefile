ANTLR_JAR = lib/antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = grammar/Delphi.g4
GENERATED_GRAMMAR_DIR = $(SRC_DIR)/frontend/grammar
LL_DIR = tests/ll
OUT_DIR = tests/out
WASM_DIR = tests/wasm
TEST_FILES = $(sort $(wildcard tests/pas/*.pas))

all: compile

generate:
	java -jar $(ANTLR_JAR) -visitor -o $(SRC_DIR)/frontend $(GRAMMAR)

compile: generate
	mkdir -p $(BIN_DIR)
	javac -cp ".:$(ANTLR_JAR)" -d $(BIN_DIR) $$(find $(SRC_DIR) -name '*.java' | sort)

compile-ll: compile
	@if [ -z "$(file)" ]; then \
		echo "Usage: make compile-ll file=tests/pas/<testfile.pas>"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(file)

compile-tests: compile
	@for file in $(TEST_FILES); do \
		echo "Compiling $$file"; \
		java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main "$$file" || exit 1; \
	done

compile-outs: compile-tests
	mkdir -p $(OUT_DIR)
	@for file in $(LL_DIR)/*.ll; do \
		base=$$(basename "$$file" .ll); \
		echo "Building $(OUT_DIR)/$$base.out"; \
		clang "$$file" -o "$(OUT_DIR)/$$base.out" || exit 1; \
	done

run-outs: compile-outs
	@for file in $(OUT_DIR)/*.out; do \
		echo "Running $$file"; \
		"$$file" || exit 1; \
	done

wasm-file:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make wasm-file file=tests/wasm/<file>.ll"; \
		exit 1; \
	fi
	llc -march=wasm32 -filetype=obj "$(file)" -o "$(basename $(file)).o"
	wasm-ld --no-entry --export=run "$(basename $(file)).o" -o "$(basename $(file)).wasm"

wasm-tests:
	@for file in $(WASM_DIR)/*.ll; do \
		base=$${file%.ll}; \
		echo "Building $$base.wasm"; \
		llc -march=wasm32 -filetype=obj "$$file" -o "$$base.o" || exit 1; \
		wasm-ld --no-entry --export=run "$$base.o" -o "$$base.wasm" || exit 1; \
	done

run-wasm-minimal: wasm-tests
	node -e "const fs = require('fs'); WebAssembly.instantiate(fs.readFileSync('$(WASM_DIR)/minimal_run.wasm')).then(({ instance }) => console.log(instance.exports.run()));"

clean:
	rm -rf $(BIN_DIR)
	rm -f $(SRC_DIR)/Delphi*.java
	rm -f $(SRC_DIR)/frontend/Delphi*.java
	rm -f $(SRC_DIR)/frontend/*.tokens $(SRC_DIR)/frontend/*.interp
	rm -f $(GENERATED_GRAMMAR_DIR)/Delphi*.java
	rm -f $(GENERATED_GRAMMAR_DIR)/*.tokens $(GENERATED_GRAMMAR_DIR)/*.interp
	rm -f grammar/*.tokens grammar/*.interp
	rm -rf $(LL_DIR)
	rm -rf $(OUT_DIR)
	rm -f $(WASM_DIR)/*.o $(WASM_DIR)/*.wasm
	-rm -f .codex
