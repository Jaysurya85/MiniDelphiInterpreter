# -------- CONFIG --------
ANTLR_JAR = lib/antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = grammar/Delphi.g4
TEST_FILES = $(sort $(wildcard tests/*.pas))

# -------- DEFAULT TARGET --------
all: generate compile

# -------- GENERATE PARSER --------
generate:
	java -jar $(ANTLR_JAR) -visitor $(GRAMMAR)
	mv grammar/Delphi*.java $(SRC_DIR)/

# -------- COMPILE --------
compile:
	mkdir -p $(BIN_DIR)
	javac -cp ".:$(ANTLR_JAR)" -d $(BIN_DIR) $(SRC_DIR)/*.java

# -------- RUN --------
run:
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main tests/${file}

run-all:
	@for file in $(TEST_FILES); do \
		echo "Running $$file"; \
		java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main "$$file"; \
		echo; \
	done

# -------- CLEAN --------
clean:
	rm -rf $(BIN_DIR)
	rm -f $(SRC_DIR)/Delphi*.java
	rm -f grammar/*.tokens grammar/*.interp
