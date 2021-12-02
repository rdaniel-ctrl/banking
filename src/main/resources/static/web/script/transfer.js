const app = Vue.createApp({
  data() {
    return {
      data: [],
      cuentaori:"",
      visible: true,
      mostrar:"",
      balance:"",
      descrip:"",
      destino:""
    };
  },
  created() {
    axios
      .get("/api/clients/current")
      .then((response) => {
        this.data = response.data.account;
      })
      .catch();
  },
  methods: {
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    enviarDinero(e){
      e.preventDefault();
      axios
      .post(`/api/transactions?amount=${this.balance}&description=${this.descrip}&numberOrigin=${this.cuentaori}&numberDesti=${this.destino}`)
      .then(response=> window.location.href ="/web/html/accounts.html")
      .catch(e => alert(e.response.data));
    }
  },
  computed: {
    cuEnta() {
       return this.data.filter((cuenta) => cuenta.number != this.cuentaori);
    },
  },
});
app.mount("#app");
