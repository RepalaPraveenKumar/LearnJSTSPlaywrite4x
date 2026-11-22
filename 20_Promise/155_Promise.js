let apiCall = new Promise(function(resolve,reject){
           resolve({status : 200, body : "User Data"});
});


apiCall.then(function(response){
    console.log(response); 
});


let testRun = new Promise(function (resolve, reject) {
    reject("Assertion Failed");
});

testRun.then(function (data) { // Resolve
    console.log(data);
}).catch(function (error) { // Reject
    console.log(error);
});