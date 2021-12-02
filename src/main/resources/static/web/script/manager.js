const app = Vue.createApp({
  data() {
    return {
      clientes: [],
      data: {},
      nombre: [],
      apel: [],
      ema: [],
    };
  },
  created() {
    this.rest();
  },
  methods: {
    sendss(e) {
      e.preventDefault();
      axios
        .post("/rest/clients", {
          firstName: this.nombre,
          lastName: this.apel,
          email: this.ema,
        })
        .then((re) => this.rest())
        .catch((err) => err.message);
    },
    eliminarA(e) {
      console.log(e.target.id);
      axios
        .delete(`/rest/cle/delete`)
        .then((e) => this.rest())
        .catch((error) => alert(error.response.data));
    },
    rest() {
      axios.get("/rest/clients").then((rest) => {
        this.data = rest;
        this.clientes = rest.data._embedded.clients;
      });
    },
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
  },
});
let cosas = app.mount("#app");
