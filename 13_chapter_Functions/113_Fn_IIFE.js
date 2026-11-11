//Immediately Invoked Function Expression (IIFE)
//They don't need to be called. 


// function name1() {
//     console.log("Hi")
// }
// name1();


// (function(){
//     console.log("Anonymous Fn");
// })();

// (function () {
//     console.log("Staging")
// })();


(() => {
    console.log("Setup complete");
})();