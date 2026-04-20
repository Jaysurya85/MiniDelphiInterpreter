async function loadWasm() {
  const result = document.getElementById("result");
  const status = document.getElementById("status");

  try {
    const response = await fetch("minimal_run.wasm");
    if (!response.ok) {
      throw new Error(`HTTP ${response.status} while loading minimal_run.wasm`);
    }

    const bytes = await response.arrayBuffer();
    const { instance } = await WebAssembly.instantiate(bytes);

    if (typeof instance.exports.run !== "function") {
      throw new Error("WASM export run() was not found");
    }

    const value = instance.exports.run();
    result.textContent = value;
    status.textContent = "Loaded minimal_run.wasm successfully";
    console.log("run() =", value);
  } catch (error) {
    result.textContent = "Error";
    status.textContent = error.message;
    console.error(error);
  }
}

loadWasm();
