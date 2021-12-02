const app = Vue.createApp({
  data() {
    return {
      data: [],
      lOans: [],
      name: "",
      payments: [],
      client: [],
      balance: 0,
      cuota: "",
      montototal: 0,
      cuenta: "",
      pagoencuotas:0,
    };
  },
  created() {
    this.traerLoans();
    this.traerClient();
  },
  methods: {
    traerLoans() {
      axios.get("/api/loans").then(response => {
        this.data = response.data;
      });
    },
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    traerClient() {
      axios.get("/api/clients/current").then(response => {
        this.client = response.data.account;
      });
    },
    enviarLoans() {
      console.log(this.name)
      console.log(this.balance)
      console.log(this.cuota)
      console.log(this.cuenta)
      axios
        .post("/api/loans", {
          "name": this.name,
          "amount": this.balance,
          "payment": this.cuota,
          "account": this.cuenta,
          "percentage":20
        })
        .then((response) => (window.location.href = "/web/html/accounts.html"))
        .catch(error=> {
            alert(error.response.data)
            console.log(error.response)
          })
    },
    traerPayments2(){
      if(this.name != ""){
        return this.data.filter(evento=> evento.name == this.name)[0].payments;
      }
    },
  },
  computed: {
   calcularCuotas() {
      let number = Number(this.cuota);
      let numero = Number(this.balance);
     return this.pagoencuotas = parseFloat(((numero * 1.20) / number).toFixed(2));
    },
    calcularTotal(){
      let number = Number(this.balance)
      return this.total = parseFloat(number * 1.20)
    }
  },
});
app.mount("#app");
