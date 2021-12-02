const app = Vue.createApp({
  data() {
    return {
      email: "",
      password: "",
      email2: "",
      password2: "",
      nombre: "",
      apellido: "",
      // ejemplo para mostrar cosas.
      // registro:false
    };
  },
  methods: {
    login(email, password) {
      axios
        .post("/api/login", `email=${email}&password=${password}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((response) => {
          if(email == "admin@mindhub.com"){
            return window.location.href = "/manager.html"
          }else{
           return window.location.href = "/web/html/accounts.html"
          }
      })
        .catch((error) => {
         alert("invalid data")
        });
    },
    // ejemplo para mostrar cosas.
    // mostrasregistro(){
    //   this.registro = !this.registro
    // }
    registroClient() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.nombre}&lastName=${this.apellido}&email=${this.email2}&password=${this.password2}`,
          {
            headers: { "content-type": "application/x-www-form-urlencoded" },
          }
        )
        .then((response) => {
          this.login(this.email2, this.password2);
        })
        .catch((error) => alert(error.response.data));
    },
  },
});
app.mount("#app");
