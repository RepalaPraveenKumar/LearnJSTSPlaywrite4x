function learn() { … }

# JavaScript Functions: visual notes

Beginner to intermediate, in simple English. Every topic ends with how it shows up in automation testing (Playwright, Cypress, WebdriverIO, Selenium JS) and a quick interview check.

## What is a function?

A function is a **named, reusable block of code**. Write once, call many times. In tests, it means you write `login()` once and use it in 100 tests.

Input (arguments)*→*Function body*→*Output (return value)

### Three ways to create a function

declaration

```
function add(a, b) {
  return a + b;
}
add(2, 3); // 5
```

Has a name. **Hoisted**: you can call it before the line where it is written.

expression

```
const add = function (a, b) {
  return a + b;
};
```

Function stored in a variable. **Not usable before** its line (variable is in the temporal dead zone).

anonymous

```
setTimeout(function () {
  console.log("done");
}, 1000);
```

A function **with no name**. Used inline, mostly as a callback or inside an expression.

### Hoisting: the classic interview trap

```
sayHi();          // works: "Hi"
function sayHi() { console.log("Hi"); }

sayExpr();        // ReferenceError (const) / TypeError (var)
const sayExpr = function () { console.log("Hi"); };
```

**Remember:** named function expressions (`const f = function g(){}`) give `g` a name only inside itself. This helps stack traces and recursion.

**In automation:** anonymous functions are everywhere: `test("login", async () => {...})`, `page.evaluate(() => document.title)`, `cy.get("li").each(function ($el) {...})`.

Interview check: declaration vs expression?

Declarations are hoisted fully (name + body). Expressions are only as hoisted as their variable, so calling early fails. Declarations always need a name; expressions can be anonymous.

## User-defined functions: the 4 types

You create them yourself. They are grouped by two questions: **does it take input (arguments)?** and **does it give back output (return)?**

No argument

With argument

No return

**Type 1**\
does an action only

**Type 2**\
takes input, does an action

With return

**Type 3**\
gives a value back

**Type 4**\
input in, value out

Type 1 No return, no argument

```
function openApp() {
  console.log("Browser launched");
}
openApp();
```

Use: setup steps like launching a browser or clearing cookies.

Type 2 No return, with argument

```
function login(user, pass) {
  console.log(`Login as ${user}`);
}
login("qa1", "secret");
```

Use: fill a form, click a menu item, write a log line.

Type 3 With return, no argument

```
function getBaseUrl() {
  return "https://staging.example.com";
}
const url = getBaseUrl();
```

Use: read config, current time, a random test id.

Type 4 With return, with argument

```
function isPriceValid(price) {
  return price > 0 && price < 10000;
}
isPriceValid(499); // true
```

Use: validations, calculations, data builders. Most reusable type.

**Common mistake:** forgetting `return`. A function without return gives `undefined`. Printing inside a function is not the same as returning.

Interview check: what does a function return if there is no return?

`undefined`.

## Arrow functions `=>`

A short way to write function expressions (ES6).

```
// normal                       // arrow
const sq = function (n) { return n * n; };
const sq = (n) => { return n * n; };
const sq = n => n * n;           // one param, implicit return
const hi = () => "Hi";           // no params
const mk = (n) => ({ name: n }); // returning an object: wrap in ( )
```

|  | Normal function | Arrow function |
| --- | --- | --- |
| `this` | Depends on how it is called | Takes `this` from the surrounding code |
| `arguments` | Available | Not available (use rest `...args`) |
| Use with `new` | Yes (constructor) | No |
| Hoisting | Declarations yes | No (always an expression) |

**In automation:** arrows are the default for callbacks: `items.map(x => x.price)`, `await expect.poll(() => getCount()).toBe(3)`.

**Cypress note:** inside `describe/it` you may use `this.alias` only with normal `function () {}`, because arrows do not get their own `this`.

Interview check: when not to use arrows?

Object methods that need their own `this`, constructors, and code that needs `arguments`.

## Parameters, arguments, default and rest

**Parameter**

The variable name in the definition.

```
function add(a, b) {}
```

**Argument**

The real value you pass when calling.

```
add(10, 20);
```

Too few arguments: missing ones become `undefined`. Too many: extras are ignored (unless you use rest).

### Default parameters

