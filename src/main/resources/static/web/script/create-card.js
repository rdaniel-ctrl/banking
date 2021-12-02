const app = Vue.createApp({
  data() {
    return {
      tipo: "",
      color: "",
    };
  },
  methods: {
    creAte() {
      axios
        .post(
          "/api/clients/current/cards",
          `cardType=${this.tipo}&cardColor=${this.color}`,
          {
            headers: { "content-type": "application/x-www-form-urlencoded" },
          }
        )
        .then(response => window.location.href ="/web/html/cards.html")
        .catch(error => alert(error.response.data));
    },
  },
});
app.mount("#app");