//node ./test.js
//se puede ejecutar desde el explorador
const ingredient = {
 "name":"tomate",
 "cost":1.2
}
//const url = 'http://localhost:8080/api/ingredients'
const url = 'http://localhost:5092/ingredientes'
const response = await fetch(url,{
   method:'POST',
   body:JSON.stringify(ingredient),
   headers:{
      "Content-Type":"application/json"
   }
})

const data = await response.json()

console.log(data)