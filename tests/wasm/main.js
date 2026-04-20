async function loadWasm() {
  const result = document.getElementById("result");
  const status = document.getElementById("status");
  const printOutput = document.getElementById("print-output");

  try {
    const response = await fetch("wasm_print.wasm", { cache: "no-store" });
    if (!response.ok) {
      throw new Error(`HTTP ${response.status} while loading wasm_print.wasm`);
    }

    const bytes = await response.arrayBuffer();
    const imports = {
      env: {
        print_i32(value) {
          printOutput.textContent += `${value}\n`;
          console.log("print_i32 =", value);
        },
      },
    };
    const { instance } = await WebAssembly.instantiate(bytes, imports);

    if (typeof instance.exports.run !== "function") {
      throw new Error("WASM export run() was not found");
    }

    const value = instance.exports.run();
    result.textContent = value;
    status.textContent = "Loaded compiler-generated wasm_print.wasm successfully";
    console.log("run() =", value);
  } catch (error) {
    result.textContent = "Error";
    status.textContent = error.message;
    console.error(error);
  }
}

loadWasm();
