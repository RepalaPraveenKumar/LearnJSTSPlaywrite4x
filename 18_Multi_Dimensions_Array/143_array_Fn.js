let scores = [
    [85, 90, 78],   // student 0 , 253
    [60, 45, 70],   // student 1,  175
    [95, 88, 92]    // student 2, 275
];

// Matrix -> Row = Col -> 

let rowsSum = scores.map(row => row.reduce((a,b) => a+b));
console.log(rowsSum);