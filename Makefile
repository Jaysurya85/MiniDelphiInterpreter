# -------- CONFIG --------
ANTLR_JAR = lib/antlr-4.13.1-complete.jar
SRC_DIR = src
BIN_DIR = bin
GRAMMAR = grammar/Delphi.g4
TEST_FILES = $(sort $(wildcard tests/*.pas))
COMPILER_TEST_FILES = $(sort $(wildcard tests/compiler_test*.pas))
PHASE4_TEST_FILE = /tmp/phase4_test.pas
PHASE5_PROC_TEST_FILE = /tmp/phase5_proc_test.pas
PHASE5_FUNC_TEST_FILE = /tmp/phase5_func_test.pas
PHASE6_WHILE_TEST_FILE = /tmp/phase6_while_test.pas
PHASE6_FOR_TEST_FILE = /tmp/phase6_for_test.pas
PHASE7_TEST1_FILE = /tmp/phase7_test1.pas
PHASE7_TEST2_FILE = /tmp/phase7_test2.pas
PHASE7_TEST3_FILE = /tmp/phase7_test3.pas

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

# -------- COMMANDS --------
compile-ll:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make compile-ll file=<path/to/testfile.pas>"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(file)

ast:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make ast file=<path/to/testfile.pas>"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(file)

expr-test:
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main --expr-test

all-phase: phase4-test phase5-proc-test phase5-func-test phase6-while-test phase6-for-test phase7-test1 phase7-test2 phase7-test3

phase4-test:
	@printf 'var\n  x: integer;\n\nbegin\n  x := 2 + 3;\nend.\n' > $(PHASE4_TEST_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE4_TEST_FILE)

phase5-proc-test:
	@printf 'procedure show(x);\nbegin\nend;\n\nbegin\n  show(10);\nend.\n' > $(PHASE5_PROC_TEST_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE5_PROC_TEST_FILE)

phase5-func-test:
	@printf 'function getVal(x);\nbegin\n  return x;\nend;\n\nbegin\n  writeln(getVal(5));\nend.\n' > $(PHASE5_FUNC_TEST_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE5_FUNC_TEST_FILE)

phase6-while-test:
	@printf 'var\n  x: integer;\n\nbegin\n  x := 1;\n  while x do\n  begin\n    break;\n  end;\nend.\n' > $(PHASE6_WHILE_TEST_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE6_WHILE_TEST_FILE)

phase6-for-test:
	@printf 'var\n  i: integer;\n\nbegin\n  for i := 1 to 3 do\n  begin\n    continue;\n  end;\nend.\n' > $(PHASE6_FOR_TEST_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE6_FOR_TEST_FILE)

phase7-test1:
	@printf 'begin\n  writeln(5);\nend.\n' > $(PHASE7_TEST1_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE7_TEST1_FILE)

phase7-test2:
	@printf 'var\n  x: integer;\n\nbegin\n  x := 2 + 3;\n  writeln(x);\nend.\n' > $(PHASE7_TEST2_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE7_TEST2_FILE)

phase7-test3:
	@printf 'function getVal(x);\nbegin\n  return x;\nend;\n\nbegin\n  writeln(getVal(5));\nend.\n' > $(PHASE7_TEST3_FILE)
	java -cp ".:$(ANTLR_JAR):$(BIN_DIR)" Main $(PHASE7_TEST3_FILE)

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
