<template>
  <div id="app">
    <h2>IDATT2105 Full-stack applikasjonsutvikling Øving 4</h2>
    <h1>Kalkulator i Vue.js</h1>
    <calculator></calculator>
  </div>
</template>

<script>

import Calculator from './components/calculator.vue'

export default {
  name: 'app',
  components: { Calculator }
}
</script>

<style>
:root {font-family: Arial, Helvetica, sans-serif; text-align: center;}
      #chat{width: 20rem; height: 10rem; overflow: scroll; border: 1px solid black;}
  
</style>
