# -------- CONFIG --------
ANTLR_JAR = antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = Delphi.g4

# -------- DEFAULT TARGET --------
all: generate compile

# -------- GENERATE PARSER --------
generate:
	java -jar $(ANTLR_JAR) -visitor $(GRAMMAR)
	mv Delphi*.java $(SRC_DIR)/

# -------- COMPILE --------
compile:
	mkdir -p $(BIN_DIR)
	javac -cp ".:$(ANTLR_JAR)" -d $(BIN_DIR) $(SRC_DIR)/*.java

# -------- RUN --------
run:
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main

# -------- CLEAN --------
clean:
	rm -rf $(BIN_DIR)
	rm -f $(SRC_DIR)/Delphi*.java
	rm -f *.tokens *.interp
