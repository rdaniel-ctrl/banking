const app = Vue.createApp({
  data() {
    return {
      cliente: [],
      cuentas: [],
      loans: [],
      delte:"",
      type:"",
    };
  },
  created() {
    axios
      .get("/api/clients/current")
      .then((e) => {
        this.cliente = e.data;
        this.cuentas = e.data.account;
        this.loans = e.data.loans;
        console.log(this.cliente)
        this.cuentas.sort((a, b) => {
          if (a.id < b.id) {
            return -1;
          }
          if (b.id < a.id) {
            return 1;
          }
          return 0;
        });
      })
      .catch((err) => err.message);
  },
  methods: {
    //aca se obtiene el url para la vinculacion de el boton del url
    obtenerurlid(id) {
      return `./account.html?id=${id}`;
    },
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    // creacion de cuentas
    create(e) {
      e.preventDefault();
      axios
      .post(`/api/clients/current/accounts?type=${this.type}`)
      .then(res=>setTimeout(function(){
       location.reload()
      }),1000);
    },
    deleteAccount(){
      axios.delete(`/api/clients/current/accounts/delete/?number=${this.delte}`)
      .then(res=>setTimeout(function(){
        location.reload()
      },100))
      .catch(e=>alert(e.response.data))
    }
  },
});
app.mount("#app");
