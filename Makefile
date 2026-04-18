ANTLR_JAR = lib/antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = grammar/Delphi.g4
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
		java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main "$$file"; \
	done

clean:
	rm -rf $(BIN_DIR)
	rm -f $(SRC_DIR)/Delphi*.java
	rm -f $(SRC_DIR)/frontend/Delphi*.java
	rm -f $(SRC_DIR)/frontend/*.tokens $(SRC_DIR)/frontend/*.interp
	rm -f grammar/*.tokens grammar/*.interp
	rm -rf tests/ll
	-rm -f .codex
