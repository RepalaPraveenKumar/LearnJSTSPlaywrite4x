let grid = [
    [10, 20, 30],
    [40, 50, 60],
    [70, 80, 90]
];

console.log(grid[0][0]);
// console.log(grid[7][7]);

grid[0][0] = 99;
console.log(grid);

console.log(grid.length); // Row -> 3
console.log(grid[0].length); // Columns -> 3