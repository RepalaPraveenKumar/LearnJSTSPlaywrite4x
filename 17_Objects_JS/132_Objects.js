let a1 = {
    name : "Pramod"
};


let a = { status: "pass" };
console.log(a.status);
console.log(a["status"]);

// keys are case sensitive.
let a22 = { status: "pass", Status: "fail" };
console.log(a22["status"]);
console.log(a22["Status"]);