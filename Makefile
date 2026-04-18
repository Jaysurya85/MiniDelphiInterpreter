# -------- CONFIG --------
ANTLR_JAR = lib/antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = grammar/Delphi.g4
TEST_FILES = $(sort $(wildcard tests/*.pas))
COMPILER_TEST_FILES = $(sort $(wildcard tests/compiler_test*.pas))

# -------- DEFAULT TARGET --------
all: generate compile

# -------- GENERATE PARSER --------
generate:
	java -jar $(ANTLR_JAR) -visitor $(GRAMMAR)
	mv grammar/Delphi*.java $(SRC_DIR)/frontend/

# -------- COMPILE --------
compile:
	mkdir -p $(BIN_DIR)
	javac -cp ".:$(ANTLR_JAR)" -d $(BIN_DIR) $$(find $(SRC_DIR) -name '*.java' | sort)

# -------- RUN --------
run:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make run file=<testfile.pas>"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main tests/${file}

ast:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make ast file=<testfile.pas>"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main tests/${file}

run-all:
	@for file in $(COMPILER_TEST_FILES); do \
		echo "Running $$file"; \
		java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main "$$file"; \
		echo; \
	done

# -------- CLEAN --------
clean:
	rm -rf $(BIN_DIR)
	rm -f $(SRC_DIR)/Delphi*.java
	rm -f $(SRC_DIR)/frontend/Delphi*.java
	rm -f grammar/*.tokens grammar/*.interp
	rm -f .codex
