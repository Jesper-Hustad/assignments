<template>
    
    <div class="grid-container">

        <div class="result calc-element">
                <div class="result-history"></div>
                <div class="main-result"></div>
        </div>

        <Button v-for="item in numbers" 
                v-bind:key=item
                v-bind:buttonValue=item
                @clicked='buttonClicked'
                >
        </Button>
    </div>


</template>

<script>
import Button from './button.vue'


export default {
    components: { Button },
    data () {
        return {
            numbers: '7 8 9 * 4 5 6 - 1 2 3 + 0 . CA ='.split(' '),
            number: ''
        }
    },
    methods: {  
        buttonClicked(value) {

            if(value == '=') return this.addResult(this.number + ' = ' + eval(this.number))
             
            if(value == 'CA'){
                this.addResult('')
                document.querySelector('.result-history').innerHTML = ""
                return
            }

            this.number += value.toString()
            document.querySelector('.main-result').textContent =  this.number

            // scroll to bottom
            var scrollDiv = document.querySelector('.result');
            scrollDiv.scrollTop = scrollDiv.scrollHeight;
        },
        
        addResult(result){
            document.querySelector('.result-history').innerHTML += result + "<br>"
            document.querySelector('.main-result').textContent = ''
            this.number = ''
        }
    }
}
</script>

<style>
.grid-container {

    max-width: 22em;
    margin: auto;

    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    grid-template-rows: 1fr 1fr 1fr 1fr 1fr;
    gap: 1em 1em;
    grid-template-areas: "result result result result";

    border: 10px solid rgb(155, 155, 155);
    padding: 3em;
    background-color: rgb(16, 15, 26);
}

@media screen and (max-width: 1024px){
  .grid-container{
      max-width: 80%;
      gap: 5em 2em;
  }
}

.result { 
    grid-area: result; 
    overflow: auto;
    height: 6em;
    padding: 0px 15px 5px 0px;
    text-align: right;
}

.main-result{
    font-size: 3em;
    font-weight: 200;
}

.result-history{
    padding-right: 5px;
    font-size: 1.5em;
}

.calc-element{
  background-color: rgb(50, 50, 63);
  border: 3px solid rgb(59, 57, 75);
  color: rgb(255, 255, 255);
}
</style>


