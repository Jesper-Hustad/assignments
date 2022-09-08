
const input = process.argv[2].split(" ").filter(i => i != '')
const stack = []
console.log(input)

const isInt = n => /^([0-9]*)$/.test(n)
const isArithmatic = n => /^([+-/*]*)$/.test(n)


for (const i of input) {
    if(isInt(i)) { 
        stack.push(Number(i)); 
    } else if(isArithmatic(i)) {
        const numbers = stack.splice(stack.length-2, stack.length)
        stack.push(Number(eval(numbers[0] + " " + i + " " + numbers[1])))
    } else {
        console.log(i == "p" ? stack[stack.length -1] : stack)
    }
    // console.log(i)
}



