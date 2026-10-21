// let a = 100;
// console.log(a++ + ++a +a++ + ++a);//output is 408
// console.log(a);//output is 104

//*****************
// let a = 37;
// console.log(--a + a--);//Output is 72
// console.log(a)//a=35

//**************** */
let a = 5; 
let b = a-- - --a; // Gives 2
console.log(b, a);// b=2 and a=3

//**************** */
let i = 1; 
let r = i++ > 1 ? i++ : ++i; 
console.log(r, i);//r=3 and i=3
/*Hear i++ is not grater than 1 so it is false
  so it gives ++i means it gives output 3
  i++ gives 2 than after ++i gives 3
*/
