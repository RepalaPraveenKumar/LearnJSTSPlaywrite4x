let a = { status: "pass" };
let b = a;  //Copy the reference
b.status = "fail";
console.log(a.status);