const app = Vue.createApp({
  data() {
    return {
        cuenta:[],
        cartas:[],
        numerodesec:0,
        monto:0,
        descripcion:"",
        cuentas:"",
        cvv:0,
        cardnumber:0,
        amount:0,
    };
  },
  created() {
      axios.get("/api/clients/current")
      .then(res=>{
        this.cuenta=res.data.account
        this.cartas=res.data.cards
        console.log(this.cartas)
        
      })
  },
  methods: {
      enviarPagos(){
          axios.post("/api/clients/current/cards/payment",{
            number:this.cardnumber,
            securitycode:this.cvv,
            account:this.cuentas,
            amount:this.amount,
            description:this.descripcion
        })
          .then(res=>alert(res.data))
          .catch(err=>alert(err.response.data))
      }, 
      logout(e) {
        e.preventDefault();
        axios.post("/api/logout").then((window.location = "/web/index.html"));
      },
  },
});
app.mount("#app");
