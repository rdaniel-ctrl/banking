const app = Vue.createApp({
  data() {
    return {
      name: "",
      amount: 0,
      payment: [],
      numero:0,
      vacio:[]
    };
  },
  created() {},
  methods: {
    sendLoan() {
      axios
        .post("/api/loans/admin", {
          name: this.name,
          amount: this.amount,
          payments: this.vacio
        })
        .then(res=>alert(res.data))
        .catch(res=>alert("invalid date"))
       },
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    guardarcuota(e) {
      console.log(e.target.value)
      this.numero=Number(e.target.value)
    },
    save(){
      this.vacio.push(this.numero)
      this.numero = 0
    }
  },
});
app.mount("#app");