```
function openPage(url, timeout = 5000, browser = "chromium") {
  console.log(url, timeout, browser);
}
openPage("/home");            // 5000, chromium
openPage("/home", undefined, "firefox"); // skip middle one
```

Defaults apply only for `undefined`, not for `null` or `0`.

### Rest parameters `...`

Collects all remaining arguments into a **real array**. Must be the last parameter.

```
function logAll(level, ...messages) {
  messages.forEach(m => console.log(`[${level}]`, m));
}
logAll("INFO", "start", "login ok", "end");
```

**Rest vs spread:** rest *gathers* values in a definition; spread *expands* values in a call: `Math.max(...[3,9,4])`.

### Bonus: destructured parameters

```
function createUser({ name, role = "tester" }) { … }
createUser({ name: "Asha" });
```

**In automation:** option objects like `page.click(sel, { timeout: 3000 })` and helpers such as `waitFor(sel, { timeout = 10000 } = {})`.

**Primitives vs objects:** numbers and strings are passed by value. Objects and arrays are passed by reference, so changing them inside a function changes the original.

## Return values

- `return` sends a value back and **stops** the function immediately.
- No `return` (or bare `return;`) gives `undefined`.
- One return only. To send many values, return an array or object.
- Code after `return` never runs.

```
function stats(nums) {
  if (nums.length === 0) return null;   // early return (guard)
  const total = nums.reduce((a, b) => a + b, 0);
  return { total, avg: total / nums.length };
}
const { total, avg } = stats([4, 6, 8]);
```

**Gotcha:** `return` followed by a new line returns `undefined` because of automatic semicolons. Start the value on the same line: `return {`.

**In automation:** `async` functions always return a **Promise**. Use `await` to get the value: `const text = await getToastText();`. Forgetting `await` or `return` is the number one cause of flaky tests.

## Callbacks and higher-order functions

In JavaScript, functions are **first-class**: you can store them in variables, pass them as arguments, and return them.

callback

A function you **pass into** another function, to be called later.

```
function fetchData(done) {
  setTimeout(() => done("data"), 500);
}
fetchData(d => console.log(d));
```

higher-order

A function that **takes** a function or **returns** a function.

```
function repeat(n, action) {
  for (let i = 0; i < n; i++) action(i);
}
repeat(3, i => console.log("try", i));
```

### Built-in HOFs you must know

| Method | Job | Example |
| --- | --- | --- |
| `map` | Transform every item | `rows.map(r => r.name)` |
| `filter` | Keep matching items | `prices.filter(p => p > 100)` |
| `reduce` | Combine into one value | `prices.reduce((s, p) => s + p, 0)` |
| `find` / `some` / `every` | Search or check | `names.every(n => n.length > 0)` |
| `forEach` | Do something per item (returns nothing) | `links.forEach(check)` |

### A function that returns a function

```
const withRetry = (fn, times = 3) => async (...args) => {
  for (let i = 1; i <= times; i++) {
    try { return await fn(...args); }
    catch (e) { if (i === times) throw e; }
  }
};
const safeClick = withRetry(clickButton);
```

**In automation:** verifying a sorted table: `const names = await getNames(); expect(names).toEqual([...names].sort());`. Retry wrappers, `page.waitForResponse(res => res.ok())`, and Cypress `.then(cb)` are all callbacks / HOFs.

**Callback hell:** deeply nested callbacks are hard to read. Prefer Promises and `async/await`. `forEach` does not wait for `await`; use `for...of` instead.

## Scope and closures

**Scope** decides where a variable can be seen. Inner scopes can look outward, never inward.

**Global scope** `const baseUrl`

**Function scope** `const token`

**Block scope { }** `let i`, `const temp`

|  | `var` | `let` | `const` |
| --- | --- | --- | --- |
| Scope | Function | Block | Block |
| Re-assign | Yes | Yes | No |
| Hoisted value | `undefined` | TDZ error | TDZ error |

**Scope chain:** JavaScript looks in the current scope, then the parent, then the global one, and stops at the first match.

### Closure

A closure is a function that **remembers the variables of the place where it was created**, even after that outer function has finished.

```
function makeCounter() {
  let count = 0;              // private, hidden from outside
  return () => ++count;
}
const next = makeCounter();
next(); next();               // 1, 2
const other = makeCounter();
other();                      // 1 (own separate count)
```

**In automation:** closures keep private state: a test-data generator with its own counter, a logger configured once, or a page-object factory `const loginPage = createPage(page)` that remembers `page`.

