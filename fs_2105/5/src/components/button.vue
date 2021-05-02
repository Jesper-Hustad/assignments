<template>
  <div id="vuebutton">
      <button v-on:click="onClickButton" class="calc-element">
        {{ buttonValue }}
        </button>
  </div>
</template>

<script>

export default {
  name: 'vuebutton',
  props: { buttonValue: String },
  methods:{
      onClickButton () { this.$emit('clicked', this.$props.buttonValue) }
  }
}
</script>

<style>
button{
  width: 1.5em;
  height: 2em;
  font-size: 3em;
}
</style>