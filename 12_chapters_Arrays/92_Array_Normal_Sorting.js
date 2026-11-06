let nums = [10,1,21,2];
nums.sort((a,b) => a-b);
console.log(nums);
nums.sort((a,b) => b-a);
console.log(nums);

let fruits = ['apple','banana',"cherry"];
fruits.sort((a, b) => a.localeCompare(b))


// arrow Functions