**Classic loop bug:** `for (var i=0;i<3;i++) setTimeout(()=>console.log(i))` prints 3,3,3 because all callbacks share one `var`. Use `let` and you get 0,1,2.

## Recursion

A function that **calls itself** on a smaller problem. Every recursive function needs two things:

**Base case**

The stop condition. Without it: infinite calls and `RangeError: Maximum call stack size exceeded`.

**Recursive case**

The call to itself with a value closer to the base case.

```
function factorial(n) {
  if (n <= 1) return 1;          // base case
  return n * factorial(n - 1);   // recursive case
}
factorial(4); // 24
```

### Call stack for `factorial(3)`

factorial(3) waits…

factorial(2) waits…

factorial(1) returns 1

Then it unwinds: `1 → 2×1=2 → 3×2=6`.

### Practical example: nested data

```
function collectLinks(menu) {
  return menu.flatMap(item =>
    [item.url, ...collectLinks(item.children || [])]);
}
```

**In automation:** walking nested JSON from an API, flattening menus or tree tables, retrying until a condition is true (`waitUntil` style), deep-comparing objects.

**Trade-off:** recursion is elegant but uses stack memory. For huge loops prefer a normal `for` loop.

## Pure and impure functions, plus other must-knows

pure

- Same input gives the same output
- No side effects

```
const total = (a, b) => a + b;
```

Easy to test, safe to reuse, no flaky behavior.

impure

- Changes outside data, reads time/random, calls an API, logs, writes files

```
let sum = 0;
const add = n => (sum += n);
```

Needed sometimes, but keep them few and small.

### Other important topics

**IIFE**

Runs immediately.

```
(() => {
  console.log("setup");
})();
```

**Async functions**

```
async function open(url) {
  await page.goto(url);
}
```

**Constructor / method**

```
const user = {
  name: "Asha",
  greet() { return this.name; }
};
```

**Function chaining / currying**

```
const add = a => b => a + b;
add(2)(3); // 5
```

- **Function composition:** small functions combined: `trim(lower(text))`.
- **Generators** (`function*`) pause and resume; rarely needed in tests.
- **`call`, `apply`, `bind`:** set `this` manually.
- **Mutation:** avoid changing input arrays or objects; copy with `[...arr]` or `{...obj}`.

**Best practice for test code:** keep helpers pure where possible (data builders, formatters, validators), and isolate side effects (clicks, API calls) into a few clear action functions.

## Revision cheat sheet and interview questions

| Concept | One-line memory |
| --- | --- |
| Declaration | Named, hoisted |
| Expression | Stored in variable, not hoisted |
| Arrow | Short syntax, no own `this` or `arguments` |
| Default param | Used when value is `undefined` |
| Rest | Gathers extra args into an array, last position |
| Callback | Function passed to be called later |
| HOF | Takes or returns a function |
| Closure | Function + remembered outer variables |
| Recursion | Self call + base case |
| Pure | Same in, same out, no side effects |

### Tap to reveal answers

1\. What is the difference between parameters and arguments?

Parameters are names in the function definition. Arguments are the actual values passed during the call.

2\. Why can arrow functions be a problem as object methods?

They do not have their own `this`, so `this` will not point to the object.

3\. What is a closure? Give a testing use.

A function that keeps access to variables from its creation scope. Example: a unique-ID generator that keeps its own counter.

4\. Difference between `map` and `forEach`?

`map` returns a new array. `forEach` returns `undefined`.

5\. Why does `forEach(async ...)` cause flaky tests?

It does not wait for the promises. Use `for...of` with `await`, or `Promise.all(arr.map(...))`.

6\. What is a pure function and why is it good for testing?

Same input always gives the same output with no side effects, so it is predictable and easy to unit test.

7\. What happens without a base case in recursion?

Stack overflow: `RangeError: Maximum call stack size exceeded`.

8\. Output? `console.log(typeof add); function add(){}`

`"function"`, because declarations are hoisted.

### Practice tasks

- Write `isValidEmail(email)` (Type 4).
- Write `withTimer(fn)` that logs how long `fn` takes (HOF + closure).
- Write `sumAll(...nums)` using rest and `reduce`.
- Flatten a nested array with recursion.

Tip: write each example yourself in a `.js` file and run it with `node file.js`. Typing it builds memory faster than reading.