<template>
  <div class="content">
    <div class="network-history"></div>

    <div class="grid-container">
      <div class="result calc-element">
        <div class="result-history"></div>
        <div class="main-result"></div>
      </div>

      <Button
        v-for="item in numbers"
        v-bind:key="item"
        v-bind:buttonValue="item"
        @clicked="buttonClicked"
      >
      </Button>
    </div>
  </div>
</template>

<script>
import Button from "./button.vue";

export default {
  components: { Button },
  data() {
    return {
      numbers: "7 8 9 * 4 5 6 - 1 2 3 + 0 . CA = ( ) / %".split(" "),
      number: "",
    };
  },
  methods: {
    async buttonClicked(value) {
      if (value == "=") {
        const response = await this.queryServer(this.number);
        const color = response.status == "success" ? "white" : "red";
        return this.addResult(this.number + " = " + response.result, color);
      }

      if (value == "CA") {
        this.addResult("");
        document.querySelector(".result-history").innerHTML = "";
        return;
      }

      this.number += value.toString();
      document.querySelector(".main-result").textContent = this.number;

      // scroll to bottom
      var scrollDiv = document.querySelector(".result");
      scrollDiv.scrollTop = scrollDiv.scrollHeight;
    },

    addResult(result, color) {
      document.querySelector(
        ".result-history"
      ).innerHTML += `<span style="color : ${color}">${result}</span><br>`;
      document.querySelector(".main-result").textContent = "";
      this.number = "";
    },

    async queryServer(equation) {
      const params = { equation: equation };
      const url = "/api/v1/equation?" + new URLSearchParams(params).toString();
      const response = await fetch(url);
      const data = await response.json();

      this.addNetworkHistory(url, data);

      return data;
    },

    addNetworkHistory(url, obj) {
      const networkHistory = document.querySelector(".network-history");

      networkHistory.innerHTML += `
              \n\n${url}\n
              <pre><code>${JSON.stringify(obj, undefined, 2)}</code></pre>`;

      networkHistory.scrollTop = networkHistory.scrollHeight;
    },
  },
};
</script>

<style>
.content {
  width: 30em;
  margin: auto;
}

.network-history {
  width: 90%;
  height: 6.5em;
  font-size: 1.5em;
  text-align: left;
  overflow: auto;

  padding: 1em;

  margin-bottom: 1em;

  border: 1px rgb(168, 168, 168) solid;
  border-radius: 0.5em;
  background-color: rgb(247, 247, 247);
}

.grid-container {
  /* max-width: 22em; */
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

@media screen and (max-width: 1024px) {
  .grid-container {
    gap: 5em 2em;
  }

  .content {
    max-width: 80%;
  }
}

.result {
  grid-area: result;
  overflow: auto;
  height: 6em;
  padding: 0px 15px 5px 0px;
  text-align: right;
}

.main-result {
  font-size: 3em;
  font-weight: 200;
}

.result-history {
  padding-right: 5px;
  font-size: 1.5em;
}

.calc-element {
  background-color: rgb(50, 50, 63);
  border: 3px solid rgb(59, 57, 75);
  color: rgb(255, 255, 255);
}
</style>


