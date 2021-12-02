const app = Vue.createApp({
  data() {
    return {
      card: [],
      debit:[],
      credit:[],
      name:"",
      borrar:"",
    };
  },
  created() {
    axios
      .get("/api/clients/current")
      .then((res) => {
        this.card = res.data.cards;   
         this.name= res.data
        this.separartarjeta(this.card)
      })
      .catch((err) => err.message);
  },
  methods: {
    // debit and credit separator
    separartarjeta(array){
        this.credit = array.filter(card=> card.type === "CREDIT");
        this.debit = array.filter(card => card.type === "DEBIT")
    },
    // logout 
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    // delete cards
    deleteCard(){
      axios.delete(`/api/clients/current/cards/delete/${this.borrar}`)
      .then(res=>window.location="/web/html/accounts.html")
      .catch(e=>alert(e.response.data))
    }
  },
});
let cons =app.mount("#app");
